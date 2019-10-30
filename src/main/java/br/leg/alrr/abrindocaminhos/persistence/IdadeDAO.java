package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Idade;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Heliton
 */
@Stateless
public class IdadeDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Idade o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar idade.", e);
        }
    }

    public void atualizar(Idade o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza idade.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            List l =  em.createQuery("select o from Idade o order by o.descricao asc").getResultList();
            Collections.sort(l);
            return l;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar series.", e);
        }
    }
    
    public List listarAtivas() throws DAOException {
        try {
            List l = em.createQuery("select o from Idade o order by (o.descricao)").getResultList();
            Collections.sort(l);
            return l;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar series.", e);
        }
    }

    public void remover(Idade o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover idade.", e);
        }
    }

    public Idade buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Idade.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar idade por ID.", e);
        }
    }

}
