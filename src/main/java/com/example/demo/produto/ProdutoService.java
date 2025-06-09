package com.example.demo.produto;

import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoEntity;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository fRepository;

    @Autowired
    private ProdutoMovimentacaoRepository fProdutoMovimentacaoRepository;

    @Autowired
    private ProdutoMapper fProdutoMapper;

    public ResponseEntity<?> cadastrar(ProdutoCadastroDTO mProdutoCadastroDTO) {
        ProdutoEntity mEntity = fRepository.findFirstByNomeAndAtivoTrue(mProdutoCadastroDTO.getNome());
        if ((mEntity != null) && (mEntity.getAtivo())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um produto com esse nome");
        }
        try {
            ProdutoEntity mProdutoEntity = fProdutoMapper.preencherProduto(mProdutoCadastroDTO);
            fRepository.save(mProdutoEntity);

            if (mProdutoCadastroDTO.getEstoqueAtual() != null)
                fProdutoMovimentacaoRepository.save(fProdutoMapper.preencherProdutoMovEntity(
                        mProdutoEntity,
                        TipoMovimentoEnum.ENTRADA,
                        OrigemMovimentoEnum.CADASTRO,
                        null,
                        mProdutoEntity.getEstoqueAtual()));

            return ResponseEntity.status(HttpStatus.OK).body("Produto cadastrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar\n" + e.getMessage());
        }
    }

    public ResponseEntity<?> alterar(ProdutoAlterarDTO mProdutoAlterarDTO, Long mId) {
        try {
            ProdutoEntity mProdutoEntity = fRepository.findById(mId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum produto localizado com esse id"));

            if (!mProdutoAlterarDTO.getNome().isEmpty()) {
                mProdutoEntity.setNome(mProdutoAlterarDTO.getNome());
            }

            if (!mProdutoAlterarDTO.getAtivo().toString().isEmpty()) {
                mProdutoEntity.setAtivo(mProdutoAlterarDTO.getAtivo());
            }

            if (!mProdutoAlterarDTO.getUnidadeMedida().isEmpty()) {
                mProdutoEntity.setUnidadeMedida(mProdutoAlterarDTO.getUnidadeMedida());
            }

            mProdutoEntity.setDataUltimaAtualizacao(LocalDate.now());

            fRepository.save(mProdutoEntity);
            return ResponseEntity.status(HttpStatus.OK).body("Produto alterado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao alterar" + e.getMessage());
        }
    }

    public ResponseEntity<?> buscarPorId(Long mId){
        Optional<ProdutoEntity> mProdutoEntity = fRepository.findById(mId);
        if (mProdutoEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum produto localizado com esse id");
        }

        return ResponseEntity.status(HttpStatus.OK).body(mProdutoEntity);
    }

    public ResponseEntity<?> deletar(Long mId){
        Optional<ProdutoEntity> mProdutoEntity = fRepository.findById(mId);
        if (mProdutoEntity.isEmpty() || !mProdutoEntity.get().getAtivo()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum produto localizado com esse id");
        }

        try {
            fRepository.inativar(mProdutoEntity.get().getId());
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao inativar produto\n" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Produto inativado com sucesso");
    }
}
