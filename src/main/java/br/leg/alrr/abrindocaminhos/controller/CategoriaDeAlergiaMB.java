package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.CategoriaDeAlergia;
import br.leg.alrr.abrindocaminhos.persistence.CategoriaDeAlergiaDAO;
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
public class CategoriaDeAlergiaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CategoriaDeAlergiaDAO categoriaDeAlergiaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private CategoriaDeAlergia categoriaDeAlergia;

    private ArrayList<CategoriaDeAlergia> categoriasDeAlergias;

    private CategoriaDeAlergia categoriaDeAlergiaSelecionada;

    private boolean removerCategoriaDeAlergiaSelecionada = false;

    // ==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public String salvarCategoria() {
        try {
            if (categoriaDeAlergia.getId() != null) {
                categoriaDeAlergiaDAO.atualizar(categoriaDeAlergia);
                FacesUtils.addInfoMessageFlashScoped("Categoria atualizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método CategoriaAtividadeMB.salvarCategoria() para atualizar a categoria de alergia "+ categoriaDeAlergia.getId()+".");
            } else {
                categoriaDeAlergiaDAO.salvar(categoriaDeAlergia);
                FacesUtils.addInfoMessageFlashScoped("Categoria salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método CategoriaAtividadeMB.salvarCategoria() para salvar a categoria de alergia "+ categoriaDeAlergia.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "categoria-alergia.xhtml" + "?faces-redirect=true";
    }

    public void listarCategorias() {
        try {
            categoriasDeAlergias = (ArrayList<CategoriaDeAlergia>) categoriaDeAlergiaDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void removerCategoria() {
        try {
            if (removerCategoriaDeAlergiaSelecionada) {
                categoriaDeAlergiaDAO.remover(categoriaDeAlergiaSelecionada);
                FacesUtils.addInfoMessage("Categoria removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método CategoriaAtividadeMB.removerCategoria() para excluir a categoria de alergia "+ categoriaDeAlergia.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A categoria não pode ser excluída porque há ainda está referenciada em alergias.");
        }
        limparForm();
    }

    private void limparForm() {
        categoriaDeAlergia = new CategoriaDeAlergia();
        categoriaDeAlergia.setStatus(true);
        categoriasDeAlergias = new ArrayList<>();
        removerCategoriaDeAlergiaSelecionada = false;
        listarCategorias();
    }

    public String cancelar() {
        return "categoria-alergia.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public CategoriaDeAlergia getCategoriaDeAlergia() {
        return categoriaDeAlergia;
    }

    public void setCategoriaDeAlergia(CategoriaDeAlergia categoriaDeAlergia) {
        this.categoriaDeAlergia = categoriaDeAlergia;
    }

    public CategoriaDeAlergia getCategoriaDeAlergiaSelecionada() {
        return categoriaDeAlergiaSelecionada;
    }

    public void setCategoriaDeAlergiaSelecionada(CategoriaDeAlergia categoriaDeAlergiaSelecionada) {
        this.categoriaDeAlergiaSelecionada = categoriaDeAlergiaSelecionada;
    }

    public boolean isRemoverCategoriaDeAlergiaSelecionada() {
        return removerCategoriaDeAlergiaSelecionada;
    }

    public void setRemoverCategoriaDeAlergiaSelecionada(boolean removerCategoriaDeAlergiaSelecionada) {
        this.removerCategoriaDeAlergiaSelecionada = removerCategoriaDeAlergiaSelecionada;
    }

    public ArrayList<CategoriaDeAlergia> getCategoriasDeAlergias() {
        return categoriasDeAlergias;
    }
   
    

}
