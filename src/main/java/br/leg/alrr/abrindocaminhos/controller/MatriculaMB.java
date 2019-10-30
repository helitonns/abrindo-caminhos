package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Inscricao;
import br.leg.alrr.abrindocaminhos.model.Matricula;
import br.leg.alrr.abrindocaminhos.model.Turma;
import br.leg.alrr.abrindocaminhos.model.Usuario;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.AlunoDAO;
import br.leg.alrr.abrindocaminhos.persistence.InscricaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.MatriculaDAO;
import br.leg.alrr.abrindocaminhos.persistence.TurmaDAO;
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
public class MatriculaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AlunoDAO alunoDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private InscricaoDAO inscricaoDAO;

    private ArrayList<Turma> turmas;

    private ArrayList<Aluno> alunos;
    private ArrayList<Matricula> alunosJaMatriculados;

    private Turma turma;
    private Aluno aluno;
    private Matricula matricula;
    private Inscricao inscricao;

    private boolean mostrarAlunosJaMatriculados;
    private boolean cancelaMatricula;

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

            if (FacesUtils.getBean("inscricao") != null) {
                inscricao = (Inscricao) FacesUtils.getBean("inscricao");
                alunos.add((Aluno) inscricao.getAluno());
                FacesUtils.removeBean("inscricao");
            }

        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro ao tentar matricular aluno.");
        }
    }

    private void listarTurmasIniciadas() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadasPorUnidade(u.getUnidade());
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

    public String matricular() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            matricula.setUnidade(u.getUnidade());

            matricula.setAluno(aluno);
            matricula.setTurma(turma);

            //atualizar
            if (matricula.getId() != null) {
                matriculaDAO.atualizar(matricula);
                FacesUtils.addInfoMessageFlashScoped("Matrícula atualizada com sucesso!!!");
            } //salvar
            else {

                GregorianCalendar gc = new GregorianCalendar();
                matricula.setDataMatricula(gc.getTime());
                matricula.setStatus(true);

                boolean b = matriculaDAO.podeMatricular(turma.getId(), aluno.getId());

                if (b) {
                    matriculaDAO.salvar(matricula);
                    FacesUtils.addInfoMessageFlashScoped("Matrícula salva com sucesso!!!");

                    if (inscricao != null) {
                        inscricao.setStatus(false);
                        inscricaoDAO.atualizar(inscricao);
                    }

                } else {
                    FacesUtils.addWarnMessageFlashScoped("O aluno já está matriculado neste curso!");
                    if (inscricao != null) {
                        inscricao.setStatus(false);
                        inscricaoDAO.atualizar(inscricao);
                    }
                }

                //VERIFICA SE O ALUNO ESTÁ DENTRO DA FAIXA ETÁRIA DA TURMA
                if (!matricula.podeMatricular(turma, aluno)) {
                    FacesUtils.addWarnMessageFlashScoped("Lembrando que o aluno está fora da faxa etária da turma!");
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage() + ": " + e.getCause().toString());
        }
        return "matricula.xhtml" + "?faces-redirect=true";
    }

    public void cancelarMatricula() {
        try {
            if (cancelaMatricula) {
                matriculaDAO.remover(matricula);
                FacesUtils.addInfoMessage("Matrícula cancelada com sucesso!!!");
                limparForm();
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro ao cacelar matrícula!!!");
        }
    }

    private void limparForm() {
        alunos = new ArrayList<>();
        turmas = new ArrayList<>();

        aluno = new Aluno();
        matricula = new Matricula();
        matricula.setStatus(true);

        listarTurmasIniciadas();
        mostrarAlunosJaMatriculados = false;
        cancelaMatricula = false;
    }

    public void selecionarTurma(ValueChangeEvent event) {
        try {
            Turma t = (Turma) event.getNewValue();
            alunosJaMatriculados = (ArrayList<Matricula>) matriculaDAO.listarMatriculasPorTurma(t.getId());
            mostrarAlunosJaMatriculados = true;
        } catch (DAOException | NumberFormatException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String cancelar() {
        return "matricula.xhtml" + "?faces-redirect=true";
    }
//==========================================================================

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public ArrayList<Turma> getTurmas() {
        return turmas;
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

    public ArrayList<Matricula> getAlunosJaMatriculados() {
        return alunosJaMatriculados;
    }

    public boolean isMostrarAlunosJaMatriculados() {
        return mostrarAlunosJaMatriculados;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public boolean isCancelaMatricula() {
        return cancelaMatricula;
    }

    public void setCancelaMatricula(boolean cancelaMatricula) {
        this.cancelaMatricula = cancelaMatricula;
    }

}
