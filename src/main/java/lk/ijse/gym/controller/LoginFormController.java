package lk.ijse.gym.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gym.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;


public class LoginFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUseName;

    public void btnSignUpOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/signUp_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("SignUp Form");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String Username = txtUseName.getText();
        String Password = txtPassword.getText();
        try{
            boolean useIsExist = UserModel.isExistUser(Username,Password);
            if (useIsExist) {

                AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/Dashboard_form.fxml"));
                Scene scene = new Scene(anchorPane);
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.centerOnScreen();
                stage.show();
            } else {
            new Alert(Alert.AlertType.ERROR, "Incorrect username or password!!!").show();
            }
        } catch (SQLException | IOException e){
             new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}



