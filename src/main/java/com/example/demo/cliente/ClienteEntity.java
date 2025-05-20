package com.example.demo.cliente;

import com.example.demo.enums.TipoDocumentoEnum;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "CLIENTE")
public class ClienteEntity implements ClienteDados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cli_id")
    private Long id;

    @Column(name = "cli_nome")
    private String nome;

    @Column(name = "cli_documento")
    private String documento;

    @Column(name = "cli_tipo_documento")
    @Enumerated(EnumType.STRING)
    private TipoDocumentoEnum tipoDocumento;

    @Column(name = "cli_data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "cli_email")
    private String email;

    @Column(name = "cli_telefone")
    private String telefone;

    @Column(name = "cli_endereco")
    private String endereco;

    @Column(name = "cli_complemento")
    private String complemento;

    @Column(name = "cli_bairro")
    private String bairro;

    @Column(name = "cli_cidade")
    private String cidade;

    @Column(name = "cli_estado", length = 2)
    private String estado;

    @Column(name = "cli_cep", length = 8)
    private String cep;

    @Column(name = "cli_ativo")
    private Boolean ativo;

    @Column(name = "cli_data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "cli_data_ultima_atualizacao")
    private LocalDate dataUltimaAtualizacao;

    public ClienteEntity(Long id, String nome, String documento, TipoDocumentoEnum tipoDocumento, LocalDate dataNascimento,
                         String email, String telefone, String endereco, String complemento, String bairro, String cidade,
                         String estado, String cep, Boolean ativo, LocalDate dataCadastro, LocalDate dataUltimaAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public ClienteEntity(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public TipoDocumentoEnum getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoEnum tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDate dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ClienteEntity that)) return false;

        return id.equals(that.id) && nome.equals(that.nome) && documento.equals(that.documento) && tipoDocumento == that.tipoDocumento && dataNascimento.equals(that.dataNascimento) && email.equals(that.email) && telefone.equals(that.telefone) && endereco.equals(that.endereco) && complemento.equals(that.complemento) && bairro.equals(that.bairro) && cidade.equals(that.cidade) && estado.equals(that.estado) && cep.equals(that.cep) && ativo.equals(that.ativo) && dataCadastro.equals(that.dataCadastro) && dataUltimaAtualizacao.equals(that.dataUltimaAtualizacao);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + documento.hashCode();
        result = 31 * result + tipoDocumento.hashCode();
        result = 31 * result + dataNascimento.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + telefone.hashCode();
        result = 31 * result + endereco.hashCode();
        result = 31 * result + complemento.hashCode();
        result = 31 * result + bairro.hashCode();
        result = 31 * result + cidade.hashCode();
        result = 31 * result + estado.hashCode();
        result = 31 * result + cep.hashCode();
        result = 31 * result + ativo.hashCode();
        result = 31 * result + dataCadastro.hashCode();
        result = 31 * result + dataUltimaAtualizacao.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", documento='" + documento + '\'' +
                ", tipoDocumento=" + tipoDocumento +
                ", dataNascimento=" + dataNascimento +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", endereco='" + endereco + '\'' +
                ", complemento='" + complemento + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", cep='" + cep + '\'' +
                ", ativo=" + ativo +
                ", dataCadastro=" + dataCadastro +
                ", dataUltimaAtualizacao=" + dataUltimaAtualizacao +
                '}';
    }
}
