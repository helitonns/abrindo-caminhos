package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Situacao;
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
public class SituacaoDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Situacao o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar situacao.", e);
        }
    }

    public void atualizar(Situacao o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza situacao.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Situacao o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar situações.", e);
        }
    }
    
    public List listarSituacoesAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Situacao o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar situações.", e);
        }
    }

    public void remover(Situacao o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover situacao.", e);
        }
    }

    public Situacao buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Situacao.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar situacao por ID.", e);
        }
    }

}
