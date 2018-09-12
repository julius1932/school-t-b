/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

/**
 *
 * @author cherutombo
 */
public class TrTimeTableDisplay {
   private String sltAR;
    private String slt0;
    private String slt1;
    private String slt2;
    private String slt3;
    private String slt4;
    private String slt5;

    private String slt6;
    private String slt7;
    private String slt8;
    private String slt9;
    private String slt10;
    private String slt11;
    private String slt12;
    private String slt13;
    private String slt14;
    private String slt15;
    private String slt16;
    private String slt17;

    private String brkSlt;
    private String lnchSlt;

    public TrTimeTableDisplay() {

    }
    public void setSlt(int priod ,String slt){
        switch (priod) {
            case 0:
                this.setSlt0(slt);
                break;
            case 1:
                this.setSlt1(slt);
                break;
            case 2:
                this.setSlt2(slt);
                break;
            case 3:
                this.setSlt3(slt);
                break;
            case 4:
                this.setSlt4(slt);
                break;
            case 5:
                this.setSlt5(slt);
                break;
            case 6:
                this.setSlt6(slt);
                break;
            case 7:
                this.setSlt7(slt);
                break;
            case 8:
                this.setSlt8(slt);
                break;
            case 9:
                this.setSlt9(slt);
                break;
            case 10:
                this.setSlt10(slt);
                break;
            case 11:
                this.setSlt11(slt);
                break;
            case 12:
                this.setSlt12(slt);
                break;
            case 13:
                this.setSlt13(slt);
                break;
            case 14:
                this.setSlt14(slt);
                break;
            case 15:
                this.setSlt16(slt);
                break;
            case 17:
                this.setSlt17(slt);
                break;
            default:
                break;
        }
    }
    /**
     * @return the slt0
     */
    public String getSlt0() {
        return slt0;
    }

    /**
     * @param slt0 the slt0 to set
     */
    public void setSlt0(String slt0) {
        this.slt0 = slt0;
        this.setBrkSlt("");
        this.setLnchSlt("");
    }

    /**
     * @return the slt1
     */
    public String getSlt1() {
        return slt1;
    }

    /**
     * @param slt1 the slt1 to set
     */
    public void setSlt1(String slt1) {
        this.slt1 = slt1;
    }

    /**
     * @return the slt2
     */
    public String getSlt2() {
        return slt2;
    }

    /**
     * @param slt2 the slt2 to set
     */
    public void setSlt2(String slt2) {
        this.slt2 = slt2;
    }

    /**
     * @return the slt3
     */
    public String getSlt3() {
        return slt3;
    }

    /**
     * @param slt3 the slt3 to set
     */
    public void setSlt3(String slt3) {
        this.slt3 = slt3;
    }

    /**
     * @return the slt4
     */
    public String getSlt4() {
        return slt4;
    }

    /**
     * @param slt4 the slt4 to set
     */
    public void setSlt4(String slt4) {
        this.slt4 = slt4;
    }

    /**
     * @return the slt5
     */
    public String getSlt5() {
        return slt5;
    }

    /**
     * @param slt5 the slt5 to set
     */
    public void setSlt5(String slt5) {
        this.slt5 = slt5;
    }

    /**
     * @return the slt6
     */
    public String getSlt6() {
        return slt6;
    }

    /**
     * @param slt6 the slt6 to set
     */
    public void setSlt6(String slt6) {
        this.slt6 = slt6;
    }

    /**
     * @return the slt7
     */
    public String getSlt7() {
        return slt7;
    }

    /**
     * @param slt7 the slt7 to set
     */
    public void setSlt7(String slt7) {
        this.slt7 = slt7;
    }

    /**
     * @return the slt8
     */
    public String getSlt8() {
        return slt8;
    }

    /**
     * @param slt8 the slt8 to set
     */
    public void setSlt8(String slt8) {
        this.slt8 = slt8;
    }

    /**
     * @return the slt9
     */
    public String getSlt9() {
        return slt9;
    }

    /**
     * @param slt9 the slt9 to set
     */
    public void setSlt9(String slt9) {
        this.slt9 = slt9;
    }

    /**
     * @return the slt10
     */
    public String getSlt10() {
        return slt10;
    }

    /**
     * @param slt10 the slt10 to set
     */
    public void setSlt10(String slt10) {
        this.slt10 = slt10;
    }

    /**
     * @return the slt11
     */
    public String getSlt11() {
        return slt11;
    }

    /**
     * @param slt11 the slt11 to set
     */
    public void setSlt11(String slt11) {
        this.slt11 = slt11;
    }

    /**
     * @return the slt12
     */
    public String getSlt12() {
        return slt12;
    }

    /**
     * @param slt12 the slt12 to set
     */
    public void setSlt12(String slt12) {
        this.slt12 = slt12;
    }

    /**
     * @return the slt13
     */
    public String getSlt13() {
        return slt13;
    }

    /**
     * @param slt13 the slt13 to set
     */
    public void setSlt13(String slt13) {
        this.slt13 = slt13;
    }

    /**
     * @return the slt14
     */
    public String getSlt14() {
        return slt14;
    }

    /**
     * @param slt14 the slt14 to set
     */
    public void setSlt14(String slt14) {
        this.slt14 = slt14;
    }

    /**
     * @return the slt15
     */
    public String getSlt15() {
        return slt15;
    }

    /**
     * @param slt15 the slt15 to set
     */
    public void setSlt15(String slt15) {
        this.slt15 = slt15;
    }

    /**
     * @return the slt16
     */
    public String getSlt16() {
        return slt16;
    }

    /**
     * @param slt16 the slt16 to set
     */
    public void setSlt16(String slt16) {
        this.slt16 = slt16;
    }

    /**
     * @return the slt17
     */
    public String getSlt17() {
        return slt17;
    }

    /**
     * @param slt17 the slt17 to set
     */
    public void setSlt17(String slt17) {
        this.slt17 = slt17;
    }

    /**
     * @return the brkSlt
     */
    public String getBrkSlt() {
        return brkSlt;
    }

    /**
     * @param brkSlt the brkSlt to set
     */
    public void setBrkSlt(String brkSlt) {
        this.brkSlt = brkSlt;
    }

    /**
     * @return the lnchSlt
     */
    public String getLnchSlt() {
        return lnchSlt;
    }

    /**
     * @param lnchSlt the lnchSlt to set
     */
    public void setLnchSlt(String lnchSlt) {
        this.lnchSlt = lnchSlt;
    }

    /**
     * @return the sltAR
     */
    public String getSltAR() {
        return sltAR;
    }

    /**
     * @param sltAR the sltAR to set
     */
    public void setSltAR(String sltAR) {
        this.sltAR = sltAR;
    }
}
