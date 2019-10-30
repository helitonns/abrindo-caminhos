package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Escola;
import br.leg.alrr.abrindocaminhos.model.Denominacao;
import br.leg.alrr.abrindocaminhos.model.Usuario;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.EscolaDAO;
import br.leg.alrr.abrindocaminhos.persistence.DenominacaoDAO;
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
public class EscolaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EscolaDAO escolaDAO;

    @EJB
    private DenominacaoDAO denominacaoDAO;

    private Escola escola;
    private Denominacao denominacao;

    private ArrayList<Escola> escolas;
    private ArrayList<Denominacao> denominacoes;

    private boolean removerEscola = false;
    private Long idDenominacao = (long) 0;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
        listarDenominacao();
    }

    private void listarEscola() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            escolas = (ArrayList<Escola>) escolaDAO.listarTodosPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarDenominacao() {
        try {
            denominacoes = (ArrayList<Denominacao>) denominacaoDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarEscola() {
        try {
            Denominacao d = denominacaoDAO.buscarPorID(idDenominacao);
            escola.setDenominacao(d);

            if (escola.getId() != null) {
                escolaDAO.atualizar(escola);
                FacesUtils.addInfoMessageFlashScoped("Escola atualizada com sucesso!");
            } else {
                UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
                escola.setUnidade(u.getUnidade());
                escolaDAO.salvar(escola);
                FacesUtils.addInfoMessageFlashScoped("Escola salva com sucesso!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "escola.xhtml" + "?faces-redirect=true";
    }

    public void salvarEscola2() {
        try {
            Denominacao d = denominacaoDAO.buscarPorID(idDenominacao);
            escola.setDenominacao(d);

            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            escola.setUnidade(u.getUnidade());

            escolaDAO.salvar(escola);
            FacesUtils.addInfoMessageFlashScoped("Escola salva com sucesso!");
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

    public void removerEscola() {
        try {
            if (removerEscola) {
                escolaDAO.remover(escola);
                FacesUtils.addInfoMessage("Escola removida com sucesso!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        denominacao = new Denominacao();
        escola = new Escola();
        escola.setStatus(true);
        escolas = new ArrayList<>();
        removerEscola = false;
        idDenominacao = 0l;
        listarEscola();
    }

    public String cancelar() {
        return "escola.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public ArrayList<Escola> getEscolas() {
        return escolas;
    }

    public ArrayList<Denominacao> getDenominacoes() {
        return denominacoes;
    }

    public Denominacao getDenominacao() {
        return denominacao;
    }

    public void setDenominacao(Denominacao denominacao) {
        this.denominacao = denominacao;
    }

    public boolean isRemoverEscola() {
        return removerEscola;
    }

    public void setRemoverEscola(boolean removerEscola) {
        this.removerEscola = removerEscola;
    }

    public Long getIdDenominacao() {
        return idDenominacao;
    }

    public void setIdDenominacao(Long idDenominacao) {
        this.idDenominacao = idDenominacao;
    }

}
