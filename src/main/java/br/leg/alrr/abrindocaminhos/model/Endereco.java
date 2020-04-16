package br.leg.alrr.abrindocaminhos.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="endereco_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rua;
    private String complemento;
    private String numero;
    private String cep;

    @ManyToOne
    private Bairro bairro;
    
    @Transient
    private BigInteger rev;
    
    @Transient
    private Short revtype;
    
    @Transient
    private Date dataOperacao;
    
    @Transient
    private String usuario;
    
    @Transient
    private BigInteger idEntidade;
    

    //========================================================================//
    public Endereco() {
    }

    public Endereco(BigInteger idEntidade, BigInteger rev, Short revtype, String usuario, Date dataOperacao) {
        this.rev = rev;
        this.revtype = revtype;
        this.dataOperacao = dataOperacao;
        this.usuario = usuario;
        this.idEntidade = idEntidade;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        
        if (bairro != null) {
            sb.append(bairro.getMunicipio().getNome()).append(", ");;
            sb.append(bairro.getNome()).append(", ");
        }
        
        if (rua != null) {
            sb.append(rua).append(", ");
        }
        if (numero != null) {
            sb.append(numero);
        }
        
        return sb.toString();
    }

    public BigInteger getRev() {
        return rev;
    }

    public void setRev(BigInteger rev) {
        this.rev = rev;
    }

    public Short getRevtype() {
        return revtype;
    }

    public void setRevtype(Short revtype) {
        this.revtype = revtype;
    }

    public Date getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(Date dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigInteger getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(BigInteger idEntidade) {
        this.idEntidade = idEntidade;
    }
    
    public String getTipoDeOperacao() {
        if (null == revtype) {
            return "DELETE";
        }else switch (revtype) {
            case 0:
                return "INSERT";
            case 1:
                return "UPDATE";
            default:
                return "DELETE";
        }
    }
}
