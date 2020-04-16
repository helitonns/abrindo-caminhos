package br.leg.alrr.abrindocaminhos.model;

import br.leg.alrr.abrindocaminhos.util.BaseEntity;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Heliton
 */
@Audited
@AuditTable(value = "turma_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Turma implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne(fetch = FetchType.EAGER)
    private Horario horario;

    @ManyToOne
    private Atividade atividade;

    private String diasDaSemana;

    private boolean status;

    private boolean iniciada;
    
    private int idadeMinima;
    
    private int idadeMaxima;

    @ManyToOne(fetch = FetchType.EAGER)
    private Unidade unidade;

    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    private Date dataTermino;

    @Transient
    private Long quantidadeDeAlunos;
    
    @Transient
    private BigInteger rev;
    
    @Transient
    private Short revtype;
    
    @Transient
    private Date dataOperacao;
    
    @Transient
    private String usuario;
    
    @Transient
    private BigInteger idEntidade;
//==============================================================================

    public Turma() {
    }

    public Turma(BigInteger idEntidade, String nome, BigInteger rev, Short revtype, String usuario, Date dataOperacao) {
        this.nome = nome;
        this.rev = rev;
        this.revtype = revtype;
        this.dataOperacao = dataOperacao;
        this.usuario = usuario;
        this.idEntidade = idEntidade;
    }
    
    
    
    public Turma(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(String diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getNomeExibicao() {
        try {
            StringBuilder sb = new StringBuilder();
            
            sb.append(atividade.getDescricao());
            sb.append(" | ");
            sb.append(nome);
            sb.append(" | ");

            // TRANSFORMANDO A STRING EM UM ARRAY PARA PEGAR CADA DIA DA SEMANA SEPARADAMENTE
            String[] s = diasDaSemana.split(",");
            for (int i = 0; i < s.length; i++) {
                sb.append(s[i]);

                if ((i + 1) >= s.length) {
                } else {
                    sb.append(", ");
                }
            }
            //--------------------------------------------------------------------------------

            sb.append(" | ");
            sb.append(horario.getDescricao());
           
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            if(dataInicio != null){
                sb.append(" | ");
                sb.append(sdf.format(dataInicio));
            }else{
                sb.append(" | ");
                sb.append("data início");
            }
            
            if(dataTermino != null){
                sb.append(" | ");
                sb.append(sdf.format(dataTermino));
            }else{
                sb.append(" | ");
                sb.append("data término");
            }
            
            
            return sb.toString();
        } catch (Exception e) {
            return nome;
        }
    }

    public String getExibirDiasDaSemana() {
        StringBuilder sb = new StringBuilder();
        // TRANSFORMANDO A STRING EM UM ARRAY PARA PEGAR CADA DIA DA SEMANA SEPARADAMENTE
        String[] s = diasDaSemana.split(",");
        for (int i = 0; i < s.length; i++) {
            sb.append(s[i]);

            if ((i + 1) >= s.length) {
            } else {
                sb.append(", ");
            }
        }
        //--------------------------------------------------------------------------------
        return sb.toString();
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isIniciada() {
        return iniciada;
    }

    public void setIniciada(boolean iniciada) {
        this.iniciada = iniciada;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Long getQuantidadeDeAlunos() {
        return quantidadeDeAlunos;
    }

    public void setQuantidadeDeAlunos(Long quantidadeDeAlunos) {
        this.quantidadeDeAlunos = quantidadeDeAlunos;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public int getIdadeMinima() {
        return idadeMinima;
    }

    public void setIdadeMinima(int idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    public int getIdadeMaxima() {
        return idadeMaxima;
    }

    public void setIdadeMaxima(int idadeMaxima) {
        this.idadeMaxima = idadeMaxima;
    }

    public BigInteger getRev() {
        return rev;
    }

    public void setRev(BigInteger rev) {
        this.rev = rev;
    }

    public Short getRevtype() {
        return revtype;
    }

    public void setRevtype(Short revtype) {
        this.revtype = revtype;
    }

    public Date getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(Date dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigInteger getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(BigInteger idEntidade) {
        this.idEntidade = idEntidade;
    }

    public String getTipoDeOperacao() {
        if (null == revtype) {
            return "DELETE";
        }else switch (revtype) {
            case 0:
                return "INSERT";
            case 1:
                return "UPDATE";
            default:
                return "DELETE";
        }
    }
}
