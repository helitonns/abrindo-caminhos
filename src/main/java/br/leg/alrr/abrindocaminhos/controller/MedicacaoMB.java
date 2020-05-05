package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Medicacao;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MedicacaoDAO;
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
public class MedicacaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MedicacaoDAO medicacaoDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Medicacao medicacao;

    private ArrayList<Medicacao> medicacoes;

    private Medicacao medicacaoSelecionada;

    private boolean removerMedicacaoSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarMedicacao() {
        try {
            if (medicacao.getId() != null) {
                medicacaoDAO.atualizar(medicacao);
                FacesUtils.addInfoMessageFlashScoped("Medicação atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método MedicacaoMB.salvarMedicacao() para atualizar a medicação "+ medicacao.getId()+".");
            } else {
                medicacaoDAO.salvar(medicacao);
                FacesUtils.addInfoMessageFlashScoped("Medicação salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método MedicacaoMB.salvarMedicacao() para salvar a medicação "+ medicacao.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "medicacao.xhtml" + "?faces-redirect=true";
    }

    public void listarMedicacaoes() {
        try {
            medicacoes = (ArrayList<Medicacao>) medicacaoDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerMedicacao() {
        try {
            if (removerMedicacaoSelecionada) {
                medicacaoDAO.remover(medicacaoSelecionada);
                FacesUtils.addInfoMessage("Medicação removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método MedicacaoMB.removerMedicacao() para excluir a medicação "+ medicacao.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A medicação não pode ser excluída pois ainda está referenciada em prontuário.");
        }
        limparForm();
    }

    private void limparForm() {
        medicacao = new Medicacao();
        medicacao.setStatus(true);
        medicacoes = new ArrayList<>();
        removerMedicacaoSelecionada = false;
        listarMedicacaoes();
    }

    public String cancelar() {
        return "medicacao.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Medicacao getMedicacao() {
        return medicacao;
    }

    public void setMedicacao(Medicacao medicacao) {
        this.medicacao = medicacao;
    }

    public ArrayList<Medicacao> getMedicacaoes() {
        return medicacoes;
    }

    public Medicacao getMedicacaoSelecionada() {
        return medicacaoSelecionada;
    }

    public void setMedicacaoSelecionada(Medicacao medicacaoSelecionada) {
        this.medicacaoSelecionada = medicacaoSelecionada;
    }

    public boolean isRemoverMedicacaoSelecionada() {
        return removerMedicacaoSelecionada;
    }

    public void setRemoverMedicacaoSelecionada(boolean removerMedicacaoSelecionada) {
        this.removerMedicacaoSelecionada = removerMedicacaoSelecionada;
    }

   

}
