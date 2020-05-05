package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Alergia;
import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.CategoriaDeAlergia;
import br.leg.alrr.abrindocaminhos.model.Doenca;
import br.leg.alrr.abrindocaminhos.model.EspecialidadeMedica;
import br.leg.alrr.abrindocaminhos.model.Medicacao;
import br.leg.alrr.abrindocaminhos.model.Prontuario;
import br.leg.alrr.abrindocaminhos.model.Sindrome;
import br.leg.alrr.abrindocaminhos.persistence.AlergiaDAO;
import br.leg.alrr.abrindocaminhos.persistence.AlunoDAO;
import br.leg.alrr.abrindocaminhos.persistence.CategoriaDeAlergiaDAO;
import br.leg.alrr.abrindocaminhos.persistence.DoencaDAO;
import br.leg.alrr.abrindocaminhos.persistence.EspecialidadeMedicaDAO;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MedicacaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.ProntuarioDAO;
import br.leg.alrr.abrindocaminhos.persistence.SindromeDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
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

    @EJB
    private EspecialidadeMedicaDAO especialidadeMedicaDAO;

    @EJB
    private DoencaDAO doencaDAO;

    @EJB
    private SindromeDAO sindromeDAO;
    
    @EJB
    private CategoriaDeAlergiaDAO categoriaDeAlergiaDAO;
    
    @EJB
    private LogSistemaDAO logSistemaDAO;

    private List<Aluno> alunos;
    private List<Alergia> alergias;
    private List<Alergia> alergiasParaInclusao;
    private List<Medicacao> medicacoes;
    private List<Medicacao> medicacoesParaInclusao;
    private List<EspecialidadeMedica> especialidades;
    private List<EspecialidadeMedica> especialidadesParaInclusao;
    private List<Doenca> doencas;
    private List<Doenca> doencasParaInclusao;
    private List<Sindrome> sindromes;
    private List<Sindrome> sindromesParaInclusao;
    private List<CategoriaDeAlergia> categorias;

    private Aluno aluno;
    private Aluno alunoProntuario;
    private Prontuario prontuario;

    private Alergia alergia;
    private Alergia recebeAlergia;
    private Medicacao medicacao;
    private Medicacao recebeMedicacao;
    private EspecialidadeMedica especialidadeMedica;
    private EspecialidadeMedica recebeEspecialidadeMedica;
    private Doenca doenca;
    private Doenca recebeDoenca;
    private Sindrome sindrome;
    private Sindrome recebeSindrome;
    private Long categoriaSelecionada;

    private boolean exibirProntuario;
