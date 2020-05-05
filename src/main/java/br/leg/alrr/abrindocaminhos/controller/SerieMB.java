package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Serie;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.SerieDAO;
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
public class SerieMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SerieDAO serieDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private Serie serie;

    private ArrayList<Serie> series;

    private Serie serieSelecionada;

    private boolean removerSerieSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarSerie() {
        try {
            if (serie.getId() != null) {
                serieDAO.atualizar(serie);
                FacesUtils.addInfoMessageFlashScoped("Série atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método SerieMB.salvarSerie() para atualizar a série "+ serie.getId()+".");
            } else {
                serieDAO.salvar(serie);
                FacesUtils.addInfoMessageFlashScoped("Série salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método SerieMB.salvarSerie() para salvar a série "+ serie.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "serie.xhtml" + "?faces-redirect=true";
    }

    public void listarSeriees() {
        try {
            series = (ArrayList<Serie>) serieDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerSerie() {
        try {
            if (removerSerieSelecionada) {
                serieDAO.remover(serieSelecionada);
                FacesUtils.addInfoMessage("Série removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método SerieMB.removerSerie() para excluir série a série "+ serie.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A série não pode ser excluída pois ainda está referenciada em instrução.");
        }
        limparForm();
    }

    private void limparForm() {
        serie = new Serie();
        serie.setStatus(true);
        series = new ArrayList<>();
        removerSerieSelecionada = false;
        listarSeriees();
    }

    public String cancelar() {
        return "serie.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public ArrayList<Serie> getSeries() {
        return series;
    }

    public Serie getSerieSelecionada() {
        return serieSelecionada;
    }

    public void setSerieSelecionada(Serie serieSelecionada) {
        this.serieSelecionada = serieSelecionada;
    }

    public boolean isRemoverSerieSelecionada() {
        return removerSerieSelecionada;
    }

    public void setRemoverSerieSelecionada(boolean removerSerieSelecionada) {
        this.removerSerieSelecionada = removerSerieSelecionada;
    }

}
