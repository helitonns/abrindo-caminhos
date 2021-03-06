package br.leg.alrr.abrindocaminhos.controller;

import br.leg.alrr.abrindocaminhos.business.Loger;
import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Autorizacao;
import br.leg.alrr.abrindocaminhos.model.Mensagem;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.AutorizacaoDAO;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.persistence.MensagemDAO;
import br.leg.alrr.abrindocaminhos.persistence.UsuarioComUnidadeDAO;
import br.leg.alrr.abrindocaminhos.util.Criptografia;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@Named
public class StartMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AutorizacaoDAO autorizacaoDAO;

    @EJB
    private UsuarioComUnidadeDAO usuarioDAO;

    @EJB
    private MensagemDAO mensagemDAO;

    @EJB
    private LogSistemaDAO logSistemaDAO;

    private UsuarioComUnidade usuario;
    private Autorizacao autorizacao;

    private ArrayList<Mensagem> mensagens;

    private String login = "";
    private String senha = "";
    private String senha1 = "";

    //===========================================================
    @PostConstruct
    private void init() {
        usuario = new UsuarioComUnidade();
        listarMensagens();
        
        Loger.registrar(logSistemaDAO, TipoAcao.ACESSAR, "O usuário acessou a página: " + FacesUtils.getURL()+".");
    }

    private void listarMensagens() {
        try {
            mensagens = new ArrayList<>();
            mensagens = (ArrayList<Mensagem>) mensagemDAO.listarTodasAsMensagensAtivasParaAData(LocalDate.now());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String logar() {
        try {
            usuario = usuarioDAO.pesquisarPorLogin(login);
            //ENCONTROU UM USUARIO COM O RESPECTIVO LOGIN
            if (usuario != null) {
                usuario = usuarioDAO.pesquisarPorLoginESenha(login, Criptografia.criptografarEmMD5(senha));
                if (usuario != null && usuario.isStatus()) {

                    Loger.registrar(logSistemaDAO, TipoAcao.ENTRAR, "O usuário entrou o sistema.");

                    FacesUtils.setBean("usuario", usuario);
                    return "/pages/user/listar-editar-aluno.xhtml" + "?faces-redirect=true";
                } else {
                    FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
                }
            } else {
                FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
        }
        return "/index.xhtml" + "?faces-redirect=true";
    }

    public String logar2() {
        try {
            String[] s = FacesUtils.getURL().split("/");

            autorizacao = autorizacaoDAO.verificarSeOUsuarioPossuiAutorizacao(s[1], login, Criptografia.criptografarEmMD5(senha));

            //ENCONTROU UM USUARIO COM AUTORIZAÇÃO
            if (autorizacao != null) {
                usuario = usuarioDAO.pesquisarPorLoginESenha(login, Criptografia.criptografarEmMD5(senha));
                FacesUtils.setBean("usuario", usuario);
                FacesUtils.setBean("autorizacao", autorizacao);
                Loger.registrar(logSistemaDAO, TipoAcao.ENTRAR, "O usuário entrou o sistema.");

                if (usuario.getNome() == null || usuario.getMatricula() == null) {
                    FacesUtils.addWarnMessageFlashScoped("O usuário está com o nome ou matrícula não preenchidos. Complete o seu perfil!");
                    return "/pages/user/perfil.xhtml" + "?faces-redirect=true";
                } else {
                    return "/pages/user/listar-editar-aluno.xhtml" + "?faces-redirect=true";
                }
            } else {
                FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
            System.out.println(e.getCause());
        }

        return "/index.xhtml" + "?faces-redirect=true";
    }

    public String sair() {
        try {
            Loger.registrar(logSistemaDAO, TipoAcao.SAIR, "O usuário saiu do sistema.");
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } catch (Exception e) {
        }
        return "/index.xhtml" + "?faces-redirect=true";
    }

    public void trocarSenha() {

        try {
            if (verificarForcaDaSenha(senha1)) {
                usuario.setSenha(Criptografia.criptografarEmMD5(senha1));
                usuarioDAO.atualizar(usuario);
                FacesUtils.addInfoMessage("Senha atualizada com sucesso!!!");

                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método StartMB.trocarSenha().", "A senha fo atualizada com sucesso.");
            } else {
                FacesUtils.addWarnMessage("A senha deve atender aos seguintes requisitos: ter no mínimo 8 caracteres, possuir letra minúcula 'a', possuir letra maiúscula 'A' e número '123'!!!");

                Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método StartMB.trocarSenha().", "A senha não foi atualizada porque não atendeu aos requisitos mínimos.");
            }

        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro au atualizar senha.");
        }
    }

    public String salvarNomeMatricula() {

        try {
            usuarioDAO.atualizar(usuario);
            FacesUtils.addInfoMessage("Usuário atualizado com sucesso!!!");
            Loger.registrar(logSistemaDAO, TipoAcao.ATUALIZAR, "O usuário executou o método StartMB.salvarNomeMatricula().");
            return "listar-editar-aluno.xhtml" + "?faces-redirect=true";
        } catch (DAOException e) {
            FacesUtils.addErrorMessage("Erro au atualizar senha.");
        }
        return null;
    }

    public String retornarUnidadeAtiva() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            String s = u.getUnidade().getNome().substring(0, 1);
            String t = u.getUnidade().getNome().substring(1).toLowerCase();
            String f = s + t;
            if (f.length() > 14) {
                return f.substring(0, 13) + ".";
            } else {
                return f;
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String retornarUnidadeAtivaNomeCompleto() {
        try {
            UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
            return u.getUnidade().getNome();
        } catch (Exception e) {
            return "";
        }
    }

    private boolean verificarForcaDaSenha(String senha) {
        if (senha.length() < 8) {
            return false;
        }

        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;

        for (char c : senha.toCharArray()) {
            if (c >= '0' && c <= '9') {
                achouNumero = true;
            } else if (c >= 'A' && c <= 'Z') {
                achouMaiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }
        }

        //defini os parâmetros que serão avalidados
        //neste caso não irá levar em consideração o requisito "símbolo"
        return achouNumero && achouMaiuscula && achouMinuscula;
    }

    //===========================================================
    public UsuarioComUnidade getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioComUnidade usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isUsuarioRelatorio() {
        return autorizacao.getPrivilegio().getDescricao().equals("RELATORIO");
    }

    public boolean isAdmin() {
        return autorizacao.getPrivilegio().getDescricao().equals("ADMIN");
    }

    public boolean isSuperAdmin() {
        return autorizacao.getPrivilegio().getDescricao().equals("SUPER_ADMIN");
    }

    public String getSenha1() {
        return senha1;
    }

    public void setSenha1(String senha1) {
        this.senha1 = senha1;
    }

    public Autorizacao getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Autorizacao autorizacao) {
        this.autorizacao = autorizacao;
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

}
