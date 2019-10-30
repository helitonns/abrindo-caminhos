package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Atividade;
import br.leg.alrr.abrindocaminhos.model.Unidade;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Heliton
 */
@Stateless
public class AtividadeDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Atividade o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar atividade.", e);
        }
    }

    public void atualizar(Atividade o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza atividade.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Atividade o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar series.", e);
        }
    }
    
    public List listarTodosPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Atividade o where o.unidade.id =:idUnidade order by o.descricao asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar series.", e);
        }
    }
    
    public List listarAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Atividade o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar series.", e);
        }
    }
    
    public List listarAtivasPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Atividade o where o.status = true and o.unidade.id =:idUnidade order by o.descricao asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar series.", e);
        }
    }

    public void remover(Atividade o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover atividade.", e);
        }
    }

    public Atividade buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Atividade.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar atividade por ID.", e);
        }
    }

}
