package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Genitores;
import br.leg.alrr.abrindocaminhos.model.Matricula;
import br.leg.alrr.abrindocaminhos.persistence.AlunoDAO;
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
 * @author Heliton
 */
@Named
@ViewScoped
public class AlunoListagemMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AlunoDAO alunoDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    private ArrayList<Aluno> alunos;
    private ArrayList<Aluno> irmaos;
    private ArrayList<Matricula> matriculas;
    private ArrayList<Matricula> matriculasParaExcluir;

    private Aluno aluno;
    private Aluno irmao;
    private Genitores genitores;

    private boolean removerAluno = false;
    private boolean removerAlunoComMatricula = false;
    private boolean mostrarTabelaDeCurso = false;
    private boolean exibirAtividade = false;
    private boolean exibirModalAlunoMatricula = false;

    private Long idAluno;
    private Long idGenitor;
    private Long idEndereco;

//==============================================================================
    @PostConstruct
    public void init() {
        limparForm();

        try {
            //VERIFICA SE HÁ ALUNO PARA EXIBIR SUAS ATIVIDADES
            if (FacesUtils.getBean("alunoAtividade") != null) {
                aluno = (Aluno) FacesUtils.getBean("alunoAtividade");
                FacesUtils.removeBean("alunoAtividade");
                pegarTurma2();
            }

            if (FacesUtils.getBean("idAluno") != null) {
                idAluno = (Long) FacesUtils.getBean("idAluno");
                idEndereco = (Long) FacesUtils.getBean("idEndereco");
                idGenitor = (Long) FacesUtils.getBean("idGenitor");
                exibirModalAlunoMatricula = (boolean) FacesUtils.getBean("exibirModalAlunoMatricula");
                matriculasParaExcluir = (ArrayList<Matricula>) FacesUtils.getBean("matriculasParaExcluir");

                FacesUtils.removeBean("idAluno");
                FacesUtils.removeBean("idEndereco");
                FacesUtils.removeBean("idGenitor");
                FacesUtils.removeBean("exibirModalAlunoMatricula");
                FacesUtils.removeBean("matriculasParaExcluir");

            }

        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao tentar editar aluno.");
        }
    }

    public void pesquisarAluno() {
        try {
            alunos = new ArrayList<>();
            irmaos = new ArrayList<>();
            genitores = new Genitores();
            exibirAtividade = false;

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

    public String removerAluno() {
        try {

            //PRIMEIRO VERIFICAR SE O ALUNO ESTÁMATRICULADO EM ALGUMA ATIVIDADE
            if (!removerAlunoComMatricula) {
                matriculasParaExcluir = (ArrayList<Matricula>) matriculaDAO.listarMatriculasPorAluno(new Aluno(idAluno));

                FacesUtils.setBean("idAluno", idAluno);
                FacesUtils.setBean("idEndereco", idEndereco);
                FacesUtils.setBean("idGenitor", idGenitor);
                FacesUtils.setBean("exibirModalAlunoMatricula", true);
                FacesUtils.setBean("matriculasParaExcluir", matriculasParaExcluir);

            }

            //O ALUNO NÃO TEM MATRÍCULA
            if (matriculasParaExcluir.isEmpty()) {

                if (removerAluno) {
                    if (alunoDAO.genitorTemMaisDeUmFilho(new Genitores(idGenitor))) {
                        alunoDAO.DeleteProntuario(idAluno);
                        alunoDAO.DeleteAluno(new Aluno(idAluno));
                        alunoDAO.DeleteEndereco(idEndereco);
                    } else {
                        alunoDAO.DeleteProntuario(idAluno);
                        alunoDAO.DeleteAluno(new Aluno(idAluno));
                        alunoDAO.DeleteEndereco(idEndereco);
                        alunoDAO.removerGenitor(alunoDAO.buscarGenitor(idGenitor));
                    }
                    FacesUtils.addInfoMessageFlashScoped("Aluno removido com sucesso!");
                    exibirModalAlunoMatricula = false;
                }
            } //O ALUNO TEM MATRICLA
            else {

                if (removerAlunoComMatricula) {

                    if (alunoDAO.genitorTemMaisDeUmFilho(new Genitores(idGenitor))) {

                        for (Matricula m : matriculasParaExcluir) {
                            alunoDAO.DeleteMatricula(m.getId());
                        }

                        alunoDAO.DeleteProntuario(idAluno);
                        alunoDAO.DeleteAluno(new Aluno(idAluno));
                        alunoDAO.DeleteEndereco(idEndereco);
                    } else {
                        for (Matricula m : matriculasParaExcluir) {
                            alunoDAO.DeleteMatricula(m.getId());
                        }

                        alunoDAO.DeleteProntuario(idAluno);
                        alunoDAO.DeleteAluno(new Aluno(idAluno));
                        alunoDAO.DeleteEndereco(idEndereco);
                        alunoDAO.removerGenitor(alunoDAO.buscarGenitor(idGenitor));
                    }
                    FacesUtils.addInfoMessageFlashScoped("Aluno removido com sucesso!");
                }
                exibirModalAlunoMatricula = false;
            }

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "listar-editar-aluno.xhtml" + "?faces-redirect=true";
    }

    public void pegarTurma() {
        try {
            exibirAtividade = true;

            matriculas = (ArrayList<Matricula>) turmaDAO.listarTurmasPorAluno(irmao);
            mostrarTabelaDeCurso = true;
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage() + ": " + e.getCause());
        }
    }

    public void pegarTurma2() {
        try {
            exibirAtividade = true;

            matriculas = (ArrayList<Matricula>) turmaDAO.listarTurmasPorAluno(aluno);
            mostrarTabelaDeCurso = true;
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage() + ": " + e.getCause());
        }
    }

    public String editarAluno() {
        FacesUtils.setBean("aluno", aluno);
        return "aluno.xhtml" + "?faces-redirect=true";
    }

    public String exibirAluno() {
        FacesUtils.setBean("aluno", aluno);
        return "exibir-aluno.xhtml" + "?faces-redirect=true";
    }

    public String enviarAlunoParaMatricula() {
        FacesUtils.setBean("aluno", aluno);
        return "matricula.xhtml" + "?faces-redirect=true";
    }

    public String cancelar() {
        return "minhas-atividades.xhtml" + "?faces-redirect=true";
    }

    private void limparForm() {
        aluno = new Aluno();
        genitores = new Genitores();
        alunos = new ArrayList<>();
        matriculasParaExcluir = new ArrayList<>();
        mostrarTabelaDeCurso = false;
        removerAluno = false;
        removerAlunoComMatricula = false;
        exibirModalAlunoMatricula = false;
    }

    public void pesquisarFamilia() {
        try {
            genitores = aluno.getGenitores();
            irmaos = (ArrayList<Aluno>) alunoDAO.pesquisarFamilia(aluno.getGenitores());
        } catch (DAOException e) {
        }
    }

    public void verificarMInhasAtividades() {
        try {
            matriculas = (ArrayList<Matricula>) turmaDAO.listarTurmasPorAluno(aluno);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage() + ": " + e.getCause());
        }
    }
