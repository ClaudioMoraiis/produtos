package com.example.demo.produto;

import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProdutoMapper{

    public ProdutoEntity preencherProduto(ProdutoCadastroDTO mProdutoCadastroDTO){
        ProdutoEntity mProdutoEntity = new ProdutoEntity();

        mProdutoEntity.setNome(mProdutoCadastroDTO.getNome());
        mProdutoEntity.setAtivo(mProdutoCadastroDTO.getAtivo());
        mProdutoEntity.setUnidadeMedida(mProdutoCadastroDTO.getUnidadeMedida());
        mProdutoEntity.setDataCadastro(LocalDate.now());
        mProdutoEntity.setEstoqueAtual(mProdutoCadastroDTO.getEstoqueAtual());
        mProdutoEntity.setPrecoCusto(mProdutoCadastroDTO.getPrecoCusto());
        mProdutoEntity.setPrecoVenda(mProdutoCadastroDTO.getPrecoVenda());

        return mProdutoEntity;
    }

    public ProdutoMovimentacaoEntity preencherProdutoMovEntity(ProdutoEntity mProdutoEntity, TipoMovimentoEnum mTipoMovimentoEnum,
      OrigemMovimentoEnum mOrigemMovimentoEnum, Long mIdOrigem)
    {
        ProdutoMovimentacaoEntity mProdutoMovimentacaoEntity = new ProdutoMovimentacaoEntity();
        mProdutoMovimentacaoEntity.setProduto(mProdutoEntity);
        mProdutoMovimentacaoEntity.setTipoMovimento(mTipoMovimentoEnum);
        mProdutoMovimentacaoEntity.setQuantidade(mProdutoEntity.getEstoqueAtual());
        mProdutoMovimentacaoEntity.setOrigemMovimentoEnum(mOrigemMovimentoEnum);
        mProdutoMovimentacaoEntity.setDataMovimento(LocalDate.now());
        mProdutoMovimentacaoEntity.setIdOrigem(mIdOrigem);

        return mProdutoMovimentacaoEntity;
    }
}
