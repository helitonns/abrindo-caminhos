package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Situacao;
import br.leg.alrr.abrindocaminhos.persistence.SituacaoDAO;
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
public class SituacaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SituacaoDAO situacaoDAO;

    private Situacao situacao;

    private ArrayList<Situacao> situacoes;

    private Situacao situacaoSelecionada;

    private boolean removerSituacaoSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarSituacao() {
        try {
            if (situacao.getId() != null) {
                situacaoDAO.atualizar(situacao);
                FacesUtils.addInfoMessageFlashScoped("Situação atualizada com sucesso!");
            } else {
                situacaoDAO.salvar(situacao);
                FacesUtils.addInfoMessageFlashScoped("Situação salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "situacao.xhtml" + "?faces-redirect=true";
    }

    public void listarSituacaoesAtivas() {
        try {
            situacoes = (ArrayList<Situacao>) situacaoDAO.listarSituacoesAtivas();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerSituacao() {
        try {
            if (removerSituacaoSelecionada) {
                situacaoDAO.remover(situacaoSelecionada);
                FacesUtils.addInfoMessage("Situação removida com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    private void limparForm() {
        situacao = new Situacao();
        situacao.setStatus(true);
        situacoes = new ArrayList<>();
        removerSituacaoSelecionada = false;
        listarSituacaoesAtivas();
    }

    public String cancelar() {
        return "situacao.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public ArrayList<Situacao> getSituacoes() {
        return situacoes;
    }

    public Situacao getSituacaoSelecionada() {
        return situacaoSelecionada;
    }

    public void setSituacaoSelecionada(Situacao situacaoSelecionada) {
        this.situacaoSelecionada = situacaoSelecionada;
    }

    public boolean isRemoverSituacaoSelecionada() {
        return removerSituacaoSelecionada;
    }

    public void setRemoverSituacaoSelecionada(boolean removerSituacaoSelecionada) {
        this.removerSituacaoSelecionada = removerSituacaoSelecionada;
    }

}
