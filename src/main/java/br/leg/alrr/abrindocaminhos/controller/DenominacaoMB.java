package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Denominacao;
import br.leg.alrr.abrindocaminhos.persistence.DenominacaoDAO;
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
public class DenominacaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private DenominacaoDAO denominacaoDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Denominacao denominacao;

    private ArrayList<Denominacao> denominacoes;

    private Denominacao denominacaoSelecionada;

    private boolean removerDenominacaoSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarDenominacao() {
        try {
            if (denominacao.getId() != null) {
                denominacaoDAO.atualizar(denominacao);
                FacesUtils.addInfoMessageFlashScoped("Denominação atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método DenominacaoMB.salvarDenominacao() para atualizar a denominação "+ denominacao.getId()+".");
            } else {
                denominacaoDAO.salvar(denominacao);
                FacesUtils.addInfoMessageFlashScoped("Denominação salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método DenominacaoMB.salvarDenominacao() para salvar a denominação "+ denominacao.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "denominacao.xhtml" + "?faces-redirect=true";
    }

    public void listarDenominacaoes() {
        try {
            denominacoes = (ArrayList<Denominacao>) denominacaoDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerDenominacao() {
        try {
            if (removerDenominacaoSelecionada) {
                denominacaoDAO.remover(denominacaoSelecionada);
                FacesUtils.addInfoMessage("Denominação removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método DenominacaoMB.removerDenominacao() para excluir a denominação "+ denominacao.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A esfera de atuação não pode ser excluída pois ainda está referenciada em escola.");
        }
        limparForm();
    }

    private void limparForm() {
        denominacao = new Denominacao();
        denominacao.setStatus(true);
        denominacoes = new ArrayList<>();
        removerDenominacaoSelecionada = false;
        listarDenominacaoes();
    }

    public String cancelar() {
        return "denominacao.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Denominacao getDenominacao() {
        return denominacao;
    }

    public void setDenominacao(Denominacao denominacao) {
        this.denominacao = denominacao;
    }

    public ArrayList<Denominacao> getDenominacoes() {
        return denominacoes;
    }

    public Denominacao getDenominacaoSelecionada() {
        return denominacaoSelecionada;
    }

    public void setDenominacaoSelecionada(Denominacao denominacaoSelecionada) {
        this.denominacaoSelecionada = denominacaoSelecionada;
    }

    public boolean isRemoverDenominacaoSelecionada() {
        return removerDenominacaoSelecionada;
    }

    public void setRemoverDenominacaoSelecionada(boolean removerDenominacaoSelecionada) {
        this.removerDenominacaoSelecionada = removerDenominacaoSelecionada;
    }

}
