/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Junta
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private JFXTextField user;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton signUp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Validation.validateTextField(user);
        Validation.validatePasswordField(password);
       
    }

    @FXML
    void makeLogin(ActionEvent event) {
        String username = user.getText();
        String pass = password.getText();
    }

}
