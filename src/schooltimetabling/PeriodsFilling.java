/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Junta
 */
public class PeriodsFilling {
    
    private TeacherSubjectKlass rootTrSubKlas;
   
    private ArrayList<Integer> possiblePeriods=new ArrayList<>();
   
    public PeriodsFilling(TeacherSubjectKlass rootTrSubKlas,ArrayList<Integer> possiblePeriods){
        this.rootTrSubKlas=rootTrSubKlas;
        this.possiblePeriods=possiblePeriods;
    }
    public static void sortPeriodsFilling(ArrayList<PeriodsFilling> arrLis){
        sortPossiblePeriods(arrLis);
        for(int i=0;i<arrLis.size();i++){
            for(int j=0;j<arrLis.size()-1;j++){               
                if(arrLis.get(j).possiblePeriods.size()>arrLis.get(j+1).possiblePeriods.size()){
                    PeriodsFilling temp=arrLis.get(j);
                    arrLis.set(j, arrLis.get(j+1));
                    arrLis.set(j+1, temp);
                    
                }
            }
        }
        arrLis.stream().forEach((arrLi) -> {
            System.out.println(arrLi);
        });
    }
    private static void sortPossiblePeriods(ArrayList<PeriodsFilling> arrLis){
        HashMap<Integer, Integer> occuranceMap = logOccurance(12,arrLis);
        arrLis.stream().forEach((arrLi) -> {
            ArrayList<Integer> pPds = arrLi.getPossiblePeriods();
            for(int i=0;i<pPds.size();i++){
                for(int j=0;j<pPds.size()-1;j++){
                    if(occuranceMap.get(pPds.get(j))>occuranceMap.get(pPds.get(j+1))){
                        int temp=pPds.get(j);
                        pPds.set(j,pPds.get(j+1));
                        pPds.set(j+1,temp);
                    }
                }
            }
        });
    }
    private static  HashMap<Integer,Integer> logOccurance(int numberOfPeriods, ArrayList<PeriodsFilling> arrLis){
        HashMap<Integer,Integer> occuranceMap= new HashMap< > ();
        for(int j=1;j <= numberOfPeriods;j++){
            occuranceMap.put(j,0);
        }
        arrLis.stream().forEach((PeriodsFilling pf) -> {
            pf.possiblePeriods.stream().forEach((Integer period) -> {
                int num =occuranceMap.get(period);
                occuranceMap.put(period, ++num);
            });
        });
        return occuranceMap;
    }
    @Override
    public String toString(){
        return this.rootTrSubKlas.getSubject()+" "+this.possiblePeriods;
    }
    
    /**
     * @return the possiblePeriods
     */
    public ArrayList<Integer> getPossiblePeriods() {
        return possiblePeriods;
    }
    /**
     * @param possiblePeriods the possiblePeriods to set
     */
    public void setPossiblePeriods(ArrayList<Integer> possiblePeriods) {
        this.possiblePeriods = possiblePeriods;
    }

    /**
     * @return the rootTrSubKlas
     */
    public TeacherSubjectKlass getRootTrSubKlas() {
        return rootTrSubKlas;
    }

    /**
     * @param rootTrSubKlas the rootTrSubKlas to set
     */
    public void setRootTrSubKlas(TeacherSubjectKlass rootTrSubKlas) {
        this.rootTrSubKlas = rootTrSubKlas;
    }
}
