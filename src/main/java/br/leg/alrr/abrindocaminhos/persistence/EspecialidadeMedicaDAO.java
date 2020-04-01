package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Alergia;
import br.leg.alrr.abrindocaminhos.model.EspecialidadeMedica;
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
public class EspecialidadeMedicaDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(EspecialidadeMedica o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar especialidade médica.", e);
        }
    }

    public void atualizar(EspecialidadeMedica o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza especialidade médica.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from EspecialidadeMedica o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar paíss.", e);
        }
    }
    
    public List listarAtivas() throws DAOException {
        try {
            return em.createQuery("select o from EspecialidadeMedica o where o.status = TRUE order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar paíss.", e);
        }
    }

    public void remover(EspecialidadeMedica o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover especialidade médica.", e);
        }
    }

    public EspecialidadeMedica buscarPorID(Long id) throws DAOException {
        try {
            return em.find(EspecialidadeMedica.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar especialidade médica por ID.", e);
        }
    }

}
