package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Doenca;
import br.leg.alrr.abrindocaminhos.persistence.DoencaDAO;
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
public class DoencaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private DoencaDAO doencaDAO;

    private Doenca doenca;

    private ArrayList<Doenca> doencas;

    private Doenca doencaSelecionada;

    private boolean removerDoencaSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarDoenca() {
        try {
            if (doenca.getId() != null) {
                doencaDAO.atualizar(doenca);
                FacesUtils.addInfoMessageFlashScoped("Doença atualizada com sucesso!");
            } else {
                doencaDAO.salvar(doenca);
                FacesUtils.addInfoMessageFlashScoped("Doença salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "doenca.xhtml" + "?faces-redirect=true";
    }

    public void listarDoencaes() {
        try {
            doencas = (ArrayList<Doenca>) doencaDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerDoenca() {
        try {
            if (removerDoencaSelecionada) {
                doencaDAO.remover(doencaSelecionada);
                FacesUtils.addInfoMessage("Doenca removida com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    private void limparForm() {
        doenca = new Doenca();
        doenca.setStatus(true);
        doencas = new ArrayList<>();
        removerDoencaSelecionada = false;
        listarDoencaes();
    }

    public String cancelar() {
        return "doenca.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Doenca getDoenca() {
        return doenca;
    }

    public void setDoenca(Doenca doenca) {
        this.doenca = doenca;
    }

    public ArrayList<Doenca> getDoencas() {
        return doencas;
    }

    public Doenca getDoencaSelecionado() {
        return doencaSelecionada;
    }

    public void setDoencaSelecionado(Doenca doencaSelecionada) {
        this.doencaSelecionada = doencaSelecionada;
    }

    public boolean isRemoverDoencaSelecionado() {
        return removerDoencaSelecionada;
    }

    public void setRemoverDoencaSelecionado(boolean removerDoencaSelecionada) {
        this.removerDoencaSelecionada = removerDoencaSelecionada;
    }

}
