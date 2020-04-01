package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.EspecialidadeMedica;
import br.leg.alrr.abrindocaminhos.persistence.EspecialidadeMedicaDAO;
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

    private EspecialidadeMedica especialidadeMedica;

    private ArrayList<EspecialidadeMedica> especialidadesMedicas;

    private EspecialidadeMedica especialidadeMedicaSelecionada;

    private boolean removerEspecialidadeMedicaSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }

    public String salvarEspecialidadeMedica() {
        try {
            if (especialidadeMedica.getId() != null) {
                especialidadeMedicaDAO.atualizar(especialidadeMedica);
                FacesUtils.addInfoMessageFlashScoped("EspecialidadeMedica atualizado com sucesso!");
            } else {
                especialidadeMedicaDAO.salvar(especialidadeMedica);
                FacesUtils.addInfoMessageFlashScoped("EspecialidadeMedica salva com sucesso!");
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
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
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
