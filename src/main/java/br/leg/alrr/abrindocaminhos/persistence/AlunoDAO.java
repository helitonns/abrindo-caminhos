package br.leg.alrr.abrindocaminhos.persistence;

import br.leg.alrr.abrindocaminhos.business.BlocoParametro;
import br.leg.alrr.abrindocaminhos.business.Sexo;
import br.leg.alrr.abrindocaminhos.model.Aluno;
import br.leg.alrr.abrindocaminhos.model.Endereco;
import br.leg.alrr.abrindocaminhos.model.Genitores;
import br.leg.alrr.abrindocaminhos.model.Unidade;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author heliton
 */
@Stateless
public class AlunoDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Aluno o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar aluno.", e);
        }
    }

    public void atualizar(Aluno o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar aluno.", e);
        }
    }

    public Aluno buscarReferencia(Aluno o) throws DAOException {
        try {
            return em.getReference(Aluno.class, o);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de aluno.", e);
        }
    }

    public void recarregar(Aluno aluno) throws DAOException {
        try {
            em.refresh(aluno);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de aluno.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Aluno o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar alunos.", e);
        }
    }

    public List listarTodosPorUnidade(Unidade u) throws DAOException {
        try {
            return em.createQuery("select o from Aluno o where o.unidade.id = :idUnidade order by o.nome asc")
                    .setParameter("idUnidade", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar alunos por unidade.", e);
        }
    }

    public void remover(Aluno o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover aluno.", e);
        }
    }

    public Aluno pesquisarPorCPF(String cpf) throws DAOException {
        try {
            return (Aluno) em.createQuery("select o from Aluno o where o.cpf =:cpf")
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar aluno por CPF.", e);
        }
    }

    public boolean cpfUnico(String cpf) throws DAOException {
        try {
            List l = (List) em.createQuery("select o from Aluno o where o.cpf =:cpf")
                    .setParameter("cpf", cpf)
                    .getResultList();
            return l.size() <= 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao verificar unicidade de CPF.", e);
        }
    }

    public Aluno pesquisarPorID(Long id) throws DAOException {
        try {
            return (Aluno) em.createQuery("select o from Aluno o where o.id =:id")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar aluno por ID.", e);
        }
    }

    public List pesquisarPorNome(String nome) throws DAOException {
        try {
            return em.createQuery("select o from Aluno o where o.nome LIKE :nome")
                    .setParameter("nome", "%" + nome + "%")
                    .getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException("Erro ao buscar aluno por ID.", e);
        }
    }
    
    public Genitores pesquisarPorCPFDaMae(String cpf) throws DAOException {
        try {
            return (Genitores) em.createQuery("select o from Genitores o where o.cpfMae=:cpf")
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar genitores.", e);
        }
    }
    
    public Genitores pesquisarPorCPFDoPai(String cpf) throws DAOException {
        try {
            return (Genitores) em.createQuery("select o from Genitores o where o.cpfPai=:cpf")
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar genitores.", e);
        }
    }
    
    public Genitores pesquisarGenitoresPorCPF(String cpf) throws DAOException {
        try {
            return (Genitores) em.createQuery("select o from Genitores o where o.cpfMae=:cpf or o.cpfPai=:cpf")
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar genitores.", e);
        }
    }
    
    public List<Aluno> pesquisarFamilia(Genitores genitores) throws DAOException {
        try {
            return em.createQuery("select o from Aluno o where o.genitores.id=:idGenitores order by o.nome")
                    .setParameter("idGenitores", genitores.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar famlia.", e);
        }
    }

    public List<Aluno> gerarRelatorioDinamico(String consulta, List<BlocoParametro> parametros) throws DAOException {
        try {

            Query q = em.createQuery(consulta);
            Calendar d = new GregorianCalendar();

            for (BlocoParametro b : parametros) {

                if (b.getParametro().equals("dataNascimento")) {
                    d.setTime((Date) b.getValor());
                    q.setParameter("dia", d.get(Calendar.DAY_OF_MONTH));
                    q.setParameter("mes", d.get(Calendar.MONTH) + 1);
                } else if (b.getParametro().equals("dataNascimento1")) {
                    d.setTime((Date) b.getValor());
                    q.setParameter("dia1", d.get(Calendar.DAY_OF_MONTH));
                    q.setParameter("mes1", d.get(Calendar.MONTH) + 1);
                } else if (b.getParametro().equals("dataNascimento2")) {
                    d.setTime((Date) b.getValor());
                    q.setParameter("dia2", d.get(Calendar.DAY_OF_MONTH));
                    q.setParameter("mes2", d.get(Calendar.MONTH) + 1);
                } else if (b.getParametro().equals("possuiFilho")) {
                    q.setParameter(b.getParametro(), (boolean) b.getValor());
                } else if (b.getParametro().equals("idMunicipio") || b.getParametro().equals("idBairro") || b.getParametro().equals("idPais")) {
                    q.setParameter(b.getParametro(), (Long) b.getValor());
                } else if (b.getParametro().equals("sexo")) {
                    q.setParameter(b.getParametro(), (Sexo) b.getValor());
                }
            }
            return q.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao gerar relatório.", e);
        }
    }
    
    public void salvarGenitor(Genitores o) throws DAOException {
        try {
             em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar aluno.", e);
        }
    }
    
    public boolean genitorTemMaisDeUmFilho(Genitores o) throws DAOException {
        try {
            Long n = (Long) em.createQuery("SELECT COUNT(DISTINCT a.id) FROM Aluno a WHERE a.genitores.id=:idGenitores")
                    .setParameter("idGenitores", o.getId())
                    .getSingleResult();
            return n > 1;
        } catch (Exception e) {
            throw new DAOException("Erro ao contar número de filhos.", e);
        }
    }
    
    public void removerGenitor(Genitores o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover Genitor.", e);
        }
    }
    
    public void removerEndereco(Endereco o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover Endereco.", e);
        }
    }
    
    public void atualizarGenitor(Genitores o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar aluno.", e);
        }
    }
    
    public void DeleteAluno(Aluno a) throws DAOException {
        try {
            em.createQuery("Delete from Aluno a where a.id=:idAluno")
                    .setParameter("idAluno", a.getId())
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao deletar aluno.", e);
        }
    }
    
    public Genitores buscarGenitor(Long id) throws DAOException {
        try {
            return em.find(Genitores.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de genitores.", e);
        }
    }
    
    public Genitores buscarReferenciaGenitor(Genitores o) throws DAOException {
        try {
            return em.getReference(Genitores.class, o.getId());
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar referência de genitores.", e);
        }
    }
    
    public void DeleteEndereco(Long idEndereco) throws DAOException {
        try {
            em.createQuery("Delete from Endereco e where e.id=:idEndereco")
                    .setParameter("idEndereco", idEndereco)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao deletar aluno.", e);
        }
    }
    
    public void DeleteMatricula(Long idMatricula) throws DAOException {
        try {
            em.createQuery("Delete from Matricula e where e.id=:idMatricula")
                    .setParameter("idMatricula", idMatricula)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao deletar aluno.", e);
        }
    }
    
    public void DeleteProntuario(Long idAluno) throws DAOException {
        try {
            em.createQuery("Delete from Prontuario e where e.aluno.id = :idAluno")
                    .setParameter("idAluno", idAluno)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao deletar aluno.", e);
        }
    }
    
    public void DeleteInscricao(Long idAluno) throws DAOException {
        try {
            em.createQuery("Delete from Inscricao e where e.aluno.id = :idAluno")
                    .setParameter("idAluno", idAluno)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao deletar aluno.", e);
        }
    }
    
    public void DeleteFamiliar(Long idFamiliar) throws DAOException {
        try {
            em.createQuery("Delete from Familiar e where e.id = :idFamiliar")
                    .setParameter("idFamiliar", idFamiliar)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao deletar familiar.", e);
        }
    }
}
