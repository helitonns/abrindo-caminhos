package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Inscricao;
import br.leg.alrr.abrindocaminhos.model.ListaDeEspera;
import br.leg.alrr.abrindocaminhos.model.Usuario;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.AlunoDAO;
import br.leg.alrr.abrindocaminhos.persistence.InscricaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.ListaDeEsperaDAO;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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
public class InscricaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AlunoDAO alunoDAO;

    @EJB
    private InscricaoDAO inscricaoDAO;

    @EJB
    private ListaDeEsperaDAO listaDeEsperaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private ArrayList<ListaDeEspera> listas;

    private ArrayList<Aluno> alunos;
    private ArrayList<Inscricao> alunosJaInscritos;

    private ListaDeEspera lista;
    private Aluno aluno;
    private Inscricao inscricao;

    private boolean mostrarAlunosJaInscritos;
    private boolean cancelaInscricao;
    
//==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        // VERIFICA SE HÁ ALGUM ALUNO NA SESSÃO PARA SER MATRICULADO
        try {
            if (FacesUtils.getBean("aluno") != null) {
                alunos.add((Aluno) FacesUtils.getBean("aluno"));
                FacesUtils.removeBean("aluno");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro ao tentar matricular aluno.");
        }
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarListasIniciadas() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            listas = (ArrayList<ListaDeEspera>) listaDeEsperaDAO.listarListaDeEsperasIniciadasPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void pesquisarAluno() {
        try {
            alunos = new ArrayList<>();

            if (aluno.getId() != null) {
                Aluno a = alunoDAO.pesquisarPorID(aluno.getId());
                alunos.add(a);
            } else if (aluno.getCpf() != null && !aluno.getCpf().isEmpty()) {
                Aluno a = alunoDAO.pesquisarPorCPF(aluno.getCpf());
                alunos.add(a);
            } else if (aluno.getNome() != null && !aluno.getNome().isEmpty()) {
                alunos = (ArrayList<Aluno>) alunoDAO.pesquisarPorNome(aluno.getNome());
            } else {
                FacesUtils.addWarnMessageFlashScoped("Indique o código, o CPF ou o nome do aluno!");
            }
        } catch (DAOException e) {
            FacesUtils.addWarnMessageFlashScoped("Sem resultado!");
        }
        aluno = new Aluno();
    }

    public String inscrever() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            inscricao.setUnidade(u.getUnidade());

            inscricao.setAluno(aluno);
            inscricao.setListaDeEspera(lista);

            //atualizar
            if (inscricao.getId() != null) {
                inscricaoDAO.atualizar(inscricao);
                FacesUtils.addInfoMessageFlashScoped("Inscrição atualizada com sucesso!!!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método InscricaoMB.inscrever() para atualizar a inscrição "+ inscricao.getId()+".");
            } //salvar
            else {
                GregorianCalendar gc = new GregorianCalendar();
                inscricao.setDataDaInscricao(gc.getTime());
                inscricao.setStatus(true);

                boolean b = inscricaoDAO.podeInscrever(lista.getId(), aluno.getId());

                if (b) {
                    inscricaoDAO.salvar(inscricao);
                    FacesUtils.addInfoMessageFlashScoped("Inscrição salva com sucesso!!!");
                    Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método InscricaoMB.inscrever() para salvar a inscrição "+ inscricao.getId()+".");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("O aluno já está inscrito nesta lista!");
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage() + ": " + e.getCause().toString());
        }
        return "inscricao.xhtml" + "?faces-redirect=true";
    }

    public void cancelarInscricao() {
        try {
            if (cancelaInscricao) {
                inscricaoDAO.remover(inscricao);
                FacesUtils.addInfoMessage("Inscrição cancelada com sucesso!!!");
                Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método InscricaoMB.cancelarInscricao() para excluir a inscricao "+inscricao.getId()+".");
                limparForm();
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro ao cacelar matrícula!!!");
        }
    }

    private void limparForm() {
        alunos = new ArrayList<>();
        listas = new ArrayList<>();

        aluno = new Aluno();
        inscricao = new Inscricao();
        inscricao.setStatus(true);

        listarListasIniciadas();
        mostrarAlunosJaInscritos = false;
        cancelaInscricao = false;
    }

    public void selecionarLista(ValueChangeEvent event) {
        try {
            ListaDeEspera l = (ListaDeEspera) event.getNewValue();
            alunosJaInscritos = (ArrayList<Inscricao>) inscricaoDAO.listarInscricoesPorOrdemDeInscricao(l.getId());
            mostrarAlunosJaInscritos = true;
        } catch (DAOException | NumberFormatException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String cancelar() {
        return "inscricao.xhtml" + "?faces-redirect=true";
    }

    public String enviarAlunoParaMatricula() {
        try {
            
            
//            if (!inscricaoDAO.verificarSeHaInscricoesAnteriores(inscricao)) {
                FacesUtils.setBean("inscricao", inscricao);
                return "matricula.xhtml" + "?faces-redirect=true";
//            }else{
//                FacesUtils.addWarnMessageFlashScoped("A matrícula a partir da lista de espera deve obedecer, rigorosamente, a ordem de inscrição!");
//                return "inscricao.xhtml" + "?faces-redirect=true";
//            }
        } catch (Exception ex) {
            FacesUtils.addErrorMessageFlashScoped(ex.getCause().toString());
        }
        return "inscricao.xhtml" + "?faces-redirect=true";
    }
//==========================================================================

    public ListaDeEspera getLista() {
        return lista;
    }

    public void setLista(ListaDeEspera lista) {
        this.lista = lista;
    }

    public ArrayList<ListaDeEspera> getListas() {
        return listas;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public ArrayList<Inscricao> getAlunosJaMatriculados() {
        return alunosJaInscritos;
    }

    public boolean isMostrarAlunosJaInscritos() {
        return mostrarAlunosJaInscritos;
    }

    public Inscricao getMatricula() {
        return inscricao;
    }

    public void setMatricula(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

    public boolean isCancelaInscricao() {
        return cancelaInscricao;
    }

    public void setCancelaInscricao(boolean cancelaInscricao) {
        this.cancelaInscricao = cancelaInscricao;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

}
