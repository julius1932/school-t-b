/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

/**
 *
 * @author Junta
 */
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty(value = "icon")
public class TimeValidator extends ValidatorBase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void eval() {
        if (srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        try {
            String txt = textField.getText();
            String[] split = txt.split(":");
            if(split.length!=2){
                hasErrors.set(true);
                return;
            }
            int parseInt = Integer.parseInt(split[0]);
            int parseInt1 = Integer.parseInt(split[1]);
            
            if(parseInt>23||split[0].length()!=2){
                hasErrors.set(true);
                return;
            }
            if(parseInt1>59||split[1].length()!=2){
                hasErrors.set(true);
                return;
            }         
              hasErrors.set(false);
        } catch (Exception e) {
            hasErrors.set(true);
        }
    }
}
