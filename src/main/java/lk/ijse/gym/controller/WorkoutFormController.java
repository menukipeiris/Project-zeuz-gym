package lk.ijse.gym.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.WorkoutDto;
import lk.ijse.gym.dto.tm.WorkoutTm;
import lk.ijse.gym.model.WorkoutModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class WorkoutFormController {

    @FXML
    private TableColumn<?, ?> colCardio;

    @FXML
    private TableColumn<?, ?> colTraining;

    @FXML
    private TableColumn<?, ?> colWarmDown;

    @FXML
    private TableColumn<?, ?> colWarmUp;

    @FXML
    private TableColumn<?, ?> colWorkoutPlanId;

    @FXML
    private TableView<WorkoutTm> tblWorkoutPlan;

    @FXML
    private TextField txtCardio;

    @FXML
    private TextField txtPlanId;

    @FXML
    private TextArea txtTraining;

    @FXML
    private TextArea txtWarmDown;

    @FXML
    private TextArea txtWarmUp;

    private WorkoutModel workoutModel=new WorkoutModel();

    public void initialize(){
        setCellValueFactory();
        loadAllPlans();
    }
    private void setCellValueFactory() {
        colWorkoutPlanId.setCellValueFactory(new PropertyValueFactory<>("planId"));
        colWarmUp.setCellValueFactory(new PropertyValueFactory<>("WarmUp"));
        colTraining.setCellValueFactory(new PropertyValueFactory<>("training"));
        colCardio.setCellValueFactory(new PropertyValueFactory<>("cardio"));
        colWarmDown.setCellValueFactory(new PropertyValueFactory<>("warmDown"));
    }
    private void loadAllPlans() {
        var model=new WorkoutModel();

        ObservableList<WorkoutTm> Oblist= FXCollections.observableArrayList();

        try{
            List<WorkoutDto> dtoList= model.getAllPlans();
            for(WorkoutDto dto:dtoList){
                Oblist.add(
                        new WorkoutTm(
                                dto.getPlanId(),
                                dto.getWarmUp(),
                                dto.getTraining(),
                                dto.getCardio(),
                                dto.getWarmDown()

                        )
                );
            }
            tblWorkoutPlan.setItems(Oblist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    private void clearFields() {
        txtPlanId.setText("");
        txtWarmUp.setText("");
        txtTraining.setText("");
        txtCardio.setText("");
        txtWarmDown.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String planId=txtPlanId.getText();
        try {
            boolean isDeleted = workoutModel.deletePlan(planId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Workout plan deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Workout plan not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isValidate = validateWorkout();
        if (isValidate) {

            String planId = txtPlanId.getText();
            String warmUp = txtWarmUp.getText();
            String training = txtTraining.getText();
            String cardio = txtCardio.getText();
            String warmDown = txtWarmDown.getText();

            var dto = new WorkoutDto(planId, warmUp, training, cardio, warmDown);

            try {
                boolean isSaved = workoutModel.savePlan(dto);

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Workout plan saved!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

        }

        @FXML
        void btnUpdateOnAction (ActionEvent event){
            String planId = txtPlanId.getText();
            String warmUp = txtPlanId.getText();
            String training = txtPlanId.getText();
            String cardio = txtPlanId.getText();
            String warmDown = txtPlanId.getText();

            var dto = new WorkoutDto(planId, warmUp, training, cardio, warmDown);

            try {
                boolean isUpdated = workoutModel.updatePlan(dto);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Workout plan updated!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }


        }

        @FXML
        void txtSearchWorkoutOnAction (ActionEvent event){
            String planId = txtPlanId.getText();

            try {
                WorkoutDto workoutDto = workoutModel.searchPlan(planId);
                if (workoutDto != null) {
                    txtPlanId.setText(workoutDto.getPlanId());
                    txtWarmUp.setText(workoutDto.getWarmUp());
                    txtTraining.setText(workoutDto.getTraining());
                    txtCardio.setText(workoutDto.getCardio());
                    txtWarmDown.setText(workoutDto.getWarmDown());
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Workout plan not found").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

        public void btnBackOnAction (ActionEvent event) throws IOException {
            Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashboard_form.fxml"));
            Scene scene = new Scene(anchorPane);

            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }

        public void btnReportOnAction (ActionEvent event) throws JRException, SQLException {
            InputStream resourceAsStream = getClass().getResourceAsStream("/report/Workouts.jrxml");
            JasperDesign load = JRXmlLoader.load(resourceAsStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    null,
                    DbConnection.getInstance().getConnection()
            );

            JasperViewer.viewReport(jasperPrint, false);
        }

        private boolean validateWorkout () {
            boolean matches= Pattern.matches("[P][0-9]{2,}",txtPlanId.getText());
            if (!matches) {
                new Alert(Alert.AlertType.ERROR, "Invalid plan id").show();
                return false;
            }
            boolean matches1=Pattern.matches("[A-Za-z][0-9]{2,}",txtWarmUp.getText());
            if (!matches1) {
                new Alert(Alert.AlertType.ERROR, "Invalid warm up").show();
                return false;
            }
            boolean matches2=Pattern.matches("[A-Za-z][0-9]{2,}",txtTraining.getText());
            if (!matches2) {
                new Alert(Alert.AlertType.ERROR, "Invalid training").show();
                return false;
            }
            boolean matches3=Pattern.matches("[A-Za-z][0-9]{2,}",txtCardio.getText());
            if (!matches3) {
                new Alert(Alert.AlertType.ERROR, "Invalid class id").show();
                return false;
            }
            boolean matches4=Pattern.matches("[A-Za-z][0-9]{2,}",txtWarmDown.getText());
            if (!matches4) {
                new Alert(Alert.AlertType.ERROR, "Invalid warm down").show();
                return false;
            }
            return true;
        }
    }