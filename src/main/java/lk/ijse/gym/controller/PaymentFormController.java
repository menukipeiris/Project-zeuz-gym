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
import lk.ijse.gym.dto.PaymentDto;
import lk.ijse.gym.dto.tm.PaymentTm;
import lk.ijse.gym.model.PaymentModel;
import javafx.scene.control.Label;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class PaymentFormController {

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colMemberId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPaymentId;

    @FXML
    private TableColumn<?, ?> colPlan;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TextField txtDate;


    @FXML
    private TableView<PaymentTm> tblPayment;


    @FXML
    private TextField txtMemberId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPaymentId;

    @FXML
    private TextField txtPlan;

    @FXML
    private TextField txtPrice;

    private PaymentModel payModel=new PaymentModel();

    public void initialize(){
        setCellValueFactory();
        loadAllPayment();

    }
    private  void setCellValueFactory(){
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPlan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
    private  void loadAllPayment(){
        var model=new PaymentModel();

        ObservableList<PaymentTm>obList= FXCollections.observableArrayList();
        try {
            List<PaymentDto> dtoList = model.getAllPayment();
            for (PaymentDto dto : dtoList) {
                obList.add(
                        new PaymentTm(
                                dto.getPaymentId(),
                                dto.getMemberId(),
                                dto.getName(),
                                dto.getPlan(),
                                dto.getAmountPaid(),
                                dto.getDate()

                        )
                );
            }
            tblPayment.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String paymentId = txtPaymentId.getText();
        try {
            boolean isDeleted = payModel.deletePayment(paymentId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        initialize();
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isValidate = validatePayment();
        if (isValidate) {
            String paymentId = txtPaymentId.getText();
            String memberId = txtMemberId.getText();
            String memName = txtName.getText();
            String plan = txtPlan.getText();
            double amountPaid = Double.parseDouble(txtPrice.getText());
            String date = txtDate.getText();

            var dto = new PaymentDto(paymentId, memberId, memName, plan, amountPaid, date);

            try {
                boolean isSaved = payModel.savePayment(dto);


                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Payment saved!").show();
                    loadAllPayment();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validatePayment() {
        boolean matches= Pattern.matches("[P][0-9]{2,}",txtPaymentId.getText());
        if (!matches) {
            new Alert(Alert.AlertType.ERROR, "Invalid payment id").show();
            return false;
        }
        boolean matches1= Pattern.matches("[M][0-9]{3,}",txtMemberId.getText());
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR, "Invalid member id").show();
            return false;
        }
        boolean matches2=Pattern.matches("[A-Za-z]{2,}",txtName.getText());
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();
            return false;
        }
        boolean matches3=Pattern.matches("[A-Za-z]{2,}",txtPlan.getText());
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR, "Invalid plan").show();
            return false;
        }
        boolean matches4=Pattern.matches("[0-9.]{2,}",txtPrice.getText());
        if (!matches4) {
            new Alert(Alert.AlertType.ERROR, "Invalid price").show();
            return false;
        }
        boolean matches5=Pattern.matches("[0-9-/.]{2,}",txtDate.getText());
        if (!matches5) {
            new Alert(Alert.AlertType.ERROR, "Invalid date").show();
            return false;
        }
        return true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String paymentId=txtPaymentId.getText();
        String memberId=txtMemberId.getText();
        String memName=txtName.getText();
        String plan=txtPlan.getText();
        double amountPaid= Double.parseDouble(txtPrice.getText());
        String date= txtDate.getText();

        var dto=new PaymentDto(paymentId,memberId,memName,plan,amountPaid,date);

        try {
            boolean isUpdated = payModel.updatePayment(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment updated!").show();
                loadAllPayment();
                initialize();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
       clearFields();
    }
        private void clearFields(){
        txtPaymentId.setText("");
        txtMemberId.setText("");
        txtName.setText("");
        txtPlan.setText("");
        txtPrice.setText("");
        txtDate.setText("");

        }
    @FXML
    void txtPaymentIdOnAction(ActionEvent event) {
        String paymentId=txtPaymentId.getText();

        try {
            PaymentDto paymentDto=payModel.searchPayment(paymentId);

            if(paymentDto!=null){
                txtPaymentId.setText(paymentDto.getPaymentId());
                txtMemberId.setText(paymentDto.getMemberId());
                txtName.setText(paymentDto.getName());
                txtPlan.setText(paymentDto.getPlan());
                txtPrice.setText(String.valueOf(paymentDto.getAmountPaid()));
                txtDate.setText(paymentDto.getDate());

        }else {
                new Alert(Alert.AlertType.INFORMATION, "Payment not found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

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


    public void btnReportOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/payment.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(load);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                null,
                DbConnection.getInstance().getConnection()
        );

        JasperViewer.viewReport(jasperPrint, false);
    }

}





