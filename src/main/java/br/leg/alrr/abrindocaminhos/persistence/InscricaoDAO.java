package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Inscricao;
import br.leg.alrr.abrindocaminhos.model.ListaDeEspera;
import br.leg.alrr.abrindocaminhos.model.Turma;
import br.leg.alrr.abrindocaminhos.model.Unidade;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author heliton
 */
@Stateless
public class InscricaoDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Inscricao o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar inscrição.", e);
        }
    }

    public void atualizar(Inscricao o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar inscrição.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Inscricao o order by o.listaDeEspera.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar inscricaos.", e);
        }
    }

    public List listarInscricaosAtivas() throws DAOException {
        try {
            return em.createQuery("select o from Inscricao o where o.status = true order by o.listaDeEspera.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar inscricaos ativas.", e);
        }
    }

    public List listarInscricoesPorTurma(Long id) throws DAOException {
        try {
            return em.createQuery("select o from Inscricao o where o.status = true and o.listaDeEspera.id = :idTurma order by o.listaDeEspera.nome, o.aluno.nome asc")
                    .setParameter("idTurma", id)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar inscricaos por curso.", e);
        }
    }
    
    public List listarInscricoesPorOrdemDeInscricao(Long id) throws DAOException {
        try {
            List l = em.createQuery("select o from Inscricao o where o.status = true and o.listaDeEspera.id = :idTurma order by o.dataDaInscricao")
                    .setParameter("idTurma", id)
                    .getResultList();
            Collections.sort(l);
            return l;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar inscricaos por curso.", e);
        }
    }

    public List listarInscricaosAtivasPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Inscricao o where o.status = true and o.unidade.id = :idUnidade order by o.listaDeEspera.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar inscricaos por unidade.", e);
        }
    }
    
    public List listarInscricaosConcluidasPorAluno(Aluno a) throws DAOException {
        try {
            return em.createQuery("select o from Inscricao o where o.status = FALSE and o.listaDeEspera.iniciada = false AND o.aluno.id = :idAluno order by o.listaDeEspera.nome asc")
                    .setParameter("idAluno", a.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar inscricaos concluidas por aluno.", e);
        }
    }

    public void remover(Inscricao o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover inscrição.", e);
        }
    }

    public Inscricao buscarPorID(Long id) throws DAOException {
        try {
            return em.find(Inscricao.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar inscricao por ID.", e);
        }
    }

        
    public boolean podeInscrever(Long idLista, Long idALuno) throws DAOException {
        try {
            List<Inscricao> m = em.createQuery("select m from Inscricao m where m.listaDeEspera.id = :idLista and m.aluno.id = :idAluno")
                    .setParameter("idLista", idLista)
                    .setParameter("idAluno", idALuno)
                    .getResultList();
            return m.size() <= 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao verificar se é possível inscricaor.", e);
        }
    }
    
    public void finalizarInscricaoPorLista(ListaDeEspera l){
        em.createQuery("UPDATE Inscricao m SET m.status = false where m.listaDeEspera.id = :idLista").setParameter("idLista", l.getId()).executeUpdate();
    }
    
    public int excluirInscricaoPorTurma(Turma turma) throws DAOException {
        try {
            return  em.createQuery("DELETE FROM Inscricao m WHERE m.turma.id = :idTurma")
                    .setParameter("idTurma", turma.getId())
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao contar frequência da turma.", e);
        }
    }
    
    public boolean verificarSeHaInscricoesAnteriores(Inscricao i) throws DAOException {
        try {
            List l = em.createQuery("select o from Inscricao o where o.status = true and o.unidade.id = :idUnidade and o.listaDeEspera.id =:idLista and o.id < :idInscricao")
                    .setParameter("idUnidade", i.getUnidade().getId())
                    .setParameter("idLista", i.getListaDeEspera().getId())
                    .setParameter("idInscricao", i.getId())
                    .getResultList();
            return l.size() > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar inscricaos por unidade.", e);
        }
    }
   
}
