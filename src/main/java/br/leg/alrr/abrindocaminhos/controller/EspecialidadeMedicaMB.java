package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.EspecialidadeMedica;
import br.leg.alrr.abrindocaminhos.persistence.EspecialidadeMedicaDAO;
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
public class EspecialidadeMedicaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EspecialidadeMedicaDAO especialidadeMedicaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private EspecialidadeMedica especialidadeMedica;

    private ArrayList<EspecialidadeMedica> especialidadesMedicas;

    private EspecialidadeMedica especialidadeMedicaSelecionada;

    private boolean removerEspecialidadeMedicaSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarEspecialidadeMedica() {
        try {
            if (especialidadeMedica.getId() != null) {
                especialidadeMedicaDAO.atualizar(especialidadeMedica);
                FacesUtils.addInfoMessageFlashScoped("EspecialidadeMedica atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método EspecialidadeMedicaMB.salvarEspecialidadeMedica() para atualizar a especialidade "+ especialidadeMedica.getId()+".");
            } else {
                especialidadeMedicaDAO.salvar(especialidadeMedica);
                FacesUtils.addInfoMessageFlashScoped("EspecialidadeMedica salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método EspecialidadeMedicaMB.salvarEspecialidadeMedica() para salvar a especialidade "+ especialidadeMedica.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "especialidade-medica.xhtml" + "?faces-redirect=true";
    }

    public void listarEspecialidadeMedicaes() {
        try {
            especialidadesMedicas = (ArrayList<EspecialidadeMedica>) especialidadeMedicaDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerEspecialidadeMedica() {
        try {
            if (removerEspecialidadeMedicaSelecionada) {
                especialidadeMedicaDAO.remover(especialidadeMedicaSelecionada);
                FacesUtils.addInfoMessage("EspecialidadeMedica removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método EspecialidadeMedicaMB.removerEspecialidadeMedica() para excluir a especialidade "+ especialidadeMedica.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A especialidade não pode ser excluída pois ainda está referenciada em prontuário.");
        }
        limparForm();
    }

    private void limparForm() {
        especialidadeMedica = new EspecialidadeMedica();
        especialidadeMedica.setStatus(true);
        especialidadesMedicas = new ArrayList<>();
        removerEspecialidadeMedicaSelecionada = false;
        listarEspecialidadeMedicaes();
    }

    public String cancelar() {
        return "especialidade-medica.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public EspecialidadeMedica getEspecialidadeMedica() {
        return especialidadeMedica;
    }

    public void setEspecialidadeMedica(EspecialidadeMedica especialidadeMedica) {
        this.especialidadeMedica = especialidadeMedica;
    }

    public ArrayList<EspecialidadeMedica> getEspecialidadesMedicas() {
        return especialidadesMedicas;
    }

    public EspecialidadeMedica getEspecialidadeMedicaSelecionada() {
        return especialidadeMedicaSelecionada;
    }

    public void setEspecialidadeMedicaSelecionada(EspecialidadeMedica especialidadeMedicaSelecionada) {
        this.especialidadeMedicaSelecionada = especialidadeMedicaSelecionada;
    }

    public boolean isRemoverEspecialidadeMedicaSelecionada() {
        return removerEspecialidadeMedicaSelecionada;
    }

    public void setRemoverEspecialidadeMedicaSelecionada(boolean removerEspecialidadeMedicaSelecionada) {
        this.removerEspecialidadeMedicaSelecionada = removerEspecialidadeMedicaSelecionada;
    }

}
