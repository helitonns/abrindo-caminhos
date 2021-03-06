package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Bairro;
import br.leg.alrr.abrindocaminhos.model.Endereco;
import br.leg.alrr.abrindocaminhos.model.Municipio;
import br.leg.alrr.abrindocaminhos.model.Unidade;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.BairroDAO;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MunicipioDAO;
import br.leg.alrr.abrindocaminhos.persistence.UnidadeDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author heliton
 */
@ViewScoped
@Named
public class UnidadeMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UnidadeDAO unidadeDAO;

    @EJB
    private MunicipioDAO municipioDAO;

    @EJB
    private BairroDAO bairroDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private ArrayList<Unidade> unidades;
    private ArrayList<Municipio> municipios;
    private ArrayList<Bairro> bairros;

    private Unidade unidade;
    private Endereco endereco;
    private Municipio municipio;
    private Bairro bairro;

    private Long idMunicipio = 0l;

    private boolean removerUnidade = false;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        listarMunicipio();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarUnidade() {
        try {
            endereco.setBairro(bairro);
            unidade.setEndereco(endereco);
            if (unidade.getId() != null) {
                unidadeDAO.atualizar(unidade);
                FacesUtils.addInfoMessageFlashScoped("Unidade atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método UnidadeMB.salvarUnidade() para atualizar a unidade "+ unidade.getId()+".");
            } else {
                unidadeDAO.salvar(unidade);
                FacesUtils.addInfoMessageFlashScoped("Unidade salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método UnidadeMB.salvarUnidade() para salvar a unidade "+ unidade.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "unidade.xhtml" + "?faces-redirect=true";
    }

    public void removerUnidade() {
        try {
            if (removerUnidade) {
                unidadeDAO.remover(unidade);
                FacesUtils.addInfoMessage("Unidade removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método UnidadeMB.removerUnidade() para excluir a unidade "+ unidade.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A unidade não pode ser excluída pois ainda está sendo referenciada.");
        }
        limparForm();
    }

    public void valueChanged(ValueChangeEvent event) {
        try {
            idMunicipio = Long.parseLong(event.getNewValue().toString());
            municipio.setId(idMunicipio);
            listarBairroPorMunicipio();
        } catch (NumberFormatException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarBairroPorMunicipio() {
        try {
            bairros = (ArrayList<Bairro>) bairroDAO.listarBairroPorMunicipio(municipio);
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

    private void listarUnidade() {
        try {
            unidades = (ArrayList<Unidade>) unidadeDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void limparForm() {
        unidade = new Unidade();
        endereco = new Endereco();
        municipio = new Municipio();
        bairro = new Bairro();
        idMunicipio = 0l;
        removerUnidade = false;

        unidades = new ArrayList<>();
        bairros = new ArrayList<>();
        listarUnidade();
    }

    public void prepararEdicao() {
        try {
            endereco = unidade.getEndereco();
            bairro = endereco.getBairro();
            municipio = endereco.getBairro().getMunicipio();
            idMunicipio = municipio.getId();
            listarBairroPorMunicipio();
        } catch (Exception e) {
        }
    }

    public String setarUnidade() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            u.setUnidade(unidade);
            FacesUtils.setBean("usuario", u);
            FacesUtils.addInfoMessage("Mudança de unidade feita com sucesso!");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro ao realizar mudança de unidade!");
        }
        return "unidade.xhtml?faces-redirect=true";
    }

    public String cancelar() {
        return "unidade.xhtml" + "?faces-redirect=true";
    }
//==========================================================================

    public ArrayList<Bairro> getBairros() {
        return bairros;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public ArrayList<Unidade> getUnidades() {
        return unidades;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public boolean isRemoverUnidade() {
        return removerUnidade;
    }

    public void setRemoverUnidade(boolean removerUnidade) {
        this.removerUnidade = removerUnidade;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

}
