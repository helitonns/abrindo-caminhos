package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Serie;
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
public class SerieDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Serie o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar série.", e);
        }
    }

    public void atualizar(Serie o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza série.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Serie o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar series.", e);
        }
    }
    
    public List listarAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Serie o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar series.", e);
        }
    }

    public void remover(Serie o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover série.", e);
        }
    }

    public Serie buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Serie.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar série por ID.", e);
        }
    }

}
