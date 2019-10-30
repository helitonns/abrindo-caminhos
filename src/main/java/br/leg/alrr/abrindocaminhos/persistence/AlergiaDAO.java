package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Alergia;
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
public class AlergiaDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Alergia o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar alergia.", e);
        }
    }

    public void atualizar(Alergia o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza alergia.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Alergia o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar paíss.", e);
        }
    }
    
    public List listarAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Alergia o where o.status = TRUE order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar paíss.", e);
        }
    }

    public void remover(Alergia o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover alergia.", e);
        }
    }

    public Alergia buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Alergia.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar país por ID.", e);
        }
    }

}
