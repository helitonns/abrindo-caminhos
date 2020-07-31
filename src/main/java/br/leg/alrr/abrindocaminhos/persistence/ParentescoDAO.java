package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Parentesco;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author heliton
 */
@Stateless
public class ParentescoDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Parentesco o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
             throw new DAOException("Erro ao salvar parentesco.", e);
        }
    }

    public void atualizar(Parentesco o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar parentesco.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Parentesco o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar horários.", e);
        }
    }
    
    public List listarAtivos() throws DAOException{
        try {
            return em.createQuery("select o from Parentesco o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar horários ativos.", e);
        }
    }

    public void remover(Parentesco o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover parentesco.", e);
        }
    }

    public Parentesco buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Parentesco.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar parentesco por ID.", e);
        }
    }
    
    public boolean parentescoNaoCadastrado(String parentesco) throws DAOException{
        try {
            List l =  em.createQuery("select o from Parentesco o where o.descricao = :descricao")
                    .setParameter("descricao", parentesco)
                    .getResultList();
            return l.size() <= 0;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao verificar se o parentesco já foi cadastrado.", e);
        }
    }
}
