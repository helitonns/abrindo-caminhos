package br.leg.alrr.abrindocaminhos.model;

import br.leg.alrr.abrindocaminhos.util.BaseEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.envers.AuditJoinTable;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value="protuario_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Prontuario implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @OneToOne(fetch = FetchType.EAGER)
    private Aluno aluno;
    
    private long peso;
    
    private String observacoes;
    
    private boolean convulsoes;
    private boolean faltaDeAr;
    private boolean acompanhamentoDeDentista;
    private boolean deficienciaFisica;
    private boolean deficienciaIntelectual;

    @ManyToMany(cascade = CascadeType.MERGE)
    @AuditJoinTable(name="protuario_alergias_auditoria", schema = "abrindo_caminhos")
    @JoinTable(schema = "abrindo_caminhos")
    private List<Alergia> alergias;
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @AuditJoinTable(name="protuario_medicacoes_auditoria", schema = "abrindo_caminhos")
    @JoinTable(schema = "abrindo_caminhos")
    private List<Medicacao> medicacoes;
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @AuditJoinTable(name="protuario_especialidades_auditoria", schema = "abrindo_caminhos")
    @JoinTable(schema = "abrindo_caminhos")
    private List<EspecialidadeMedica> especialidades;
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @AuditJoinTable(name="protuario_doencas_auditoria", schema = "abrindo_caminhos")
    @JoinTable(schema = "abrindo_caminhos")
    private List<Doenca> doencas;
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @AuditJoinTable(name="protuario_sindromes_auditoria", schema = "abrindo_caminhos")
    @JoinTable(schema = "abrindo_caminhos")
    private List<Sindrome> sindromes;

//==============================================================================

    public Prontuario() {
    }
   
    public Prontuario(Long id) {
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

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public long getPeso() {
        return peso;
    }

    public void setPeso(long peso) {
        this.peso = peso;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<Alergia> getAlergias() {
        return alergias;
    }

    public void setAlergias(List<Alergia> alergias) {
        this.alergias = alergias;
    }
    
    public boolean isConvulsoes() {
        return convulsoes;
    }

    public void setConvulsoes(boolean convulsoes) {
        this.convulsoes = convulsoes;
    }

    public boolean isFaltaDeAr() {
        return faltaDeAr;
    }

    public void setFaltaDeAr(boolean faltaDeAr) {
        this.faltaDeAr = faltaDeAr;
    }

    public boolean isAcompanhamentoDeDentista() {
        return acompanhamentoDeDentista;
    }

    public void setAcompanhamentoDeDentista(boolean acompanhamentoDeDentista) {
        this.acompanhamentoDeDentista = acompanhamentoDeDentista;
    }

    public boolean isDeficienciaFisica() {
        return deficienciaFisica;
    }

    public void setDeficienciaFisica(boolean deficienciaFisica) {
        this.deficienciaFisica = deficienciaFisica;
    }

    public boolean isDeficienciaIntelectual() {
        return deficienciaIntelectual;
    }

    public void setDeficienciaIntelectual(boolean deficienciaIntelectual) {
        this.deficienciaIntelectual = deficienciaIntelectual;
    }

    public List<Medicacao> getMedicacoes() {
        return medicacoes;
    }

    public void setMedicacoes(List<Medicacao> medicacoes) {
        this.medicacoes = medicacoes;
    }

    public List<EspecialidadeMedica> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<EspecialidadeMedica> especialidades) {
        this.especialidades = especialidades;
    }

    public List<Doenca> getDoencas() {
        return doencas;
    }

    public void setDoencas(List<Doenca> doencas) {
        this.doencas = doencas;
    }

    public List<Sindrome> getSindromes() {
        return sindromes;
    }

    public void setSindromes(List<Sindrome> sindromes) {
        this.sindromes = sindromes;
    }
    
}
