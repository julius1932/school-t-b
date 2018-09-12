/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.NumberValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Junta
 */
public class Validation {

    public static void validateTextField(JFXTextField user) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("No Input Given");
        user.setValidators(validator);

        user.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    user.validate();
                }
            }
        });
    }

    public static void validateTextFieldNumberOnly(JFXTextField user) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("No Input Given");
        NumberValidator nuValidator = new NumberValidator();
        nuValidator.setMessage(" Input must be a number");
        user.setValidators(validator, nuValidator);

        user.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    user.validate();
                }
            }
        });
    }

    public static void validateTextFieldTime(JFXTextField user) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("No Input Given");
        TimeValidator timValidator = new TimeValidator();
        timValidator.setMessage(" Time not valid");
        user.setValidators(validator, timValidator);

        user.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    user.validate();
                }
            }
        });
    }

    public static void validatePasswordField(JFXPasswordField user) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        user.setValidators(validator);
        validator.setMessage("No Input Given");
        user.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    user.validate();
                }
            }
        });
    }
}
