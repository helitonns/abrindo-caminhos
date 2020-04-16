package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.auditoria.AuditEntity;
import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Endereco;
import br.leg.alrr.abrindocaminhos.model.Genitores;
import br.leg.alrr.abrindocaminhos.model.Instrucao;
import br.leg.alrr.abrindocaminhos.model.Matricula;
import br.leg.alrr.abrindocaminhos.model.Prontuario;
import br.leg.alrr.abrindocaminhos.model.Turma;
import br.leg.alrr.abrindocaminhos.model.Usuario;
import br.leg.alrr.abrindocaminhos.persistence.AutorizacaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.auditoria.RelatorioAuditoriaDAO;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class RelatorioAuditoriaMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AutorizacaoDAO autorizacaoDAO;

    @EJB
    private RelatorioAuditoriaDAO relatorioAuditoriaDAO;

    private ArrayList<Usuario> usuarios;
    private ArrayList<AuditEntity> auditEntitys;
    private ArrayList<Aluno> alunos;
    private ArrayList<Matricula> matriculas;
    private ArrayList<Turma> turmas;
    private ArrayList<Prontuario> prontuarios;
    private ArrayList<Endereco> enderecos;
    private ArrayList<Instrucao> instrucoes;
    private ArrayList<Genitores> genitores;

    private LocalDate dataParaPesquisa;
    private LocalDate data1ParaPesquisa;
    private LocalDate data2ParaPesquisa;

    private Long idUsuario;
    private Long idEntidade;

    //======================================================================================================================================================================
    @PostConstruct
    public void init() {
        limparForm();

        listarUsuariosDoSistema();
        definirAsDatasDaSemana();

        if (FacesUtils.getURL().contains("auditoria-revisao")) {
            listarAuditEntityDoPeriodo();
        } else if (FacesUtils.getURL().contains("auditoria-aluno")) {
            listarAlunosDoPeriodo();
        } else if (FacesUtils.getURL().contains("auditoria-matricula")) {
            listarMatriculasDoPeriodo();
        } else if (FacesUtils.getURL().contains("auditoria-turma")) {
            listarTurmasDoPeriodo();
        } else if (FacesUtils.getURL().contains("auditoria-prontuario")) {
            listarProntuariosDoPeriodo();
        } else if (FacesUtils.getURL().contains("auditoria-endereco")) {
            listarEnderecosDoPeriodo();
        } else if (FacesUtils.getURL().contains("auditoria-instrucao")) {
            listarInstrucoesDoPeriodo();
        } else if (FacesUtils.getURL().contains("auditoria-genitores")) {
            listarGenitoresDoPeriodo();
        }

    }

    private void listarUsuariosDoSistema() {
        try {
            String[] s = FacesUtils.getURL().split("/");
            usuarios = (ArrayList<Usuario>) autorizacaoDAO.listarUsuariosQueTemPermissaoNoSistema(s[1]);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void definirAsDatasDaSemana() {
        LocalDate hoje = LocalDate.now();
        data1ParaPesquisa = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        data2ParaPesquisa = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
    }

    //================================= REVISÃO ============================================================================================================================
    private void listarAuditEntityDoPeriodo() {
        try {
            auditEntitys = null;
            auditEntitys = new ArrayList<>();
            auditEntitys = (ArrayList<AuditEntity>) relatorioAuditoriaDAO.listarAuditEntitysPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void listarAuditEntityDoPeriodoPorUsuario() {
        try {
            auditEntitys = null;
            auditEntitys = new ArrayList<>();
            if (idUsuario != 0l) {
                auditEntitys = (ArrayList<AuditEntity>) relatorioAuditoriaDAO.listarAuditEntityPorUsuarioEIntervaloDeDatas(idUsuario, data1ParaPesquisa, data2ParaPesquisa);
            } else {
                auditEntitys = (ArrayList<AuditEntity>) relatorioAuditoriaDAO.listarAuditEntitysPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa);
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //================================== ALUNO =============================================================================================================================
    public void listarAlunosDoPeriodo() {
        try {
            alunos = null;
            alunos = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            if (idEntidade != 0l) {
                sb.append("AND a.id = ").append(idEntidade).append(" ");
            }
            if (idUsuario != 0l) {
                sb.append("AND r.idusuario = ").append(idUsuario).append(" ");
            }

            List<Object[]> list;

            if (sb.length() > 0) {
                list = relatorioAuditoriaDAO.listarAlunoPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, sb.toString());
            } else {
                list = relatorioAuditoriaDAO.listarAlunoPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, "");
            }
            
            if (list != null) {
                for (Object[] objectArray : list) {
                    BigInteger id = (BigInteger) objectArray[0];
                    String nome = (String) objectArray[1];
                    BigInteger rev = (BigInteger) objectArray[2];
                    Short revType = (Short) objectArray[3];
                    String usuario = (String) objectArray[4];
                    Date dataOperacao = (Date) objectArray[5];

                    Aluno a = new Aluno(Long.parseLong(id.toString()), nome, Long.parseLong(rev.toString()), Long.parseLong(revType.toString()), usuario, dataOperacao);
                    alunos.add(a);
                }
            }

        } catch (DAOException e) {
            System.out.println(e.getCause());
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //================================== MATRICULA =========================================================================================================================
    public void listarMatriculasDoPeriodo() {
        try {
            matriculas = null;
            matriculas = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            if (idEntidade != 0l) {
                sb.append("AND m.id = ").append(idEntidade).append(" ");
            }
            if (idUsuario != 0l) {
                sb.append("AND r.idusuario = ").append(idUsuario).append(" ");
            }
            
            List<Object[]> list;

            if (sb.length() > 0) {
                list = relatorioAuditoriaDAO.listarMatriculasPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, sb.toString());
            } else {
                list = relatorioAuditoriaDAO.listarMatriculasPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, "");
            }

            if (list != null) {
                for (Object[] objectArray : list) {
                    BigInteger id = (BigInteger) objectArray[0];
                    BigInteger rev = (BigInteger) objectArray[1];
                    Short revType = (Short) objectArray[2];
                    String usuario = (String) objectArray[3];
                    Date dataOperacao = (Date) objectArray[4];
                    BigInteger alunoID = (BigInteger) objectArray[5];
                    BigInteger turmaID = (BigInteger) objectArray[6];

                    Matricula a = new Matricula(Long.parseLong(id.toString()), Long.parseLong(rev.toString()), Long.parseLong(revType.toString()), usuario, dataOperacao, alunoID, turmaID);
                    matriculas.add(a);
                }
            }

        } catch (DAOException e) {
            System.out.println(e.getCause());
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //================================== TURMA ============================================================================================================================
    public void listarTurmasDoPeriodo() {
        try {
            turmas = null;
            turmas = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            if (idEntidade != 0l) {
                sb.append("AND t.id = ").append(idEntidade).append(" ");
            }
            if (idUsuario != 0l) {
                sb.append("AND r.idusuario = ").append(idUsuario).append(" ");
            }

            List<Object[]> list;

            if (sb.length() > 0) {
                list = relatorioAuditoriaDAO.listarTurmasPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, sb.toString());
            } else {
                list = relatorioAuditoriaDAO.listarTurmasPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, "");
            }

            if (list != null) {
                for (Object[] objectArray : list) {
                    Turma a = new Turma((BigInteger) objectArray[0], (String) objectArray[1], (BigInteger) objectArray[2], (Short) objectArray[3], (String) objectArray[4], (Date) objectArray[5]);
                    turmas.add(a);
                }
            }

        } catch (DAOException e) {
            System.out.println(e.getCause());
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //================================== PRONTUARIO =======================================================================================================================
    public void listarProntuariosDoPeriodo() {
        try {
            prontuarios = null;
            prontuarios = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            if (idEntidade != 0l) {
                sb.append("AND t.id = ").append(idEntidade).append(" ");
            }
            if (idUsuario != 0l) {
                sb.append("AND r.idusuario = ").append(idUsuario).append(" ");
            }

            List<Object[]> list;

            if (sb.length() > 0) {
                list = relatorioAuditoriaDAO.listarProntuariosPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, sb.toString());
            } else {
                list = relatorioAuditoriaDAO.listarProntuariosPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, "");
            }

            if (list != null) {
                for (Object[] objectArray : list) {
                    Prontuario a = new Prontuario((BigInteger) objectArray[0], (BigInteger) objectArray[1], (BigInteger) objectArray[2], (Short) objectArray[3], (String) objectArray[4], (Date) objectArray[5]);
                    prontuarios.add(a);
                }
            }

        } catch (DAOException e) {
            System.out.println(e.getCause());
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //================================== ENDERECO =========================================================================================================================
    public void listarEnderecosDoPeriodo() {
        try {
            enderecos = null;
            enderecos = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            if (idEntidade != 0l) {
                sb.append("AND t.id = ").append(idEntidade).append(" ");
            }
            if (idUsuario != 0l) {
                sb.append("AND r.idusuario = ").append(idUsuario).append(" ");
            }

            List<Object[]> list;

            if (sb.length() > 0) {
                list = relatorioAuditoriaDAO.listarEnderecosPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, sb.toString());
            } else {
                list = relatorioAuditoriaDAO.listarEnderecosPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, "");
            }

            if (list != null) {
                for (Object[] objectArray : list) {
                    Endereco a = new Endereco((BigInteger) objectArray[0], (BigInteger) objectArray[1], (Short) objectArray[2], (String) objectArray[3], (Date) objectArray[4]);
                    enderecos.add(a);
                }
            }

        } catch (DAOException e) {
            System.out.println(e.getCause());
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //================================== INSTRUCAO =========================================================================================================================
    public void listarInstrucoesDoPeriodo() {
        try {
            instrucoes = null;
            instrucoes = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            if (idEntidade != 0l) {
                sb.append("AND t.id = ").append(idEntidade).append(" ");
            }
            if (idUsuario != 0l) {
                sb.append("AND r.idusuario = ").append(idUsuario).append(" ");
            }

            List<Object[]> list;

            if (sb.length() > 0) {
                list = relatorioAuditoriaDAO.listarInstrucoesPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, sb.toString());
            } else {
                list = relatorioAuditoriaDAO.listarInstrucoesPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, "");
            }

            if (list != null) {
                for (Object[] objectArray : list) {
                    Instrucao a = new Instrucao((BigInteger) objectArray[0], (BigInteger) objectArray[1], (Short) objectArray[2], (String) objectArray[3], (Date) objectArray[4]);
                    instrucoes.add(a);
                }
            }

        } catch (DAOException e) {
            System.out.println(e.getCause());
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //================================== INSTRUCAO =========================================================================================================================
    public void listarGenitoresDoPeriodo() {
        try {
            genitores = null;
            genitores = new ArrayList<>();

            StringBuilder sb = new StringBuilder();

            if (idEntidade != 0l) {
                sb.append("AND t.id = ").append(idEntidade).append(" ");
            }
            if (idUsuario != 0l) {
                sb.append("AND r.idusuario = ").append(idUsuario).append(" ");
            }

            List<Object[]> list;

            if (sb.length() > 0) {
                list = relatorioAuditoriaDAO.listarGenitoresPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, sb.toString());
            } else {
                list = relatorioAuditoriaDAO.listarGenitoresPorIntervaloDeDatas(data1ParaPesquisa, data2ParaPesquisa, "");
            }

            if (list != null) {
                for (Object[] objectArray : list) {
                    Genitores a = new Genitores((BigInteger) objectArray[0], (BigInteger) objectArray[1], (Short) objectArray[2], (String) objectArray[3], (Date) objectArray[4]);
                    genitores.add(a);
                }
            }

        } catch (DAOException e) {
            System.out.println(e.getCause());
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    //=====================================================================================================================================================================
    private void limparForm() {
        dataParaPesquisa = LocalDate.now();
        idUsuario = 0l;
        idEntidade = 0l;
        usuarios = new ArrayList<>();
    }

    public String cancelar() {
        return "auditoria-aluno.xhtml" + "?faces-redirect=true";
    }

    //=====================================================================================================================================================================
    /**
     * Método usado para liberar memória. Foi necessário adicionar este método
     * porque, possivelmente, está havendo vazamento de memória, fazendo com que
     * a aplicação pare de funcionar. Basicamente o método irá anular as
     * referências das variáveis, sinalizando para o Garbage Collector realizar
     * a coleta.
     */
    private void limparMemoria() {
        dataParaPesquisa = null;
        usuarios = null;
    }

    /**
     * Ao sair da página executa o método @limparMemoria.
     */
    @PreDestroy
    public void saindoDaPagina() {
        limparMemoria();
    }

    //=====================================================================================================================================================================
    public LocalDate getDataParaPesquisa() {
        return dataParaPesquisa;
    }

    public void setDataParaPesquisa(LocalDate dataParaPesquisa) {
        this.dataParaPesquisa = dataParaPesquisa;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getData1ParaPesquisa() {
        return data1ParaPesquisa;
    }

    public void setData1ParaPesquisa(LocalDate data1ParaPesquisa) {
        this.data1ParaPesquisa = data1ParaPesquisa;
    }

    public LocalDate getData2ParaPesquisa() {
        return data2ParaPesquisa;
    }

    public void setData2ParaPesquisa(LocalDate data2ParaPesquisa) {
        this.data2ParaPesquisa = data2ParaPesquisa;
    }

    public ArrayList<AuditEntity> getAuditEntitys() {
        return auditEntitys;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public Long getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(Long idEntidade) {
        this.idEntidade = idEntidade;
    }

    public ArrayList<Matricula> getMatriculas() {
        return matriculas;
    }

    public ArrayList<Turma> getTurmas() {
        return turmas;
    }

    public ArrayList<Prontuario> getProntuarios() {
        return prontuarios;
    }

    public ArrayList<Endereco> getEnderecos() {
        return enderecos;
    }

    public ArrayList<Instrucao> getInstrucoes() {
        return instrucoes;
    }

    public ArrayList<Genitores> getGenitores() {
        return genitores;
    }

}
