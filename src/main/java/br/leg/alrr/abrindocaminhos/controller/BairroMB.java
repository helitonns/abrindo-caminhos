package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Bairro;
import br.leg.alrr.abrindocaminhos.model.Municipio;
import br.leg.alrr.abrindocaminhos.persistence.BairroDAO;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MunicipioDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class BairroMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BairroDAO bairroDAO;

    @EJB
    private MunicipioDAO municipioDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Bairro bairro;
    private Municipio municipio;

    private ArrayList<Bairro> bairros;
    private ArrayList<Municipio> municipios;

    private boolean removerBairro = false;

    //==========================================================================
    @PostConstruct
    public void init() {
        listarMunicipio();
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarBairro() {
        try {
            bairros = (ArrayList<Bairro>) bairroDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarMunicipio() {
        try {
            municipios = (ArrayList<Municipio>) municipioDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarBairro() {
        try {
            bairro.setMunicipio(municipio);

            if (bairro.getId() != null) {
                bairroDAO.atualizar(bairro);
                FacesUtils.addInfoMessageFlashScoped("Bairro atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método BairroMB.salvarBairro() para atualizar o bairro "+ bairro.getId()+".");
            } else {
                bairroDAO.salvar(bairro);
                FacesUtils.addInfoMessageFlashScoped("Bairro salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método BairroMB.salvarBairro() para salvar o bairro "+ bairro.getId()+".");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "bairro.xhtml" + "?faces-redirect=true";
    }

    public void removerBairro() {
        try {
            if (removerBairro) {
                bairroDAO.remover(bairro);
                FacesUtils.addInfoMessage("Bairro removido com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método BairroMB.removerBairro() para excluir o bairro "+ bairro.getId()+".");
            }
            limparForm();
        } catch (Exception e) {
            FacesUtils.addErrorMessage("O bairro não pode ser excluído pois ainda estáreferenciado em endereço.");
        }
    }

    private void limparForm() {
        municipio = new Municipio();
        bairro = new Bairro();
        bairros = new ArrayList<>();
        removerBairro = false;
        listarBairro();
    }

    public String cancelar() {
        return "bairro.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public ArrayList<Bairro> getBairros() {
        return bairros;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public boolean isRemoverBairro() {
        return removerBairro;
    }

    public void setRemoverBairro(boolean removerBairro) {
        this.removerBairro = removerBairro;
    }

}
