package lk.ijse.gym.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.EmployeeDto;
import lk.ijse.gym.dto.tm.EmployeeTm;
import lk.ijse.gym.model.EmployeeModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colAva;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colRole;

    @FXML
    private TableColumn<?, ?> colTrainerId;

    @FXML
    private TableView<EmployeeTm> tblTrainer;

    @FXML
    private TextField txtAdd;

    @FXML
    private TextField txtAvailability;

    @FXML
    private TextField txtCon;

    @FXML
    private TextField txtFitnessArea;

    @FXML
    private TextField txtTrainerId;

    @FXML
    private TextField txtTrainerName;

    private EmployeeModel empModel=new EmployeeModel();

    public void initialize(){
        setCellValueFactory();
        loadAllTrainers();
    }

    private void setCellValueFactory(){
        colTrainerId.setCellValueFactory(new PropertyValueFactory<>("TrainerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("ContactNu"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("Role"));
        colAva.setCellValueFactory(new PropertyValueFactory<>("Availability"));

    }

    private void loadAllTrainers(){
        var model=new EmployeeModel();

        ObservableList<EmployeeTm>Oblist= FXCollections.observableArrayList();

        try{
            List<EmployeeDto>dtoList=model.getAllTrainers();
            for(EmployeeDto dto:dtoList){
                Oblist.add(
                        new EmployeeTm(
                                dto.getTrainerId(),
                                dto.getName(),
                                dto.getContactNu(),
                                dto.getAddress(),
                                dto.getRole(),
                                dto.getAvailability()

                        )
                );
            }
            tblTrainer.setItems(Oblist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    private void clearFields() {
        txtTrainerId.setText("");
        txtTrainerName.setText("");
        txtCon.setText("");
        txtAdd.setText("");
        txtFitnessArea.setText("");
        txtAvailability.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
          String trainerId=txtTrainerId.getText();
        try {
            boolean isDeleted = empModel.deleteTrainer(trainerId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Trainer deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Trainer not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        initialize();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isValidate = validateTrainer();
        if (isValidate) {
            String trainerId = txtTrainerId.getText();
            String name = txtTrainerName.getText();
            String contactNu = txtCon.getText();
            String address = txtAdd.getText();
            String role = txtFitnessArea.getText();
            String availability = txtAvailability.getText();

            var dto = new EmployeeDto(trainerId, name, contactNu, address, role, availability);

            try {
                boolean isSaved = empModel.saveTrainer(dto);

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Trainer saved!").show();
                    loadAllTrainers();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

    }

        @FXML
        void btnUpdateOnAction (ActionEvent event){
            String trainerId = txtTrainerId.getText();
            String name = txtTrainerName.getText();
            String contactNu = txtCon.getText();
            String address = txtAdd.getText();
            String role = txtFitnessArea.getText();
            String availability = txtAvailability.getText();

            var dto = new EmployeeDto(trainerId, name, contactNu, address, role, availability);

            try {
                boolean isUpdated = empModel.updateTrainer(dto);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Trainer updated!").show();
                    initialize();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

        }

        @FXML
        void txtSearchPaymentIdOnAction (ActionEvent event){
            String trainerId = txtTrainerId.getText();

            try {
                EmployeeDto employeeDto = empModel.searchTrainer(trainerId);
                if (employeeDto != null) {
                    txtTrainerId.setText(employeeDto.getTrainerId());
                    txtTrainerName.setText(employeeDto.getName());
                    txtCon.setText(employeeDto.getContactNu());
                    txtAdd.setText(employeeDto.getAddress());
                    txtFitnessArea.setText(employeeDto.getRole());
                    txtAvailability.setText(employeeDto.getAvailability());

                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Trainer not found").show();
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
            InputStream resourceAsStream = getClass().getResourceAsStream("/report/trainer.jrxml");
            JasperDesign load = JRXmlLoader.load(resourceAsStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    null,
                    DbConnection.getInstance().getConnection()
            );

            JasperViewer.viewReport(jasperPrint, false);
        }


        private boolean validateTrainer () {
            boolean matches= Pattern.matches("[T][0-9]{3,}",txtTrainerId.getText());
            if (!matches){
                new Alert(Alert.AlertType.ERROR, "Invalid trainer id").show();
                return false;
            }
            boolean matches1=Pattern.matches("[A-Za-z]{2,}",txtTrainerName.getText());
            if (!matches1) {
                new Alert(Alert.AlertType.ERROR, "Invalid trainer name").show();
                return false;
            }
            boolean matches2=Pattern.matches("[0-9]{2,}",txtCon.getText());
            if (!matches2) {
                new Alert(Alert.AlertType.ERROR, "Invalid contact").show();
                return false;
            }
            boolean matches3=Pattern.matches("[A-Za-z]{2,}",txtAdd.getText());
            if (!matches3) {
                new Alert(Alert.AlertType.ERROR, "Invalid address").show();
                return false;
            }
            boolean matches4=Pattern.matches("[A-Za-z]{2,}",txtFitnessArea.getText());
            if (!matches4) {
                new Alert(Alert.AlertType.ERROR, "Invalid fitness area").show();
                return false;
            }
            boolean matches5=Pattern.matches("[A-Za-z]{3,}",txtAvailability.getText());
            if (!matches5) {
                new Alert(Alert.AlertType.ERROR, "Invalid availability").show();
                return false;
            }

            return true;
        }
    }


