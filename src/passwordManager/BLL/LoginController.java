/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordManager.BLL;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import passwordManager.DBL.DatabaseConnectionParameter;
import passwordManager.DBL.HibernateUtil;

/**
 *
 * @author m-hassnain
 */
public class LoginController implements Initializable {

    private static final String logAttemp = "Login Attemp(s) Remaining:";
    private int noLogAttemp = 3;

    //fxml variable
    @FXML
    TextField tfUserName;
    @FXML
    PasswordField tfPassword;
    @FXML
    Button btnLogin;
    @FXML
    Label lbLogAttempt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbLogAttempt.setText(logAttemp + noLogAttemp);
    }

    @FXML
    private void btnLoginClicked(MouseEvent event) {
        String username = tfUserName.getText();
        String password = tfPassword.getText();
        DatabaseConnectionParameter conn = null;
        SessionFactory factory = HibernateUtil.getSessionFactory(username, password);

        if (factory == null) {
            noLogAttemp--;
            if (noLogAttemp == 0) {
                System.exit(0);
            }
            lbLogAttempt.setText(logAttemp + noLogAttemp);
        } else {
            factory.close();
            conn = DatabaseConnectionParameter.getConnection(username, password);
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/passwordManager/PTL/main.fxml"));
                root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Password Manager");
                stage.getIcons().add(new Image("/passwordManager/PTL/styleSheet/lock-480.png"));
                MainController controller = loader.<MainController>getController();
                controller.setConnectionParameter(conn);
                tfUserName.getScene().getWindow().hide();

                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
               
            } catch (Exception ex){
                showALertBoxInformation("", "", ex.getMessage().toString()); 
            }
        }

    }

    private void showALertBoxInformation(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

}
