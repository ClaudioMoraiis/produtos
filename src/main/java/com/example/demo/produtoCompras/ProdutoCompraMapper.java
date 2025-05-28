package com.example.demo.produtoCompras;

import com.example.demo.compra.CompraEntity;
import com.example.demo.produto.ProdutoEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProdutoCompraMapper {
    public ProdutoComprasEntity preencherProdutoCompra(ProdutoCompraDTO mProdutoCompraDTO, ProdutoEntity mProdutoEntity,
      CompraEntity mCompraEntity)
    {
        ProdutoComprasEntity mProdutoComprasEntity = new ProdutoComprasEntity();
        mProdutoComprasEntity.setProduto(mProdutoEntity);
        mProdutoComprasEntity.setQuantidade(mProdutoCompraDTO.getQuantidade());
        mProdutoComprasEntity.setPrecoUnitario(mProdutoCompraDTO.getPreco_unitario());
        mProdutoComprasEntity.setCompra(mCompraEntity);

        BigDecimal mQuantidade = new BigDecimal(mProdutoCompraDTO.getQuantidade());
        BigDecimal mPrecoUnitario = mProdutoCompraDTO.getPreco_unitario();
        BigDecimal mSubTotal = mPrecoUnitario.multiply(mQuantidade);

        mProdutoComprasEntity.setSubTotal(mSubTotal);

        return mProdutoComprasEntity;
    }
}
