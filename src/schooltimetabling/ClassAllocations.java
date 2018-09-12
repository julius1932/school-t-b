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
public class ClassAllocations {
    private String subject;
    private String teacher;
    private String klass;
    
    public ClassAllocations(TeacherSubjectKlass trsb){
        this.subject=trsb.getSubject().toString();
        this.klass=trsb.getKlass().toString();
        this.teacher=trsb.getTeacher().toString();
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the teacher
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * @param teacher the teacher to set
     */
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    /**
     * @return the klass
     */
    public String getKlass() {
        return klass;
    }

    /**
     * @param klass the klass to set
     */
    public void setKlass(String klass) {
        this.klass = klass;
    }
}
