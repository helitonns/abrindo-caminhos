package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Acesso;
import br.leg.alrr.abrindocaminhos.model.Usuario;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe usada para fazer as operações no banco de dados para a Classe Acesso.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-27
 * @version 1.0
 * @see Acesso
 */
@Stateless
public class AcessoDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Acesso o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar acesso.", e);
        }
    }

    public void atualizar(Acesso o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar acesso.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Acesso o order by o.dataDeAcesso, o.inicioDoAcesso").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar bairros.", e);
        }
    }

    public void remover(Acesso o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover acesso.", e);
        }
    }
    
    public Acesso buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Acesso.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar acesso por ID.", e);
        }
    }
    
    public List<Acesso> listarAcessoPorUsuario(Usuario u) throws DAOException{
        try {
            return em.createQuery("select o from Acesso o where o.usuario =:u order by o.dataDeAcesso, o.inicioDoAcesso")
                    .setParameter("u", u)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar bairro por município.", e);
        }
    }

}
