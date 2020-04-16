package br.leg.alrr.abrindocaminhos.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="matricula_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Matricula implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.EAGER)
    private Turma turma;
    
    @Temporal(TemporalType.DATE)
    private Date dataMatricula;

    private boolean status;
    
    @ManyToOne
    private Unidade unidade;
    
    @Transient
    private Long rev;
    
    @Transient
    private Long revtype;
    
    @Transient
    private Date dataOperacao;
    
    @Transient
    private String usuario;
    
    @Transient
    private BigInteger alunoID;
    
    @Transient
    private BigInteger turmaID;
    //==========================================================================
    public Matricula() {
    }

    public Matricula(Long id, Long rev, Long revtype, String usuario, Date dataOperacao, BigInteger alunoID, BigInteger turmaID) {
        this.id = id;
        this.rev = rev;
        this.revtype = revtype;
        this.dataOperacao = dataOperacao;
        this.usuario = usuario;
        this.alunoID = alunoID;
        this.turmaID = turmaID;
    }
    
    public Matricula(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Date getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
    
    public boolean podeMatricular(Turma t, Aluno a){
        return a.getIdadeNumero() >= t.getIdadeMinima() && a.getIdadeNumero() <= t.getIdadeMaxima();
    }

    public Long getRev() {
        return rev;
    }

    public void setRev(Long rev) {
        this.rev = rev;
    }

    public Long getRevtype() {
        return revtype;
    }

    public void setRevtype(Long revtype) {
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

   public String getTipoDeOperacao() {
        if (revtype == 0) {
            return "INSERT";
        }else if(revtype == 1){
            return "UPDATE";
        }else{
            return "DELETE";
        }
    }

    public BigInteger getAlunoID() {
        return alunoID;
    }

    public void setAlunoID(BigInteger alunoID) {
        this.alunoID = alunoID;
    }

    public BigInteger getTurmaID() {
        return turmaID;
    }

    public void setTurmaID(BigInteger turmaID) {
        this.turmaID = turmaID;
    }

   
}
