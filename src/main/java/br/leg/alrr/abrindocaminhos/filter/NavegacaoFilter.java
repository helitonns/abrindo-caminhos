package br.leg.alrr.abrindocaminhos.filter;

import br.leg.alrr.abrindocaminhos.business.TipoAcao;
import br.leg.alrr.abrindocaminhos.model.Autorizacao;
import br.leg.alrr.abrindocaminhos.model.LogSistema;
import br.leg.alrr.abrindocaminhos.model.UsuarioComUnidade;
import br.leg.alrr.abrindocaminhos.persistence.LogSistemaDAO;
import br.leg.alrr.abrindocaminhos.util.FacesUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/pages/*")
public class NavegacaoFilter implements Filter {

    public NavegacaoFilter() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {

            HttpSession httpSession = ((HttpServletRequest) request).getSession();
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            if (httpServletRequest.getRequestURI().indexOf("index.xhtml") <= -1) {

                UsuarioComUnidade u = (UsuarioComUnidade) httpSession.getAttribute("usuario");
                if (u == null) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/index.xhtml");
                } else {
                    Autorizacao autorizacao = (Autorizacao) httpSession.getAttribute("autorizacao");

                    if (autorizacao.getPrivilegio().getDescricao().equals("RELATORIO") && (httpServletRequest.getRequestURI().contains("superadmin") || httpServletRequest.getRequestURI().contains("admin") || httpServletRequest.getRequestURI().contains("user"))) {
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/relatorio/relatorio-aniversariante-endereco.xhtml");
                    } else if (autorizacao.getPrivilegio().getDescricao().equals("OPERADOR") && (httpServletRequest.getRequestURI().contains("admin") || httpServletRequest.getRequestURI().contains("superadmin"))) {
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/user/listar-editar-aluno.xhtml");
                    } else if (autorizacao.getPrivilegio().getDescricao().equals("ADMIN") && httpServletRequest.getRequestURI().contains("superadmin")) {
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/user/listar-editar-aluno.xhtml");
                    } else {
                        chain.doFilter(request, response);
                    }
                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
