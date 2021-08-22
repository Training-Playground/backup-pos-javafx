package lk.ijse.dep7.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep7.util.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageCustomersFormController {
    public AnchorPane root;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerId;
    public JFXButton btnDelete;
    public JFXButton btnSave;
    public JFXTextField txtCustomerAddress;
    public TableView<CustomerTM> tblCustomers;
    public JFXButton btnAddNewCustomer;

    public void initialize() {
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        txtCustomerId.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        textFieldSetDisable(true,txtCustomerId,txtCustomerName,txtCustomerAddress);

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                btnDelete.setDisable(newValue == null);
                btnSave.setText(newValue != null? "Update": "Save");

                if (newValue != null){
                    txtCustomerId.setText(newValue.getId());
                    txtCustomerName.setText(newValue.getName());
                    txtCustomerAddress.setText(newValue.getAddress());
                    btnSave.setDisable(false);
                    textFieldSetDisable(false,txtCustomerId,txtCustomerName,txtCustomerAddress);

                }
        });
    }

    private void textFieldSetDisable(boolean status, TextField... textFields){
        for (TextField txt : textFields){
            txt.setDisable(status);
        }
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/main-form.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    public void btnAddNew_OnAction(ActionEvent actionEvent) {
        clearTextFields();
        textFieldSetDisable(false,txtCustomerId,txtCustomerName,txtCustomerAddress);
        txtCustomerId.setText(generateNewId());
        txtCustomerName.requestFocus();
        btnSave.setDisable(false);
        tblCustomers.getSelectionModel().clearSelection();
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        String id = txtCustomerId.getText();
        String name = txtCustomerName.getText();
        String address = txtCustomerAddress.getText();

        if (!name.matches("[A-Za-z ]+") || name.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name").show();
            txtCustomerName.requestFocus();
            return;
        }

        if (!address.matches(".{3,}") || address.trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Invalid Address").show();
            txtCustomerAddress.requestFocus();
            return;
        }

        if (btnSave.getText().equalsIgnoreCase("save")) {

            /* Todo: Need to Save this DB first */
            tblCustomers.getItems().add(new CustomerTM(id, name, address));
        }else{
            /* Todo: Need to update this to DB first ,then proceed*/
            CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
            selectedCustomer.setName(name);
            selectedCustomer.setAddress(address);
            tblCustomers.refresh();
        }
        btnAddNewCustomer.fire();
    }

    private void clearTextFields(){
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        tblCustomers.getItems().remove(tblCustomers.getSelectionModel().getSelectedItem());
        tblCustomers.getSelectionModel().clearSelection();
        clearTextFields();
        textFieldSetDisable(true,txtCustomerId,txtCustomerName,txtCustomerAddress);
        btnSave.setDisable(true);
    }

    private String generateNewId() {
       if (tblCustomers.getItems().isEmpty()) {
                return "C001";
       } else {
           ArrayList<Integer> customerIdNumList = new ArrayList<>();
           for (int i = 0; i < tblCustomers.getItems().size(); i++) {
               String id = tblCustomers.getItems().get(i).getId();
               customerIdNumList.add(Integer.parseInt(id.replace("C","")));
           }
           return String.format("C%03d",Collections.max(customerIdNumList)+1);
       }
    }
}

