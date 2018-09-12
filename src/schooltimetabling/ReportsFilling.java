/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author cherutombo
 */
public class ReportsFilling {
public void fillShortages(List<ShortageLog> myList, HashMap map) {
        String path = System.getProperty("user.dir") + "/reports/klassShortage.jasper";
        try {
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(myList);
            JasperPrint printFileNme = JasperFillManager.fillReport(path, map, beanColDataSource);
            JasperViewer.viewReport(printFileNme, false);
        } catch (JRException ex) {
            Logger.getLogger(ReportsFilling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void fillClassAllocations(List<ClassAllocations> myList, HashMap map) {
        String path = System.getProperty("user.dir") + "/reports/klassAllocations.jasper";
        try {
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(myList);
            JasperPrint printFileNme = JasperFillManager.fillReport(path, map, beanColDataSource);
            JasperViewer.viewReport(printFileNme, false);
        } catch (JRException ex) {
            Logger.getLogger(ReportsFilling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fillKlassTimeTable(List<TrTimeTableDisplay> myList, HashMap map) {
        String rootpath = System.getProperty("user.dir") + "/reports/KlassTimeTable.jasper";
        fillReport(myList, map, rootpath);
    }

    public void fillMasterTimeTable(List<TrTimeTableDisplay> myList, HashMap map) {
        String rootpath = System.getProperty("user.dir") + "/reports/master.jasper";
        fillReport(myList, map, rootpath);
    }

    private void fillReport(List<TrTimeTableDisplay> myList, HashMap map, String path) {
        try {
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(myList);

            JasperPrint printFileNme = JasperFillManager.fillReport(path, map, beanColDataSource);
            JasperViewer.viewReport(printFileNme, false);
        } catch (JRException ex) {
            Logger.getLogger(ReportsFilling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JasperPrint fillReportForExporting(List<TrTimeTableDisplay> myList, HashMap map, String path) {
        try {
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(myList);
            return JasperFillManager.fillReport(path, map, beanColDataSource);
        } catch (JRException ex) {
            Logger.getLogger(ReportsFilling.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void exportRtfTrOrKlass(List<TrTimeTableDisplay> myList, HashMap map, String REPORT_DIRECTORY) {
        try {
            String path = System.getProperty("user.dir") + "/reports/KlassTimeTable.jasper";
            JasperPrint jasperPrint = this.fillReportForExporting(myList, map, path);
            JRRtfExporter rtfExporter = new JRRtfExporter();
            rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, REPORT_DIRECTORY + map.get("ReportTitle") + ".rtf");
            System.out.println("Exporting report...");
            rtfExporter.exportReport();
            System.out.println("Done!");
        } catch (JRException ex) {
            Logger.getLogger(ReportsFilling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportRtfMaster(List<TrTimeTableDisplay> myList, HashMap map) {
        try {
            String path = System.getProperty("user.dir") + "/reports/master.jasper";
            JasperPrint jasperPrint = this.fillReportForExporting(myList, map, path);
            String REPORT_DIRECTORY = System.getProperty("user.dir") + "/TIMETABLES/";
            System.out.println(REPORT_DIRECTORY);
            File file = new File(REPORT_DIRECTORY);
            if (!file.exists()) {
                file.mkdir();
            }
            REPORT_DIRECTORY += "MASTER/";
            file = new File(REPORT_DIRECTORY);
            if (!file.exists()) {
                file.mkdir();
            }
            JRRtfExporter rtfExporter = new JRRtfExporter();
            rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, REPORT_DIRECTORY + map.get("ReportTitle") + ".rtf");
            System.out.println("Exporting report...");
            rtfExporter.exportReport();
            System.out.println("Done!");
        } catch (JRException ex) {
            Logger.getLogger(ReportsFilling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportRtfTr(List<TrTimeTableDisplay> myList, HashMap map) {
        String REPORT_DIRECTORY = System.getProperty("user.dir") + "/TIMETABLES/";
        File file = new File(REPORT_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        REPORT_DIRECTORY += "TEACHERS/";
        file = new File(REPORT_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        this.exportRtfTrOrKlass(myList, map, REPORT_DIRECTORY);
    }

    public void exportRtfKlass(List<TrTimeTableDisplay> myList, HashMap map) {
        String REPORT_DIRECTORY = System.getProperty("user.dir") + "/TIMETABLES/";
        System.out.println(REPORT_DIRECTORY);
        File file = new File(REPORT_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        REPORT_DIRECTORY += "CLASSES/";
        file = new File(REPORT_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        this.exportRtfTrOrKlass(myList, map, REPORT_DIRECTORY);
    }

    public void rtfExport(String tr, JasperPrint jasperPrint) {
        try {
            String REPORT_DIRECTORY = "src\\virtualschoolmanager\\reports\\trtimetable";
            JRRtfExporter rtfExporter = new JRRtfExporter();
            rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, REPORT_DIRECTORY + "/" + tr + ".rtf");
            System.out.println("Exporting report...");
            rtfExporter.exportReport();
            System.out.println("Done!");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
