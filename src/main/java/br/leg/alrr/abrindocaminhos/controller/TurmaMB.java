package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.DiasDaSemana;
import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Atividade;
import br.leg.alrr.abrindocaminhos.model.Horario;
import br.leg.alrr.abrindocaminhos.model.Idade;
import br.leg.alrr.abrindocaminhos.model.Turma;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.AtividadeDAO;
import br.leg.alrr.abrindocaminhos.persistence.HorarioDAO;
import br.leg.alrr.abrindocaminhos.persistence.IdadeDAO;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MatriculaDAO;
import br.leg.alrr.abrindocaminhos.persistence.TurmaDAO;
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
public class TurmaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private HorarioDAO horarioDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private AtividadeDAO atividadeDAO;

    @EJB
    private IdadeDAO idadeDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private ArrayList<Horario> horarios;
    private ArrayList<Atividade> atividades;
    private ArrayList<Turma> turmas;
    private ArrayList<String> diasDaSemana;
    private ArrayList<String> diasDaSemanaSelecionados;
    private ArrayList<Idade> idades;

    private Turma turma;

    private boolean removerTurma;
    private boolean finalizarTurma;
    private Long idAtividade;
    private Long idHorario;

//==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        diasDaSemana.add("SEG");
        diasDaSemana.add("TER");
        diasDaSemana.add("QUA");
        diasDaSemana.add("QUI");
        diasDaSemana.add("SEX");
        diasDaSemana.add("SAB");
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarHorarios() {
        try {
            horarios = (ArrayList<Horario>) horarioDAO.listarAtivos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarAtividades() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            atividades = (ArrayList<Atividade>) atividadeDAO.listarAtivasPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarTurmasIniciadasPorUnidade() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadasPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarIdadesAtivas() {
        try {
            idades = (ArrayList<Idade>) idadeDAO.listarAtivas();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarTurma() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");

            Horario h = horarioDAO.buscarPorID(idHorario);
            Atividade a = atividadeDAO.buscarPorID(idAtividade);

            turma.setUnidade(u.getUnidade());
            turma.setHorario(h);
            turma.setAtividade(a);
            turma.setDiasDaSemana(prepararInclusaoDiasDaSemana());

            if (turma.getId() != null) {
                turmaDAO.atualizar(turma);
                FacesUtils.addInfoMessageFlashScoped("Turma atulizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método TurmaMB.salvarTurma() para atualizar a turma "+ turma.getId()+".");
            } else {
                turmaDAO.salvar(turma);
                FacesUtils.addInfoMessageFlashScoped("Turma salva com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método TurmaMB.salvarTurma() para salvar a turma "+ turma.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "turma.xhtml" + "?faces-redirect=true";
    }

    public void concluirTurma() {
        try {
            turma.setIniciada(false);
            turmaDAO.atualizar(turma);
            limparForm();
            FacesUtils.addInfoMessage("Turma concluída com sucesso!");
            Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método TurmaMB.concluirTurma() para finalizar a turma "+ turma.getId()+".");
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void limparForm() {
        turma = new Turma();
        turma.setStatus(true);
        turma.setIniciada(true);

        horarios = new ArrayList<>();
        diasDaSemana = new ArrayList<>();
        diasDaSemanaSelecionados = new ArrayList<>();
        turmas = new ArrayList<>();
        idades = new ArrayList<>();

        removerTurma = false;

        listarHorarios();
        listarAtividades();
        listarTurmasIniciadasPorUnidade();
        listarIdadesAtivas();
    }

    private String prepararInclusaoDiasDaSemana() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < diasDaSemanaSelecionados.size(); i++) {
            DiasDaSemana d = DiasDaSemana.getDiasDaSemanaPelaAbreviacao(diasDaSemanaSelecionados.get(i));
            sb.append(d.getValue());

            if ((i + 1) >= diasDaSemanaSelecionados.size()) {
            } else {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public void prepararEdicaoDeTurma() {
        try {
            // TRANSFORMANDO A STRING EM UM ARRAY PARA PEGAR CADA DIA DA SEMANA SEPARADAMENTE
            String[] s = turma.getDiasDaSemana().split(",");
            for (String item : s) {
                DiasDaSemana d = DiasDaSemana.fromValue(item.trim());
                diasDaSemanaSelecionados.add(d.getAbreviacao());
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro ao preparar edição de turma.");
        }
    }

    public void removerTurma() {
        try {
            if (removerTurma) {
                if (!matriculaDAO.haMatriculaNaTurma(turma.getId())) {
                    turmaDAO.remover(turma);
                    FacesUtils.addInfoMessage("Turma removida com sucesso!");
                    Loger.registrar(logSistemaDAO, TipoAcao.APAGAR, "O usuário executou o método TurmaMB.removerTurma() para deletar a turma "+ turma.getId()+".");
                }else{
                    FacesUtils.addWarnMessage("A turma não pode ser apagada porque há matrículas a ela vinculadas!!!");
                }

            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage(e.getMessage());
            System.out.println(e.getCause());
        }
        limparForm();
    }

    public void finalizarTurma() {
        try {
            if (finalizarTurma) {
                //Primeiro finlaiza as matrículas da turma
                matriculaDAO.finalizarMatriculaPorTurma(turma);
                //Depois finaliza a turma
                turma.setIniciada(false);
                turmaDAO.atualizar(turma);
                FacesUtils.addInfoMessage("Turma finalizada com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método TurmaMB.concluirTurma() para finalizar a turma "+ turma.getId()+".");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }

    public String cancelar() {
        return "turma.xhtml" + "?faces-redirect=true";
    }
//==========================================================================

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public ArrayList<String> getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(ArrayList<String> diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
    }

    public ArrayList<String> getDiasDaSemanaSelecionados() {
        return diasDaSemanaSelecionados;
    }

    public void setDiasDaSemanaSelecionados(ArrayList<String> diasDaSemanaSelecionados) {
        this.diasDaSemanaSelecionados = diasDaSemanaSelecionados;
    }

    public ArrayList<Turma> getTurmas() {
        return turmas;
    }

    public boolean isRemoverTurma() {
        return removerTurma;
    }

    public void setRemoverTurma(boolean removerTurma) {
        this.removerTurma = removerTurma;
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

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public boolean isFinalizarTurma() {
        return finalizarTurma;
    }

    public void setFinalizarTurma(boolean finalizarTurma) {
        this.finalizarTurma = finalizarTurma;
    }

    public ArrayList<Idade> getIdades() {
        return idades;
    }

}
