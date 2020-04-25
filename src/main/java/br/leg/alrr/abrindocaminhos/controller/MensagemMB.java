package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Mensagem;
import br.leg.alrr.abrindocaminhos.persistence.MensagemDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Classe de gerenciamento das regras de negócio para a entidade Mensagem.
 *
 * @author Heliton Nascimento
 * @since 2020-02-07
 * @version 1.0
 * @see Mensagem
 * @see MensagemDAO
 */
@ViewScoped
@Named
public class MensagemMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MensagemDAO mensagemDAO;
    
    
    private List<Mensagem> mensagens;

    private Mensagem mensagem;
    
    private boolean excluirMensagem;


    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
        
        listarTodasAsMensgens();
    }

    private void iniciar() {
        excluirMensagem = false;

        mensagem = new Mensagem();
        mensagem.setStatus(true);
        
        mensagens = new ArrayList<>();
    }

    @PreDestroy
    public void finalizar() {
        mensagem = null;
        mensagens = null;
    }

    public String salvarMensagem() {
        try {
            if (mensagem.getId() != null) {
                mensagemDAO.atualizar(mensagem);
                FacesUtils.addInfoMessageFlashScoped("Mensagem atualizada com sucesso!");
            } else {
                mensagemDAO.salvar(mensagem);
                FacesUtils.addInfoMessageFlashScoped("Mensagem salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "mensagem.xhtml" + "?faces-redirect=true";
    }

    private void listarTodasAsMensgens() {
        try {
            mensagens = mensagemDAO.listarTodos();
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    

    public String excluirMensagem() {
        try {
            if (excluirMensagem) {
                mensagemDAO.remover(mensagem);
                FacesUtils.addInfoMessageFlashScoped("Mensagem excluída com sucesso!");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "mensagem.xhtml" + "?faces-redirect=true";
    }
    
    public String cancelar() {
        return "mensagem.xhtml" + "?faces-redirect=true";
    }
    
    //==========================================================================

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }
    
    public boolean isExcluirMensagem() {
        return excluirMensagem;
    }

    public void setExcluirMensagem(boolean excluirMensagem) {
        this.excluirMensagem = excluirMensagem;
    }
    
    
}
