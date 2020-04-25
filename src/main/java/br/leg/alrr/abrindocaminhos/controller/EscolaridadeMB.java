package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Escolaridade;
import br.leg.alrr.abrindocaminhos.persistence.EscolaridadeDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class EscolaridadeMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EscolaridadeDAO escolaridadeDAO;

    private Escolaridade escolaridade;

    private ArrayList<Escolaridade> escolaridades;

    private Escolaridade escolaridadeSelecionada;

    private boolean removerEscolaridadeSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarEscolaridade() {
        try {
            if (escolaridade.getId() != null) {
                escolaridadeDAO.atualizar(escolaridade);
                FacesUtils.addInfoMessageFlashScoped("Escolaridade atualizada com sucesso!");
            } else {
                escolaridadeDAO.salvar(escolaridade);
                FacesUtils.addInfoMessageFlashScoped("Escolaridade salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "escolaridade.xhtml" + "?faces-redirect=true";
    }

    public void listarEscolaridadees() {
        try {
            escolaridades = (ArrayList<Escolaridade>) escolaridadeDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerEscolaridade() {
        try {
            if (removerEscolaridadeSelecionada) {
                escolaridadeDAO.remover(escolaridadeSelecionada);
                FacesUtils.addInfoMessage("Escolaridade removida com sucesso!");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A escolaridade não pode ser excluída pois ainda está referenciada em instrução.");
        }
        limparForm();
    }

    private void limparForm() {
        escolaridade = new Escolaridade();
        escolaridade.setStatus(true);
        escolaridades = new ArrayList<>();
        removerEscolaridadeSelecionada = false;
        listarEscolaridadees();
    }

    public String cancelar() {
        return "escolaridade.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Escolaridade getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(Escolaridade escolaridade) {
        this.escolaridade = escolaridade;
    }

    public ArrayList<Escolaridade> getEscolaridades() {
        return escolaridades;
    }

    public Escolaridade getEscolaridadeSelecionada() {
        return escolaridadeSelecionada;
    }

    public void setEscolaridadeSelecionada(Escolaridade escolaridadeSelecionada) {
        this.escolaridadeSelecionada = escolaridadeSelecionada;
    }

    public boolean isRemoverEscolaridadeSelecionada() {
        return removerEscolaridadeSelecionada;
    }

    public void setRemoverEscolaridadeSelecionada(boolean removerEscolaridadeSelecionada) {
        this.removerEscolaridadeSelecionada = removerEscolaridadeSelecionada;
    }

}
