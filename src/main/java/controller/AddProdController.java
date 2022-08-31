package controller;

import javafx.collections.FXCollections;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.Inventory.*;

/**
 * Displays and controls the Add Product Screen.
 */
public class AddProdController implements Initializable {
    public TableView allPartsTable;
    public TableColumn allPartsPartId;
    public TableColumn allPartsPartName;
    public TableColumn allPartsInventoryLevel;
    public TableColumn allPartsPrice;
    public Button addSelectedPart;
    public TableView selectedPartsTable;
    public TableColumn selectedPartsPartId;
    public TableColumn selectedPartsPartName;
    public TableColumn selectedPartInventoryLevel;
    public TableColumn selectedPartsPrice;
    public Button removeSelectedPart;
    public TextField addProductIdTxt;
    public TextField addProductNameTxt;
    public TextField addProductInvTxt;
    public TextField addProductPriceTxt;
    public TextField addProductMaxTxt;
    public TextField addProductMinTxt;
    public TextField addProductSearchTxt;
    public Button cancelButton;
    public Label addProdSearchNoResultsLbl;
    private int id;
    private String name = "";
    private double price = 0.00;
    private int stock = 0;
    private int min = 0;
    private int max = 0;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Product currentProduct = new Product(id, name, price, stock, min, max, associatedParts);

    /**
     * Generates a unique Product ID for new Products.
     * @return newProductId
     */
    public int newProdIdNumber(){
        int lastProduct = Inventory.getAllProducts().size();
        int newProductId = lastProduct + 1001;
        while (Inventory.getAllProducts().contains(newProductId)){
            newProductId++;
        }
        return newProductId;
    }

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
     * Adds the selected part to the <code>associatedParts</code> list for the current Product.
     * @param actionEvent Add button clicked
     */
    public void onAddSelectedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();
        currentProduct.addAssociatedPart(selectedPart);
    }

    /**
     * Removes the selected part from the <code>associatedParts</code> list for the current Product.
     * @param actionEvent Remove Associated Part button clicked
     */
    public void onRemoveSelectedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) selectedPartsTable.getSelectionModel().getSelectedItem();
        currentProduct.deleteAssociatedPart(selectedPart);
    }

    /**
     * Displays a new Product ID in the Product ID text field, populates the <code>allPartsTable</code> table and the <code>selectedPartsTable</code> with the appropriate data, and adds search functionality to the <code>allPartsTable</code>.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newProdIdNumber();
        addProductIdTxt.setText(String.valueOf(newProdIdNumber()));

        allPartsTable.setItems(Inventory.getAllParts());
        selectedPartsTable.setItems(currentProduct.getAllAssociatedParts());

        // Populating the table views
        allPartsPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        selectedPartsPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        selectedPartsPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        selectedPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        selectedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductSearchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!(newValue.isEmpty())) {
                String t = addProductSearchTxt.getText();
                ObservableList<Part> partsList = lookupPart(t);

                if(partsList.size() == 0) {
                    try {
                        addProdSearchNoResultsLbl.setText("No results found.");
                        int partId = Integer.parseInt(t);
                        partsList = lookupPart(partId);
                        if(partsList.size() != 0) {
                            allPartsTable.setItems(partsList);
                            addProdSearchNoResultsLbl.setText("");
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
                addProdSearchNoResultsLbl.setText("");
            }
        });
    }

    /**
     * Adds a new Product to the <code>allProducts</code> list with the information provided by the user.
     * @param actionEvent Save button clicked
     */
    public void onProductAddSave(ActionEvent actionEvent) {
        try {
            id = Integer.parseInt(addProductIdTxt.getText());
            currentProduct.setId(id);
            name = addProductNameTxt.getText();
            currentProduct.setName(name);
            price = Double.parseDouble(addProductPriceTxt.getText());
            currentProduct.setPrice(price);
            stock = Integer.parseInt(addProductInvTxt.getText());
            currentProduct.setStock(stock);
            min = Integer.parseInt(addProductMinTxt.getText());
            currentProduct.setMin(min);
            max = Integer.parseInt(addProductMaxTxt.getText());
            currentProduct.setMax(max);
            if(min>stock || min>max || stock>max){
                Alert stockInvalid = new Alert(Alert.AlertType.ERROR);
                stockInvalid.setTitle("Invalid entry");
                stockInvalid.setContentText("Please ensure the inventory level falls between the minimum and maximum levels allowed.");
                stockInvalid.showAndWait();
            }
            else{
                associatedParts = currentProduct.getAllAssociatedParts();
                addProduct(currentProduct);
                cancelButton.fire();
            }
        }
        catch (NumberFormatException e){
            Alert invalidInfo = new Alert(Alert.AlertType.ERROR);
            invalidInfo.setTitle("Invalid Submission");
            invalidInfo.setContentText("All fields require numeric entries except Name.");
            invalidInfo.showAndWait();
        }
    }
}