//==============================================================================

    @PostConstruct
    public void init() {
        limparForm();

        listarCategorias();
        listarMedicacoes();
        listarEspecialidades();
        listarDoencas();
        listarSindromes();

        try {
            //PRONTUÁRIO DO ALUNO PASSADO COMO PARÂMETRO
            if (FacesUtils.getBean("idAlunoParaProntuario") != null) {
                aluno.setId((Long) FacesUtils.getBean("idAlunoParaProntuario"));
                pesquisarAluno();
                aluno = alunos.get(0);
                preSalvar();
                FacesUtils.removeBean("idAlunoParaProntuario");
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Erro no construtor: \n" + e.getCause());
        }

        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    public void listarAlergias() {
        try {
            alergias = (ArrayList<Alergia>) alergiaDAO.listarAtivasPorCategoria(new CategoriaDeAlergia(categoriaSelecionada));
        } catch (DAOException e) {
        }
    }
    
    public void listarCategorias() {
        try {
            categorias = categoriaDeAlergiaDAO.listarAtivas();
        } catch (DAOException e) {
        }
    }
    
    public void valueChanged(ValueChangeEvent event) {
        try {
            categoriaSelecionada = Long.parseLong(event.getNewValue().toString());
            listarAlergias();
        } catch (NumberFormatException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    public void listarMedicacoes() {
        try {
            medicacoes = (ArrayList<Medicacao>) medicacaoDAO.listarAtivas();
        } catch (DAOException e) {
        }
    }

    public void listarEspecialidades() {
        try {
            especialidades = (ArrayList<EspecialidadeMedica>) especialidadeMedicaDAO.listarAtivas();
        } catch (DAOException e) {
        }
    }

    public void listarDoencas() {
        try {
            doencas = (ArrayList<Doenca>) doencaDAO.listarAtivas();
        } catch (DAOException e) {
        }
    }

    public void listarSindromes() {
        try {
            sindromes = (ArrayList<Sindrome>) sindromeDAO.listarAtivas();
        } catch (DAOException e) {
        }
    }

    public void adicionarAlergia() {
        alergiasParaInclusao.add(alergia);

        alergia = new Alergia();
    }

    public void adicionarMedicacao() {
        medicacoesParaInclusao.add(medicacao);

        medicacao = new Medicacao();
    }

    public void adicionarEspecialidade() {
        especialidadesParaInclusao.add(especialidadeMedica);

        especialidadeMedica = new EspecialidadeMedica();
    }

    public void adicionarDoenca() {
        doencasParaInclusao.add(doenca);

        doenca = new Doenca();
    }

    public void adicionarSindrome() {
        sindromesParaInclusao.add(sindrome);

        sindrome = new Sindrome();
    }

    public void removerAlergia() {
        for (Alergia a : alergiasParaInclusao) {
            if (a.getId().equals(recebeAlergia.getId())) {
                alergiasParaInclusao.remove(a);
                recebeAlergia = new Alergia();
                break;
            }
        }
    }

    public void removerMedicao() {
        for (Medicacao m : medicacoesParaInclusao) {
            if (m.getId().equals(recebeMedicacao.getId())) {
                medicacoesParaInclusao.remove(m);
                recebeMedicacao = new Medicacao();
                break;
            }
        }
    }

    public void removerEspecialidade() {
        for (EspecialidadeMedica e : especialidadesParaInclusao) {
            if (e.getId().equals(recebeEspecialidadeMedica.getId())) {
                especialidadesParaInclusao.remove(e);
                recebeEspecialidadeMedica = new EspecialidadeMedica();
                break;
            }
        }
    }

    public void removerDoenca() {
        for (Doenca d : doencasParaInclusao) {
            if (d.getId().equals(recebeDoenca.getId())) {
                doencasParaInclusao.remove(d);
                recebeDoenca = new Doenca();
                break;
            }
        }
    }

    public void removerSindrome() {
        for (Sindrome s : sindromesParaInclusao) {
            if (s.getId().equals(recebeSindrome.getId())) {
                sindromesParaInclusao.remove(s);
                recebeSindrome = new Sindrome();
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

                alergiasParaInclusao = prontuarioDAO.retornarAlergiasDoProntuarioDoAluno(alunoProntuario).getAlergias();
                medicacoesParaInclusao = prontuarioDAO.retornarMedicacoesDoProntuarioDoAluno(alunoProntuario).getMedicacoes();
                especialidadesParaInclusao = prontuarioDAO.retornarEspecialidadesDoProntuarioDoAluno(alunoProntuario).getEspecialidades();
                doencasParaInclusao = prontuarioDAO.retornarDoencasDoProntuarioDoAluno(alunoProntuario).getDoencas();
                sindromesParaInclusao = prontuarioDAO.retornarSindromesDoProntuarioDoAluno(alunoProntuario).getSindromes();

            }
        } catch (DAOException e) {
            System.out.println(e.getCause());
        }

    }

    public String salvarProntuario() {
        try {

            if (prontuario.getId() != null) {

                prontuario.setAlergias(alergiasParaInclusao);
                prontuario.setMedicacoes(medicacoesParaInclusao);
                prontuario.setEspecialidades(especialidadesParaInclusao);
                prontuario.setDoencas(doencasParaInclusao);
                prontuario.setSindromes(sindromesParaInclusao);

                prontuarioDAO.atualizar(prontuario);
                FacesUtils.addInfoMessageFlashScoped("Prontuário atualizado com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método ProntuarioMB.salvarProntuario() para atualizar prontuário do aluno "+ alunoProntuario.getId()+".");
            } else {
                prontuario.setAluno(alunoProntuario);

                prontuario.setAlergias(alergiasParaInclusao);
                prontuario.setMedicacoes(medicacoesParaInclusao);
                prontuario.setEspecialidades(especialidadesParaInclusao);
                prontuario.setDoencas(doencasParaInclusao);
                prontuario.setSindromes(sindromesParaInclusao);

                prontuarioDAO.salvar(prontuario);
                FacesUtils.addInfoMessageFlashScoped("Prontuário salvo com sucesso!");
                Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ProntuarioMB.salvarProntuario() para salvar prontuário do aluno "+ alunoProntuario.getId()+".");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "prontuario.xhtml" + "?faces-redirect=true";
    }

    public void salvarAlergia(){
        try {
            alergia.setStatus(true);
            alergiaDAO.salvar(alergia);
            FacesUtils.addInfoMessage("Alergia salva com sucesso!");
            Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ProntuarioMB.salvarAlergia() para salvar a alegia "+ alergia.getNome()+".");

            alergia = new Alergia();

            listarAlergias();

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    public void salvarMedicacao(){
        try {
            medicacao.setStatus(true);
            medicacaoDAO.salvar(medicacao);
            FacesUtils.addInfoMessage("Medicação salva com sucesso!");
            Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ProntuarioMB.salvarMedicacao() para salvar a medicação "+ medicacao.getNome()+".");

            medicacao = new Medicacao();

            listarMedicacoes();

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    public void salvarEspecialidade(){
        try {
            especialidadeMedica.setStatus(true);
            especialidadeMedicaDAO.salvar(especialidadeMedica);
            FacesUtils.addInfoMessage("Especialidade médica salva com sucesso!");
            Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ProntuarioMB.salvarEspecialidade() para salvar a especialidade "+ especialidadeMedica.getNome()+".");

            especialidadeMedica = new EspecialidadeMedica();

            listarEspecialidades();

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    public void salvarDoenca(){
        try {
            doenca.setStatus(true);
            doencaDAO.salvar(doenca);
            FacesUtils.addInfoMessage("Doença salva com sucesso!");
            Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ProntuarioMB.salvarDoenca() para salvar a doenca "+ doenca.getNome()+".");

            doenca = new Doenca();

            listarDoencas();

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    public void salvarSindrome(){
        try {
            sindrome.setStatus(true);
            sindromeDAO.salvar(sindrome);
            FacesUtils.addInfoMessage("Sídrome/transtorno salva com sucesso!");
            Loger.registrar(logSistemaDAO, TipoAcao.SALVAR, "O usuário executou o método ProntuarioMB.salvarSindrome() para salvar a sídrome "+ sindrome.getNome()+".");

            sindrome = new Sindrome();

            listarSindromes();

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    private void limparForm() {
        exibirProntuario = false;

        aluno = new Aluno();
        alunoProntuario = new Aluno();
        prontuario = new Prontuario();

        alergia = new Alergia();
        recebeAlergia = new Alergia();
        medicacao = new Medicacao();
        especialidadeMedica = new EspecialidadeMedica();
        doenca = new Doenca();
        recebeDoenca = new Doenca();
        sindrome = new Sindrome();
        recebeSindrome = new Sindrome();

        alunos = new ArrayList<>();
        alergias = new ArrayList<>();
        alergiasParaInclusao = new ArrayList<>();
        medicacoes = new ArrayList<>();
        medicacoesParaInclusao = new ArrayList<>();
        especialidades = new ArrayList<>();
        especialidadesParaInclusao = new ArrayList<>();
        doencas = new ArrayList<>();
        doencasParaInclusao = new ArrayList<>();
        sindromes = new ArrayList<>();
        sindromesParaInclusao = new ArrayList<>();
    }

    public String cancelar() {
        return "prontuario.xhtml" + "?faces-redirect=true";
    }
//==============================================================================

    public List<Aluno> getAlunos() {
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

    public List<Alergia> getAlergiasParaInclusao() {
        return alergiasParaInclusao;
    }

    public void setAlergiasParaInclusao(ArrayList<Alergia> alergias) {
        this.alergiasParaInclusao = alergias;
    }

    public List<Alergia> getAlergias() {
        return alergias;
    }

    public List<Medicacao> getMedicacoes() {
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

    public boolean isExibirProntuario() {
        return exibirProntuario;
    }

    public void setExibirProntuario(boolean exibirProntuario) {
        this.exibirProntuario = exibirProntuario;
    }

    public Alergia getRecebeAlergia() {
        return recebeAlergia;
    }

    public void setRecebeAlergia(Alergia recebeAlergia) {
        this.recebeAlergia = recebeAlergia;
    }

    public Medicacao getRecebeMedicacao() {
        return recebeMedicacao;
    }

    public void setRecebeMedicacao(Medicacao recebeMedicacao) {
        this.recebeMedicacao = recebeMedicacao;
    }

    public List<Medicacao> getMedicacoesParaInclusao() {
        return medicacoesParaInclusao;
    }

    public EspecialidadeMedica getEspecialidadeMedica() {
        return especialidadeMedica;
    }

    public void setEspecialidadeMedica(EspecialidadeMedica especialidadeMedica) {
        this.especialidadeMedica = especialidadeMedica;
    }

    public List<EspecialidadeMedica> getEspecialidades() {
        return especialidades;
    }

    public List<EspecialidadeMedica> getEspecialidadesParaInclusao() {
        return especialidadesParaInclusao;
    }

    public EspecialidadeMedica getRecebeEspecialidadeMedica() {
        return recebeEspecialidadeMedica;
    }

    public void setRecebeEspecialidadeMedica(EspecialidadeMedica recebeEspecialidadeMedica) {
        this.recebeEspecialidadeMedica = recebeEspecialidadeMedica;
    }

    public Doenca getDoenca() {
        return doenca;
    }

    public void setDoenca(Doenca doenca) {
        this.doenca = doenca;
    }

    public Doenca getRecebeDoenca() {
        return recebeDoenca;
    }

    public void setRecebeDoenca(Doenca recebeDoenca) {
        this.recebeDoenca = recebeDoenca;
    }

    public List<Doenca> getDoencas() {
        return doencas;
    }

    public List<Doenca> getDoencasParaInclusao() {
        return doencasParaInclusao;
    }

    public Sindrome getSindrome() {
        return sindrome;
    }

    public void setSindrome(Sindrome sindrome) {
        this.sindrome = sindrome;
    }

    public Sindrome getRecebeSindrome() {
        return recebeSindrome;
    }

    public void setRecebeSindrome(Sindrome recebeSindrome) {
        this.recebeSindrome = recebeSindrome;
    }

    public List<Sindrome> getSindromes() {
        return sindromes;
    }

    public List<Sindrome> getSindromesParaInclusao() {
        return sindromesParaInclusao;
    }

    public List<CategoriaDeAlergia> getCategorias() {
        return categorias;
    }

    public Long getCategoriaSelecionada() {
        return categoriaSelecionada;
    }

    public void setCategoriaSelecionada(Long categoriaSelecionada) {
        this.categoriaSelecionada = categoriaSelecionada;
    }
    
}
