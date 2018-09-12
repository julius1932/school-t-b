/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Junta
 */
public class DialogManager implements Initializable {

    private static boolean delete;

    @FXML

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void closeDialog(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public boolean showConformationDialog(String msg) {
        try {
            Stage newStage = new Stage();
            AnchorPane rty = FXMLLoader.load(getClass().getResource("FXML/deleteDaliagW.fxml"));
            Label label = new Label(msg);
            label.wrapTextProperty().set(true);
            label.relocate(66, 73);
            rty.getChildren().add(label);//
            DialogManager.setDelete(false);
            Scene dialogScene = new Scene(rty, 448, 145);
            newStage.setScene(dialogScene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setTitle("Delete Selected Data");
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return DialogManager.isDelete();
    }

    public static boolean showConformationDialoge(String msg) {
        Alert alert = new Alert(AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            return true;
        }
        return false;
    }

    public static void showMassageDialoge(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    public static void showErrorMassageDialoge(String msg) {
       Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    public void showMassageDialog(String msg) {
        try {
            Stage newStage = new Stage();
            AnchorPane rty = FXMLLoader.load(getClass().getResource("FXML/messageDaliag.fxml"));
            Label label = new Label(msg);
            label.wrapTextProperty().set(true);
            label.relocate(66, 73);
            rty.getChildren().add(label);//
            Scene dialogScene = new Scene(rty, 448, 145);
            newStage.setScene(dialogScene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setTitle("Massage");
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void showErrorDialog(String msg) {
        try {
            Stage newStage = new Stage();
            AnchorPane rty = FXMLLoader.load(getClass().getResource("FXML/errorDaliag.fxml"));
            Label label = new Label(msg);
            label.wrapTextProperty().set(true);
            label.relocate(66, 73);
            rty.getChildren().add(label);//
            Scene dialogScene = new Scene(rty, 448, 145);
            newStage.setScene(dialogScene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setTitle("Massage");
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void okButtonAction(ActionEvent event) {
        this.closeDialog(event);
    }

    @FXML
    private void yesButtonAction(ActionEvent event) {
        DialogManager.setDelete(true);
        this.closeDialog(event);
    }

    @FXML
    private void noButtonAction(ActionEvent event) {
        DialogManager.setDelete(false);
        this.closeDialog(event);
    }

    /**
     * @return the delete
     */
    public static boolean isDelete() {
        return delete;
    }

    /**
     * @param delete the delete to set
     */
    public static void setDelete(boolean delete) {
        DialogManager.delete = delete;
        System.out.println("uuuuuuuuu" + isDelete());
    }
    public static void showDialog( StackPane stackPane,String title,String msg){
        JFXDialogLayout content=new JFXDialogLayout();
        content.setHeading(new Text(title.toUpperCase()));
        content.setBody(new Text(msg+ "   "
                + "         "
                + "          "
                + "            "));
        JFXDialog dialog=new JFXDialog( stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton bb= new JFXButton("Ok");
       
        bb.setOnAction(new EventHandler<ActionEvent> (){
            @Override
            public void handle(ActionEvent event) {
               dialog.close();
            }
        });
        content.setActions(bb);
        dialog.show();
    }
    public static void showDialog( StackPane stackPane,String msg){
        JFXDialogLayout content=new JFXDialogLayout();
        content.setHeading(new Text("Massege"));
        content.setBody(new Text(msg+ "   "
                + "         "
                + "          "
                + "            "));
        JFXDialog dialog=new JFXDialog( stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton bb= new JFXButton("Ok");
       
        bb.setOnAction(new EventHandler<ActionEvent> (){
            @Override
            public void handle(ActionEvent event) {
               dialog.close();
            }
        });
        content.setActions(bb);
        dialog.show();
    }
    public static void showDialog(String msg){
        JFXDialogLayout content=new JFXDialogLayout();
        content.setHeading(new Text("Massege"));
        content.setBody(new Text(msg+ "   "
                + "         "
                + "          "
                + "            "));
        JFXDialog dialog=new JFXDialog( MainPageController.stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton bb= new JFXButton("Ok");
       
        bb.setOnAction(new EventHandler<ActionEvent> (){
            @Override
            public void handle(ActionEvent event) {
               dialog.close();
            }
        });
        content.setActions(bb);
        dialog.show();
    }
}
