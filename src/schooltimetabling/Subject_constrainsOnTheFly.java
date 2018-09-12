/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.util.Collections;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
public class Subject_constrainsOnTheFly extends SubjectConstrainEntity{	
  
    private int     numLessonLoged;
    private boolean greatestLoged;
     
    public Subject_constrainsOnTheFly( ){  
        this.greatestLoged=false;
    }
   
    public Subject_constrainsOnTheFly( SubjectConstrainEntity subCon,List<Integer> preferedDuration,int numLessonLoged){  
        super(subCon.getSubject(), subCon.getKlass() ,subCon.getNumPeriods());
        this.greatestLoged=false;
        this.setPreferedDuration(preferedDuration);
        this.numLessonLoged=numLessonLoged;
        if(this.getPreferedDuration()!=null){
             Collections.sort(this.preferedDuration);
        }
    }

    public int getDifference(){
        int diff = this.numPeriods-this.numLessonLoged;
        if(diff>=0){
            return diff;
        }
        return 0;
    }
    /**
     * @return the numLessonLoged
     */
    public int getNumLessonLoged() {
        return numLessonLoged;
    }

    /**
     * @param numLessonLoged the numLessonLoged to set
     */
    public void setNumLessonLoged(int numLessonLoged) {
        this.numLessonLoged = numLessonLoged;
    }
    /**
     * @param numLesson the numLesson to add
     */
    public void addToWeeklyNumLessonLoged(int numLesson) {
        this.numLessonLoged += numLesson;
    }
    /**
     * @param numLesson the numLesson to subtract
     */
    public void subtractFromWeeklyNumLessonLoged(int numLesson) {
         this.numLessonLoged -= numLesson;
    }

    /**
     * @return the greatestLoged
     */
    public boolean isGreatestLoged() {
        return greatestLoged;
    }
    
    /**
     * @param greatestLoged the greatestLoged to set
     */
    public void setGreatestLoged(boolean greatestLoged) {
        if(this.getPreferedDuration().size()>1){
           this.greatestLoged = greatestLoged; 
        }
        
    }
    public boolean hasBlockOf4(){
       return this.getPreferedDuration().contains(4);
    }
    public TeacherSubjectKlass pullSubKlasTr(){
        try (Session session = HibernateCentre.getHibernateSession();) {
            return (TeacherSubjectKlass) session.createQuery("from TeacherSubjectKlass  where klass=" + this.getKlass().getId() + " and subject ='" + this.getSubject().getCode() + "'").uniqueResult();
        }          
    }
    
   public boolean isGreatest(int n){
       if(this.getPreferedDuration().size()<=1){
          return false; 
       } 
       Collections.sort(this.getPreferedDuration());
       return (this.getPreferedDuration().get(this.getPreferedDuration().size()-1))==n; 
    }
    @Override
   public String toString(){
       return this.getKlass().toString()+" : "+ this.getSubject().toString()+" : "+this.getNumPeriods();
   }
}
