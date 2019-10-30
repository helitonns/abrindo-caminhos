package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Denominacao;
import br.leg.alrr.abrindocaminhos.model.Escola;
import br.leg.alrr.abrindocaminhos.model.Unidade;
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
public class EscolaDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Escola o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar escola.", e);
        }
    }

    public void atualizar(Escola o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar escola.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Escola o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar escolas.", e);
        }
    }
    
    public List listarTodosPorUnidade(Unidade u) throws DAOException{
        try {
            return em.createQuery("select o from Escola o where o.unidade.id =:idUnidade order by o.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar escolas.", e);
        }
    }

    public void remover(Escola o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover escola.", e);
        }
    }
    
    public Escola buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Escola.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar escola por ID.", e);
        }
    }
    
    public List<Escola> listarEscolaPorDenominacao(Denominacao d) throws DAOException{
        try {
            return em.createQuery("select o from Escola o where o.denominacao =:d order by o.nome asc")
                    .setParameter("d", d)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar escola por município.", e);
        }
    }
    
    public List<Escola> listarEscolaPorDenominacaoEPorUnidade(Denominacao d, Unidade u) throws DAOException{
        try {
            return em.createQuery("select o from Escola o where o.denominacao =:d and o.unidade.id=:idUnidade order by o.nome asc")
                    .setParameter("d", d)
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar escola por município.", e);
        }
    }
    
    public List<Escola> listarEscolaAtivasPorDenominacaoEPorUnidade(Denominacao d, Unidade u) throws DAOException{
        try {
            return em.createQuery("select o from Escola o where o.denominacao =:d and o.unidade.id=:idUnidade and o.status=true order by o.nome asc")
                    .setParameter("d", d)
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar escola por município.", e);
        }
    }

}
