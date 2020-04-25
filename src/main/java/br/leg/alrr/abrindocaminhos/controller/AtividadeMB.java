package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Atividade;
import br.leg.alrr.abrindocaminhos.model.Usuario;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.AtividadeDAO;
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
public class AtividadeMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AtividadeDAO atividadeDAO;

    private Atividade atividade;

    private ArrayList<Atividade> atividades;

    private Atividade atividadeSelecionada;

    private boolean removerAtividadeSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarAtividade() {
        try {
            if (atividade.getId() != null) {
                atividadeDAO.atualizar(atividade);
                FacesUtils.addInfoMessageFlashScoped("Atividade atualizada com sucesso!");
            } else {
                UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
                atividade.setUnidade(u.getUnidade());
                atividadeDAO.salvar(atividade);
                FacesUtils.addInfoMessageFlashScoped("Atividade salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "atividade.xhtml" + "?faces-redirect=true";
    }

    public void listarAtividades() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            atividades = (ArrayList<Atividade>) atividadeDAO.listarTodosPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerAtividade() {
        try {
            if (removerAtividadeSelecionada) {
                atividadeDAO.remover(atividadeSelecionada);
                FacesUtils.addInfoMessage("Atividade removida com sucesso!");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A atividade não pode ser excluída pois ainda está referenciada em turma.");
        }
        limparForm();
    }

    private void limparForm() {
        atividade = new Atividade();
        atividade.setStatus(true);
        atividades = new ArrayList<>();
        removerAtividadeSelecionada = false;
        listarAtividades();
    }

    public String cancelar() {
        return "atividade.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public ArrayList<Atividade> getAtividades() {
        return atividades;
    }

    public Atividade getAtividadeSelecionada() {
        return atividadeSelecionada;
    }

    public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
        this.atividadeSelecionada = atividadeSelecionada;
    }

    public boolean isRemoverAtividadeSelecionada() {
        return removerAtividadeSelecionada;
    }

    public void setRemoverAtividadeSelecionada(boolean removerAtividadeSelecionada) {
        this.removerAtividadeSelecionada = removerAtividadeSelecionada;
    }

}
