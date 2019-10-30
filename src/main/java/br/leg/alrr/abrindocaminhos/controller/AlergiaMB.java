package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Alergia;
import br.leg.alrr.abrindocaminhos.persistence.AlergiaDAO;
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
public class AlergiaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AlergiaDAO alergiaDAO;

    private Alergia alergia;

    private ArrayList<Alergia> alergias;

    private Alergia alergiaSelecionada;

    private boolean removerAlergiaSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarAlergia() {
        try {
            if (alergia.getId() != null) {
                alergiaDAO.atualizar(alergia);
                FacesUtils.addInfoMessageFlashScoped("Alergia atualizado com sucesso!");
            } else {
                alergiaDAO.salvar(alergia);
                FacesUtils.addInfoMessageFlashScoped("Alergia salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "alergia.xhtml" + "?faces-redirect=true";
    }

    public void listarAlergiaes() {
        try {
            alergias = (ArrayList<Alergia>) alergiaDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerAlergia() {
        try {
            if (removerAlergiaSelecionada) {
                alergiaDAO.remover(alergiaSelecionada);
                FacesUtils.addInfoMessage("Alergia removida com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    private void limparForm() {
        alergia = new Alergia();
        alergia.setStatus(true);
        alergias = new ArrayList<>();
        removerAlergiaSelecionada = false;
        listarAlergiaes();
    }

    public String cancelar() {
        return "alergia.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Alergia getAlergia() {
        return alergia;
    }

    public void setAlergia(Alergia alergia) {
        this.alergia = alergia;
    }

    public ArrayList<Alergia> getAlergiaes() {
        return alergias;
    }

    public Alergia getAlergiaSelecionado() {
        return alergiaSelecionada;
    }

    public void setAlergiaSelecionado(Alergia alergiaSelecionada) {
        this.alergiaSelecionada = alergiaSelecionada;
    }

    public boolean isRemoverAlergiaSelecionado() {
        return removerAlergiaSelecionada;
    }

    public void setRemoverAlergiaSelecionado(boolean removerAlergiaSelecionada) {
        this.removerAlergiaSelecionada = removerAlergiaSelecionada;
    }

}
