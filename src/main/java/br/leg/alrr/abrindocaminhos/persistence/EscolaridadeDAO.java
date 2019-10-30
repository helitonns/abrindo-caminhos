package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Escolaridade;
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
public class EscolaridadeDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Escolaridade o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar escolaridade.", e);
        }
    }

    public void atualizar(Escolaridade o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza escolaridade.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Escolaridade o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar escolaridades.", e);
        }
    }
    
    public List listarAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Escolaridade o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar escolaridades.", e);
        }
    }

    public void remover(Escolaridade o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover escolaridade.", e);
        }
    }

    public Escolaridade buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Escolaridade.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar escolaridade por ID.", e);
        }
    }

}
