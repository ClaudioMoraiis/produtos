package com.example.demo.produtoCompras;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.cliente.ClienteRepository;
import com.example.demo.compra.CompraEntity;
import com.example.demo.compra.CompraRepository;
import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.produto.ProdutoEntity;
import com.example.demo.produto.ProdutoMapper;
import com.example.demo.produto.ProdutoRepository;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public ResponseEntity<?> cadastrar(CompraRequestDTO mCompraRequestDTO){
        Optional<ClienteEntity> mClienteEntity = fClienteRepository.findById(mCompraRequestDTO.getId_cliente());
        if (mClienteEntity == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum cliente localizado com esse id");
        }

        BigDecimal mTotalCompra = BigDecimal.ZERO;
        for (ProdutoCompraDTO mProdutoDTO : mCompraRequestDTO.getLista_produto()){
            Optional<ProdutoEntity> mProdutoEntityOpt = fProdutoRepository.findById(mProdutoDTO.getId_produto());
            if (mProdutoEntityOpt.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum produto localizado com o ID: " + mProdutoDTO.getId_produto());
            }

            BigDecimal mSubTotal = mProdutoDTO.getPreco_unitario().multiply(BigDecimal.valueOf(mProdutoDTO.getQuantidade()));
            mTotalCompra = mTotalCompra.add(mSubTotal);
        }

        CompraEntity mCompraEntity = new CompraEntity();
        try {
            mCompraEntity.setCliente(mClienteEntity.orElse(null));
            mCompraEntity.setData(LocalDate.now());
            mCompraEntity.setTotal(mTotalCompra);

            fCompraRepository.save(mCompraEntity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao inserir compra\n" + e.getMessage());
        }

        try {
            for (ProdutoCompraDTO mProdutoCompraDTO : mCompraRequestDTO.getLista_produto()){
                ProdutoEntity mProdutoEntity = fProdutoRepository.findById(mProdutoCompraDTO.getId_produto()).get();
                if (mClienteEntity == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum produto localizado com o ID: " + mProdutoCompraDTO.getId_produto());
                }

                fRepository.save(fMapper.preencherProdutoCompra(mProdutoCompraDTO, mProdutoEntity, mCompraEntity));
                fProdutoRepository.ajustarSaldo(mProdutoCompraDTO.getQuantidade(), mProdutoCompraDTO.getId_produto());
                fProdutoMovimentacaoRepository.save(fProdutoMapper.preencherProdutoMovEntity(
                        mProdutoEntity,
                        TipoMovimentoEnum.ENTRADA,
                        OrigemMovimentoEnum.COMPRA,
                        mCompraEntity.getId())
                );
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao inserir item compra\n" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Compra inserida com sucesso");
    }
}
