package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Sindrome;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.SindromeDAO;
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
public class SindromeMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SindromeDAO sindromeDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Sindrome sindrome;

    private ArrayList<Sindrome> sindromes;

    private Sindrome sindromeSelecionada;

    private boolean removerSindromeSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarSindrome() {
        try {
            if (sindrome.getId() != null) {
                sindromeDAO.atualizar(sindrome);
                FacesUtils.addInfoMessageFlashScoped("Sindrome atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método SindromeMB.salvarSindrome() para atualizar a síndrome "+ sindrome.getId()+".");
            } else {
                sindromeDAO.salvar(sindrome);
                FacesUtils.addInfoMessageFlashScoped("Sindrome salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método SindromeMB.salvarSindrome() para apagar a síndrome "+ sindrome.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "sindrome.xhtml" + "?faces-redirect=true";
    }

    public void listarSindromees() {
        try {
            sindromes = (ArrayList<Sindrome>) sindromeDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerSindrome() {
        try {
            if (removerSindromeSelecionada) {
                sindromeDAO.remover(sindromeSelecionada);
                FacesUtils.addInfoMessage("Sindrome removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método SindromeMB.salvarSindrome() para excluir a síndrome "+ sindrome.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A síndorme não pode ser excluída pois ainda está referenciada em prontuário.");
        }
        limparForm();
    }

    private void limparForm() {
        sindrome = new Sindrome();
        sindrome.setStatus(true);
        sindromes = new ArrayList<>();
        removerSindromeSelecionada = false;
        listarSindromees();
    }

    public String cancelar() {
        return "sindrome.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Sindrome getSindrome() {
        return sindrome;
    }

    public void setSindrome(Sindrome sindrome) {
        this.sindrome = sindrome;
    }

    public ArrayList<Sindrome> getSindromes() {
        return sindromes;
    }

    public Sindrome getSindromeSelecionada() {
        return sindromeSelecionada;
    }

    public void setSindromeSelecionada(Sindrome alergiaSelecionada) {
        this.sindromeSelecionada = alergiaSelecionada;
    }

    public boolean isRemoverSindromeSelecionada() {
        return removerSindromeSelecionada;
    }

    public void setRemoverSindromeSelecionada(boolean removerSindromeSelecionada) {
        this.removerSindromeSelecionada = removerSindromeSelecionada;
    }

}
