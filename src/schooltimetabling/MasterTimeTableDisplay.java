/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

/**
 *
 * @author cherutombo
 */
public class MasterTimeTableDisplay extends RecursiveTreeObject<MasterTimeTableDisplay> {
    private static int  type;

    /**
     * @return the type
     */
    public static int getType() {
        return type;
    }

    /**
     * @param aType the type to set
     */
    public static void setType(int aType) {
        type = aType;
    }
    private Slot slot0;
    private Slot slot1;
    private Slot slot2;
    private Slot slot3;
    private Slot slot4;
    private Slot slot5;
    private Slot slot6;
    private Slot slot7;
    private Slot slot8;
    private Slot slot9;
    private Slot slot10;
    private Slot slot11;
    private Slot slot12;
    private Slot slot13;
    private Slot slot14;
    private Slot slot15;
    private Slot slot16;
    private Slot slot17;
    private Slot brkSlot;
    private Slot LanchSlot;

    public void setSlots(Slot slot) {
        this.fitSotToPosition(slot);
        Slot curr = slot;
        while (curr.hasPrev()) {
            curr = curr.getPrev();
            this.fitSotToPosition(curr);
        }
        curr = slot;
        while (curr.hasNext()) {
            curr = curr.getNext();
            this.fitSotToPosition(curr);
        }
    }
    public Slot getSlot(int colmn){
       
        switch (colmn) {
            case 0:
               return  this.getSlot0();
            case 1:
                return this.getSlot1();               
            case 2:
                return this.getSlot2();
            case 3:
                return this.getSlot3();  
            case 4:
                return this.getSlot4();  
            case 5:
                return this.getSlot5(); 
            case 6:
                return this.getSlot6();  
            case 7:
                return this.getSlot7( );  
            case 8:
                return this.getSlot8();
            case 9:
               return this.getSlot9();   
            case 10:
                return this.getSlot10();      
            case 11:
                return this.getSlot11();  
            case 12:
               return this.getSlot12();
            case 13:
                return this.getSlot13();              
            case 14:
                return this.getSlot14();  
            case 15:
               return this.getSlot16();
                
            case 17:
                return this.getSlot17();
                
            default:
               return null;
        } 
    }
    public void fitSotToPosition(Slot slot) {
        int priod = slot.getPeriodi();
        switch (priod) {
            case 0:
                this.setSlot0(slot);
                break;
            case 1:
                this.setSlot1(slot);
                break;
            case 2:
                this.setSlot2(slot);
                break;
            case 3:
                this.setSlot3(slot);
                break;
            case 4:
                this.setSlot4(slot);
                break;
            case 5:
                this.setSlot5(slot);
                break;
            case 6:
                this.setSlot6(slot);
                break;
            case 7:
                this.setSlot7(slot);
                break;
            case 8:
                this.setSlot8(slot);
                break;
            case 9:
                this.setSlot9(slot);
                break;
            case 10:
                this.setSlot10(slot);
                break;
            case 11:
                this.setSlot11(slot);
                break;
            case 12:
                this.setSlot12(slot);
                break;
            case 13:
                this.setSlot13(slot);
                break;
            case 14:
                this.setSlot14(slot);
                break;
            case 15:
                this.setSlot16(slot);
                break;
            case 17:
                this.setSlot17(slot);
                break;
            default:
                break;
        }
    }

    /**
     * @return the slot1
     */
    public Slot getSlot1() {
        return slot1;
    }

    /**
     * @param slot1 the slot1 to set
     */
    public void setSlot1(Slot slot1) {
        this.slot1 = slot1;
    }

    /**
     * @return the slot2
     */
    public Slot getSlot2() {
        return slot2;
    }

    /**
     * @param slot2 the slot2 to set
     */
    public void setSlot2(Slot slot2) {
        this.slot2 = slot2;
    }

    /**
     * @return the slot3
     */
    public Slot getSlot3() {
        return slot3;
    }

    /**
     * @param slot3 the slot3 to set
     */
    public void setSlot3(Slot slot3) {
        this.slot3 = slot3;
    }

    /**
     * @return the slot4
     */
    public Slot getSlot4() {
        return slot4;
    }

