package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Periodo;
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
public class PeriodoDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Periodo o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar período.", e);
        }
    }

    public void atualizar(Periodo o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza período.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Periodo o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar períodos.", e);
        }
    }
    
    public List listarPeriodosAtivos() throws DAOException {
        try {
            return em.createQuery("select o from Periodo o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar períodos.", e);
        }
    }

    public void remover(Periodo o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover período.", e);
        }
    }

    public Periodo buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Periodo.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar período por ID.", e);
        }
    }

}
