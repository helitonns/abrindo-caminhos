package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.model.Bairro;
import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Endereco;
import br.leg.alrr.abrindocaminhos.model.Municipio;
import br.leg.alrr.abrindocaminhos.business.Sexo;
import br.leg.alrr.abrindocaminhos.model.Denominacao;
import br.leg.alrr.abrindocaminhos.model.Escola;
import br.leg.alrr.abrindocaminhos.model.Escolaridade;
import br.leg.alrr.abrindocaminhos.model.Genitores;
import br.leg.alrr.abrindocaminhos.model.Inscricao;
import br.leg.alrr.abrindocaminhos.model.Instrucao;
import br.leg.alrr.abrindocaminhos.model.ListaDeEspera;
import br.leg.alrr.abrindocaminhos.model.Matricula;
import br.leg.alrr.abrindocaminhos.model.Pais;
import br.leg.alrr.abrindocaminhos.model.Periodo;
import br.leg.alrr.abrindocaminhos.model.Serie;
import br.leg.alrr.abrindocaminhos.model.Situacao;
import br.leg.alrr.abrindocaminhos.model.Turma;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.BairroDAO;
import br.leg.alrr.abrindocaminhos.persistence.AlunoDAO;
import br.leg.alrr.abrindocaminhos.persistence.DenominacaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.EscolaDAO;
import br.leg.alrr.abrindocaminhos.persistence.EscolaridadeDAO;
import br.leg.alrr.abrindocaminhos.persistence.InscricaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.ListaDeEsperaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MatriculaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MunicipioDAO;
import br.leg.alrr.abrindocaminhos.persistence.PaisDAO;
import br.leg.alrr.abrindocaminhos.persistence.PeriodoDAO;
import br.leg.alrr.abrindocaminhos.persistence.SerieDAO;
import br.leg.alrr.abrindocaminhos.persistence.SituacaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.TurmaDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class AlunoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AlunoDAO alunoDAO;

    @EJB
    private MunicipioDAO municipioDAO;

    @EJB
    private BairroDAO bairroDAO;

    @EJB
    private SituacaoDAO situacaoDAO;

    @EJB
    private MatriculaDAO matriculaDAO;

    @EJB
    private PaisDAO paisDAO;

    @EJB
    private EscolaridadeDAO escolaridadeDAO;

    @EJB
    private PeriodoDAO periodoDAO;

    @EJB
    private DenominacaoDAO denominacaoDAO;

    @EJB
    private EscolaDAO escolaDAO;

    @EJB
    private SerieDAO serieDAO;

    @EJB
    private ListaDeEsperaDAO listaDAO;

    @EJB
    private TurmaDAO turmaDAO;

    @EJB
    private InscricaoDAO inscricaoDAO;

    private ArrayList<Pais> paises;
    private ArrayList<Municipio> municipios;
    private ArrayList<Bairro> bairros;
    private ArrayList<Situacao> situacoes;
    private ArrayList<Escolaridade> escolaridades;
    private ArrayList<Serie> series;
    private ArrayList<Periodo> periodos;
    private ArrayList<Denominacao> denominacoes;
    private ArrayList<Escola> escolas;
    private ArrayList<Turma> turmas;
    private ArrayList<ListaDeEspera> listas;
    private ArrayList<Matricula> matriculas;

    private Aluno aluno;
    private Endereco endereco;
    private Municipio municipio;
    private Bairro bairro;
    private Genitores genitores;
    private Instrucao instrucao;
    private Turma turma;
    private ListaDeEspera listaDeEspera;
    private Escola escola;
    private Pais pais;

    private Sexo sexo = Sexo.MASCULINO;
    private Long idMunicipio;
    private Long idPais;
    private Long idSituacao;
    private Long idEscolaridade;
    private Long idSerie;
    private Long idPeriodo;
    private Long idEscola;
    private Long idDenominacao;
    private String cpfPesquisa;
    private boolean editandoALuno;
    private boolean matricularNaTurma;
    private boolean inscreverNaLista;
    private boolean encontrouGenitores;
    private boolean exibirMinhasAtividades;

    private byte[] imagem;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        bairros = new ArrayList<>();
        municipios = new ArrayList<>();
        matriculas = new ArrayList<>();

        listarMunicipio();
        listarPaises();
        listarSituacoesAtivas();

        listarEscolaridadesAtivas();
        listarSeriesAtivas();
        listarPeriodosAtivos();
        listarDenominacoesAtivas();
        listarTurmasAtivas();
        listarListasAtivas();

        try {
            //VERIFICA SE HÁ CPF A SER VIRIFICADO
            if (FacesUtils.getBean("alunoCPF") != null) {
                aluno = (Aluno) FacesUtils.getBean("alunoCPF");
                FacesUtils.removeBean("alunoCPF");
            }

            // VERIFICA SE HÁ ALGUM ALUNO NA SESSÃO PARA SER EDITADO, SE HOUVER SETA OS VALORES CORRESPONDENTES
            if (FacesUtils.getBean("aluno") != null) {
                preEditar();
                FacesUtils.removeBean("aluno");
            }
        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao tentar editar aluno. \n" + e.getCause());
        }
    }

    public void verificarMinhasAtividades() {
        try {
            matriculas = (ArrayList<Matricula>) turmaDAO.listarTurmasPorAluno(aluno);
            exibirMinhasAtividades = true;
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage() + ": " + e.getCause());
        }
    }

    private byte[] getFileContents(InputStream in) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int read = 0;
            bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                bos.write(bytes, 0, read);
            }
            bytes = bos.toByteArray();
            in.close();
            in = null;
            bos.flush();
            bos.close();
            bos = null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return bytes;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        imagem = getFileContents(event.getFile().getInputstream());
    }

    public void onCapture(CaptureEvent captureEvent) {
        imagem = captureEvent.getData();
    }

    public StreamedContent getImagemStreamed() {
        return new DefaultStreamedContent(new ByteArrayInputStream(getImagem()));
    }

    private void preEditar() {
        aluno = (Aluno) FacesUtils.getBean("aluno");
        endereco = aluno.getEndereco();
        bairro = endereco.getBairro();
        municipio = bairro.getMunicipio();
        idMunicipio = municipio.getId();
        listarBairroPorMunicipio();
        editandoALuno = true;
        idPais = aluno.getPaisDeOrigem().getId();

        if (aluno.getInstrucao() != null) {
            instrucao = aluno.getInstrucao();

            if (aluno.getInstrucao().getEscola() != null) {
                idEscola = instrucao.getEscola().getId();
                idDenominacao = instrucao.getEscola().getDenominacao().getId();
                listarEscolaPorDenominacao(new Denominacao(idDenominacao));
            }

            if (aluno.getInstrucao().getEscolaridade() != null) {
                idEscolaridade = instrucao.getEscolaridade().getId();
            }

            if (aluno.getInstrucao().getSerie() != null) {
                idSerie = instrucao.getSerie().getId();
            }

            if (aluno.getInstrucao().getPeriodo() != null) {
                idPeriodo = instrucao.getPeriodo().getId();
            }

        }

        genitores = aluno.getGenitores();

        if (aluno.getFoto() != null) {
            imagem = aluno.getFoto();
        }
    }

    private void preSalvar() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            aluno.setUnidade(u.getUnidade());

            boolean podeIncluirInstrucao = false;

            if (idEscola != 0l) {
                instrucao.setEscola(escolaDAO.buscarPorID(idEscola));
                podeIncluirInstrucao = true;
            }
            if (idPeriodo != 0l) {
                instrucao.setPeriodo(new Periodo(idPeriodo));
                podeIncluirInstrucao = true;
            }
            if (idSerie != 0l) {
                instrucao.setSerie(new Serie(idSerie));
                podeIncluirInstrucao = true;
            }
            if (idEscolaridade != 0l) {
                instrucao.setEscolaridade(new Escolaridade(idEscolaridade));
                podeIncluirInstrucao = true;
            }

            if (podeIncluirInstrucao) {
                aluno.setInstrucao(instrucao);
            }

            endereco.setBairro(bairro);
            aluno.setEndereco(endereco);
            aluno.setPaisDeOrigem(new Pais(idPais));

            //VERIFICAR SE GENITORES JÁ EXISTEM
            if (!editandoALuno) {
                if (genitores.getCpfMae() != null && !genitores.getCpfMae().isEmpty()) {
                    cpfPesquisa = genitores.getCpfMae();
                    pesquisarGenitores();
                } else if (genitores.getCpfPai() != null && !genitores.getCpfPai().isEmpty()) {
                    cpfPesquisa = genitores.getCpfPai();
                    pesquisarGenitores();
                }
            }

            aluno.setGenitores(genitores);
            aluno.setFoto(imagem);
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage() + ": " + e.getCause());
        }

    }

    public String salvarAluno() {
        try {

            preSalvar();

            if (aluno.getId() != null) {
                alunoDAO.atualizar(aluno);
                FacesUtils.addInfoMessageFlashScoped("Aluno atualizado com sucesso!!!");

                //FAZENDO A MATRICULA NA TURMA
                if (matricularNaTurma && turma.getId() != null) {
                    if (!turmaDAO.verificarSeAlunoJaEstaMatriculadoNaTurma(aluno, turma)) {
                        UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");

                        //FAZENDO A MATRÍCULA NO CURSO
                        Matricula matricula = new Matricula();
                        GregorianCalendar gc = new GregorianCalendar();
                        matricula.setDataMatricula(gc.getTime());
                        matricula.setAluno(aluno);
                        matricula.setTurma(turma);

                        matricula.setUnidade(u.getUnidade());
                        matricula.setStatus(true);
                        matriculaDAO.salvar(matricula);
                        FacesUtils.addInfoMessageFlashScoped("Aluno matriculado na turma com sucesso!!!");

                        if (!matricula.podeMatricular(turma, aluno)) {
                            FacesUtils.addWarnMessageFlashScoped("Lembrando que o aluno está fora da faxa etária da turma!");
                        }

                    } else {
                        FacesUtils.addWarnMessageFlashScoped("Aluno já está matriculado na turma!!!");
                    }
                }

                //FAZENDO A INSCRICAO NA LISTA
                if (inscreverNaLista && listaDeEspera.getId() != null) {
                    UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
                    GregorianCalendar gc = new GregorianCalendar();
                    Inscricao i = new Inscricao();

                    i.setUnidade(u.getUnidade());
                    i.setAluno(aluno);
                    i.setListaDeEspera(listaDeEspera);
                    i.setDataDaInscricao(gc.getTime());
                    i.setStatus(true);

                    if (inscricaoDAO.podeInscrever(listaDeEspera.getId(), aluno.getId())) {
                        inscricaoDAO.salvar(i);
                        FacesUtils.addInfoMessageFlashScoped("Inscrição salva com sucesso!!!");
                    } else {
                        FacesUtils.addWarnMessageFlashScoped("O aluno já está inscrito nesta lista!");
                    }

                }
            } else {
                //salva genitores primeiro
                if (!encontrouGenitores) {
                    alunoDAO.salvarGenitor(genitores);
                }
                genitores = alunoDAO.buscarReferenciaGenitor(genitores);
                aluno.setGenitores(genitores);
                aluno.setDataDeCadastro(new Date());

                //verifica se o aluno tem CPF, se tiver será verificado se o CPF é único, caso contrário, o aluno será salvo sem a verificação
                if (aluno.getCpf().length() >= 11) {
                    if (alunoDAO.cpfUnico(aluno.getCpf())) {
                        alunoDAO.salvar(aluno);
                        FacesUtils.addInfoMessageFlashScoped("Aluno salvo com sucesso!!!");
                        FacesUtils.addInfoMessageFlashScoped("Código do aluno: " + aluno.getId());
                    } else {
                        FacesUtils.addWarnMessageFlashScoped("O CPF de número " + aluno.getCpf() + " já está cadastrado!!!");
                    }
                } else {
                    alunoDAO.salvar(aluno);
                    FacesUtils.addInfoMessageFlashScoped("Aluno salvo com sucesso!!!");
                    FacesUtils.addInfoMessageFlashScoped("Código do aluno: " + aluno.getId());
                }

                //FAZENDO A MATRICULA NA TURMA
                if (matricularNaTurma && turma.getId() != null) {

                    //verificar se o aluno já está matriculado na turma, se não estiver matricula o aluno, caso contrário nada faz
                    if (!turmaDAO.verificarSeAlunoJaEstaMatriculadoNaTurma(aluno, turma)) {

                        UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");

                        //FAZENDO A MATRÍCULA NA TURMA
                        Matricula matricula = new Matricula();
                        GregorianCalendar gc = new GregorianCalendar();
                        matricula.setDataMatricula(gc.getTime());
                        matricula.setAluno(aluno);
                        matricula.setTurma(turma);

                        matricula.setUnidade(u.getUnidade());
                        matricula.setStatus(true);
                        matriculaDAO.salvar(matricula);
                        FacesUtils.addInfoMessageFlashScoped("Aluno matriculado na turma com sucesso!!!");

                        if (!matricula.podeMatricular(turma, aluno)) {
                            FacesUtils.addWarnMessageFlashScoped("Lembrando que o aluno está fora da faxa etária da turma!");
                        }

                    } else {
                        FacesUtils.addWarnMessageFlashScoped("Aluno já está matriculado na turma!!!");
                    }
                }

                //FAZENDO A INSCRICAO NA LISTA
                if (inscreverNaLista && listaDeEspera.getId() != null) {
                    UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
                    GregorianCalendar gc = new GregorianCalendar();
                    Inscricao i = new Inscricao();

                    i.setUnidade(u.getUnidade());
                    i.setAluno(aluno);
                    i.setListaDeEspera(listaDeEspera);
                    i.setDataDaInscricao(gc.getTime());
                    i.setStatus(true);

                    if (inscricaoDAO.podeInscrever(listaDeEspera.getId(), aluno.getId())) {
                        inscricaoDAO.salvar(i);
                        FacesUtils.addInfoMessageFlashScoped("Inscrição salva com sucesso!!!");
                    } else {
                        FacesUtils.addWarnMessageFlashScoped("O aluno já está inscrito nesta lista!");
                    }

                }

            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage() + ": " + e.getCause());
            System.out.println(e.getCause());
        }
        FacesUtils.removeBean("cpf");
        return "aluno.xhtml" + "?faces-redirect=true";
    }

    private void listarTurmasAtivas() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            turmas = (ArrayList<Turma>) turmaDAO.listarTurmasIniciadasPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarListasAtivas() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            listas = (ArrayList<ListaDeEspera>) listaDAO.listarListaDeEsperasIniciadasPorUnidade(u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarMunicipio() {
        try {
            municipios = (ArrayList<Municipio>) municipioDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarBairroPorMunicipio() {
        try {
            bairros = (ArrayList<Bairro>) bairroDAO.listarBairroPorMunicipio(municipio);
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    public void listarPaises() {
        try {
            paises = (ArrayList<Pais>) paisDAO.listarAtivos();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarSituacoesAtivas() {
        try {
            situacoes = (ArrayList<Situacao>) situacaoDAO.listarSituacoesAtivas();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarEscolaridadesAtivas() {
        try {
            escolaridades = (ArrayList<Escolaridade>) escolaridadeDAO.listarAtivas();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarSeriesAtivas() {
        try {
            series = (ArrayList<Serie>) serieDAO.listarAtivas();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarPeriodosAtivos() {
        try {
            periodos = (ArrayList<Periodo>) periodoDAO.listarPeriodosAtivos();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarDenominacoesAtivas() {
        try {
            denominacoes = (ArrayList<Denominacao>) denominacaoDAO.listarAtivas();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarEscolaPorDenominacao(Denominacao d) {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            escolas = (ArrayList<Escola>) escolaDAO.listarEscolaAtivasPorDenominacaoEPorUnidade(d, u.getUnidade());
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    public String verificarCodigoOuCpfCadastrado() {

        if (aluno.getId() != null && aluno.getId() != 0l) {
            try {
                Aluno a = alunoDAO.pesquisarPorID(aluno.getId());
                if (a != null) {
                    FacesUtils.setBean("aluno", a);
                    return "matricula.xhtml" + "?faces-redirect=true";
                }
            } catch (DAOException e) {
                FacesUtils.addWarnMessageFlashScoped("Código não encontrado!!!");
                return "aluno.xhtml" + "?faces-redirect=true";
            }

        }

        if (aluno.getCpf() != null && !aluno.getCpf().isEmpty()) {
            try {
                Aluno a = alunoDAO.pesquisarPorCPF(aluno.getCpf());
                if (a != null) {
                    FacesUtils.setBean("aluno", a);
                    return "matricula.xhtml" + "?faces-redirect=true";
                }
            } catch (DAOException e) {
                FacesUtils.setBean("alunoCPF", aluno);
                return "aluno.xhtml" + "?faces-redirect=true";
            }

        } else {
            FacesUtils.addWarnMessageFlashScoped("Informe o CPF!!!");
        }

        return null;
    }

    public void valueChanged(ValueChangeEvent event) {
        try {
            idMunicipio = Long.parseLong(event.getNewValue().toString());
            municipio.setId(idMunicipio);
            listarBairroPorMunicipio();
        } catch (NumberFormatException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    public void valueChangedDenominacao(ValueChangeEvent event) {
        try {
            Long idDenominacao = Long.parseLong(event.getNewValue().toString());
            listarEscolaPorDenominacao(new Denominacao(idDenominacao));
        } catch (NumberFormatException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    public void salvarBairro() {
        try {
            bairro.setMunicipio(municipio);
            bairroDAO.salvar(bairro);
            FacesUtils.addInfoMessageFlashScoped("Bairro salvo com sucesso!");
            listarBairroPorMunicipio();
            bairro = new Bairro();
        } catch (DAOException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void limparForm() {
        aluno = new Aluno();
        aluno.setCpf("");
        bairro = new Bairro();
        municipio = new Municipio();
        endereco = new Endereco();
        genitores = new Genitores();
        instrucao = new Instrucao();
        escola = new Escola();
        pais = new Pais();

        cpfPesquisa = "";

        idPais = 0l;
        idSituacao = 0l;
        idMunicipio = 0l;
        idEscola = 0l;
        idEscolaridade = 0l;
        idPeriodo = 0l;
        idSerie = 0l;

        editandoALuno = false;
        matricularNaTurma = false;
        encontrouGenitores = false;
        exibirMinhasAtividades = false;
    }

    public String cancelar() {
        return "aluno.xhtml" + "?faces-redirect=true";
    }

    public String incluirMatricula() {
        FacesUtils.setBean("idAluno", aluno.getId());
        return "matricula.xhtml" + "?faces-redirect=true";
    }

    public String enviarMinhasAtividades() {
        try {
            Aluno a = new Aluno(aluno.getId());
            FacesUtils.setBean("alunoAtividade", a);
            return "minhas-atividades.xhtml" + "?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

    public String editarAluno() {
        FacesUtils.setBean("aluno", aluno);
        return "aluno.xhtml" + "?faces-redirect=true";
    }

    public String enviarAlunoParaMatricula() {
        FacesUtils.setBean("aluno", aluno);
        return "matricula.xhtml" + "?faces-redirect=true";
    }

    public void pesquisarGenitores() {
        Genitores g = new Genitores();
        try {
            if (cpfPesquisa != null && !cpfPesquisa.isEmpty()) {
                g = alunoDAO.pesquisarGenitoresPorCPF(cpfPesquisa);
            }

            if (g.getId() != null) {
                genitores = g;
                encontrouGenitores = true;
            }
        } catch (DAOException e) {
        }
    }

    public void salvarEscola2() {
        try {
            Denominacao d = denominacaoDAO.buscarPorID(idDenominacao);
            escola.setDenominacao(d);

            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            escola.setUnidade(u.getUnidade());
            escola.setStatus(true);
            escolaDAO.salvar(escola);
            FacesUtils.addInfoMessageFlashScoped("Escola salva com sucesso!");

            escola = new Escola();
            listarEscolaPorDenominacao(d);

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

    public void salvarPais() {
        try {
            pais.setStatus(true);
            paisDAO.salvar(pais);
            FacesUtils.addInfoMessage("País salvo com sucesso!");

            pais = new Pais();

            listarPaises();

        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

//==========================================================================
    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public ArrayList<Municipio> getMunicipios() {
        return municipios;
    }

    public ArrayList<Bairro> getBairros() {
        return bairros;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public boolean isEditandoALuno() {
        return editandoALuno;
    }

    public boolean isMatricularNaTurma() {
        return matricularNaTurma;
    }

    public void setMatricularNaTurma(boolean matricularNaTurma) {
        this.matricularNaTurma = matricularNaTurma;
    }

    public ArrayList<Pais> getPaises() {
        return paises;
    }

    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public ArrayList<Situacao> getSituacoes() {
        return situacoes;
    }

    public Long getIdSituacao() {
        return idSituacao;
    }

    public void setIdSituacao(Long idSituacao) {
        this.idSituacao = idSituacao;
    }

    public Genitores getGenitores() {
        return genitores;
    }

    public void setGenitores(Genitores genitores) {
        this.genitores = genitores;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public ArrayList<Escolaridade> getEscolaridades() {
        return escolaridades;
    }

    public ArrayList<Serie> getSeries() {
        return series;
    }

    public ArrayList<Periodo> getPeriodos() {
        return periodos;
    }

    public ArrayList<Denominacao> getDenominacoes() {
        return denominacoes;
    }

    public ArrayList<Escola> getEscolas() {
        return escolas;
    }

    public Long getIdEscolaridade() {
        return idEscolaridade;
    }

    public void setIdEscolaridade(Long idEscolaridade) {
        this.idEscolaridade = idEscolaridade;
    }

    public Long getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(Long idSerie) {
        this.idSerie = idSerie;
    }

    public Long getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Long idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Long getIdEscola() {
        return idEscola;
    }

    public void setIdEscola(Long idEscola) {
        this.idEscola = idEscola;
    }

    public Long getIdDenominacao() {
        return idDenominacao;
    }

    public void setIdDenominacao(Long idDenominacao) {
        this.idDenominacao = idDenominacao;
    }

    public String getCpfPesquisa() {
        return cpfPesquisa;
    }

    public void setCpfPesquisa(String cpfPesquisa) {
        this.cpfPesquisa = cpfPesquisa;
    }

    public ArrayList<Turma> getTurmas() {
        return turmas;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public ListaDeEspera getListaDeEspera() {
        return listaDeEspera;
    }

    public void setListaDeEspera(ListaDeEspera listaDeEspera) {
        this.listaDeEspera = listaDeEspera;
    }

    public boolean isInscreverNaLista() {
        return inscreverNaLista;
    }

    public void setInscreverNaLista(boolean inscreverNaLista) {
        this.inscreverNaLista = inscreverNaLista;
    }

    public ArrayList<ListaDeEspera> getListas() {
        return listas;
    }

    public ArrayList<Matricula> getMatriculas() {
        return matriculas;
    }

    public boolean isExibirMinhasAtividades() {
        return exibirMinhasAtividades;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

}
