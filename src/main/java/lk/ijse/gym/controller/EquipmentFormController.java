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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.EquipmentDto;
import lk.ijse.gym.dto.tm.EquipmentTm;
import lk.ijse.gym.model.EquipmentModel;
import javafx.scene.control.TableView;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EquipmentFormController {

    @FXML
    private TableColumn<?, ?> colDeliDate;

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colEquipId;

    @FXML
    private TableColumn<?, ?> colEquipName;

    @FXML
    private TableColumn<?, ?> colMuscleUsed;

    @FXML
    private AnchorPane pane2;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtDeliveryDate;

    @FXML
    private TextArea txtDesc;

    @FXML
    private TextField txtEquId;

    @FXML
    private TextField txtEquName;

    @FXML
    private TextField txtMuscle;

    @FXML
    private TableView<EquipmentTm> tblEquipment;

    private EquipmentModel equipmentModel = new EquipmentModel();

    public void initialize() {
        setCellValueFactory();
        loadAllEquipment();

    }

    private void setCellValueFactory() {
        colEquipId.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colEquipName.setCellValueFactory(new PropertyValueFactory<>("equName"));
        colDeliDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colMuscleUsed.setCellValueFactory(new PropertyValueFactory<>("muscleUsed"));
    }

    private void loadAllEquipment() {
        var model = new EquipmentModel();

        ObservableList<EquipmentTm> obList = FXCollections.observableArrayList();
        try {
            List<EquipmentDto> dtoList = model.getAllEquipment();
            for (EquipmentDto dto : dtoList) {
                obList.add(
                        new EquipmentTm(
                                dto.getEquipmentId(),
                                dto.getEquName(),
                                dto.getDeliveryDate(),
                                dto.getDescription(),
                                dto.getCost(),
                                dto.getMuscleUsed()
                        )
                );
            }
            tblEquipment.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {

        txtEquId.setText("");
        txtEquName.setText("");
        txtDeliveryDate.setText("");
        txtDesc.setText("");
        txtCost.setText("");
        txtMuscle.setText("");
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String equipmentId = txtEquId.getText();
        try {
            boolean isDeleted = equipmentModel.deleteEquipment(equipmentId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Equipment deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Equipment not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        initialize();
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isValidate = validateEquipment();
        if (isValidate) {
            String equipmentId = txtEquId.getText();
            String equName = txtEquName.getText();
            String deliveryDate = txtDeliveryDate.getText();
            String description = txtDesc.getText();
            double cost = Double.parseDouble(txtCost.getText());
            String muscleUsed = txtMuscle.getText();

            var dto = new EquipmentDto(equipmentId, equName, deliveryDate, description, cost, muscleUsed);

            try {
                boolean isSaved = equipmentModel.saveEquipment(dto);


                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Equipment saved!").show();
                    loadAllEquipment();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String equipmentId = txtEquId.getText();
        String equName = txtEquName.getText();
        String deliveryDate = txtDeliveryDate.getText();
        String description = txtDesc.getText();
        double cost = Double.parseDouble(txtCost.getText());
        String muscleUsed = txtMuscle.getText();

        var dto = new EquipmentDto(equipmentId, equName, deliveryDate, description, cost, muscleUsed);

        try {
            boolean isUpdated = equipmentModel.updateEquipment(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Equipment updated!").show();
                initialize();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    @FXML
    void txtEquipmentIdOnAction(ActionEvent event) {

        String equipmentId = txtEquId.getText();

        try {
            EquipmentDto equipmentDto = equipmentModel.searchEquipment(equipmentId);

            if (equipmentDto != null) {
                txtEquId.setText(equipmentDto.getEquipmentId());
                txtEquName.setText(equipmentDto.getEquName());
                txtDeliveryDate.setText(equipmentDto.getDeliveryDate());
                txtDesc.setText(equipmentDto.getDescription());
                txtCost.setText(String.valueOf(equipmentDto.getCost()));
                txtMuscle.setText(equipmentDto.getMuscleUsed());

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Equipment not found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnReportOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/equipment.jrxml");
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

    private boolean validateEquipment() {
        boolean matches= Pattern.matches("[E][0-9]{3,}",txtEquId.getText());
        if (!matches) {
            new Alert(Alert.AlertType.ERROR, "Invalid Equipment id").show();
            return false;
        }
        boolean matches1=Pattern.matches("[A-Za-z]{2,}",txtEquName.getText());
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR, "Invalid Equipment name").show();
            return false;
        }
        boolean matches2=Pattern.matches("[0-9-/.]{2,}",txtDeliveryDate.getText());
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR, "Invalid DeliveryDate").show();
            return false;
        }
        boolean matches3=Pattern.matches("[A-Za-z]{2,}",txtDesc.getText());
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR, "Invalid description").show();
            return false;
        }
        boolean matches4=Pattern.matches("[0-9]{2,}",txtCost.getText());
        if (!matches4) {
            new Alert(Alert.AlertType.ERROR, "Invalid cost").show();
            return false;
        }
        boolean matches5=Pattern.matches("[A-Za-z]{2,}",txtMuscle.getText());
        if (!matches5) {
            new Alert(Alert.AlertType.ERROR, "Invalid muscle used").show();
            return false;
        }
        return true;
    }

}


