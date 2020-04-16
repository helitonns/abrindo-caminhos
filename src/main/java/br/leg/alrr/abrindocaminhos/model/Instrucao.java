package br.leg.alrr.abrindocaminhos.model;

import br.leg.alrr.abrindocaminhos.util.BaseEntity;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

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
@AuditTable(value="instrucao_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Instrucao implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @ManyToOne
    private Escolaridade escolaridade;
    
    @ManyToOne
    private Escola escola;
    
    @ManyToOne
    private Serie serie;
    
    @ManyToOne
    private Periodo periodo;

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
    public Instrucao() {
    }

    public Instrucao(BigInteger idEntidade, BigInteger rev, Short revtype, String usuario, Date dataOperacao) {
        this.rev = rev;
        this.revtype = revtype;
        this.dataOperacao = dataOperacao;
        this.usuario = usuario;
        this.idEntidade = idEntidade;
    }

    public Instrucao(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Escolaridade getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(Escolaridade escolaridade) {
        this.escolaridade = escolaridade;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Instrucao other = (Instrucao) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
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
