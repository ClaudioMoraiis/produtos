package com.example.demo.cliente;

import com.example.demo.enums.TipoDocumentoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ClienteDTO implements ClienteDados{
    @JsonProperty("nome")
    @NotNull(message = "Campo ''nome'' deve ser informado no body")
    private String nome;

    @JsonProperty("documento")
    @NotNull(message = "Campo ''documento'' deve ser informado no body")
    private String documento;

    @JsonProperty("tipo_documento")
    private TipoDocumentoEnum tipoDocumento;

    @JsonProperty("data_nascimento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull(message = "Campo ''data_nascimento'' deve ser informado no body")
    private LocalDate dataNascimento;

    @JsonProperty("email")
    @Email
    @NotNull(message = "Campo ''email'' deve ser informado no body")
    private String email;

    @JsonProperty("telefone")
    @NotNull(message = "Campo ''telefone'' deve ser informado no body")
    private String telefone;

    @JsonProperty("endereco")
    @NotNull(message = "Campo ''endereco'' deve ser informado no body")
    private String endereco;

    @JsonProperty("complemento")
    @NotNull(message = "Campo ''complemento'' deve ser informado no body")
    private String complemento;

    @JsonProperty("bairro")
    @NotNull(message = "Campo ''bairro'' deve ser informado no body")
    private String bairro;

    @JsonProperty("cidade")
    @NotNull(message = "Campo ''cidade'' deve ser informado no body")
    private String cidade;

    @JsonProperty("estado")
    @Size(max = 2, min = 2)
    @NotNull(message = "Campo ''estado'' deve ser informado no body")
    private String estado;

    @JsonProperty("cep")
    @Size(max = 8, min = 8)
    @NotNull(message = "Campo ''cep'' deve ser informado no body")
    private String cep;

    @JsonProperty("ativo")
    @NotNull(message = "Campo ''ativo'' deve ser informado no body")
    private Boolean ativo;

    public ClienteDTO(
            String nome, String documento, TipoDocumentoEnum tipoDocumento, LocalDate dataNascimento,
            String email, String telefone, String endereco, String complemento, String bairro, String cidade,
            String estado, String cep, Boolean ativo) {
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
    }

    public ClienteDTO(){}

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

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ClienteDTO that)) return false;

        return nome.equals(that.nome) && documento.equals(that.documento) && tipoDocumento == that.tipoDocumento && dataNascimento.equals(that.dataNascimento) && email.equals(that.email) && telefone.equals(that.telefone) && endereco.equals(that.endereco) && complemento.equals(that.complemento) && bairro.equals(that.bairro) && cidade.equals(that.cidade) && estado.equals(that.estado) && cep.equals(that.cep) && ativo.equals(that.ativo);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
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
        return result;
    }

    @Override
    public String toString() {
        return "ClienteDTO{" +
                "nome='" + nome + '\'' +
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
                '}';
    }
}
