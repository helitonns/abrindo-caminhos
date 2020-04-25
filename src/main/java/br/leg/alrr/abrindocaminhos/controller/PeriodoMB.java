package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Periodo;
import br.leg.alrr.abrindocaminhos.persistence.PeriodoDAO;
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
public class PeriodoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PeriodoDAO periodoDAO;

    private Periodo periodo;

    private ArrayList<Periodo> periodos;

    private Periodo periodoSelecionado;

    private boolean removerPeriodoSelecionado = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarPeriodo() {
        try {
            if (periodo.getId() != null) {
                periodoDAO.atualizar(periodo);
                FacesUtils.addInfoMessageFlashScoped("Período atualizada com sucesso!");
            } else {
                periodoDAO.salvar(periodo);
                FacesUtils.addInfoMessageFlashScoped("Período salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "periodo.xhtml" + "?faces-redirect=true";
    }

    public void listarPeriodoesAtivas() {
        try {
            periodos = (ArrayList<Periodo>) periodoDAO.listarPeriodosAtivos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerPeriodo() {
        try {
            if (removerPeriodoSelecionado) {
                periodoDAO.remover(periodoSelecionado);
                FacesUtils.addInfoMessage("Período removida com sucesso!");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("O período não pode ser excluído pois ainda está referenciado em instrução.");
        }
        limparForm();
    }

    private void limparForm() {
        periodo = new Periodo();
        periodo.setStatus(true);
        periodos = new ArrayList<>();
        removerPeriodoSelecionado = false;
        listarPeriodoesAtivas();
    }

    public String cancelar() {
        return "periodo.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public ArrayList<Periodo> getPeriodos() {
        return periodos;
    }

    public Periodo getPeriodoSelecionado() {
        return periodoSelecionado;
    }

    public void setPeriodoSelecionado(Periodo periodoSelecionado) {
        this.periodoSelecionado = periodoSelecionado;
    }

    public boolean isRemoverPeriodoSelecionado() {
        return removerPeriodoSelecionado;
    }

    public void setRemoverPeriodoSelecionado(boolean removerPeriodoSelecionado) {
        this.removerPeriodoSelecionado = removerPeriodoSelecionado;
    }

}
