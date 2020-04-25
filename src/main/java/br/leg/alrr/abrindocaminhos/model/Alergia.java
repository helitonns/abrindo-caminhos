package br.leg.alrr.abrindocaminhos.model;

import br.leg.alrr.abrindocaminhos.util.BaseEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 * @Organization ALRR
 * @author Heliton
 * @Matricula 14583
 */
@Audited
@AuditTable(value="alergia_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Alergia implements Serializable, BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    private String nome;
    
    private boolean status;
    
    @ManyToOne
    private CategoriaDeAlergia categoria;

//==============================================================================

    public Alergia() {
    }
    
    public Alergia(Long id) {
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CategoriaDeAlergia getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDeAlergia categoria) {
        this.categoria = categoria;
    }
    
    
    
}
