package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.ListaDeEspera;
import br.leg.alrr.abrindocaminhos.model.Unidade;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author heliton
 */
@Stateless
public class ListaDeEsperaDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(ListaDeEspera o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar lista de espera: " + e.getMessage(), e);
        }
    }

    public void atualizar(ListaDeEspera o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar lista de espera.", e);
        }
    }

    public ListaDeEspera buscarReferencia(ListaDeEspera o) throws DAOException {
        try {
            return em.getReference(ListaDeEspera.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência do lista de espera.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from ListaDeEspera o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar lista de esperas.", e);
        }
    }

    public List listarListaDeEsperasAtivas() throws DAOException {
        try {
            return em.createQuery("select o from ListaDeEspera o where o.status = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar lista de esperas ativas.", e);
        }
    }

    public List listarListaDeEsperasIniciadas() throws DAOException {
        try {
            return em.createQuery("select o from ListaDeEspera o where o.iniciada = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar lista de esperas ativas.", e);
        }
    }

    public List listarListaDeEsperasIniciadasPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from ListaDeEspera o where o.status = TRUE AND o.iniciada = true and o.unidade.id = :idUnidade order by o.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar lista de esperas por unidade.", e);
        }
    }

    public List listarListaDeEsperasPorUnidadeESeIniciadas(Unidade u, String statusListaDeEspera) throws DAOException {
        try {
            StringBuilder sb = new StringBuilder();

            if (statusListaDeEspera.equals("ambas")) {
                sb.append("select o from ListaDeEspera o where o.status = TRUE ");

                //VERIFICA SE A UNIDADE FOI PASSADA
                if (u.getId() <= 0) {
                    sb.append("order by o.nome asc");
                    return em.createQuery(sb.toString())
                            .getResultList();
                } else {
                    sb.append("AND o.unidade.id = :idUnidade order by o.nome asc");
                    return em.createQuery(sb.toString())
                            .setParameter("idUnidade", u.getId())
                            .getResultList();
                }
            } else {
                boolean b = (statusListaDeEspera.equals("ativas"));

                sb.append("select o from ListaDeEspera o where o.status = TRUE ");

                //VERIFICA SE A UNIDADE FOI PASSADA
                if (u.getId() <= 0) {
                    sb.append("AND o.iniciada =:statusListaDeEspera order by o.nome asc");
                    return em.createQuery(sb.toString())
                            .setParameter("statusListaDeEspera", b)
                            .getResultList();
                } else {
                    sb.append("AND o.unidade.id = :idUnidade AND o.iniciada =:statusListaDeEspera order by o.nome asc");
                    return em.createQuery(sb.toString())
                            .setParameter("idUnidade", u.getId())
                            .setParameter("statusListaDeEspera", b)
                            .getResultList();
                }
            }
        } catch (Exception e) {
            throw new DAOException("Erro ao listar lista de esperas por unidade.", e);
        }
    }

    public List listarListaDeEsperasPorAluno(Aluno a) throws DAOException {
        try {
            return em.createQuery("select o from Matricula o where o.aluno.id = :idAluno order by o.lista de espera.nome asc")
                    .setParameter("idAluno", a.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar lista de esperas por aluno.", e);
        }
    }

    public void remover(ListaDeEspera o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover lista de espera.", e);
        }
    }

    public ListaDeEspera buscarPorID(Long id) throws DAOException {
        try {
            return em.find(ListaDeEspera.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar lista de espera por ID.", e);
        }
    }

    public boolean verificarSeAlunoJaEstaMatriculadoNaListaDeEspera(Aluno a, ListaDeEspera t) throws DAOException {
        try {
            List l = em.createQuery("select o from Matricula o where o.aluno.id = :idAluno and o.lista de espera.id = :idListaDeEspera")
                    .setParameter("idAluno", a.getId())
                    .setParameter("idListaDeEspera", t.getId())
                    .getResultList();
            return l.size() > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar lista de esperas por aluno.", e);
        }
    }

}
