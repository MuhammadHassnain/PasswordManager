/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordManager.BLL;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import passwordManager.DBL.Account;
import passwordManager.DBL.AccountManager;
import passwordManager.DBL.DatabaseConnectionParameter;

/**
 * FXML Controller class
 *
 * @author m-hassnain
 */
public class MainController implements Initializable {

    private ObservableList<Account> accounts;
    private AccountManager accountManager;
    @FXML
    TableColumn<Account, Integer> idColumn;

    @FXML
    TableColumn<Account, String> nameColumn;
    @FXML
    TableColumn<Account, String> passwordColumn;
    @FXML
    TableColumn<Account, String> urlColumn;

    @FXML
    TextField tfUsername;
    @FXML
    TextField tfPassword;
    @FXML
    TextField tfUrl;

    @FXML
    TableView<Account> tvAccount;

    @FXML
    MenuItem menuBtnClose;
    @FXML
    MenuItem menuBtnSetting;

    public DatabaseConnectionParameter getConnectionParameter() {
        return accountManager.getConnectionParameter();
    }

    public void setConnectionParameter(DatabaseConnectionParameter connectionParameter) {
        System.out.println("passwordManager.BLL.MainController.setConnectionParameter()");
        accountManager = new AccountManager();
        accountManager.setConnectionParameter(connectionParameter);
        accounts = FXCollections.observableArrayList();
        tvAccount.setItems(getAccount());
        tvAccount.refresh();

        tfUsername.getScene().getWindow().setOnCloseRequest((WindowEvent t) -> {
            System.exit(0);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("passwordManager.BLL.MainController.initialize()");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setVisible(false);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<Account, String> t) -> {
            if ("".equals(t.getNewValue())) {
                showALertBoxInformation("UserName Error", null, "UserName can't be Empty");
                tvAccount.getSelectionModel().getSelectedItem().setUserName(t.getOldValue());
                tvAccount.refresh();
            } else {
                Account account = tvAccount.getSelectionModel().getSelectedItem();
                account.setUserName(t.getNewValue());
                accountManager.updateAccount(account);
            }
        });
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        passwordColumn.setOnEditCommit((TableColumn.CellEditEvent<Account, String> t) -> {
            if ("".equals(t.getNewValue())) {
                showALertBoxInformation("Password Error", null, "Password can't be Empty");
                tvAccount.getSelectionModel().getSelectedItem().setPassword(t.getOldValue());
                tvAccount.refresh();
            } else {
                Account account = tvAccount.getSelectionModel().getSelectedItem();
                account.setPassword(t.getNewValue());
                accountManager.updateAccount(account);
            }
        });
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setOnEditCommit((TableColumn.CellEditEvent<Account, String> t) -> {
            if ("".equals(t.getNewValue())) {
                showALertBoxInformation("URL Error", null, "URL can't be Empty");
                tvAccount.getSelectionModel().getSelectedItem().setUrl(t.getOldValue());
                tvAccount.refresh();
            } else {
                Account account = tvAccount.getSelectionModel().getSelectedItem();
                account.setUrl(t.getNewValue());
                accountManager.updateAccount(account);
            }
        });
        urlColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tvAccount.setItems(accounts);
        tvAccount.setEditable(true);

        menuBtnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        menuBtnSetting.setOnAction((ActionEvent t) -> {
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/passwordManager/PTL/Setting.fxml"));
                root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Setting");
                stage.getIcons().add(new Image("/passwordManager/PTL/icon/setting-48.png"));

                SettingController controller = loader.<SettingController>getController();
                controller.setAccountManager(accountManager);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

    public ObservableList<Account> getAccount() {

        List<Account> list = accountManager.getAllAccount();
        System.out.println(list.size());
        list.forEach((a) -> {
            accounts.add(a);
        });
        return accounts;
    }

    @FXML
    private void btnAddClicked() {
        //get values from text field and show them
        String username = tfUsername.getText();
        if (username.length() > 20 || username.length() <= 0) {
            showALertBoxInformation("UserName length Error", null, "Username Must be with (1-20)");
            makeTextFieldEmpty();
            return;
        }
        String password = tfPassword.getText();
        if (password.length() > 20 || password.length() <= 0) {
            showALertBoxInformation("Password length Error", null, "Password Must be with (1-20)");
            makeTextFieldEmpty();
            return;
        }
        String url = tfUrl.getText();
        if (url.length() > 100 || url.length() <= 0) {
            showALertBoxInformation("URL length Error", null, "URL Must be with (1-100)");
            makeTextFieldEmpty();
            return;
        }
        Account account = new Account(username, password, url);
        int res = accountManager.addAccount(account);
        //Ask Account Manager to add new Values
        if (res <= 0) {
            String message = "Error adding account. Please try Again !!";;
            if (res == -1) {
                message = "Account already added!";
            }
            showALertBoxInformation("Add Account Error", null, message);
        } else {
            showALertBoxInformation("Add Account", null, "Account Added Sussfully");
            accounts.add(account);
        }
        makeTextFieldEmpty();
        tvAccount.refresh();
    }

    @FXML
    private void btnDeleteClicked() {
        Account account = tvAccount.getSelectionModel().getSelectedItem();
        int result = accountManager.deleteAccount(account);
        System.out.println("passwordManager.BLL.MainController.btnDeleteClicked()" + result);
        if (result > 0) {
            accounts.remove(account);
            tvAccount.refresh();
        }
    }

    private void showALertBoxInformation(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void makeTextFieldEmpty() {
        tfUsername.setText("");
        tfPassword.setText("");
        tfUrl.setText("");
    }

}
