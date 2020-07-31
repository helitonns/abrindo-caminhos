package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Parentesco;
import br.leg.alrr.abrindocaminhos.persistence.ParentescoDAO;
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
@Named
@ViewScoped
public class ParentescoMB implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private ParentescoDAO parentescoDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;
   
    private ArrayList<Parentesco> parentescos;
    
    private Parentesco parentesco;
    private Parentesco parentescoSelecionado;

    private boolean removerParentesco = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public void limparForm() {
        parentesco = new Parentesco();
        parentesco.setStatus(true);
        parentescos = new ArrayList<>();
        removerParentesco = false;
        listarParentescos();
    }

    public String salvarParentesco() {
        try {
            if (parentesco.getId() != null) {
                parentescoDAO.atualizar(parentesco);
                FacesUtils.addInfoMessageFlashScoped("Parentesco atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método ParentescoMB.salvarParentesco() para atualizar o Parentesco "+ parentesco.getId()+".");
            } else {
                //verifica se o horário já está cadastrado, se não procede ao cadastramento
                if (parentescoDAO.parentescoNaoCadastrado(parentesco.getDescricao())) {
                    parentescoDAO.salvar(parentesco);
                    FacesUtils.addInfoMessageFlashScoped("Parentesco salvo com sucesso!");
                    Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ParentescoMB.salvarParentesco() para salva o Parentesco "+ parentesco.getId()+".");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("Parentesco já cadastrado!!!");
                }
            }
            limparForm();

        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "parentesco.xhtml" + "?faces-redirect=true";
    }

    private void listarParentescos() {
        try {
            parentescos = (ArrayList<Parentesco>) parentescoDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerParentesco() {
        try {
            if (removerParentesco) {
                parentescoDAO.remover(parentescoSelecionado);
                FacesUtils.addInfoMessage("Parentesco removido com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método ParentescoMB.removerParentesco() para excluir o Parentesco "+ parentesco.getId()+".");
            }
            removerParentesco = false;
            limparForm();
        } catch (Exception e) {
            FacesUtils.addErrorMessage("O Parentesco não pode ser excluído pois ainda está referenciado em turma.");
        }
    }

    public String cancelar() {
        return "parentesco.xhtml" + "?faces-redirect=true";
    }

    // ==========================================================================
    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public ArrayList<Parentesco> getParentescos() {
        return parentescos;
    }

    public Parentesco getParentescoSelecionado() {
        return parentescoSelecionado;
    }

    public void setParentescoSelecionado(Parentesco parentescoSelecionado) {
        this.parentescoSelecionado = parentescoSelecionado;
    }

    public boolean isRemoverParentesco() {
        return removerParentesco;
    }

    public void setRemoverParentesco(boolean removerParentesco) {
        this.removerParentesco = removerParentesco;
    }

}
