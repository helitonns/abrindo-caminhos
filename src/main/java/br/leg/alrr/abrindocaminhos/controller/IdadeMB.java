package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Idade;
import br.leg.alrr.abrindocaminhos.persistence.IdadeDAO;
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
@ViewScoped
@Named
public class IdadeMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private IdadeDAO idadeDAO;

    private Idade idade;

    private ArrayList<Idade> idades;

    private Idade idadeSelecionada;

    private boolean removerIdadeSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarIdade() {
        try {
            if (idade.getId() != null) {
                idadeDAO.atualizar(idade);
                FacesUtils.addInfoMessageFlashScoped("Idade atualizada com sucesso!");
            } else {
                idadeDAO.salvar(idade);
                FacesUtils.addInfoMessageFlashScoped("Idade salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "idade.xhtml" + "?faces-redirect=true";
    }

    public void listarIdadees() {
        try {
            idades = (ArrayList<Idade>) idadeDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerIdade() {
        try {
            if (removerIdadeSelecionada) {
                idadeDAO.remover(idadeSelecionada);
                FacesUtils.addInfoMessage("Idade removida com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    private void limparForm() {
        idade = new Idade();
        idade.setStatus(true);
        idades = new ArrayList<>();
        removerIdadeSelecionada = false;
        listarIdadees();
    }

    public String cancelar() {
        return "idade.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Idade getIdade() {
        return idade;
    }

    public void setIdade(Idade idade) {
        this.idade = idade;
    }

    public ArrayList<Idade> getIdades() {
        return idades;
    }

    public Idade getIdadeSelecionada() {
        return idadeSelecionada;
    }

    public void setIdadeSelecionada(Idade idadeSelecionada) {
        this.idadeSelecionada = idadeSelecionada;
    }

    public boolean isRemoverIdadeSelecionada() {
        return removerIdadeSelecionada;
    }

    public void setRemoverIdadeSelecionada(boolean removerIdadeSelecionada) {
        this.removerIdadeSelecionada = removerIdadeSelecionada;
    }

}
