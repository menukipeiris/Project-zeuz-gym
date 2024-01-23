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
import lk.ijse.gym.dto.ServiceDto;
import lk.ijse.gym.dto.tm.ServiceTm;
import lk.ijse.gym.model.ServiceModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class ServiceFormController {

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colServiceId;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private TableColumn<?, ?> colTrainerName;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> coltrainerId;

    @FXML
    private TableView<ServiceTm> tblClass;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtServiceId;

    @FXML
    private TextField txtTime;

    @FXML
    private TextField txtTrainerId;

    @FXML
    private TextField txtTrainerName;

    @FXML
    private TextField txtType;

    private ServiceModel serviceModel = new ServiceModel();

    public void initialize() {
        setCellValueFactory();
        loadAllClasses();
    }

    private void setCellValueFactory() {
        colServiceId.setCellValueFactory(new PropertyValueFactory<>("ServiceId"));
        coltrainerId.setCellValueFactory(new PropertyValueFactory<>("TrainerId"));
        colTrainerName.setCellValueFactory(new PropertyValueFactory<>("TrainerName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("ClassType"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }

    private void loadAllClasses() {
        var model = new ServiceModel();

        ObservableList<ServiceTm> ObList = FXCollections.observableArrayList();

        try {
            List<ServiceDto> dtoList = model.getAllClasses();
            for (ServiceDto dto : dtoList) {
                ObList.add(
                        new ServiceTm(
                                dto.getServiceId(),
                                dto.getTrainerId(),
                                dto.getTrainerName(),
                                dto.getClassType(),
                                dto.getTime(),
                                dto.getDate()
                        )
                );
            }
            tblClass.setItems(ObList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    private void clearFields() {
        txtServiceId.setText("");
        txtTrainerId.setText("");
        txtTrainerName.setText("");
        txtType.setText("");
        txtTime.setText("");
        txtDate.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String serviceId = txtServiceId.getText();
        try {
            boolean isDeleted = serviceModel.deleteClass(serviceId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Class deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Class not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        initialize();

    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isValidate = validateClasses();
        if (isValidate) {
            // new Alert(Alert.AlertType.INFORMATION, "Class is validated ");
            String serviceId = txtServiceId.getText();
            String trainerId = txtTrainerId.getText();
            String trainerName = txtTrainerName.getText();
            String classType = txtType.getText();
            String time = txtTime.getText();
            String date = txtDate.getText();

            var dto = new ServiceDto(serviceId, trainerId, trainerName, classType, time, date);

            try {
                boolean isSaved = serviceModel.saveClass(dto);

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Class saved!").show();
                    loadAllClasses();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String serviceId = txtServiceId.getText();
        String trainerId = txtTrainerId.getText();
        String trainerName = txtTrainerName.getText();
        String classType = txtType.getText();
        String time = txtTime.getText();
        String date = txtDate.getText();

        var dto = new ServiceDto(serviceId, trainerId, trainerName, classType, time, date);


        try {
            boolean isUpdated = serviceModel.updateClass(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Class updated!").show();
                initialize();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
    void txtSearchServiceIdOnAction(ActionEvent event) {

        String serviceId = txtServiceId.getText();

        try {
            ServiceDto serviceDto = serviceModel.searchClass(serviceId);

            if (serviceDto != null) {
                txtServiceId.setText(serviceDto.getServiceId());
                txtTrainerId.setText(serviceDto.getTrainerId());
                txtTrainerName.setText(serviceDto.getTrainerName());
                txtType.setText(serviceDto.getClassType());
                txtTime.setText(serviceDto.getTime());
                txtDate.setText(serviceDto.getDate());
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Class not found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnReportOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/classes.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(load);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                null,
                DbConnection.getInstance().getConnection()
        );

        JasperViewer.viewReport(jasperPrint, false);
    }


    public void btnBackOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/Dashboard_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private boolean validateClasses() {
        boolean matches = Pattern.matches("[S][0-9]{2,}", txtServiceId.getText());
        if (!matches) {
            new Alert(Alert.AlertType.ERROR, "Invalid class id").show();
            return false;
        }
        boolean matches1=Pattern.matches("[T][0-9]{2,}",txtTrainerId.getText());
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR, "Invalid trainer id").show();
            return false;
        }
        boolean matches2=Pattern.matches("[A-Za-z]{2,}",txtTrainerName.getText());
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR, "Invalid trainer name").show();
            return false;
        }
        boolean matches3=Pattern.matches("[A-za-z]{3,}",txtType.getText());
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR, "Invalid type").show();
            return false;
        }
        boolean matches4=Pattern.matches("[0-9.]{1,}",txtTime.getText());
        if (!matches4) {
            new Alert(Alert.AlertType.ERROR, "Invalid time").show();
            return false;
        }
        boolean matches5=Pattern.matches("[A-Za-z]{2,}",txtDate.getText());
        if (!matches5) {
            new Alert(Alert.AlertType.ERROR, "Invalid date").show();
            return false;
        }
        return true;
    }
}
