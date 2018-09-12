package schooltimetabling;

import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

/**
 * Created by Junta on 2/19/2017.
 */
public class HibernateCentre {

    private static SessionFactory sessionFactory;

    static {
        System.out.println("oooooooooooooooooooooooooosssssssssssssssssssssssoooooooooooooooooooooooooooooooo");                  
        initSessionFactory();
    }
    public static void initSessionFactory(){
        if( sessionFactory!=null && sessionFactory.isOpen()){
            sessionFactory.close();
        }
        Configuration configure = new Configuration().configure("schooltimetabling/hibernate.cfg.xml");
        String db = School.getTheSchl();
        String ip = new DataBaseConfig().getDbIp();
         System.out.println(db+"oooooooooooooooooooooooooo "+ip);  
        if (db != null && !db.isEmpty() && ip != null && !ip.isEmpty()) {
            String url = "jdbc:mysql://"+ip+":3306/"+db.trim(); 
            System.out.println(url);
            configure.setProperty("hibernate.connection.url", url);                      
        }
        sessionFactory = configure.buildSessionFactory();
    }
    public static void killSessionFactory(){
         sessionFactory.close();
    }

    public static Session getHibernateSession() {
        // factory = new Configuration().configure().buildSessionFactory();
        return sessionFactory.openSession();
    }

    public static boolean saveDataToDb(List<Object> objects) {
        boolean saved = true;
        try (Session session = HibernateCentre.getHibernateSession()) {
            session.beginTransaction();
            objects.stream().forEach(session::saveOrUpdate);
            session.getTransaction().commit();
            session.close();
//            DialogManager.showMassageDialoge("Data Saved");
            System.out.println("Data Saved");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
//            DialogManager.showErrorMassageDialoge("Failed to save. " + e.getMessage());
            saved = false;
        } finally {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<EXIt Save To Db >>>>>>>>>>>>>>>>>>>>>>>>>>>");
            return saved;
        }
    }

    public static List pullDataFromDb(String query) {
        try (Session session = HibernateCentre.getHibernateSession()) {
             return session.createQuery(query).list();
        }
    }

    public static boolean updateDataToDb(Object object) {
        boolean saved = true;

        try (Session session = HibernateCentre.getHibernateSession();) {
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            saved = false;
        } finally {
            return saved;
        }
    }
    public static void main(String [] args){
        Subject sub =new Subject("eng", "English");
        List subs = new ArrayList<>();
        subs.add(sub);
        HibernateCentre.saveDataToDb(subs);
    }

}
