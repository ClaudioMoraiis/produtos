package com.example.demo.produtoVendas;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.cliente.ClienteRepository;
import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.StatusVendaEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.exceptions.ProdutoNaoEncontradoException;
import com.example.demo.produto.ProdutoEntity;
import com.example.demo.produto.ProdutoMapper;
import com.example.demo.produto.ProdutoRepository;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoEntity;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoRepository;
import com.example.demo.util.ResponseApiUtil;
import com.example.demo.venda.VendasEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProdutoVendasService {
    @Autowired
    private ClienteRepository fClienteRepository;

    @Autowired
    private ProdutoRepository fProdutoRepository;

    @Autowired
    private ProdutoVendaRepository fProdutoVendaRepository;

    @Autowired
    private VendaRepository fRepository;

    @Autowired
    private ProdutoMapper fProdutoMapper;

    @Autowired
    private ProdutoMovimentacaoRepository fProdutoMovimentacaoRepository;

    public ResponseEntity<?> cadastrar(VendaRequestDTO mDTO) {
        Optional<ClienteEntity> mClienteEntity = fClienteRepository.findById(mDTO.getId_cliente());
        if (mClienteEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum cliente localizado com esse id");
        }

        String mStatus = mDTO.getStatus().toUpperCase();
        if (!(mStatus == null) && (mStatus.equals(StatusVendaEnum.CANCELADA.name())))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não é possível usar o status CANCELADA no cadastro da venda.");

        try {
            StatusVendaEnum.valueOf(mStatus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status inválido. Deve ser PENDENTE ou CONCLUIDA.");
        }

        VendasEntity mVendasEntity;
        try {
            mVendasEntity = salvarVenda(mClienteEntity.get(), mDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar venda: " + e.getMessage());
        }

        try {
            List<ProdutoVendasEntity> mProdutosVendas = new ArrayList<>();
            List<ProdutoMovimentacaoEntity> mProdutoMovimentacoes = new ArrayList<>();

            for (ProdutoVendasDTO mProdutoVendaDTO : mDTO.getLista_produto()) {
                Optional<ProdutoEntity> mProdutoOpt = fProdutoRepository.findById(mProdutoVendaDTO.getId_produto());
                ProdutoEntity mProdutoEntity = mProdutoOpt.get();

                ProdutoVendasEntity mProdutoVendasEntity = preencherListaProdutosVendas(mVendasEntity, mProdutoEntity, mProdutoVendaDTO);
                mProdutosVendas.add(mProdutoVendasEntity);

                mProdutoMovimentacoes.add(fProdutoMapper.preencherProdutoMovEntity(
                        mProdutoEntity,
                        TipoMovimentoEnum.SAIDA,
                        OrigemMovimentoEnum.VENDA,
                        mVendasEntity.getId(),
                        mProdutoVendaDTO.getQuantidade()
                ));

                if (mStatus.equals(StatusVendaEnum.CONCLUIDA.name())) {
                    fProdutoRepository.diminuirSaldo(mProdutoVendaDTO.getQuantidade(), mProdutoVendaDTO.getId_produto());
                    fProdutoRepository.ajustarPrecoVenda(mProdutoVendaDTO.getPreco_unitario(), mProdutoVendaDTO.getId_produto());
                }
            }

            fProdutoVendaRepository.saveAll(mProdutosVendas);
            fProdutoMovimentacaoRepository.saveAll(mProdutoMovimentacoes);
            return ResponseEntity.ok("Venda cadastrada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar produtos da venda: " + e.getMessage());
        }
    }

    private VendasEntity salvarVenda(ClienteEntity mCliente, VendaRequestDTO mVendaRequestDTO) {
        BigDecimal mTotal = BigDecimal.ZERO;

        for (ProdutoVendasDTO mProdutoDTO : mVendaRequestDTO.getLista_produto()) {
            Optional<ProdutoEntity> mProdutoOpt = fProdutoRepository.findById(mProdutoDTO.getId_produto());
            if (mProdutoOpt.isEmpty()) {
                throw new ProdutoNaoEncontradoException(mProdutoDTO.getId_produto());
            }

            BigDecimal mSubTotal = mProdutoDTO.getPreco_unitario().multiply(BigDecimal.valueOf(mProdutoDTO.getQuantidade()));
            mTotal = mTotal.add(mSubTotal);
        }

        VendasEntity mVendasEntity = new VendasEntity();
        mVendasEntity.setCliente(mCliente);
        mVendasEntity.setDataVenda(LocalDate.now());
        mVendasEntity.setTotal(mTotal);
        mVendasEntity.setStatus(StatusVendaEnum.valueOf(mVendaRequestDTO.getStatus().toUpperCase()));

        fRepository.save(mVendasEntity);
        return mVendasEntity;
    }

    public ProdutoVendasEntity preencherListaProdutosVendas(
            VendasEntity mVendaEntity, ProdutoEntity mProdutoEntity,
            ProdutoVendasDTO mProdutoVendaDTO
    ) {
        BigDecimal mQuantidade = new BigDecimal(mProdutoVendaDTO.getQuantidade());
        BigDecimal mPrecoUnitario = mProdutoVendaDTO.getPreco_unitario();
        BigDecimal mSubTotal = mPrecoUnitario.multiply(mQuantidade);

        return new ProdutoVendasEntity(
                mVendaEntity, mProdutoEntity, mProdutoVendaDTO.getQuantidade(),
                mProdutoVendaDTO.getPreco_unitario(), mSubTotal
        );
    }

    public ResponseEntity<?> cancelar(Long mId) {
        Optional<VendasEntity> mVendasEntity = fRepository.findById(mId);
        if (mVendasEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseApiUtil.response(
                    "Erro", "Nenhuma venda localizada com esse id"));
        }

        if (mVendasEntity.get().getStatus().equals(StatusVendaEnum.CANCELADA)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseApiUtil.response(
                    "Erro", "Venda já está cancelada"
            ));
        }

        List<ProdutoMovimentacaoEntity> mProdutoMovimentacoes = new ArrayList<>();
        if (mVendasEntity.get().getStatus().equals(StatusVendaEnum.CONCLUIDA)) {
            List<ProdutoVendasEntity> mProdutoVendasEntity = fProdutoVendaRepository.getVendas(mId);

            try {
                for (ProdutoVendasEntity mVendas : mProdutoVendasEntity) {
                    Optional<ProdutoEntity> mProdutoOpt = fProdutoRepository.findById(mVendas.getProduto().getId());
                    ProdutoEntity mProdutoEntity = mProdutoOpt.get();

                    fProdutoRepository.devolverSaldo(mVendas.getQuantidade(), mVendas.getProduto().getId());
                    //Todo: precisa ver algo para atualizar o preco_venda, talvez criar um novo campo na produto
                    // para armazenar ele e quando cancelar atualizar o preco atual com esse ultimo? Tenho que analisar algo.
                    mProdutoMovimentacoes.add(fProdutoMapper.preencherProdutoMovEntity(
                            mProdutoEntity,
                            TipoMovimentoEnum.ENTRADA,
                            OrigemMovimentoEnum.CANCELAMENTO_VENDA,
                            mVendasEntity.get().getId(),
                            mVendas.getQuantidade()
                    ));
                }

                mVendasEntity.get().setStatus(StatusVendaEnum.CANCELADA);
                fRepository.save(mVendasEntity.get());
                fProdutoMovimentacaoRepository.saveAll(mProdutoMovimentacoes);
            } catch (Exception e) {
                return ResponseEntity.status(500).body(ResponseApiUtil.response("Erro", e.getMessage()));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseApiUtil.response(
                "Sucesso", "Venda cancelada"));
    }

    public ResponseEntity<?> confirmar(Long mId) {
        Optional<VendasEntity> mVendasEntity = fRepository.findById(mId);
        if (mVendasEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseApiUtil.response("Erro", "Nenhuma venda localizada com esse id")
            );
        }

        if (mVendasEntity.get().getStatus().equals(StatusVendaEnum.CANCELADA)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseApiUtil.response(
                    "Erro", "Não foi possível confirmar, venda já cancelada")
            );
        } else if (mVendasEntity.get().getStatus().equals(StatusVendaEnum.CONCLUIDA)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseApiUtil.response(
                    "Erro", "Não foi possível confirmar, venda já confirmada")
            );
        }

        List<ProdutoVendasEntity> mProdutoVendasEntity = fProdutoVendaRepository.getVendas(mId);
        List<Long> mIdsProdutos = mProdutoVendasEntity.stream()
                .map(pv -> pv.getProduto().getId())
                .collect(Collectors.toList());

        List<ProdutoEntity> mProdutos = fProdutoRepository.findAllById(mIdsProdutos);

        Map<Long, ProdutoEntity> mProdutosMap = mProdutos.stream()
                .collect(Collectors.toMap(ProdutoEntity::getId, Function.identity()));

        Map<Long, String> mMap = new HashMap<>();
        for (ProdutoVendasEntity mPv : mProdutoVendasEntity) {
            ProdutoEntity mProduto = mProdutosMap.get(mPv.getProduto().getId());
            if (mProduto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ResponseApiUtil.response("Erro", "Produto não encontrado: id " + +mPv.getProduto().getId())
                );
            }

            if (mPv.getQuantidade() > mProduto.getEstoqueAtual()) {
                mMap.put(mProduto.getId(), mProduto.getNome());
            }
        }

        if (!mMap.isEmpty()) {
            String mNomes = mMap.values().stream()
                    .map(mNome -> "- " + mNome)
                    .collect(Collectors.joining("\n"));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseApiUtil.response(
                            "Erro",
                            "Não foi possível confirmar a compra, pois os seguintes produtos não possuem saldo suficiente em estoque:\n" +
                                    mNomes
                    )
            );
        }

        List<ProdutoMovimentacaoEntity> mProdutoMovimentacoes = new ArrayList<>();
        for (ProdutoVendasEntity mProdutosVendas : mProdutoVendasEntity) {
            Optional<ProdutoEntity> mProdutoEntity = fProdutoRepository.findById(mProdutosVendas.getProduto().getId());
            mProdutoMovimentacoes.add(fProdutoMapper.preencherProdutoMovEntity(
                    mProdutoEntity.get(),
                    TipoMovimentoEnum.ENTRADA,
                    OrigemMovimentoEnum.VENDA,
                    mVendasEntity.get().getId(),
                    mProdutosVendas.getQuantidade()
            ));
        }

        try {
            mVendasEntity.get().setStatus(StatusVendaEnum.CONCLUIDA);
            fRepository.save(mVendasEntity.get());
            fProdutoMovimentacaoRepository.saveAll(mProdutoMovimentacoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseApiUtil.response("Erro", "Falha ao confirmar a venda\n" + e.getMessage())
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseApiUtil.response("Ok", "Venda confirmada"));
    }

    public ResponseEntity<?> alterar(VendaRequestDTO mDto, Long mId){
        List<ProdutoVendasEntity> mProdutosVendas = new ArrayList<>();
        List<ProdutoMovimentacaoEntity> mProdutoMovimentacaoEntity = new ArrayList<>();

        List<ProdutoVendasDTO> mListaProdutoVendasDTO = new ArrayList<>();
        for (ProdutoVendasDTO mProdutoVendasDTO : mDto.getLista_produto()){
            mListaProdutoVendasDTO.add(mProdutoVendasDTO);
        }

        ResponseEntity<?> mValidacaoRequest = validarRequest(mListaProdutoVendasDTO, mDto.getId_cliente(), mId);
        if (mValidacaoRequest != null){
            return mValidacaoRequest;
        }

        ResponseEntity<?> mValidacaoProduto = validarProduto(mListaProdutoVendasDTO);
        if (mValidacaoProduto != null){
            return mValidacaoProduto;
        }

        Optional<VendasEntity> mVendasEntity = fRepository.findById(mId);
        Optional<ClienteEntity> mClienteEntity = fClienteRepository.findById(mDto.getId_cliente());
        ProdutoVendasEntity mProdutoVendasEntity = new ProdutoVendasEntity();
        try {
            mVendasEntity.get().setCliente(mClienteEntity.get());
            mVendasEntity.get().setStatus(StatusVendaEnum.valueOf(mDto.getStatus().toUpperCase()));
            for (ProdutoVendasDTO mProdutoVendasDTO : mDto.getLista_produto()){
                Optional<ProdutoEntity> mProdutoEntity = fProdutoRepository.findById(mProdutoVendasDTO.getId_produto());
                mProdutoVendasEntity.setProduto(mProdutoEntity.get());
                mProdutoVendasEntity.setQuantidade(mProdutoVendasDTO.getQuantidade());
                mProdutoVendasEntity.setPrecoUnitario(mProdutoVendasDTO.getPreco_unitario());
                mProdutoVendasEntity.setVenda(mVendasEntity.get());

                BigDecimal mSubTotal = mProdutoVendasDTO.getPreco_unitario().multiply(BigDecimal.valueOf(mProdutoVendasDTO.getQuantidade()));
                mSubTotal = mSubTotal.add(mSubTotal);
                mProdutoVendasEntity.setSubTotal(mSubTotal);

                mProdutosVendas.add(mProdutoVendasEntity);

                if (mDto.getStatus().equals(StatusVendaEnum.CONCLUIDA.name())){
                    fProdutoRepository.diminuirSaldo(mProdutoVendasDTO.getQuantidade(), mProdutoVendasDTO.getId_produto());
                    fProdutoRepository.ajustarPrecoVenda(mProdutoVendasDTO.getPreco_unitario(), mProdutoVendasDTO.getId_produto());
                }

                mProdutoMovimentacaoEntity.add(fProdutoMapper.preencherProdutoMovEntity(
                        mProdutoEntity.get(),
                        TipoMovimentoEnum.SAIDA,
                        OrigemMovimentoEnum.VENDA,
                        mVendasEntity.get().getId(),
                        mProdutoVendasDTO.getQuantidade()
                ));
            }

            fRepository.save(mVendasEntity.get());
            fProdutoVendaRepository.saveAll(mProdutosVendas);
            fProdutoMovimentacaoRepository.saveAll(mProdutoMovimentacaoEntity);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseApiUtil.response(
                    "Sucesso", "Venda alterada com sucesso"));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseApiUtil.response("Erro", "Erro ao alterar venda\n" + e.getMessage())
            );
        }
    }

    public ResponseEntity<?> validarRequest(List<ProdutoVendasDTO> mListaProdutoVendasDTO, Long mIdCliente, Long mIdVenda){
        Optional<VendasEntity> mVendasEntity = fRepository.findById(mIdVenda);
        if (mVendasEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseApiUtil.response("Erro", "Nenhuma venda localizada com esse id"));
        }

        if (mVendasEntity.get().getStatus().equals(StatusVendaEnum.CANCELADA)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseApiUtil.response("Erro", "Impossível alterar, a venda está cancelada"));
        }

        if (fClienteRepository.findById(mIdCliente).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseApiUtil.response("Erro", "Nenhum cliente localizado com esse id"));
        }

        return null;
    }

    public ResponseEntity<?> validarProduto(List<ProdutoVendasDTO> mProdutoVendasDTO) {
        List<Long> mIds = mProdutoVendasDTO.stream()
                .map(ProdutoVendasDTO::getId_produto)
                .distinct()
                .collect(Collectors.toList());

        List<ProdutoEntity> encontrados = fProdutoRepository.findAllById(mIds);
        Set<Long> mIdsEncontrados = encontrados.stream()
                .map(ProdutoEntity::getId)
                .collect(Collectors.toSet());

        List<Long> mIdsNaoEncontrados = mIds.stream()
                .filter(mId -> !mIdsEncontrados.contains(mId))
                .collect(Collectors.toList());

        if (!mIdsNaoEncontrados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseApiUtil.response("Erro",
                            "Produto(s) não localizado(s) com o(s) id(s): " + mIdsNaoEncontrados)
            );
        }

        List<ProdutoEntity> mProdutosEncontrados  = fProdutoRepository.findAllById(mIdsEncontrados);
        Map<String, Long> mProdutoSemEstoque = new HashMap<>();
        for(ProdutoEntity mProduto : mProdutosEncontrados ){
            if (mProduto.getEstoqueAtual() <= 0){
                mProdutoSemEstoque.put(mProduto.getNome(), mProduto.getId());
            }
        }

        if (!mProdutoSemEstoque.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseApiUtil.response("Erro",
                            "O(s) produto(s) de id: " + mProdutoSemEstoque.values() + " não possuem saldo em estoque")
            );
        }

        return null;
    }

}
