/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;
import net.sf.jasperreports.engine.JasperCompileManager;

/**
 *
 * @author Junta
 */
public class jasperreportTemplateCompile {
    public  void compileJXML() {

        try {
            String sourceFileNme = System.getProperty("user.dir") + "/reports/KlassTimetable.jrxml";
            System.out.println(sourceFileNme);
            JasperCompileManager.compileReport(this.getClass().getClassLoader().getResourceAsStream("/FXML/FirstJasper_1.jrxml"));
            System.out.println(sourceFileNme);
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }       

    public static void main(String[] args){
        //String sourceFileNme=System.getProperty("user.dir") +"/reports/klasstimetable.jrxml";
         String sourceFileNme=System.getProperty("user.dir") +"/reports/klassShortage.jrxml";
        try{
            String compileReportToFile = JasperCompileManager.compileReportToFile(sourceFileNme);
            System.out.println(compileReportToFile);
           // new  jasperreportTemplateCompile().compileJXML();
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