    /**
     * @param slot4 the slot4 to set
     */
    public void setSlot4(Slot slot4) {
        this.slot4 = slot4;
    }

    /**
     * @return the slot5
     */
    public Slot getSlot5() {
        return slot5;
    }

    /**
     * @param slot5 the slot5 to set
     */
    public void setSlot5(Slot slot5) {
        this.slot5 = slot5;
    }

    /**
     * @return the slot6
     */
    public Slot getSlot6() {
        return slot6;
    }

    /**
     * @param slot6 the slot6 to set
     */
    public void setSlot6(Slot slot6) {
        this.slot6 = slot6;
    }

    /**
     * @return the slot7
     */
    public Slot getSlot7() {
        return slot7;
    }

    /**
     * @param slot7 the slot7 to set
     */
    public void setSlot7(Slot slot7) {
        this.slot7 = slot7;
    }

    /**
     * @return the slot8
     */
    public Slot getSlot8() {
        return slot8;
    }

    /**
     * @param slot8 the slot8 to set
     */
    public void setSlot8(Slot slot8) {
        this.slot8 = slot8;
    }

    /**
     * @return the slot9
     */
    public Slot getSlot9() {
        return slot9;
    }

    /**
     * @param slot9 the slot9 to set
     */
    public void setSlot9(Slot slot9) {
        this.slot9 = slot9;
    }

    /**
     * @return the slot10
     */
    public Slot getSlot10() {
        return slot10;
    }

    /**
     * @param slot10 the slot10 to set
     */
    public void setSlot10(Slot slot10) {
        this.slot10 = slot10;
    }

    /**
     * @return the slot11
     */
    public Slot getSlot11() {
        return slot11;
    }

    /**
     * @param slot11 the slot11 to set
     */
    public void setSlot11(Slot slot11) {
        this.slot11 = slot11;
    }

    /**
     * @return the slot12
     */
    public Slot getSlot12() {
        return slot12;
    }

    /**
     * @param slot12 the slot12 to set
     */
    public void setSlot12(Slot slot12) {
        this.slot12 = slot12;
    }

    /**
     * @return the slot13
     */
    public Slot getSlot13() {
        return slot13;
    }

    /**
     * @param slot13 the slot13 to set
     */
    public void setSlot13(Slot slot13) {
        this.slot13 = slot13;
    }

    /**
     * @return the slot14
     */
    public Slot getSlot14() {
        return slot14;
    }

    /**
     * @param slot14 the slot14 to set
     */
    public void setSlot14(Slot slot14) {
        this.slot14 = slot14;
    }

    /**
     * @return the slot15
     */
    public Slot getSlot15() {
        return slot15;
    }

    /**
     * @param slot15 the slot15 to set
     */
    public void setSlot15(Slot slot15) {
        this.slot15 = slot15;
    }

    /**
     * @return the slot16
     */
    public Slot getSlot16() {
        return slot16;
    }

    /**
     * @param slot16 the slot16 to set
     */
    public void setSlot16(Slot slot16) {
        this.slot16 = slot16;
    }

    /**
     * @return the slot17
     */
    public Slot getSlot17() {
        return slot17;
    }

    /**
     * @param slot17 the slot17 to set
     */
    public void setSlot17(Slot slot17) {
        this.slot17 = slot17;
    }

    /**
     * @return the slot0
     */
    public Slot getSlot0() {
        return slot0;
    }

    /**
     * @param slot0 the slot0 to set
     */
    public void setSlot0(Slot slot0) {
        this.slot0 = slot0;
    }

    /**
     * @return the brkSlot
     */
    public Slot getBrkSlot() {
        return brkSlot;
    }

    /**
     * @param brkSlot the brkSlot to set
     */
    public void setBrkSlot(Slot brkSlot) {
        this.brkSlot = brkSlot;
    }

    /**
     * @return the LanchSlot
     */
    public Slot getLanchSlot() {
        return LanchSlot;
    }

    /**
     * @param LanchSlot the LanchSlot to set
     */
    public void setLanchSlot(Slot LanchSlot) {
        this.LanchSlot = LanchSlot;
    }

}
