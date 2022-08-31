package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.Inventory.lookupPart;

/**
 * Displays and controls the Modify Product Screen.
 */
public class ModProdController implements Initializable {
    public TextField modProductIdTxt;
    public TextField modProductNameTxt;
    public TextField modProductInvTxt;
    public TextField modProductPriceTxt;
    public TextField modProductMaxTxt;
    public TextField modProductMinTxt;
    public Label indexLabel;
    public TableView selectedPartsTable;
    public TableView allPartsTable;
    public TableColumn allPartsPartId;
    public TableColumn allPartsPartName;
    public TableColumn allPartsInventoryLevel;
    public TableColumn allPartsPrice;
    public TableColumn selectedPartsPartId;
    public TableColumn selectedPartsPartName;
    public TableColumn selectedPartInventoryLevel;
    public TableColumn selectedPartsPrice;
    public Label modProdSearchNoResultsLbl;
    public TextField modProdSearchTxt;

    private int indexToMod;
    private Part selectedPart;
    Product moddedProduct = Inventory.getAllProducts().get(indexToMod);

    /**
     * Returns the user to the Main Screen.
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

    /**
     * Populates allPartsTable and selectedPartsTable with the appropriate data and adds search functionality to the allPartsTable.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPartsTable.setItems(Inventory.getAllParts());
        selectedPartsTable.setItems(moddedProduct.getAllAssociatedParts());
        
        //Populating the table views
        allPartsPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        selectedPartsPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        selectedPartsPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        selectedPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        selectedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        modProdSearchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!(newValue.isEmpty())) {
                String t = modProdSearchTxt.getText();
                ObservableList<Part> partsList = lookupPart(t);

                if(partsList.size() == 0) {
                    try {
                        modProdSearchNoResultsLbl.setText("No results found.");
                        int partId = Integer.parseInt(t);
                        partsList = lookupPart(partId);
                        if(partsList.size() != 0) {
                            allPartsTable.setItems(partsList);
                            modProdSearchNoResultsLbl.setText("");
                        }
                    }
                    catch (NumberFormatException e){
                        allPartsTable.setItems(partsList);
                    }
                }
                else{
                    allPartsTable.setItems(partsList);
                }
            }
            else{
                allPartsTable.setItems(Inventory.getAllParts());
                modProdSearchNoResultsLbl.setText("");
            }
        });
    }

    /**
     * Places the parameter values from the selected product into the text fields of the Modify Product screen.
     * @param selectedIndex the index of the selected Product
     * @param productId the ID of the selected Product
     * @param productName the name of the selected Product
     * @param productPrice the price of the selected Product
     * @param productStock the inventory level of the selected Product
     * @param productMin the minimum inventory level of the selected Product
     * @param productMax the maximum inventory level of the selected Product
     */
    public void fillForm(int selectedIndex, int productId, String productName, double productPrice, int productStock, int productMin, int productMax) {
        indexLabel.setText(String.valueOf(selectedIndex));
        modProductIdTxt.setText(String.valueOf(productId));
        modProductNameTxt.setText(productName);
        modProductPriceTxt.setText(String.valueOf(productPrice));
        modProductInvTxt.setText(String.valueOf(productStock));
        modProductMinTxt.setText(String.valueOf(productMin));
        modProductMaxTxt.setText(String.valueOf(productMax));
    }

    /**
     * Saves the updated Product to the allProducts list and returns the user to the Main Screen.
     * @param actionEvent Save button clicked
     * @throws IOException
     */
    public void onSaveProdMod(ActionEvent actionEvent) throws IOException {
        int id = Integer.parseInt(modProductIdTxt.getText());
        moddedProduct.setId(id);
        String name = modProductNameTxt.getText();
        moddedProduct.setName(name);
        double price = Double.parseDouble(modProductPriceTxt.getText());
        moddedProduct.setPrice(price);
        int stock = Integer.parseInt(modProductInvTxt.getText());
        moddedProduct.setStock(stock);
        int min = Integer.parseInt(modProductMinTxt.getText());
        moddedProduct.setMin(min);
        int max = Integer.parseInt(modProductMaxTxt.getText());
        moddedProduct.setMax(max);
        if(min>stock || min>max || stock>max){
            Alert stockInvalid = new Alert(Alert.AlertType.ERROR);
            stockInvalid.setTitle("Invalid entry");
            stockInvalid.setContentText("Please ensure the inventory level falls between the minimum and maximum levels allowed.");
            stockInvalid.showAndWait();
        }
        else{
            indexToMod = Integer.parseInt(indexLabel.getText());
            ObservableList<Part> associatedParts = moddedProduct.getAllAssociatedParts();
            moddedProduct.setAssociatedParts(associatedParts);
            Inventory.getAllProducts().set(indexToMod, moddedProduct);
            //Back to Main Menu
            Parent root = FXMLLoader.load(getClass().getResource("/view/main-form-view.fxml"));
            root.setStyle("-fx-font-family: 'Arial';");
            Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1100, 475);
            primaryStage.setTitle("Inventory Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Adds the selected part to the associatedParts list for the current Product, with confirmation from the user.
     * @param actionEvent Add button clicked
     */
    public void onAddSelectedPart(ActionEvent actionEvent) {
        selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();
        Alert confirmRemove = new Alert(Alert.AlertType.CONFIRMATION);
        confirmRemove.setTitle("Confirm Part Add");
        confirmRemove.setContentText("Are you sure you want to add " + selectedPart.getName() + "?");
        confirmRemove.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                moddedProduct.addAssociatedPart(selectedPart);
            }
        });
    }

    /**
     * Removes the selected part from the associatedParts list for the current Product, with confirmation from the user.
     * @param actionEvent Remove Associated Part button clicked
     */
    public void onRemoveSelectedPart(ActionEvent actionEvent) {
        selectedPart = (Part) selectedPartsTable.getSelectionModel().getSelectedItem();
        Alert confirmRemove = new Alert(Alert.AlertType.CONFIRMATION);
        confirmRemove.setTitle("Confirm Part Removal");
        confirmRemove.setContentText("Are you sure you want to remove " + selectedPart.getName() + "?");
        confirmRemove.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                moddedProduct.deleteAssociatedPart(selectedPart);
            }
        });
    }
}
