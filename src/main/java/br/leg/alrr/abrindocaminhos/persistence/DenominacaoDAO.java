package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Denominacao;
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
public class DenominacaoDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Denominacao o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar país.", e);
        }
    }

    public void atualizar(Denominacao o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza país.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Denominacao o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar paíss.", e);
        }
    }
    
    public List listarAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Denominacao o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar paíss.", e);
        }
    }

    public void remover(Denominacao o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover país.", e);
        }
    }

    public Denominacao buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Denominacao.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar país por ID.", e);
        }
    }

}
