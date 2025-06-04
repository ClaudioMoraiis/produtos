package com.example.demo.produtoCompras;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.cliente.ClienteRepository;
import com.example.demo.compra.CompraEntity;
import com.example.demo.compra.CompraRepository;
import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.StatusCompraEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.exceptions.ProdutoNaoEncontradoException;
import com.example.demo.produto.ProdutoEntity;
import com.example.demo.produto.ProdutoMapper;
import com.example.demo.produto.ProdutoRepository;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoEntity;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (mClienteEntity == null){
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

}
