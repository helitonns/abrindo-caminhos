package br.leg.alrr.abrindocaminhos.model;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="inscricao_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Inscricao implements Serializable, Comparable<Inscricao>{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.EAGER)
    private ListaDeEspera listaDeEspera;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDaInscricao;

    private boolean status;
    
    @ManyToOne
    private Unidade unidade;
    //==========================================================================
    public Inscricao() {
    }

    public Inscricao(Long id) {
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

    public ListaDeEspera getListaDeEspera() {
        return listaDeEspera;
    }

    public void setListaDeEspera(ListaDeEspera listaDeEspera) {
        this.listaDeEspera = listaDeEspera;
    }

    public Date getDataDaInscricao() {
        return dataDaInscricao;
    }

    public void setDataDaInscricao(Date dataDaInscricao) {
        this.dataDaInscricao = dataDaInscricao;
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

    @Override
    public int compareTo(Inscricao o) {
        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.setTime(this.dataDaInscricao);
        
        GregorianCalendar gc2 = new GregorianCalendar();
        gc1.setTime(o.dataDaInscricao);
        
        return gc1.before(gc2) ? 1 : -1;  
    }

   
}
