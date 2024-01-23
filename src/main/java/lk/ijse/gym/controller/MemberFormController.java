package lk.ijse.gym.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gym.db.DbConnection;
import lk.ijse.gym.dto.MemberDto;
import lk.ijse.gym.dto.tm.MemberTm;
import lk.ijse.gym.model.MemberModel;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class MemberFormController {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> colAdd;

    @FXML
    private TableColumn<?, ?> colConNu;

    @FXML
    private TableColumn<?, ?> colDOJ;

    @FXML
    private TableColumn<?, ?> colGender;

    @FXML
    private TableColumn<?, ?> colMemId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPlan;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private AnchorPane pane1;

    @FXML
    private TableView<MemberTm> tblMember;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCon;

    @FXML
    private TextField txtDOJ;

    @FXML
    private TextField txtGender;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPlan;

    @FXML
    private TextField txtPrice;
    private MemberModel memModel = new MemberModel();

    public void initialize() {
        setCellValueFactory();
        loadAllMembers();
    }

    private void setCellValueFactory() {
        colMemId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colConNu.setCellValueFactory(new PropertyValueFactory<>("cont"));
        colAdd.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDOJ.setCellValueFactory(new PropertyValueFactory<>("dateJoin"));
        colPlan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("planPrice"));
    }

    private void loadAllMembers() {
        var model = new MemberModel();

        ObservableList<MemberTm> obList = FXCollections.observableArrayList();

        try {
            List<MemberDto> dtoList = model.getAllMember();
            for (MemberDto dto : dtoList) {
                obList.add(
                        new MemberTm(
                                dto.getMemberId(),
                                dto.getName(),
                                dto.getGender(),
                                dto.getCont(),
                                dto.getAddress(),
                                dto.getDateJoin(),
                                dto.getPlan(),
                                dto.getPlanPrice()
                        )
                );
            }
            tblMember.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String memberId = txtID.getText();
        try {
            boolean isDeleted = memModel.deleteMember(memberId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Member deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Member not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        initialize();

    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {

        boolean isValidate = validateMember();
        if (isValidate) {
            new Alert(Alert.AlertType.INFORMATION, "Member is validated ");

            String memberId = txtID.getText();
            String name = txtName.getText();
            String gender = txtGender.getText();
            int cont = Integer.parseInt(txtCon.getText());
            String address = txtAddress.getText();
            String dateJoin = txtDOJ.getText();
            String plan = txtPlan.getText();
            double planPrice = Double.parseDouble(txtPrice.getText());

            var dto = new MemberDto(memberId, name, gender, cont, address, dateJoin, plan, planPrice);


            try {
                boolean isSaved = memModel.saveMember(dto);

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Member saved!").show();
                    loadAllMembers();
                    clearFields();

                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateMember() {
        boolean matches = Pattern.matches("[M][0-9]{3,}", txtID.getText());
        if (!matches) {
            new Alert(Alert.AlertType.ERROR, "Invalid id").show();
            return false;
        }
        boolean matches1 = Pattern.matches("[A-Za-z]{2,}", txtName.getText());
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();
            return false;
        }
        boolean matches2 = Pattern.matches("[A-Za-z]{3,}", txtGender.getText());
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR, "Invalid gender").show();
            return false;
        }
        boolean matches3 = Pattern.matches("[0-9]{10,}", txtCon.getText());
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR, "Invalid contactNu").show();
            return false;
        }
        boolean matches4 = Pattern.matches("[A-Za-z]{2,}", txtAddress.getText());
        if (!matches4) {
            new Alert(Alert.AlertType.ERROR, "Invalid address").show();
            return false;
        }
        boolean matches5 = Pattern.matches("[0-9-/.]{2,}", txtDOJ.getText());
        if (!matches5) {
            new Alert(Alert.AlertType.ERROR, "Invalid date").show();
            return false;
        }
        boolean matches6 = Pattern.matches("[A-Za-z0-9]{2,}", txtPlan.getText());
        if (!matches6) {
            new Alert(Alert.AlertType.ERROR, "Invalid plan").show();
            return false;
        }
        boolean matches7 = Pattern.matches("[0-9]{2,}", txtPrice.getText());
        if (!matches7) {
            new Alert(Alert.AlertType.ERROR, "Invalid price").show();
            return false;
        }

        return true;

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtID.setText("");
        txtName.setText("");
        txtGender.setText("");
        txtCon.setText("");
        txtAddress.setText("");
        txtDOJ.setText("");
        txtPlan.setText("");
        txtPrice.setText("");


    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String memberId = txtID.getText();
        String name = txtName.getText();
        String gender = txtGender.getText();
        int con = Integer.parseInt(txtCon.getText());
        String address = txtAddress.getText();
        String dateJoin = txtDOJ.getText();
        String plan = txtPlan.getText();
        double planPrice = Double.parseDouble(txtPrice.getText());

        var dto = new MemberDto(memberId, name, gender, con, address, dateJoin, plan, planPrice);


        try {
            boolean isUpdated = memModel.updateMember(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Member updated!").show();
                initialize();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    void txtSearchIdOnAction(ActionEvent event) {
        String memberId = txtID.getText();

        try {
            MemberDto memberDto = memModel.searchMember(memberId);

            if (memberDto != null) {
                txtID.setText(memberDto.getMemberId());
                txtName.setText(memberDto.getName());
                txtGender.setText(memberDto.getGender());
                txtCon.setText(String.valueOf(memberDto.getCont()));
                txtAddress.setText(memberDto.getAddress());
                txtDOJ.setText(memberDto.getDateJoin());
                txtPlan.setText(memberDto.getPlan());
                txtPrice.setText(String.valueOf(memberDto.getPlanPrice()));
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Member not found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnReportOnAction(ActionEvent event) throws SQLException, JRException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/gym.jrxml");
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

}





