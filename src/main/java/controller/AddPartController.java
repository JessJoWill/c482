package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.Inventory.*;

/**
 * Displays and controls the Add Part Screen.
 */
public class AddPartController implements Initializable {
    public Button cancelButton;
    public RadioButton addPartIhRB;
    public ToggleGroup addPartIhToggle;
    public RadioButton addPartOsRB;
    public Label switchLbl;
    public TextField addPartIdTxt;
    public TextField addPartNameTxt;
    public TextField addPartInvTxt;
    public TextField addPartPriceTxt;
    public TextField addPartMaxTxt;
    public TextField addPartSwitchTxt;
    public TextField addPartMinTxt;

    /**
     * Generates a unique Part ID number for each part added to the allParts list.
     * @return newPartID
     */
    public int newPartIdNumber(){
        int lastPart = Inventory.getAllParts().size();
        int newPartId = lastPart + 1;
        while (Inventory.getAllParts().contains(newPartId)){
            newPartId++;
        }
        return newPartId;
    }

    /**
     * Assigns the Part ID number to the new part.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newPartIdNumber();
        addPartIdTxt.setText(String.valueOf(newPartIdNumber()));
    }

    /**
     * Toggles the seventh parameter label to Machine ID for In-House parts when the In-House radio button is clicked.
     * @param actionEvent In-House radio button selected
     */
    public void showAddIhPart(ActionEvent actionEvent) {
        switchLbl.setText("Machine ID");
    }

    /**
     * Toggles the seventh parameter label to Company Name for Outsourced parts when the Outsourced radio button is clicked.
     * @param actionEvent Outsourced radio button selected
     */
    public void showAddOsPart(ActionEvent actionEvent) {
        switchLbl.setText("Company Name");
    }

    /**
     * Adds the new In-House or Outsourced Part to the allParts list.
     * @param actionEvent Save button clicked
     */
    public void onSavePart(ActionEvent actionEvent) {
        if (!(addPartNameTxt.getText().isEmpty()) && !(addPartPriceTxt.getText().isEmpty()) && !(addPartInvTxt.getText().isEmpty()) && !(addPartMinTxt.getText().isEmpty()) && !(addPartMaxTxt.getText().isEmpty()) && !(addPartSwitchTxt.getText().isEmpty())) {
            switch (switchLbl.getText()) {
                case "Machine ID":
                    try {
                        int id = Integer.parseInt(addPartIdTxt.getText());
                        String name = addPartNameTxt.getText();
                        double price = Double.parseDouble(addPartPriceTxt.getText());
                        int stock = Integer.parseInt(addPartInvTxt.getText());
                        int min = Integer.parseInt(addPartMinTxt.getText());
                        int max = Integer.parseInt(addPartMaxTxt.getText());
                        int machineId = Integer.parseInt(addPartSwitchTxt.getText());
                        if(min>stock || min>max || stock>max){
                            Alert stockInvalid = new Alert(Alert.AlertType.ERROR);
                            stockInvalid.setTitle("Invalid entry");
                            stockInvalid.setContentText("Please ensure the inventory level falls between the minimum and maximum levels allowed.");
                            stockInvalid.showAndWait();
                        }
                        else{
                            InHouse newPart = new InHouse(id, name, price, stock, min, max, machineId);
                            addIHPart(newPart);
                            cancelButton.fire();
                        }
                    } catch (NumberFormatException e) {
                        Alert invalidInfo = new Alert(Alert.AlertType.ERROR);
                        invalidInfo.setTitle("Invalid Submission");
                        invalidInfo.setContentText("All fields require numeric entries except Part Name.");
                        invalidInfo.showAndWait();
                    }
                    break;
                case "Company Name":
                    try {
                        int id = Integer.parseInt(addPartIdTxt.getText());
                        String name = addPartNameTxt.getText();
                        double price = Double.parseDouble(addPartPriceTxt.getText());
                        int stock = Integer.parseInt(addPartInvTxt.getText());
                        int min = Integer.parseInt(addPartMinTxt.getText());
                        int max = Integer.parseInt(addPartMaxTxt.getText());
                        String companyName = addPartSwitchTxt.getText();
                        if(min>stock || min>max || stock>max){
                            Alert stockInvalid = new Alert(Alert.AlertType.ERROR);
                            stockInvalid.setTitle("Invalid entry");
                            stockInvalid.setContentText("Please ensure the inventory level falls between the minimum and maximum levels allowed.");
                            stockInvalid.showAndWait();
                        }
                        else{
                            Outsourced newPart = new Outsourced(id, name, price, stock, min, max, companyName);
                            addOSPart(newPart);
                            cancelButton.fire();
                        }
                    } catch (NumberFormatException e) {
                        Alert invalidInfo = new Alert(Alert.AlertType.ERROR);
                        invalidInfo.setTitle("Invalid Submission");
                        invalidInfo.setContentText("All fields require numeric entries except Part Name and Company Name.");
                        invalidInfo.showAndWait();
                    }
                    break;
            }
        }
        else {
            Alert missingInfo = new Alert(Alert.AlertType.ERROR);
            missingInfo.setTitle("Invalid Submission");
            missingInfo.setContentText("All fields are required.");
            missingInfo.showAndWait();
        }
    }

    /**
     * Returns the user to the Main Screen when the Cancel button is clicked.
     * @param actionEvent Cancel button clicked
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main-form-view.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100, 475);
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