//==============================================================================

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public boolean isRemoverAluno() {
        return removerAluno;
    }

    public void setRemoverAluno(boolean removerAluno) {
        this.removerAluno = removerAluno;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public boolean isMostrarTabelaDeCurso() {
        return mostrarTabelaDeCurso;
    }

    public ArrayList<Matricula> getMatriculas() {
        return matriculas;
    }

    public ArrayList<Aluno> getIrmaos() {
        return irmaos;
    }

    public Genitores getGenitores() {
        return genitores;
    }

    public void setGenitores(Genitores genitores) {
        this.genitores = genitores;
    }

    public Aluno getIrmao() {
        return irmao;
    }

    public void setIrmao(Aluno irmao) {
        this.irmao = irmao;
    }

    public boolean isExibirAtividade() {
        return exibirAtividade;
    }

    public void setIdAlunoGenitor(Long idAluno, Long idGenitor, Long idEndereco) {
        this.idAluno = idAluno;
        this.idGenitor = idGenitor;
        this.idEndereco = idEndereco;
    }

    public boolean isRemoverAlunoComMatricula() {
        return removerAlunoComMatricula;
    }

    public void setRemoverAlunoComMatricula(boolean removerAlunoComMatricula) {
        this.removerAlunoComMatricula = removerAlunoComMatricula;
    }

    public boolean isExibirModalAlunoMatricula() {
        return exibirModalAlunoMatricula;
    }

    public void setExibirModalAlunoMatricula(boolean exibirModalAlunoMatricula) {
        this.exibirModalAlunoMatricula = exibirModalAlunoMatricula;
    }

    public ArrayList<Matricula> getMatriculasParaExcluir() {
        return matriculasParaExcluir;
    }

}
