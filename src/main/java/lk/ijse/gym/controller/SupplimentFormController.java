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
import lk.ijse.gym.dto.SupplimentDto;
import lk.ijse.gym.dto.tm.CartTm;
import lk.ijse.gym.dto.tm.SupplimentTm;
import lk.ijse.gym.model.SupplimentModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class SupplimentFormController {

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableView<SupplimentTm> tblSuppliment;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtItemCode;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;
    private ObservableList<CartTm> obList = FXCollections.observableArrayList();

    private SupplimentModel supplimentModel=new SupplimentModel();

    public void initialize() {
        setCellValueFactory();
        loadAllItems();
        tableListener();

    }

    private void setColAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to delete?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int focusedIndex = tblSuppliment.getSelectionModel().getSelectedIndex();

                obList.remove(focusedIndex);
                tblSuppliment.refresh();

            }
        });
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

    private void loadAllItems() {
        ObservableList<SupplimentTm> obList = FXCollections.observableArrayList();
        try {
            List<SupplimentDto> dtoList = supplimentModel.loadAllItems();

            for (SupplimentDto dto : dtoList) {
                obList.add(new SupplimentTm(
                        dto.getCode(),
                        dto.getDescription(),
                        dto.getUnitPrice(),
                        dto.getQtyOnHand(),
                        new Button("Delete")
                ));
            }
            tblSuppliment.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setData(SupplimentTm row) {
        txtItemCode.setText(row.getCode());
        txtDescription.setText(row.getDescription());
        txtUnitPrice.setText(String.valueOf(row.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(row.getQtyOnHand()));
    }

    private void tableListener() {
        tblSuppliment.getSelectionModel().selectedItemProperty().addListener((observable, oldValued, newValue) -> {
            setData(newValue);
        });
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtItemCode.setText("");
        txtDescription.setText("");
        txtUnitPrice.setText("");
        txtQtyOnHand.setText("");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isValidate = validateSupplementForm();
        if (isValidate) {
            String code = txtItemCode.getText();
            String description = txtDescription.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

            var dto = new SupplimentDto(code, description, unitPrice, qtyOnHand);

            try {
                boolean isSaved = supplimentModel.saveItem(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplement saved!").show();
                    loadAllItems();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

        @FXML
        void btnUpdateOnAction (ActionEvent event){
            String code = txtItemCode.getText();
            String description = txtDescription.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

            try {
                boolean isUpdated = supplimentModel.updateItem(new SupplimentDto(code, description, unitPrice, qtyOnHand));
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplement updated").show();
                    loadAllItems();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

        }

        @FXML
        void searchItemOnAction (ActionEvent event){
            String code = txtItemCode.getText();

            try {
                SupplimentDto dto = supplimentModel.searchItem(code);
                if (dto != null) {
                    setFields(dto);
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Supplement not found!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

        }

        private void setFields (SupplimentDto dto){
            txtItemCode.setText(dto.getCode());
            txtDescription.setText(dto.getDescription());
            txtUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));
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
            InputStream resourceAsStream = getClass().getResourceAsStream("/report/suppliment.jrxml");
            JasperDesign load = JRXmlLoader.load(resourceAsStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    null,
                    DbConnection.getInstance().getConnection()
            );

            JasperViewer.viewReport(jasperPrint, false);
        }

        private boolean validateSupplementForm () {
            boolean matches= Pattern.matches("[I][0-9]{2,}",txtItemCode.getText());
            if (!matches) {
                new Alert(Alert.AlertType.ERROR, "Invalid item code").show();
                return false;
            }
            boolean matches1=Pattern.matches("[A-Za-z]{2,}",txtDescription.getText());
            if (!matches1) {
                new Alert(Alert.AlertType.ERROR, "Invalid description").show();
                return false;
            }
            boolean matches2=Pattern.matches("[0-9.]{2,}",txtUnitPrice.getText());
            if (!matches2) {
                new Alert(Alert.AlertType.ERROR, "Invalid unit price").show();
                return false;
            }
            boolean matches3=Pattern.matches("[0-9]{0,}",txtQtyOnHand.getText());
            if (!matches3) {
                new Alert(Alert.AlertType.ERROR, "Invalid qty on hand").show();
                return false;
            }
            return  true;
        }

    }

