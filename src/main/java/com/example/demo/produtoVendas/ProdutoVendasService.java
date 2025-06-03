package com.example.demo.produtoVendas;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.cliente.ClienteRepository;
import com.example.demo.compra.CompraEntity;
import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.StatusVendaEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.exceptions.ProdutoNaoEncontradoException;
import com.example.demo.produto.ProdutoEntity;
import com.example.demo.produto.ProdutoMapper;
import com.example.demo.produto.ProdutoRepository;
import com.example.demo.produtoCompras.ProdutoCompraDTO;
import com.example.demo.produtoCompras.ProdutoComprasEntity;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoEntity;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoRepository;
import com.example.demo.venda.VendasEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ResponseEntity<?> cadastrar(VendaRequestDTO mDTO){
        Optional<ClienteEntity> mClienteEntity = fClienteRepository.findById(mDTO.getId_cliente());
        if (mClienteEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum cliente localizado com esse id");
        }

        //TODO: Adicionar validação para ver se o status está correto(PENDENTE, CONCLUIDA, CANCELADA)

        VendasEntity mVendasEntity;
        try {
            mVendasEntity = salvarVenda(mClienteEntity.get(), mDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar venda: " + e.getMessage());
        }

        try{
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

                fProdutoRepository.diminuirSaldo(mProdutoVendaDTO.getQuantidade(), mProdutoVendaDTO.getId_produto());
                fProdutoRepository.ajustarPrecoVenda(mProdutoVendaDTO.getPreco_unitario(), mProdutoVendaDTO.getId_produto());
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

        for(ProdutoVendasDTO mProdutoDTO : mVendaRequestDTO.getLista_produto()) {
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

        fRepository.save(mVendasEntity);
        return mVendasEntity;
    }

    public ProdutoVendasEntity preencherListaProdutosVendas(
            VendasEntity mVendaEntity, ProdutoEntity mProdutoEntity,
            ProdutoVendasDTO mProdutoVendaDTO
    ){
        BigDecimal mQuantidade = new BigDecimal(mProdutoVendaDTO.getQuantidade());
        BigDecimal mPrecoUnitario = mProdutoVendaDTO.getPreco_unitario();
        BigDecimal mSubTotal = mPrecoUnitario.multiply(mQuantidade);

        return new ProdutoVendasEntity(
                mVendaEntity, mProdutoEntity, mProdutoVendaDTO.getQuantidade(),
                mProdutoVendaDTO.getPreco_unitario(), mSubTotal
        );
    }
}
