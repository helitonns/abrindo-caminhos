package br.leg.alrr.abrindocaminhos.persistence.auditoria;

import br.leg.alrr.abrindocaminhos.auditoria.AuditEntity;
import br.leg.alrr.abrindocaminhos.util.DAOException;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe usada para recuperar os dados de auditoria da Classe AuditEntity
 *
 * @author Heliton Nascimento
 * @since 2020-04-08
 * @version 1.0
 * @see AuditEntity
 */
@Stateless
public class RelatorioAuditoriaDAO {

    @PersistenceContext
    protected EntityManager em;

    //============================================= REVISÃO ==================================================================================================
    public List listarAuditEntitysPorIntervaloDeDatas(LocalDate data1, LocalDate data2) throws DAOException {
        try {
            return em.createNativeQuery("SELECT o.rev, o.dataoperacao, o.ip, o.senha, o.revstmp, o.usuario, o.idusuario FROM abrindo_caminhos.revisao_info as o where o.dataoperacao BETWEEN :data1 and :data2", AuditEntity.class)
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar AuditEntity por data.", e);
        }
    }
    
    public List listarAuditEntityPorUsuarioEIntervaloDeDatas(Long idUsuario, LocalDate data1, LocalDate data2) throws DAOException{
        try {
            return em.createNativeQuery(
                    "SELECT o.rev, o.dataoperacao, o.ip, o.senha, o.revstmp, o.usuario, o.idusuario "
                    + "FROM abrindo_caminhos.revisao_info as o "
                    + "where o.idusuario = :idUsuario AND o.dataoperacao BETWEEN :data1 and :data2", AuditEntity.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar AuditEntity por data.", e);
        }
    }

    //============================================= ALUNO ==================================================================================================
    public List listarAlunoPorIntervaloDeDatas(LocalDate data1, LocalDate data2, String parteDaConsulta) throws DAOException{
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT a.id, a.nome, a.rev, a.revtype, r.usuario, r.dataoperacao ");
            query.append("FROM abrindo_caminhos.aluno_auditoria as a ");
            query.append("inner join abrindo_caminhos.revisao_info as r on a.rev = r.rev where r.dataoperacao BETWEEN :data1 and :data2 ");
            
            if (!parteDaConsulta.isEmpty()) {
                query.append(parteDaConsulta);
            }
            
            return em.createNativeQuery(query.toString())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar AlunoAuditoria por data.", e);
        }
    }
    
    //============================================= MATRICULA ==================================================================================================
    public List listarMatriculasPorIntervaloDeDatas(LocalDate data1, LocalDate data2, String parteDaConsulta) throws DAOException{
        try {
            StringBuilder query = new StringBuilder();
            
            query.append("SELECT m.id, m.rev, m.revtype, r.usuario, r.dataoperacao, m.aluno_id, m.turma_id ");
            query.append("FROM abrindo_caminhos.matricula_auditoria as m ");
            query.append("inner join abrindo_caminhos.revisao_info as r on m.rev = r.rev ");
            query.append("where r.dataoperacao BETWEEN :data1 and :data2 ");
            
            if (!parteDaConsulta.isEmpty()) {
                query.append(parteDaConsulta);
            }
            
            return em.createNativeQuery(query.toString())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar MatriculaAuditoria por data.", e);
        }
    }
    
    //============================================= TURMA ==================================================================================================
    public List listarTurmasPorIntervaloDeDatas(LocalDate data1, LocalDate data2, String parteDaConsulta) throws DAOException{
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT t.id, t.nome, t.rev, t.revtype, r.usuario, r.dataoperacao ");
            query.append("FROM abrindo_caminhos.turma_auditoria as t ");
            query.append("inner join abrindo_caminhos.revisao_info as r on t.rev = r.rev ");
            query.append("where r.dataoperacao BETWEEN :data1 and :data2 ");
            
            if (!parteDaConsulta.isEmpty()) {
                query.append(parteDaConsulta);
            }
            
            return em.createNativeQuery(query.toString())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar TurmaAuditoria por data.", e);
        }
    }

    //============================================= PRONTUARIO ==================================================================================================
    public List listarProntuariosPorIntervaloDeDatas(LocalDate data1, LocalDate data2, String parteDaConsulta) throws DAOException{
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT t.id, t.aluno_id, t.rev, t.revtype, r.usuario, r.dataoperacao ");
            query.append("FROM abrindo_caminhos.protuario_auditoria as t ");
            query.append("inner join abrindo_caminhos.revisao_info as r on t.rev = r.rev ");
            query.append("where r.dataoperacao BETWEEN :data1 and :data2 ");
            
            if (!parteDaConsulta.isEmpty()) {
                query.append(parteDaConsulta);
            }
            
            return em.createNativeQuery(query.toString())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar ProntuarioAuditoria por data.", e);
        }
    }
    
    //============================================= ENDERECO ====================================================================================================
    public List listarEnderecosPorIntervaloDeDatas(LocalDate data1, LocalDate data2, String parteDaConsulta) throws DAOException{
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT t.id, t.rev, t.revtype, r.usuario, r.dataoperacao ");
            query.append("FROM abrindo_caminhos.endereco_auditoria as t ");
            query.append("inner join abrindo_caminhos.revisao_info as r on t.rev = r.rev ");
            query.append("where r.dataoperacao BETWEEN :data1 and :data2 ");
            
            if (!parteDaConsulta.isEmpty()) {
                query.append(parteDaConsulta);
            }
            
            return em.createNativeQuery(query.toString())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar EnderecoAuditoria por data.", e);
        }
    }
    
    //============================================= INSTRUCAO ===================================================================================================
    public List listarInstrucoesPorIntervaloDeDatas(LocalDate data1, LocalDate data2, String parteDaConsulta) throws DAOException{
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT t.id, t.rev, t.revtype, r.usuario, r.dataoperacao ");
            query.append("FROM abrindo_caminhos.instrucao_auditoria as t ");
            query.append("inner join abrindo_caminhos.revisao_info as r on t.rev = r.rev ");
            query.append("where r.dataoperacao BETWEEN :data1 and :data2 ");
            
            if (!parteDaConsulta.isEmpty()) {
                query.append(parteDaConsulta);
            }
            
            return em.createNativeQuery(query.toString())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar InstrucaoAuditoria por data.", e);
        }
    }
    
    //============================================= GENITORES ===================================================================================================
    public List listarGenitoresPorIntervaloDeDatas(LocalDate data1, LocalDate data2, String parteDaConsulta) throws DAOException{
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT t.id, t.rev, t.revtype, r.usuario, r.dataoperacao ");
            query.append("FROM abrindo_caminhos.genitores_auditoria as t ");
            query.append("inner join abrindo_caminhos.revisao_info as r on t.rev = r.rev ");
            query.append("where r.dataoperacao BETWEEN :data1 and :data2 ");
            
            if (!parteDaConsulta.isEmpty()) {
                query.append(parteDaConsulta);
            }
            
            return em.createNativeQuery(query.toString())
                    .setParameter("data1", data1)
                    .setParameter("data2", data2)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar InstrucaoAuditoria por data.", e);
        }
    }
}
