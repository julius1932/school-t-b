/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cherutombo
 */
public class DataBaseConfig {
    private String dbIp="";

    /**
     * @return the dbIp
     */
    public String getDbIp() {
        this.setDbIp();
        return dbIp;
    }

    /**
     * @param dbIp the dbIp to set
     */
    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }
    private void setDbIp() {
      String pth=  System.getProperty("user.dir") + "/reports/dDconfig";
        File file = new File(pth);
        if(file.exists()){
            try {
                Scanner scan = new Scanner(file);
                if(scan.hasNext()){
                    String  ip= scan.next().trim();
                    if(!ip.isEmpty()){
                        setDbIp(ip);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DataBaseConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
          try {
              file.getParentFile().mkdirs();
              file.createNewFile();
          } catch (IOException ex) {
              Logger.getLogger(DataBaseConfig.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
        
    }
    public static void main(String [] args){
       new DataBaseConfig ().getDbIp();
    }
}
