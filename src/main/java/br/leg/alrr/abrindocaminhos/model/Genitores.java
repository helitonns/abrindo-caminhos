package br.leg.alrr.abrindocaminhos.model;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
@AuditTable(value = "genitores_auditoria", schema = "abrindo_caminhos")
@Table(schema = "abrindo_caminhos")
@Entity
public class Genitores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeMae;

    private String cpfMae;

    private String rgMae;

    private String telefoneMae;
    private String telefoneMae2;

    @Temporal(TemporalType.DATE)
    private Date dataNascimentoMae;

    private String nomePai;

    private String cpfPai;

    private String rgPai;

    private String telefonePai;
    private String telefonePai2;

    @Temporal(TemporalType.DATE)
    private Date dataNascimentoPai;

    @Transient
    String idadeMae;
    
    @Transient
    String idadePai;
    //=========================================================================//
    public Genitores() {
    }

    public Genitores(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getCpfMae() {
        return cpfMae;
    }

    public void setCpfMae(String cpfMae) {
        this.cpfMae = cpfMae;
    }

    public String getRgMae() {
        return rgMae;
    }

    public void setRgMae(String rgMae) {
        this.rgMae = rgMae;
    }

    public String getTelefoneMae() {
        return telefoneMae;
    }

    public void setTelefoneMae(String telefoneMae) {
        this.telefoneMae = telefoneMae;
    }

    public Date getDataNascimentoMae() {
        return dataNascimentoMae;
    }

    public void setDataNascimentoMae(Date dataNascimentoMae) {
        this.dataNascimentoMae = dataNascimentoMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getCpfPai() {
        return cpfPai;
    }

    public void setCpfPai(String cpfPai) {
        this.cpfPai = cpfPai;
    }

    public String getRgPai() {
        return rgPai;
    }

    public void setRgPai(String rgPai) {
        this.rgPai = rgPai;
    }

    public String getTelefonePai() {
        return telefonePai;
    }

    public void setTelefonePai(String telefonePai) {
        this.telefonePai = telefonePai;
    }

    public Date getDataNascimentoPai() {
        return dataNascimentoPai;
    }

    public void setDataNascimentoPai(Date dataNascimentoPai) {
        this.dataNascimentoPai = dataNascimentoPai;
    }

    public String getTelefoneMae2() {
        return telefoneMae2;
    }

    public void setTelefoneMae2(String telefoneMae2) {
        this.telefoneMae2 = telefoneMae2;
    }

    public String getTelefonePai2() {
        return telefonePai2;
    }

    public void setTelefonePai2(String telefonePai2) {
        this.telefonePai2 = telefonePai2;
    }

  
    public String getIdadeMae() {
        StringBuilder sb = new StringBuilder();
        if (dataNascimentoMae != null) {
            GregorianCalendar g1 = new GregorianCalendar();
            GregorianCalendar g2 = new GregorianCalendar();
            g2.setTime(dataNascimentoMae);
            
            int idade1 = g1.get(GregorianCalendar.YEAR) - g2.get(GregorianCalendar.YEAR);
            int mesAtual = g1.get(GregorianCalendar.MONTH) + 1;
            int mesDaIdade = g2.get(GregorianCalendar.MONTH) + 1;
            int fracaoDeMes = mesAtual - mesDaIdade;
            
            if (fracaoDeMes == 0) {
                return sb.append(idade1).toString();
            }else if (fracaoDeMes > 0) {
                return sb.append(idade1).append(" anos e ").append(fracaoDeMes).append(" meses").toString();
            }else if (fracaoDeMes < 0) {
                return sb.append(idade1-1).append(" anos e ").append(12+fracaoDeMes).append(" meses").toString();
            } 
        }
        return "0";
    }
    
    public String getIdadePai() {
        StringBuilder sb = new StringBuilder();
        if (dataNascimentoPai != null) {
            GregorianCalendar g1 = new GregorianCalendar();
            GregorianCalendar g2 = new GregorianCalendar();
            g2.setTime(dataNascimentoPai);
            
            int idade1 = g1.get(GregorianCalendar.YEAR) - g2.get(GregorianCalendar.YEAR);
            int mesAtual = g1.get(GregorianCalendar.MONTH) + 1;
            int mesDaIdade = g2.get(GregorianCalendar.MONTH) + 1;
            int fracaoDeMes = mesAtual - mesDaIdade;
            
            if (fracaoDeMes == 0) {
                return sb.append(idade1).toString();
            }else if (fracaoDeMes > 0) {
                return sb.append(idade1).append(" anos e ").append(fracaoDeMes).append(" meses").toString();
            }else if (fracaoDeMes < 0) {
                return sb.append(idade1-1).append(" anos e ").append(12+fracaoDeMes).append(" meses").toString();
            } 
        }
        return "0";
    }

    public void setIdadeMae(String idadeMae) {
        this.idadeMae = idadeMae;
    }

    public void setIdadePai(String idadePai) {
        this.idadePai = idadePai;
    }
    
    
}
