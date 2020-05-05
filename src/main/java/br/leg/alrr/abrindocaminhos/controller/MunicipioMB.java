package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Municipio;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MunicipioDAO;
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
public class MunicipioMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MunicipioDAO municipioDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Municipio municipio;

    private ArrayList<Municipio> municipios;

    private Municipio municipioSelecionado;

    private boolean removerMunicipio = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarMunicipio() {
        try {
            if (municipio.getId() != null) {
                municipioDAO.atualizar(municipio);
                FacesUtils.addInfoMessageFlashScoped("Município atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método MunicipioMB.salvarMunicipio() para atualizar o município "+ municipio.getId()+".");
            } else {
                municipioDAO.salvar(municipio);
                FacesUtils.addInfoMessageFlashScoped("Município salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método MunicipioMB.salvarMunicipio() para salvar o município "+ municipio.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "municipio.xhtml" + "?faces-redirect=true";
    }

    public void listarMunicipios() {
        try {
            municipios = (ArrayList<Municipio>) municipioDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerMunicipio() {
        try {
            if (removerMunicipio) {
                municipioDAO.remover(municipioSelecionado);
                FacesUtils.addInfoMessage("Município removido com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método MunicipioMB.removerMunicipio() para excluir o município "+ municipio.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("O município não pode ser excluído pois ainda está referenciado em bairro.");
        }
        limparForm();
    }

    private void limparForm(){
        municipio = new Municipio();
        municipios = new ArrayList<>();
        listarMunicipios();
        removerMunicipio = false;
    }
    
    public String cancelar() {
        return "municipio.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public Municipio getMunicipioSelecionado() {
        return municipioSelecionado;
    }

    public void setMunicipioSelecionado(Municipio municipioSelecionado) {
        this.municipioSelecionado = municipioSelecionado;
    }

    public boolean isRemoverMunicipio() {
        return removerMunicipio;
    }

    public void setRemoverMunicipio(boolean removerMunicipio) {
        this.removerMunicipio = removerMunicipio;
    }

}
