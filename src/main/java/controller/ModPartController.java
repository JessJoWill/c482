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

/**
 * Displays and controls the Modify Part Screen.
 */
public class ModPartController implements Initializable {
    public TextField modPartIdTxt;
    public TextField modPartNameTxt;
    public TextField modPartInvTxt;
    public TextField modPartPriceTxt;
    public TextField modPartMaxTxt;
    public TextField modPartMinTxt;
    public TextField modPartSwitchField;
    public Label switchLabel;
    public RadioButton modPartIhToggle;
    public RadioButton modPartOsToggle;
    public Label indexLabel;
    public Button cancelButton;

    /**
     * Not used.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Places the parameter values from the selected part into the text fields of the Modify Part screen.
     * @param selectedIndex index of the selected part
     * @param partId ID of the selected part
     * @param partName name of the selected part
     * @param partPrice price of the selected part
     * @param partStock inventory level of the selected part
     * @param partMin minimum inventory level of the selected part
     * @param partMax maximum inventory level of the selected part
     * @param type indicator of whether the selected part is In-house or Outsourced
     * @param partSwitch seventh parameter of the selected part, either Machine ID or Company Name
     */
    public void fillForm(int selectedIndex, int partId, String partName, double partPrice, int partStock, int partMin, int partMax, String type, String partSwitch) {
        if (type == "IH") {
            switchLabel.setText("Machine ID");
        } else {
            switchLabel.setText("Company Name");
            modPartOsToggle.fire();
        }
        indexLabel.setText(String.valueOf(selectedIndex));
        modPartIdTxt.setText(String.valueOf(partId));
        modPartNameTxt.setText(partName);
        modPartPriceTxt.setText(String.valueOf(partPrice));
        modPartInvTxt.setText(String.valueOf(partStock));
        modPartMinTxt.setText(String.valueOf(partMin));
        modPartMaxTxt.setText(String.valueOf(partMax));
        modPartSwitchField.setText(partSwitch);
    }

    /**
     * Toggles the seventh parameter label to Machine ID for In-House parts when the In-House radio button is clicked.
     * @param actionEvent In-House radio button selected
     */
    public void showAddIhPart(ActionEvent actionEvent) {
        switchLabel.setText("Machine ID");
    }

    /**
     * Toggles the seventh parameter label to Company Name for Outsourced parts when the Outsourced radio button is clicked.
     * @param actionEvent Outsourced radio button selected
     */
    public void showAddOsPart(ActionEvent actionEvent) {
        switchLabel.setText("Company Name");
    }

    /**
     * Updates the selected part within the allParts list and returns the user to the Main Screen.
     * @param actionEvent Save button clicked
     * @throws IOException
     */
    public void onSaveMod(ActionEvent actionEvent) throws IOException {
        if (modPartSwitchField.getText().matches("[0-9]+")) {
            int id = Integer.parseInt(modPartIdTxt.getText());
            String name = modPartNameTxt.getText();
            double price = Double.parseDouble(modPartPriceTxt.getText());
            int stock = Integer.parseInt(modPartInvTxt.getText());
            int min = Integer.parseInt(modPartMinTxt.getText());
            int max = Integer.parseInt(modPartMaxTxt.getText());
            int machineId = Integer.parseInt(modPartSwitchField.getText());
            int indexToMod = Integer.parseInt(indexLabel.getText());
            if(min>stock || min>max || stock>max){
                Alert stockInvalid = new Alert(Alert.AlertType.ERROR);
                stockInvalid.setTitle("Invalid entry");
                stockInvalid.setContentText("Please ensure the inventory level falls between the minimum and maximum levels allowed.");
                stockInvalid.showAndWait();
            }
            else{
                InHouse moddedPart = new InHouse(id, name, price, stock, min, max, machineId);
                Inventory.getAllParts().set(indexToMod, moddedPart);
            }
        }
        else {
            int id = Integer.parseInt(modPartIdTxt.getText());
            String name = modPartNameTxt.getText();
            double price = Double.parseDouble(modPartPriceTxt.getText());
            int stock = Integer.parseInt(modPartInvTxt.getText());
            int min = Integer.parseInt(modPartMinTxt.getText());
            int max = Integer.parseInt(modPartMaxTxt.getText());
            String companyName = modPartSwitchField.getText();
            int indexToMod = Integer.parseInt(indexLabel.getText());
            if(min>stock || min>max || stock>max){
                Alert stockInvalid = new Alert(Alert.AlertType.ERROR);
                stockInvalid.setTitle("Invalid entry");
                stockInvalid.setContentText("Please ensure the inventory level falls between the minimum and maximum levels allowed.");
                stockInvalid.showAndWait();
            }
            else {
                Outsourced moddedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                Inventory.getAllParts().set(indexToMod, moddedPart);
                // Back to Main Menu
                Parent root = FXMLLoader.load(getClass().getResource("/view/main-form-view.fxml"));
                root.setStyle("-fx-font-family: 'Arial';");
                Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1100, 475);
                primaryStage.setTitle("Inventory Management");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        }
    }

    /**
     * Returns the user to the Main Screen.
     * @param actionEvent Cancel button clicked
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main-form-view.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100, 475);
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

