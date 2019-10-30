package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Alergia;
import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Medicacao;
import br.leg.alrr.abrindocaminhos.model.Prontuario;
import br.leg.alrr.abrindocaminhos.model.Receita;
import br.leg.alrr.abrindocaminhos.persistence.AlergiaDAO;
import br.leg.alrr.abrindocaminhos.persistence.AlunoDAO;
import br.leg.alrr.abrindocaminhos.persistence.MedicacaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.ProntuarioDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Heliton
 */
@ViewScoped
@Named
public class ProntuarioMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AlunoDAO alunoDAO;

    @EJB
    private ProntuarioDAO prontuarioDAO;

    @EJB
    private AlergiaDAO alergiaDAO;

    @EJB
    private MedicacaoDAO medicacaoDAO;

    private ArrayList<Aluno> alunos;
    private List<Receita> receitas;
    private ArrayList<Alergia> alergias;
    private ArrayList<Medicacao> medicacoes;

    private Aluno aluno;
    private Aluno alunoProntuario;
    private Prontuario prontuario;
    private Alergia alergia;
    private Medicacao medicacao;
    private Receita receita;

    private boolean exibirProntuario;
//==============================================================================

    @PostConstruct
    public void init() {
        limparForm();

        listarAlergias();
        listarMedicacoes();
    }

    public void listarAlergias() {
        try {
            alergias = (ArrayList<Alergia>) alergiaDAO.listarAtivas();
        } catch (DAOException e) {
        }
    }

    public void listarMedicacoes() {
        try {
            medicacoes = (ArrayList<Medicacao>) medicacaoDAO.listarAtivas();
        } catch (DAOException e) {
        }
    }

    public void adicionarReceita() {
        Receita c = new Receita();

        c.setAlergia(alergia);
        c.setMedicacao(medicacao);

        receitas.add(c);

        alergia = new Alergia();
        medicacao = new Medicacao();
    }

    public void removerReceita() {
        for (Receita r : receitas) {
            if (r.equals(receita)) {
                receitas.remove(r);
                break;
            }
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

    public void preSalvar() {
        exibirProntuario = true;
        alunoProntuario = aluno;
        prontuario.setAluno(alunoProntuario);

        Prontuario p = new Prontuario();

        try {
            p = prontuarioDAO.buscarProntuarioPorAluno(alunoProntuario);

            if (p.getId() != null) {
                prontuario = p;
                receitas =  p.getReceitas();
            }
        } catch (DAOException e) {
        }

    }

    public String salvarProntuario() {
        try {

            if (prontuario.getId() != null) {
                prontuarioDAO.atualizar(prontuario);
                FacesUtils.addInfoMessageFlashScoped("Prontuário atualizado com sucesso!");
            } else {
                prontuario.setAluno(alunoProntuario);
                prontuario.setReceitas(receitas);
                prontuarioDAO.salvar(prontuario);
                FacesUtils.addInfoMessageFlashScoped("Prontuário salvo com sucesso!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "prontuario.xhtml" + "?faces-redirect=true";
    }

    private void limparForm() {
        exibirProntuario = false;

        aluno = new Aluno();
        alunoProntuario = new Aluno();
        prontuario = new Prontuario();
        alergia = new Alergia();
        medicacao = new Medicacao();

        alunos = new ArrayList<>();
        receitas = new ArrayList<>();
        alergias = new ArrayList<>();
        medicacoes = new ArrayList<>();
    }

    public String cancelar() {
        return "prontuario.xhtml" + "?faces-redirect=true";
    }
//==============================================================================

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Prontuario getProntuario() {
        return prontuario;
    }

    public void setProntuario(Prontuario prontuario) {
        this.prontuario = prontuario;
    }

    public Aluno getAlunoProntuario() {
        return alunoProntuario;
    }

    public void setAlunoProntuario(Aluno alunoProntuario) {
        this.alunoProntuario = alunoProntuario;
    }

    public List<Receita> getReceitas() {
        return receitas;
    }

    public void setReceitas(ArrayList<Receita> receitas) {
        this.receitas = receitas;
    }

    public ArrayList<Alergia> getAlergias() {
        return alergias;
    }

    public ArrayList<Medicacao> getMedicacoes() {
        return medicacoes;
    }

    public Alergia getAlergia() {
        return alergia;
    }

    public void setAlergia(Alergia alergia) {
        this.alergia = alergia;
    }

    public Medicacao getMedicacao() {
        return medicacao;
    }

    public void setMedicacao(Medicacao medicacao) {
        this.medicacao = medicacao;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public boolean isExibirProntuario() {
        return exibirProntuario;
    }

    public void setExibirProntuario(boolean exibirProntuario) {
        this.exibirProntuario = exibirProntuario;
        System.out.println("esta executando");
    }

}
