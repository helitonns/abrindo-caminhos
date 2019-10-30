/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.leg.alrr.abrindocaminhos.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class Relatorio<T> {

    private HttpServletResponse response;
    private FacesContext context;
    private ByteArrayOutputStream baos;
    private InputStream stream;
    private InputStream subreport;
    private InputStream logo1;
    private InputStream logo2;
    
    
    public Relatorio() {
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();

    }

    /*
    Relatorio Quantidades de Aluno por Atividades - RelatorioQAA
     */
    public void getRelQuantidadeAlunoAtividade(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/quantidade-alunos-atividades.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        baos = new ByteArrayOutputStream();

        try {
        	
  
       	 
       	/*--------------------------------------------------------------------------------------*/
        	
       	 /* Relatório*/
        	
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

            
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioQAA.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
        }
    }
    
    /*
    Relatorio Dos Aniversariantes do Mês Geral - RelatorioAMG
     */
    public void getRelAniversarianteMesGeral(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/aniversariante-mes-geral.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        baos = new ByteArrayOutputStream();

        try {
        	
  
       	 
       	/*--------------------------------------------------------------------------------------*/
        	
       	 /* Relatório*/
        	
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioAMG.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
        }
    }
    
    /*
    Relatorio Dos Aniversariantes do Mês Matriculados - RelatorioAMM
     */
    public void getRelAniversarianteMesMatriculados(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/aniversariante-mes-matriculados.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        baos = new ByteArrayOutputStream();

        try {
        	
  
       	 
       	/*--------------------------------------------------------------------------------------*/
        	
       	 /* Relatório*/
        	
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioAMM.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
        }
    }
    
    /*
    Relatorio Atividades Alunos - RelatorioAA
     */
    public void getRelAtividadesAlunos(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/atividades-alunos.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        		 
        	 /* Lista de Alunos */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioAA.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar relatório!"));
        }
    }
    
    
    /*
    Relatorio Por Bairro - Matriculados - RelatorioPBM
     */
    public void getRelPorBairroMatriculados(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/bairro-matriculados.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/subreport-bairro-matriculados.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
        	 JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
        	 params.put("cabecalho", dsc);
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Alunos */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport", subreport);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioPBM.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar relatório!"));
        }
    }
    
    /*
    Relatorio Por Bairro - Geral - RelatorioPBG
     */
    public void getRelPorBairroGeral(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/bairro-geral.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/subreport-bairro-geral.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
        	 JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
        	 params.put("cabecalho", dsc);
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Alunos */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport", subreport);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioPBG.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar relatório!"));
        }
    }
   
    /*
    Lista de Frequencia
     */
    public void getListaFrequencia(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/lista-frequencia.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/subreport-frequencia.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
        	 JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
        	 params.put("cabecalho", dsc);
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Alunos */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport", subreport);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=frequencia.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar frequencia!"));
        }
    }
    
    /*
    Lista de Renovacao
     */
    public void getListaRenovacao(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/lista-renovacao.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/subreport-renovacao.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
        	 JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
        	 params.put("cabecalho", dsc);
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Alunos */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport", subreport);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=renovacao.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar renovação!"));
        }
    }
    
    
    /*
    Relatorio Atividade por Turma - RelatorioT
     */
    public void getRelTurma(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/turma.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Alunos */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioT.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar relatório!"));
        }
    }
    
    /*
    Relatório de Alunos e Telefone por Turma - RelatorioATT
     */
    public void getRelTurmaTelefone(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/turma-telefone.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/subreport-turma-telefone.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
       
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
       
        
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
        	 JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
        	 params.put("cabecalho", dsc);
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Alunos */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport", subreport);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioTT.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatório!"));
        }
    }
    
    
    /*
    Relatório Turma Mae - RelatorioTM
     */
    public void getRelTurmaMae(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/turma-mae.jasper");
        subreport = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/subreport-turma-mae.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
       
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
       
        
        
        baos = new ByteArrayOutputStream();

        try {	
        	
        	
        	
        	/* Cabeçalho da Pagina - Subreport*/
        	
        	
        	 JRBeanCollectionDataSource dsc = new JRBeanCollectionDataSource(lista);
        	 params.put("cabecalho", dsc);
        	 
        	/*--------------------------------------------------------------------------------------*/
        	 
        	 /* Lista de Alunos */
        	 
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            params.put("subreport", subreport);
            
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

           
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioTM.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatório!"));
        }
    }
    
    
    /*
    Relatorio Quantidades por Turma - RelatorioQT
     */
    public void getRelQuantidadePorTurma(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/quantidade-turma.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        baos = new ByteArrayOutputStream();

        try {
        	
  
       	 
       	/*--------------------------------------------------------------------------------------*/
        	
       	 /* Relatório*/
        	
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

            
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioQT.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
        }
    }
    
     /*
    Relatorio Quantidades Alunos por Bairro - RelatorioQAB
     */
    public void getRelQuantidadeAlunoBairro(List<T> lista) {

        stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/alunos-bairro.jasper");
        logo1 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-ac.png");
        logo2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasper/imagens/logo-alrr.png");
        
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("logo-abrindo-caminhos", logo1);
        params.put("logo-ale", logo2);
        baos = new ByteArrayOutputStream();

        try {
        	
  
       	 
       	/*--------------------------------------------------------------------------------------*/
        	
       	 /* Relatório*/
        	
            JRBeanCollectionDataSource datasrc = new JRBeanCollectionDataSource(lista);
            params.put("ItemDataSource", datasrc);
            JasperReport report = (JasperReport) JRLoader.loadObject(stream);

            
            JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, baos);

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "attachment; filename=relatorioQAB.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

            //fecharConexao();
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
        }
    }

    
}
