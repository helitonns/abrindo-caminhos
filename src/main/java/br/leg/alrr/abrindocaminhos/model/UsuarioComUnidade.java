package br.leg.alrr.abrindocaminhos.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 *
 * @author Heliton
 */
@Table(schema = "abrindo_caminhos")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class UsuarioComUnidade extends Usuario implements Serializable{

    
    @ManyToOne
    private Unidade unidade;
    //========================================================================//
    public UsuarioComUnidade() {
        super();
    }

    public UsuarioComUnidade(Long id) {
        super(id);
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
    
}
