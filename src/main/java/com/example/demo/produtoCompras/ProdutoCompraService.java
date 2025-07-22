package com.example.demo.produtoCompras;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.cliente.ClienteRepository;
import com.example.demo.cliente.ClienteResumoDTO;
import com.example.demo.compra.CompraEntity;
import com.example.demo.compra.CompraRepository;
import com.example.demo.compra.ListaCompraDTO;
import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.StatusCompraEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.exceptions.ProdutoNaoEncontradoException;
import com.example.demo.produto.ProdutoEntity;
import com.example.demo.produto.ProdutoMapper;
import com.example.demo.produto.ProdutoRepository;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoEntity;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoRepository;
import com.example.demo.util.ResponseApiUtil;
import com.example.demo.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ProdutoCompraService {
    @Autowired
    private CompraRepository fCompraRepository;

    @Autowired
    private ProdutoCompraRepository fRepository;

    @Autowired
    private ClienteRepository fClienteRepository;

    @Autowired
    private ProdutoRepository fProdutoRepository;

    @Autowired
    private ProdutoCompraMapper fMapper;

    @Autowired
    private ProdutoMapper fProdutoMapper;

    @Autowired
    private ProdutoMovimentacaoRepository fProdutoMovimentacaoRepository;

    @Transactional
    public ResponseEntity<?> cadastrar(CompraRequestDTO mCompraRequestDTO){
        Optional<ClienteEntity> mClienteEntity = fClienteRepository.findById(mCompraRequestDTO.getId_cliente());
        if (mClienteEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum cliente localizado com esse id");
        }

        String mStatus = mCompraRequestDTO.getStatus().toUpperCase();
        if (!(mStatus == null) && (mStatus.equals(StatusCompraEnum.CANCELADA.name())))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não é possível usar o status CANCELADA no cadastro da compra.");

        try{
            StatusCompraEnum.valueOf(mStatus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status inválido. Deve ser PENDENTE ou CONCLUIDA.");
        }

        CompraEntity mCompraEntity;
        try{
            mCompraEntity = salvarCompra(mClienteEntity.orElse(null), mCompraRequestDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar a compra\n" + e.getMessage());
        }

        try {
            List<ProdutoComprasEntity> mProdutosCompras = new ArrayList<>();
            List<ProdutoMovimentacaoEntity> mProdutoMovimentacoes = new ArrayList<>();

            for (ProdutoCompraDTO mProdutoCompraDTO : mCompraRequestDTO.getLista_produto()){
                Optional<ProdutoEntity> produtoOpt = fProdutoRepository.findById(mProdutoCompraDTO.getId_produto());
                ProdutoEntity mProdutoEntity = produtoOpt.get();

                ProdutoComprasEntity mItens = preencherListaProdutosCompras(mCompraEntity, mProdutoEntity, mProdutoCompraDTO);
                mProdutosCompras.add(mItens);

                mProdutoMovimentacoes.add(fProdutoMapper.preencherProdutoMovEntity(
                        mProdutoEntity,
                        TipoMovimentoEnum.ENTRADA,
                        OrigemMovimentoEnum.COMPRA,
                        mCompraEntity.getId(),
                        mProdutoCompraDTO.getQuantidade())
                );

                fProdutoRepository.ajustarSaldo(mProdutoCompraDTO.getQuantidade(), mProdutoCompraDTO.getId_produto());
                fProdutoRepository.ajustarPrecoCusto(mProdutoCompraDTO.getPreco_unitario(), mProdutoCompraDTO.getId_produto());
            }

            fRepository.saveAll(mProdutosCompras);
            fProdutoMovimentacaoRepository.saveAll(mProdutoMovimentacoes);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao inserir item compra\n" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Compra inserida com sucesso");
    }

    public CompraEntity salvarCompra(ClienteEntity mEntity, CompraRequestDTO mCompraRequestDTO){
        BigDecimal mTotal = BigDecimal.ZERO;

        for (ProdutoCompraDTO mProdutoDTO : mCompraRequestDTO.getLista_produto()) {
            Optional<ProdutoEntity> produtoOpt = fProdutoRepository.findById(mProdutoDTO.getId_produto());
            if (produtoOpt.isEmpty()) {
                throw new ProdutoNaoEncontradoException(mProdutoDTO.getId_produto());
            }

            BigDecimal subTotal = mProdutoDTO.getPreco_unitario().multiply(BigDecimal.valueOf(mProdutoDTO.getQuantidade()));
            mTotal = mTotal.add(subTotal);
        }

        CompraEntity mCompraEntity = new CompraEntity();

        mCompraEntity.setCliente(mEntity);
        mCompraEntity.setData(LocalDate.now());
        mCompraEntity.setTotal(mTotal);
        mCompraEntity.setStatus(StatusCompraEnum.valueOf(mCompraRequestDTO.getStatus().toUpperCase()));

        fCompraRepository.save(mCompraEntity);
        return mCompraEntity;
    };

    public ProdutoComprasEntity preencherListaProdutosCompras(
        CompraEntity mCompraEntity, ProdutoEntity mProdutoEntity,
        ProdutoCompraDTO mProdutoCompraDTO
    ){
        BigDecimal mQuantidade = new BigDecimal(mProdutoCompraDTO.getQuantidade());
        BigDecimal mPrecoUnitario = mProdutoCompraDTO.getPreco_unitario();
        BigDecimal mSubTotal = mPrecoUnitario.multiply(mQuantidade);

        return new ProdutoComprasEntity(
                mCompraEntity, mProdutoEntity, mProdutoCompraDTO.getQuantidade(),
                mProdutoCompraDTO.getPreco_unitario(), mSubTotal
        );
    }

    public ResponseEntity<?> listar(String mDini, String mDfin, String mStatus, HttpServletRequest mRequest){
        List<CompraEntity> mCompraEntity = new ArrayList<>();
        List<ListaCompraDTO> mCompraDTO = new ArrayList<>();
        Set<String> mParametros = mRequest.getParameterMap().keySet();
        ResponseEntity<?> mValidacao = validarRequest(mDini, mDfin, mStatus, mParametros);

        if (mValidacao.getStatusCode() == HttpStatus.BAD_REQUEST){
            return mValidacao;
        }

        if ((mDini != null) && (mDfin != null)){
            mCompraEntity = fCompraRepository.buscarPorData(Util.formatarData(mDini), Util.formatarData(mDfin));
        } else {
            mCompraEntity = fCompraRepository.findAll();
        }

        Stream<CompraEntity> mStreamFiltrados = mCompraEntity.stream();
        if ((mStatus != null) && (!mStatus.isEmpty())){
            mCompraEntity = mStreamFiltrados.filter(c -> c.getStatus() == StatusCompraEnum.valueOf(mStatus.toUpperCase())).toList();
        }

        if (mCompraEntity == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseApiUtil.response("Erro", "Nenhuma compra localizada"));
        }

        for (CompraEntity mCompra : mCompraEntity){
            ListaCompraDTO mDto = new ListaCompraDTO();
            mDto.setData(mCompra.getData());
            mDto.setTotal(mCompra.getTotal());
            mDto.setStatus(mCompra.getStatus().name());
            mDto.setId(mCompra.getId());

            ClienteEntity mCliente = mCompra.getCliente();
            if (mCliente != null){
                ClienteResumoDTO mResumoDTO = new ClienteResumoDTO(mCliente.getId(), mCliente.getNome());
                mDto.setcliente(mResumoDTO);
            }

            mCompraDTO.add(mDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(mCompraDTO);
    }

    public ResponseEntity<?> validarRequest(String mDini, String mDfin, String mStatus, Set<String> mParametros) {
        Set<String> mParametrosRecebidos = mParametros;
        Set<String> mParametrosPermitidos = Set.of("dataInicial", "dataFinal", "status");
        Set<String> mStatusLiberado = Set.of("CANCELADA", "PENDENTE", "CONCLUIDA");

        for (String mParam : mParametrosRecebidos) {
            if (!mParametrosPermitidos.contains(mParam)) {
                return ResponseEntity.badRequest().body(
                        ResponseApiUtil.response("Erro", "Utilize apenas os parâmetros " + mParametrosPermitidos)
                );
            }
        }

        if ((mStatus != null)  && (!mStatusLiberado.contains(mStatus))) {
            return ResponseEntity.badRequest().body(
                    ResponseApiUtil.response("Erro", "Parâmetro 'status' aceita apenas " + mStatusLiberado)
            );
        }

        if ((mDini != null) && (mDfin == null)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseApiUtil.response("Erro", "Informe uma data final!")
            );
        } else if ((mDfin != null) && (mDini == null)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseApiUtil.response("Erro", "Informe uma data inicial!")
            );
        };

        if ((mDini != null) && (mDfin != null)){
            if ((!mDini.isEmpty()) && (!mDfin.isEmpty())){
                if ((mDini.replaceAll("/", "").length() != 8) ||
                        (mDfin.replaceAll("/", "").length() != 8)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            ResponseApiUtil.response("Erro", "Informe uma data válida!")
                    );
                }

                if (Util.formatarData(mDfin).isBefore(Util.formatarData(mDini))) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            ResponseApiUtil.response("Erro", "Data final não pode ser menor que inicial!")
                    );
                }
            }
        }



        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

}
