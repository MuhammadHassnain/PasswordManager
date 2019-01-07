/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordManager.BLL;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import passwordManager.DBL.AccountManager;

/**
 * FXML Controller class
 *
 * @author m-hassnain
 */
public class SettingController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private AccountManager accountManager;
    @FXML
    TextField tfOldPassword, tfNewPassword;
    @FXML
    Button btnChangePassword;

    public void setAccountManager(AccountManager manager) {
        this.accountManager = manager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnChangePassword.setOnAction((ActionEvent t) -> {

            String oldPass = tfOldPassword.getText();
            String newPass = tfNewPassword.getText();
            if ("".equals(newPass)) {
                showALertBoxInformation("Field is empty", null, "New Password Field Can't be emtpy");
                return;
            }
            if ("".equals(oldPass)) {
                showALertBoxInformation("Field is empty", null, "Old Password Field Can't be emtpy");
                return;
            }

            int hasChange = accountManager.changeDatabasePassword(oldPass, newPass);
            if (hasChange == -1) {
                showALertBoxInformation("Password Missmatch Error!", null, " Old password is incorrect");
            } else if (hasChange > 0) {
                showALertBoxInformation("Confirmation", null, "Password Change Successfully");
            }
        });
    }

    private void showALertBoxInformation(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
