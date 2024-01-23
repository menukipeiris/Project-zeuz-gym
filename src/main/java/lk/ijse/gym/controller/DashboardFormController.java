package lk.ijse.gym.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.AbstractList;
import java.util.Optional;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.animation.Animation.*;

public class DashboardFormController {

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane pane1;

    @FXML
    private AnchorPane root1;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;


    public  void initialize(){
        displayDate();
        setCycleCount();
    }

    private void setCycleCount() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + " : " + currentTime.getMinute() + " : " + currentTime.getSecond()
            );
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void displayDate(){
        lblDate.setText(String.valueOf(LocalDate.now()));
    }

    public void btnMemberOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/member_form.fxml")));
    }
    public void btnEquipmentOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/equipment_form.fxml")));
    }

    public void btnTrainerOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/employee_form.fxml")));
    }

    public void btnPaymentOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/payment_form.fxml")));
    }

    public void btnWorkoutOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/workout_form.fxml")));
    }

    public void btnClassesOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/service_form.fxml")));
    }

    public void btnSupplimentOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/Suppliment_form.fxml")));

    }


    public void btnPlaceOrderOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/PlaceOrder_form.fxml")));
    }

    public void btnContactOnAction(ActionEvent event) throws IOException {
        this.pane.getChildren().clear();
        this.pane.getChildren().add(FXMLLoader.load(this.getClass().getResource("/view/contact_form.fxml")));
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");
        Optional<ButtonType>option=alert.showAndWait();
          try {
              if(option.get().equals(ButtonType.OK)){
                  pane.getScene().getWindow().hide();
                  AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/Login_form.fxml"));
                  Scene scene = new Scene(anchorPane);
                  Stage stage = (Stage) pane.getScene().getWindow();
                  stage.setScene(scene);
                  stage.centerOnScreen();
                  stage.show();
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
    }

}
