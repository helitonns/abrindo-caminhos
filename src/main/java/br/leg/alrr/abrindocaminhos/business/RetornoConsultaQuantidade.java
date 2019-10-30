package br.leg.alrr.abrindocaminhos.business;

/**
 *
 * @author heliton
 */
public class RetornoConsultaQuantidade {

    private String texto;
    private Long quantidade;

    public RetornoConsultaQuantidade() {
    }

    public RetornoConsultaQuantidade(String texto, Long quantidade) {
        this.texto = texto;
        this.quantidade = quantidade;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
    
}
