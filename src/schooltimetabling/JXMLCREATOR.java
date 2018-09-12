/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperCompileManager;

/**
 *
 * @author Junta
 */
public class JXMLCREATOR {

    public static void write(String fileName, String text) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        out.print(text);
        out.close();
    }

    public static void main(String[] args) throws Exception {

        new JXMLCREATOR().compileJXML();
    }

    public static void makeJXMLTempate() {

        LocalTime start = TimeTable.getStartTime();
        int lssn = TimeTable.getLesssonDuration();
        int brk = TimeTable.getBrkDuration();
        int lnc = TimeTable.getLnchDuration();
        int numPrds = TimeTable.getNumPeriods();
        int prdB4brk = TimeTable.getPeriodsB4Brk();
        int prdB4Lcnh = TimeTable.getPeriodsB4Lnch();
        //pageWidth="842" pageHeight="595" orientation="Landscape" columnWidth="802" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20"
        //columnCount=\"4\" columnSpacing=\"15\" 

        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">\n"
                + "\n"
                + "<jasperReport name=\"KlassTimeTable\" pageWidth=\"842\" pageHeight=\"595\" orientation=\"Landscape\" isIgnorePagination=\"true\" columnWidth=\"802\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\">\n"
                + "	<style name=\"Arial_Normal\" isDefault=\"true\" fontName=\"Arial\" fontSize=\"8\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"Helvetica\" pdfEncoding=\"Cp1252\" isPdfEmbedded=\"false\"/>\n"
                + "	<style name=\"Arial_Bold\" isDefault=\"false\" fontName=\"Arial\" fontSize=\"8\" isBold=\"true\" isItalic=\"false\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"Helvetica-Bold\" pdfEncoding=\"Cp1252\" isPdfEmbedded=\"false\"/>\n"
                + "	<style name=\"Arial_Italic\" isDefault=\"false\" fontName=\"Arial\" fontSize=\"8\" isBold=\"false\" isItalic=\"true\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"Helvetica-Oblique\" pdfEncoding=\"Cp1252\" isPdfEmbedded=\"false\"/>\n"
                + "	<style name=\"Comic_Normal\" isDefault=\"false\" fontName=\"Comic Sans MS\" fontSize=\"10\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"COMIC.TTF\" pdfEncoding=\"Identity-H\" isPdfEmbedded=\"true\"/>\n"
                + "	<style name=\"Comic_Bold\" isDefault=\"false\" fontName=\"Comic Sans MS\" fontSize=\"10\" isBold=\"true\" isItalic=\"false\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"COMICBD.TTF\" pdfEncoding=\"Identity-H\" isPdfEmbedded=\"true\"/>\n"
                + "	<parameter name=\"ReportTitle\" class=\"java.lang.String\">\n"
                + "	</parameter>\n"
                + "	<parameter name=\"qry\" class=\"java.lang.String\">\n"
                + "	</parameter>\n"
                + "	<parameter name=\"SummaryImage\" class=\"java.awt.Image\">\n"
                + "	</parameter>\n"
                + "	<queryString>         \n"
                + "              <![CDATA[$P!{qry}]]>         \n"
                + "       </queryString>   \n"
                + "        <field name=\"slt0\" class=\"java.lang.String\"></field>     \n";
        if (MainPageController.inludeAssembly) {
            text += "        <field name=\"sltAR\" class=\"java.lang.String\"></field>     \n";
        }
        for (int prd = 1; prd <= numPrds; prd++) {
            text += "        <field name=\"slt" + prd + "\" class=\"java.lang.String\"></field> \n";
            if (prd == prdB4brk) {
                text += "        <field name=\"brkSlt\" class=\"java.lang.String\"></field> \n";
            }
            if (prd == prdB4Lcnh) {
                text += "        <field name=\"lnchSlt\" class=\"java.lang.String\"></field> \n";
            }
        }
        text += "<title>\n"
                + "            <band height=\"50\">\n"
                + "	        <textField isBlankWhenNull=\"true\">\n"
                + "		    <reportElement x=\"0\" y=\"0\" width=\"500\" height=\"15\" style=\"Arial_Italic\"/>\n"
                + "		    <textElement textAlignment=\"Left\">\n"
                + "			<font size=\"12\"/>\n"
                + "		    </textElement>\n"
                + "                    <textFieldExpression  class=\"java.lang.String\"><![CDATA[$P{ReportTitle}]]></textFieldExpression>\n"
                + "                </textField>		\n"
                + "            </band>\n"
                + "	</title>\n"
                + "	<pageHeader>\n"
                + "		<band height=\"45\">\n"
                + "			\n"
                + "			<staticText>\n"
                + "				<reportElement x=\"0\" y=\"0\" width=\"555\" height=\"25\"/>\n"
                + "				<textElement textAlignment=\"Center\">\n"
                + "					<font size=\"18\"/>\n"
                + "				</textElement>\n"
                + "				<text><![CDATA[TIME TABLE]]></text>\n"
                + "			</staticText>\n"
                + "			\n"
                + "		</band>\n"
                + "	</pageHeader>\n"
                + "	<columnHeader>\n"
                + "		<band height=\"30\">                     \n"
                + "			<staticText>\n"
                + "			    <reportElement mode=\"Opaque\" x=\"0\" y=\"0\" width=\"50\" height=\"30\" forecolor=\"#ffffff\" backcolor=\"#333333\" style=\"Arial_Bold\"/>\n"
                + "			    <textElement textAlignment=\"Left\"/>\n"
                + "			    <text><![CDATA[\n"
                + "\n"
                + "Day]]></text>\n"
                + "			</staticText> \n";
        int x = 50;
        if (MainPageController.inludeAssembly) {
            text += "			<staticText>\n"
                    + "		            <reportElement mode=\"Opaque\" x=\"" + x + "\" y=\"0\" width=\"50\" height=\"30\" forecolor=\"#333333\" backcolor=\"#c0c0c0\" style=\"Arial_Bold\"/>\n"
                    + "			    <textElement textAlignment=\"Center\"/>\n"
                    + "                           <text><![CDATA[A/REG \n 07:20 \n 07:40]]></text>\n"
                    + "			</staticText> \n";
            x += 50;
        }
        for (int prd = 1; prd <= numPrds; prd++) {
            text += "			<staticText>\n"
                    + "		            <reportElement mode=\"Opaque\" x=\"" + x + "\" y=\"0\" width=\"50\" height=\"30\" forecolor=\"#333333\" backcolor=\"#c0c0c0\" style=\"Arial_Bold\"/>\n"
                    + "			    <textElement textAlignment=\"Center\"/>\n"
                    + "                            <text><![CDATA[" + prd + "  \n"
                    + start.toString().substring(0, 5) + " \n";
            start = start.plusMinutes(lssn);
            text += start.toString().substring(0, 5) + "]]></text>\n"
                    + "			</staticText> \n";
            x += 50;
            if (prd == prdB4brk) {
                text += "			<staticText>\n"
                        + "				<reportElement mode=\"Opaque\" x=\"" + x + "\" y=\"0\" width=\"50\" height=\"30\" forecolor=\"#333333\" backcolor=\"#c0c0c0\" style=\"Arial_Bold\"/>\n"
                        + "				<textElement textAlignment=\"Center\"/>\n"
                        + "                                <text><![CDATA[BREAK \n"
                        + start.toString().substring(0, 5) + " \n";
                start = start.plusMinutes(brk);
                text += start.toString().substring(0, 5) + "]]></text>\n"
                        + "			</staticText> \n";
                x += 50;
            }
            if (prd == prdB4Lcnh) {
                text += "			<staticText>\n"
                        + "				<reportElement mode=\"Opaque\" x=\"" + x + "\" y=\"0\" width=\"50\" height=\"30\" forecolor=\"#333333\" backcolor=\"#c0c0c0\" style=\"Arial_Bold\"/>\n"
                        + "				<textElement textAlignment=\"Center\"/>\n"
                        + "                                <text><![CDATA[LUNCH \n"
                        + start.toString().substring(0, 5) + " \n";
                start = start.plusMinutes(lnc);
                text += start.toString().substring(0, 5) + "]]></text>\n"
                        + "			</staticText> \n";
                x += 50;
            }
        }
        text += "	</band>\n"
                + "	</columnHeader>\n"
                + "	<detail >\n"
                + "		<band height=\"30\">\n"
                + "			<textField >\n"
                + "				<reportElement x=\"0\" y=\"0\" width=\"50\" height=\"30\"  />\n"
                + "				<textElement/>\n"
                + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{slt0}]]></textFieldExpression>\n"
                + "			</textField> \n";
        x = 50;
        if (MainPageController.inludeAssembly) {
            text += "			<textField >\n"
                    + "				<reportElement  x=\"" + x + "\" y=\"0\" width=\"50\" height=\"30\"  />\n"
                    + "                               <box border=\"Thin\"/>\n"
                    + "				 \n"
                    + "                                <textElement textAlignment=\"Center\"/>\n"
                    + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{sltAR}]]></textFieldExpression>\n"
                    + "			</textField> \n";
            x += 50;
        }
        for (int prd = 1; prd <= numPrds; prd++) {
            text += "			<textField >\n"
                    + "				<reportElement  x=\"" + x + "\" y=\"0\" width=\"50\" height=\"30\"  />\n"
                    + "                               <box border=\"Thin\"/>\n"
                    + "				 \n"
                    + "                                <textElement textAlignment=\"Center\"/>\n"
                    + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{slt" + prd + "}]]></textFieldExpression>\n"
                    + "			</textField> \n";
            x += 50;
            if (prd == prdB4brk) {
                text += "                        <textField >\n"
                        + "				<reportElement x=\"" + x + "\" y=\"0\" width=\"50\" height=\"30\"  mode=\"Opaque\" backcolor=\"lightGray\" style=\"Arial_Bold\" />\n"
                        + "				<box border=\"Thin\"/>\n"
                        + "                                <textElement textAlignment=\"Center\"/>\n"
                        + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{brkSlt}]]></textFieldExpression>\n"
                        + "			</textField>\n";
                x += 50;
            }
            if (prd == prdB4Lcnh) {
                text += "                        <textField>\n"
                        + "				<reportElement x=\"" + x + "\" y=\"0\" width=\"50\" height=\"30\" mode=\"Opaque\" backcolor=\"lightGray\" style=\"Arial_Bold\"/>\n"
                        + "                                 <box border=\"Thin\"/>\n"
                        + "				<textElement textAlignment=\"Center\" />\n"
                        + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{lnchSlt}]]></textFieldExpression>\n"
                        + "			</textField>\n";
                x += 50;
            }
        }
        text += "			<line>\n"
                + "				<reportElement positionType=\"Float\" x=\"50\" y=\"0\" width=\"650\" height=\"1\" forecolor=\"#808080\"/>\n"
                + "				<graphicElement pen=\"Thin\"/>\n"
                + "			</line>\n"
                + "		</band>\n"
                + "	</detail>\n"
                + "	\n"
                + "	<pageFooter>\n"
                + "		\n"
                + "	</pageFooter>\n"
                + "	<summary>\n"
                + "		<band height=\"50\">\n"
                + "			<rectangle>\n"
                + "				<reportElement x=\"0\" y=\"0\" width=\""+x+"\" height=\"35\" backcolor=\"#c0c0c0\"/>\n"
                + "				<graphicElement/>\n"
                + "			</rectangle>\n"
                
                + "			<staticText>\n"
                + "				<reportElement mode=\"Opaque\" x=\"0\" y=\"5\" width=\""+x+"\" height=\"30\" backcolor=\"#c0c0c0\"/>\n"
                + "				<textElement textAlignment=\"Center\">\n"
                + "					<font size=\"12\"/>\n"
                + "				</textElement>\n"
                + "				<text><![CDATA["+School.getTheSchl().toUpperCase()+"@"+LocalDate.now().getYear()+"]]></text>\n"
                + "			</staticText>\n"
                + "		</band>\n"
                + "	</summary>\n"
                + "</jasperReport>";
        String fileName = System.getProperty("user.dir") + "\\reports\\KlassTimeTable.jrxml";

        try {
            JXMLCREATOR.write(fileName, text);
        } catch (IOException ex) {
            Logger.getLogger(JXMLCREATOR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void makeJXMLTempateMaster() {
        LocalTime start = TimeTable.getStartTime();
        int lssn = TimeTable.getLesssonDuration();
        int brk = TimeTable.getBrkDuration();
        int lnc = TimeTable.getLnchDuration();
        int numPrds = TimeTable.getNumPeriods();
        int prdB4brk = TimeTable.getPeriodsB4Brk();
        int prdB4Lcnh = TimeTable.getPeriodsB4Lnch();

        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">\n"
                + "\n"
                + "<jasperReport name=\"master\" columnCount=\"4\" pageWidth=\"800\"  isIgnorePagination=\"true\" columnWidth=\"150\" columnSpacing=\"15\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"30\" bottomMargin=\"30\">\n"
                + "	<style name=\"Arial_Normal\" isDefault=\"true\" fontName=\"Arial\" fontSize=\"8\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"Helvetica\" pdfEncoding=\"Cp1252\" isPdfEmbedded=\"false\"/>\n"
                + "	<style name=\"Arial_Bold\" isDefault=\"false\" fontName=\"Arial\" fontSize=\"8\" isBold=\"true\" isItalic=\"false\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"Helvetica-Bold\" pdfEncoding=\"Cp1252\" isPdfEmbedded=\"false\"/>\n"
                + "	<style name=\"Arial_Italic\" isDefault=\"false\" fontName=\"Arial\" fontSize=\"8\" isBold=\"false\" isItalic=\"true\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"Helvetica-Oblique\" pdfEncoding=\"Cp1252\" isPdfEmbedded=\"false\"/>\n"
                + "	<style name=\"Comic_Normal\" isDefault=\"false\" fontName=\"Comic Sans MS\" fontSize=\"10\" isBold=\"false\" isItalic=\"false\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"COMIC.TTF\" pdfEncoding=\"Identity-H\" isPdfEmbedded=\"true\"/>\n"
                + "	<style name=\"Comic_Bold\" isDefault=\"false\" fontName=\"Comic Sans MS\" fontSize=\"10\" isBold=\"true\" isItalic=\"false\" isUnderline=\"false\" isStrikeThrough=\"false\" pdfFontName=\"COMICBD.TTF\" pdfEncoding=\"Identity-H\" isPdfEmbedded=\"true\"/>\n"
                + "	<parameter name=\"ReportTitle\" class=\"java.lang.String\">\n"
                + "	</parameter>\n"
                + "	<parameter name=\"qry\" class=\"java.lang.String\">\n"
                + "	</parameter>\n"
                + "	<parameter name=\"SummaryImage\" class=\"java.awt.Image\">\n"
                + "	</parameter>\n"
                + "	<queryString>         \n"
                + "              <![CDATA[$P!{qry}]]>         \n"
                + "       </queryString>   \n"
                + "        <field name=\"slt0\" class=\"java.lang.String\"></field>     \n";
        if (MainPageController.inludeAssembly) {
            text += "        <field name=\"sltAR\" class=\"java.lang.String\"></field>     \n";
        }
        for (int prd = 1; prd <= numPrds; prd++) {
            text += "        <field name=\"slt" + prd + "\" class=\"java.lang.String\"></field> \n";
            if (prd == prdB4brk) {
                text += "        <field name=\"brkSlt\" class=\"java.lang.String\"></field> \n";
            }
            if (prd == prdB4Lcnh) {
                text += "        <field name=\"lnchSlt\" class=\"java.lang.String\"></field> \n";
            }
        }
        text += "<title>\n"
                + "            <band height=\"50\">\n"
                + "	        <textField isBlankWhenNull=\"true\">\n"
                + "		    <reportElement x=\"0\" y=\"0\" width=\"100\" height=\"15\" style=\"Arial_Italic\"/>\n"
                + "		    <textElement textAlignment=\"Left\">\n"
                + "			<font size=\"12\"/>\n"
                + "		    </textElement>\n"
                + "                    <textFieldExpression  class=\"java.lang.String\"><![CDATA[$P{ReportTitle}]]></textFieldExpression>\n"
                + "                </textField>		\n"
                + "            </band>\n"
                + "	</title>\n"
                + "	<pageHeader>\n"
                + "		<band height=\"45\">\n"
                + "			\n"
                + "			<staticText>\n"
                + "				<reportElement x=\"0\" y=\"0\" width=\"400\" height=\"25\"/>\n"
                + "				<textElement textAlignment=\"Center\">\n"
                + "					<font size=\"18\"/>\n"
                + "				</textElement>\n"
                + "				<text><![CDATA[MASTER TIME TABLE]]></text>\n"
                + "			</staticText>\n"
                + "			\n"
                + "		</band>\n"
                + "	</pageHeader>\n"
                + "	<columnHeader>\n"
                + "		<band height=\"30\">                     \n"
                + "			<staticText>\n"
                + "			    <reportElement mode=\"Opaque\" x=\"0\" y=\"0\" width=\"30\" height=\"30\" forecolor=\"#ffffff\" backcolor=\"#333333\" style=\"Arial_Bold\"/>\n"
                + "			    <textElement textAlignment=\"Left\"/>\n"
                + "			    <text><![CDATA[\n"
                + "\n"
                + "Klass]]></text>\n"
                + "			</staticText> \n";
        int x = 30;
        if (MainPageController.inludeAssembly) {
            text += "			<staticText>\n"
                    + "		            <reportElement mode=\"Opaque\" x=\"" + x + "\" y=\"0\" width=\"30\" height=\"30\" forecolor=\"#333333\" backcolor=\"#c0c0c0\" style=\"Arial_Bold\"/>\n"
                    + "			    <textElement textAlignment=\"Center\"/>\n"
                    + "                            <text><![CDATA[A/REG\n07:20\n07:40]]></text>\n"
                    + "			</staticText> \n";
            x += 30;
        }
        for (int prd = 1; prd <= numPrds; prd++) {
            text += "			<staticText>\n"
                    + "		            <reportElement mode=\"Opaque\" x=\"" + x + "\" y=\"0\" width=\"30\" height=\"30\" forecolor=\"#333333\" backcolor=\"#c0c0c0\" style=\"Arial_Bold\"/>\n"
                    + "			    <textElement textAlignment=\"Center\"/>\n"
                    + "                            <text><![CDATA[" + prd + "  \n"
                    + start.toString().substring(0, 5) + " \n";
            start = start.plusMinutes(lssn);
            text += start.toString().substring(0, 5) + "]]></text>\n"
                    + "			</staticText> \n";
            x += 30;
            if (prd == prdB4brk) {
                text += "			<staticText>\n"
                        + "				<reportElement mode=\"Opaque\" x=\"" + x + "\" y=\"0\" width=\"30\" height=\"30\" forecolor=\"#333333\" backcolor=\"#c0c0c0\" style=\"Arial_Bold\"/>\n"
                        + "				<textElement textAlignment=\"Center\"/>\n"
                        + "                                <text><![CDATA[BREAK \n"
                        + start.toString().substring(0, 5) + " \n";
                start = start.plusMinutes(brk);

                text += start.toString().substring(0, 5) + "]]></text>\n"
                        + "			</staticText> \n";
                x += 30;
            }
            if (prd == prdB4Lcnh) {
                text += "			<staticText>\n"
                        + "				<reportElement mode=\"Opaque\" x=\"" + x + "\" y=\"0\" width=\"30\" height=\"30\" forecolor=\"#333333\" backcolor=\"#c0c0c0\" style=\"Arial_Bold\"/>\n"
                        + "				<textElement textAlignment=\"Center\"/>\n"
                        + "                                <text><![CDATA[LUNCH \n"
                        + start.toString().substring(0, 5) + " \n";
                start = start.plusMinutes(lnc);
                text += start.toString().substring(0, 5) + "]]></text>\n"
                        + "			</staticText> \n";
                x += 30;
            }
        }
        text += "	</band>\n"
                + "	</columnHeader>\n"
                + "	<detail >\n"
                + "		<band height=\"20\">\n"
                + "			<textField >\n"
                + "				<reportElement x=\"0\" y=\"0\" width=\"30\" height=\"20\"  />\n"
                + "				<textElement/>\n"
                + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{slt0}]]></textFieldExpression>\n"
                + "			</textField> \n";
        x = 30;
        if (MainPageController.inludeAssembly) {
            text += "			<textField >\n"
                    + "				<reportElement  x=\"" + x + "\" y=\"0\" width=\"30\" height=\"20\"  />\n"
                    + "                               <box border=\"Thin\"/>\n"
                    + "				 \n"
                    + "                                <textElement textAlignment=\"Center\"/>\n"
                    + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{sltAR}]]></textFieldExpression>\n"
                    + "			</textField> \n";
            x += 30;
        }
        for (int prd = 1; prd <= numPrds; prd++) {
            text += "			<textField >\n"
                    + "				<reportElement  x=\"" + x + "\" y=\"0\" width=\"30\" height=\"20\"  />\n"
                    + "                               <box border=\"Thin\"/>\n"
                    + "				 \n"
                    + "                                <textElement textAlignment=\"Center\"/>\n"
                    + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{slt" + prd + "}]]></textFieldExpression>\n"
                    + "			</textField> \n";
            x += 30;
            if (prd == prdB4brk) {
                text += "                        <textField >\n"
                        + "				<reportElement x=\"" + x + "\" y=\"0\" width=\"30\" height=\"20\"  mode=\"Opaque\" backcolor=\"lightGray\" style=\"Arial_Bold\" />\n"
                        + "				<box border=\"Thin\"/>\n"
                        + "                                <textElement textAlignment=\"Center\"/>\n"
                        + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{brkSlt}]]></textFieldExpression>\n"
                        + "			</textField>\n";
                x += 30;
            }
            if (prd == prdB4Lcnh) {
                text += "                        <textField>\n"
                        + "				<reportElement x=\"" + x + "\" y=\"0\" width=\"30\" height=\"20\" mode=\"Opaque\" backcolor=\"lightGray\" style=\"Arial_Bold\"/>\n"
                        + "                                 <box border=\"Thin\"/>\n"
                        + "				<textElement textAlignment=\"Center\" />\n"
                        + "				<textFieldExpression class=\"java.lang.String\"><![CDATA[$F{lnchSlt}]]></textFieldExpression>\n"
                        + "			</textField>\n";
                x += 30;
            }
        }
        x -= 30;
        text += "			<line>\n"
                + "				<reportElement positionType=\"Float\" x=\"30\" y=\"0\" width=\"" + x + "\" height=\"1\" forecolor=\"#808080\"/>\n"
                + "				<graphicElement pen=\"Thin\"/>\n"
                + "			</line>\n"
                + "		</band>\n"
                + "	</detail>\n"
                + "	\n"
                + "	<pageFooter>\n"
                + "		\n"
                + "	</pageFooter>\n"
                + "	<summary>\n"
                + "		<band height=\"50\">\n"
                + "			<rectangle>\n"
                + "				<reportElement x=\"0\" y=\"10\" width=\"" + (30 + x) + "\" height=\"35\" backcolor=\"#c0c0c0\"/>\n"
                + "				<graphicElement/>\n"
                + "			</rectangle>\n"
                + "			\n"
                + "			<staticText>\n"
                + "				<reportElement mode=\"Opaque\" x=\"200\" y=\"15\" width=\"200\" height=\"20\" backcolor=\"#c0c0c0\"/>\n"
                + "				<textElement textAlignment=\"Center\">\n"
                + "					<font size=\"12\"/>\n"
                + "				</textElement>\n"
                + "				<text><![CDATA["+School.getTheSchl().toUpperCase()+"@"+LocalDate.now().getYear()+"]]></text>\n"
                + "			</staticText>\n"
                + "		</band>\n"
                + "	</summary>\n"
                + "</jasperReport>";
        String fileName = System.getProperty("user.dir") + "\\reports\\master.jrxml";
        try {
            JXMLCREATOR.write(fileName, text);
        } catch (IOException ex) {
            Logger.getLogger(JXMLCREATOR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void compileJXML() {

        try {
            String sourceFileNme = System.getProperty("user.dir") + "/reports/KlassTimetable.jrxml";
            System.out.println(sourceFileNme);
            JasperCompileManager.compileReportToFile(sourceFileNme);
            sourceFileNme = System.getProperty("user.dir") + "/reports/master.jrxml";
            System.out.println(sourceFileNme);
            JasperCompileManager.compileReportToFile(sourceFileNme);
            sourceFileNme = System.getProperty("user.dir") + "/reports/KlassSubTrs.jrxml";
            //String sourceFileNme = System.getProperty("user.dir") + "/reports/klassAllocations.jrxml";
            System.out.println(sourceFileNme);
            JasperCompileManager.compileReportToFile(sourceFileNme);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
