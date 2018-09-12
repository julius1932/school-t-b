/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author cherutombo
 */
public class NewYearController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void deleteOverLapClasses(ActionEvent event) {
        if (!DialogManager.showConformationDialoge("Do you want to delete Invalid classes ?")) {
            return;
        }
        boolean has7 = false;
        try (Session session = HibernateCentre.getHibernateSession()) {
            List<Klass> klasses = session.createQuery("from Klass where level = 5 or level = 7 ORDER BY level").list();
            for (Klass kls : klasses) {
                if (kls.getLevel() == 7) {
                    has7 = true;
                    break;
                }
            }
            if (has7) {
                System.out.println("uttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
                for (Klass kls : klasses) {
                    session.getTransaction().begin();
                    Query query = session.createNativeQuery("delete from TeacherSubjectKlass  where klass =:klss");
                    Query query1 = session.createNativeQuery("delete from Klass where id =:kla");
                    query.setParameter("klss", kls.getId());
                    query1.setParameter("kla", kls.getId());
                    query.executeUpdate();
                    query1.executeUpdate();
                    session.getTransaction().commit();
                }
            }
        }catch(Exception ee){
            DialogManager.showErrorMassageDialoge(ee.getMessage());
        }
    }

    @FXML
    public void promoteLearnersToNextForm(ActionEvent event) {
        if (!DialogManager.showConformationDialoge("Do you want to move leaners to next form?")) {
            return;
        }

        try (Session session = HibernateCentre.getHibernateSession()) {
            List<Klass> klasses = session.createQuery("from Klass ORDER BY level").list();
            if (klasses.isEmpty()) {
                return;
            }
            session.beginTransaction();
            for (int i = klasses.size() - 1; i >= 0; i--) {
                Klass klas = klasses.get(i);
                klas.setLevel(klas.getLevel() + 1);
                session.saveOrUpdate(klas);
            }
            session.getTransaction().commit();
            DialogManager.showMassageDialoge("Data Saved");

        } catch (Exception ex) {
            DialogManager.showErrorMassageDialoge(ex.getMessage());
        }
    }

    @FXML
    public void demoteLearnersToNextForm(ActionEvent event) {
        if (!DialogManager.showConformationDialoge("Do you want to demote leaners ?")) {
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession()) {
            List<Klass> klasses = session.createQuery("from Klass ORDER BY level").list();
            if (klasses.isEmpty()) {
                return;
            }
            session.beginTransaction();
            for (int i = 0; i < klasses.size(); i++) {
                Klass klas = klasses.get(i);
                klas.setLevel(klas.getLevel() - 1);
                session.saveOrUpdate(klas);
            }
            session.getTransaction().commit();
            DialogManager.showMassageDialoge("Data Saved");

        } catch (Exception ex) {
            DialogManager.showErrorMassageDialoge(ex.getMessage());
        }
    }

}
