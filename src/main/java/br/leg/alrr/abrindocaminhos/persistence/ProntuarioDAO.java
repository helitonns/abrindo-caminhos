package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Prontuario;
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
public class ProntuarioDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Prontuario o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar prontuário.", e);
        }
    }

    public void atualizar(Prontuario o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualiza prontuário.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Prontuario o order by o.aluno.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar prontuários.", e);
        }
    }

    public void remover(Prontuario o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover prontuário.", e);
        }
    }

    public Prontuario buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Prontuario.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar prontuário por ID.", e);
        }
    }
    
    public Prontuario buscarProntuarioPorAluno(Aluno a) throws DAOException{
        try {
            return (Prontuario) em.createQuery("select o from Prontuario o where o.aluno.id =:idAluno")
                    .setParameter("idAluno", a.getId())
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar bairro por município.", e);
        }
    }

}
