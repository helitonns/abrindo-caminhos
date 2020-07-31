package br.leg.alrr.abrindocaminhos.model;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
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
@AuditTable(value="familiar_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Familiar implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
    @ManyToOne
    private Parentesco parentesco;
    
    @ManyToOne
    private Aluno aluno;
    
    
    @Transient
    private int idade;

    //--------------------------------------------------------------------------
    
    public Familiar() {
    }

    public Familiar(Long id) {
        this.id = id;
    }

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

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    public int getIdade() {
        if (dataNascimento != null) {
            GregorianCalendar g1 = new GregorianCalendar();
            GregorianCalendar g2 = new GregorianCalendar();
            g2.setTime(dataNascimento);

            return g1.get(GregorianCalendar.YEAR) - g2.get(GregorianCalendar.YEAR);
        }
        return 0;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
    
    
}
