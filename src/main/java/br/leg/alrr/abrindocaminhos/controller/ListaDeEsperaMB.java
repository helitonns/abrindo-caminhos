package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Atividade;
import br.leg.alrr.abrindocaminhos.model.ListaDeEspera;
import br.leg.alrr.abrindocaminhos.model.LogSistema;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.AtividadeDAO;
import br.leg.alrr.abrindocaminhos.persistence.InscricaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.ListaDeEsperaDAO;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
public class ListaDeEsperaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ListaDeEsperaDAO listaDeEsperaDAO;

    @EJB
    private InscricaoDAO inscricaoDAO;

    @EJB
    private AtividadeDAO atividadeDAO;

    @EJB
    private LogSistemaDAO logSistemaDAO;

    private ArrayList<Atividade> atividades;
    private ArrayList<ListaDeEspera> listas;

    private ListaDeEspera listaDeEspera;

    private boolean removerLista;
    private boolean finalizarLista;
    private Long idAtividade;

//==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL() + ".");
    }

    private void listarAtividades() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            atividades = (ArrayList<Atividade>) atividadeDAO.listarAtivasPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarListasIniciadasPorUnidade() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            listas = (ArrayList<ListaDeEspera>) listaDeEsperaDAO.listarListaDeEsperasIniciadasPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarLista() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");

            Atividade a = atividadeDAO.buscarPorID(idAtividade);

            listaDeEspera.setUnidade(u.getUnidade());
            listaDeEspera.setAtividade(a);

            if (listaDeEspera.getId() != null) {
                listaDeEsperaDAO.atualizar(listaDeEspera);
                FacesUtils.addInfoMessageFlashScoped("Lista de espera atulizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método ListaDesEsperaMB.salvarLista() para atualizar a lista "+ listaDeEspera.getId()+".");
            } else {
                listaDeEsperaDAO.salvar(listaDeEspera);
                FacesUtils.addInfoMessageFlashScoped("Lista de espera salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ListaDesEsperaMB.salvarLista() para salvar a lista "+ listaDeEspera.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "lista-de-espera.xhtml" + "?faces-redirect=true";
    }

    public void concluirTurma() {
        try {
            listaDeEspera.setIniciada(false);
            listaDeEsperaDAO.atualizar(listaDeEspera);
            limparForm();
            FacesUtils.addInfoMessage("Lista de espera concluída com sucesso!");
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void limparForm() {
        listaDeEspera = new ListaDeEspera();
        listaDeEspera.setStatus(true);
        listaDeEspera.setIniciada(true);

        removerLista = false;

        listarAtividades();
        listarListasIniciadasPorUnidade();
    }

    public void removerLista() {
        try {
            if (removerLista) {
                listaDeEsperaDAO.remover(listaDeEspera);
                FacesUtils.addInfoMessage("Lista de espera removida com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método ListaDesEsperaMB.removerLista() para remover a lista "+ listaDeEspera.getId()+".");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("A lista de espera não pode ser excluída pois ainda está sendo referenciada em inscrição.");
        }
        limparForm();
    }

    public void finalizarLista() {
        try {
            if (finalizarLista) {
                //Primeiro finlaiza as incrições da lista
                inscricaoDAO.finalizarInscricaoPorLista(listaDeEspera);

                //Depois finaliza a turma
                listaDeEspera.setIniciada(false);
                listaDeEsperaDAO.atualizar(listaDeEspera);
                FacesUtils.addInfoMessage("Lista finalizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método ListaDesEsperaMB.finalizarLista() para atualizar a lista "+ listaDeEspera.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    public String cancelar() {
        return "lista-de-espera.xhtml" + "?faces-redirect=true";
    }
//==========================================================================

    public ListaDeEspera getListaDeEspera() {
        return listaDeEspera;
    }

    public void setListaDeEspera(ListaDeEspera listaDeEspera) {
        this.listaDeEspera = listaDeEspera;
    }

    public boolean isRemoverLista() {
        return removerLista;
    }

    public void setRemoverLista(boolean removerLista) {
        this.removerLista = removerLista;
    }

    public ArrayList<Atividade> getAtividades() {
        return atividades;
    }

    public Long getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Long idAtividade) {
        this.idAtividade = idAtividade;
    }

    public boolean isFinalizarLista() {
        return finalizarLista;
    }

    public void setFinalizarLista(boolean finalizarLista) {
        this.finalizarLista = finalizarLista;
    }

    public ArrayList<ListaDeEspera> getListas() {
        return listas;
    }

}
