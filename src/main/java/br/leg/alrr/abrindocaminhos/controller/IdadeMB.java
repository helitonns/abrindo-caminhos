package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Idade;
import br.leg.alrr.abrindocaminhos.persistence.IdadeDAO;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
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
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Idade idade;

    private ArrayList<Idade> idades;

    private Idade idadeSelecionada;

    private boolean removerIdadeSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarIdade() {
        try {
            if (idade.getId() != null) {
                idadeDAO.atualizar(idade);
                FacesUtils.addInfoMessageFlashScoped("Idade atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método IdadeMB.salvarIdade() para atualizar a idade "+ idade.getId()+".");
            } else {
                idadeDAO.salvar(idade);
                FacesUtils.addInfoMessageFlashScoped("Idade salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método IdadeMB.salvarIdade() para salvar a idade "+ idade.getId()+".");
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
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método IdadeMB.removerIdade() para excluir a idade "+ idade.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Ocorreu um erro ao excluir idade.");
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
