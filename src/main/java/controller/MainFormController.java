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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.application.Platform.exit;
import static model.Inventory.*;


/**
 * Displays and controls the Main Menu screen
 * @author Jessica Williams
 */
public class MainFormController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TableView mainPartsTable;
    public TableView mainProductsTable;
    public TableColumn mainPartsPartIdCol;
    public TableColumn mainPartsPartNameCol;
    public TableColumn mainPartsInventoryCol;
    public TableColumn mainPartsPriceCol;
    public TableColumn mainProdPartIdCol;
    public TableColumn mainProdPartNameCol;
    public TableColumn mainProdInventoryCol;
    public TableColumn mainProdPriceCol;
    public TextField mainPartSearchTxt;
    public TextField mainProductSearchTxt;
    public Label mainPartSearchNoResultsLbl;
    public Label mainProdSearchNoResultsLbl;
    public Part selectedPart;
    public Product selectedProduct;
    public int selectedIndex;

    private static boolean notLoaded = true; // helps ensure test data is only loaded once

    /**
     * Includes adding test data and search features.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Loading test data
        addTestData();

        mainPartsTable.setItems(Inventory.getAllParts());
        mainProductsTable.setItems(Inventory.getAllProducts());

        // Populating the table views
        mainPartsPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartsPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainProdPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProdPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProdInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProdPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Search for Main Parts Table, listens for input in the mainPartSearchTxt field
        mainPartSearchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!(newValue.isEmpty())) {
                String t = mainPartSearchTxt.getText();
                ObservableList<Part> partsList = lookupPart(t);

                if(partsList.size() == 0) {
                    try {
                        mainPartSearchNoResultsLbl.setText("No results found.");
                        int partId = Integer.parseInt(t);
                        partsList = lookupPart(partId);
                        if(partsList.size() != 0) {
                            mainPartsTable.setItems(partsList);
                            mainPartSearchNoResultsLbl.setText("");
                        }
                    }
                    catch (NumberFormatException e){
                        mainPartsTable.setItems(partsList);
                    }
                }
                else{
                    mainPartsTable.setItems(partsList);
                }
            }
            else{
                mainPartsTable.setItems(Inventory.getAllParts());
                mainPartSearchNoResultsLbl.setText("");
            }
        });

        // Search for Main Products table
        mainProductSearchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!(newValue.isEmpty())) {
                String t = mainProductSearchTxt.getText();
                ObservableList<Product> productsList = lookupProduct(t);

                if(productsList.size() == 0) {
                    try {
                        mainProdSearchNoResultsLbl.setText("No results found.");
                        int productId = Integer.parseInt(t);
                        productsList = lookupProduct(productId);
                        if(productsList.size() != 0) {
                            mainProductsTable.setItems(productsList);
                            mainProdSearchNoResultsLbl.setText("");
                        }
                        if(productsList != null) {
                            mainProductsTable.setItems(productsList);
                        }
                    }
                    catch (NumberFormatException e){
                        mainProductsTable.setItems(productsList);
                    }
                }
                else{
                    mainProductsTable.setItems(productsList);
                }
            }
            else{
                mainProductsTable.setItems(Inventory.getAllProducts());
                mainProdSearchNoResultsLbl.setText("");
            }
        });
    }

    /**
     * Populates the allParts list with pre-coded items for testing purposes.
     */
    public void addTestData(){
        // Checking to see if test data has already been loaded
        if(!notLoaded){
            return;
        }
        notLoaded = false;

        // Creating test data
        InHouse part1 = new InHouse(1, "Brakes", 10, 15, 1, 20, 123 );
        Inventory.addIHPart(part1);
        Outsourced part2 = new Outsourced(2, "Wheel", 16, 8, 1, 15, "Wheel Depot" );
        Inventory.addOSPart(part2);
        Outsourced part3 = new Outsourced(3, "Seat", 11, 10, 1, 10, "All The Seats" );
        Inventory.addOSPart(part3);
    }

    /**
     * Opens the Add Part Screen, defaulting to the In-House Part radio button and seventh parameter.
     * @param actionEvent
     * @throws IOException
     */
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/add-part-view.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Opens the Modify Part Screen, choosing the appropriate subclass radio button and seventh parameter based on the selected part.
     * @param actionEvent Modify button click
     * @throws IOException
     */
    public void onModifyPart(ActionEvent actionEvent) throws IOException {
        selectedIndex = mainPartsTable.getSelectionModel().getSelectedIndex();
        try {
            if (mainPartsTable.getSelectionModel().getSelectedItem() instanceof InHouse) { // if the selected part is In-house
                selectedPart = (InHouse) mainPartsTable.getSelectionModel().getSelectedItem();
                int partId = selectedPart.getId();
                String partName = selectedPart.getName();
                double partPrice = selectedPart.getPrice();
                int partStock = selectedPart.getStock();
                int partMin = selectedPart.getMin();
                int partMax = selectedPart.getMax();
                int inputPartSwitch = ((InHouse) selectedPart).getMachineId();
                String partSwitch = String.valueOf(inputPartSwitch);
                String type = "IH";
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mod-part-view.fxml"));
                root = loader.load();

                ModPartController modpartcontroller = loader.getController();
                modpartcontroller.fillForm(selectedIndex, partId, partName, partPrice, partStock, partMin, partMax, type, partSwitch);
            }
            if (mainPartsTable.getSelectionModel().getSelectedItem() instanceof Outsourced) { // if the selected part is Outsourced
                selectedPart = (Outsourced) mainPartsTable.getSelectionModel().getSelectedItem();
                int partId = selectedPart.getId();
                String partName = selectedPart.getName();
                double partPrice = selectedPart.getPrice();
                int partStock = selectedPart.getStock();
                int partMin = selectedPart.getMin();
                int partMax = selectedPart.getMax();
                String partSwitch = ((Outsourced) selectedPart).getCompanyName();
                String type = "OS";
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mod-part-view.fxml"));
                root = loader.load();

                ModPartController modpartcontroller = loader.getController();
                modpartcontroller.fillForm(selectedIndex, partId, partName, partPrice, partStock, partMin, partMax, type, partSwitch);
            }
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("Invalid Selection");
            noSelection.setContentText("Please select a part from the table before clicking Modify.");
            noSelection.showAndWait();
        }
    }

     /**
     * Opens the Add Product Screen.
     * @param actionEvent Add button clicked
     * @throws IOException
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/add-product-view.fxml"));
        root.setStyle("-fx-font-family: 'Arial';");
        Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 645);
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Opens the Modify Product Screen.
     * @param actionEvent Modify button clicked
     * @throws IOException
     */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        try {
            selectedIndex = mainProductsTable.getSelectionModel().getSelectedIndex();
            selectedProduct = (Product) mainProductsTable.getSelectionModel().getSelectedItem();
            int productId = selectedProduct.getId();
            String productName = selectedProduct.getName();
            double productPrice = selectedProduct.getPrice();
            int productStock = selectedProduct.getStock();
            int productMin = selectedProduct.getMin();
            int productMax = selectedProduct.getMax();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mod-product-view.fxml"));
            root = loader.load();

            ModProdController modprodcontroller = loader.getController();
            modprodcontroller.fillForm(selectedIndex, productId, productName, productPrice, productStock, productMin, productMax);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("Invalid Selection");
            noSelection.setContentText("Please select a product from the table before clicking Modify.");
            noSelection.showAndWait();
        }
    }

    /**
     * Checks to see if a part is selected, then confirms with the user and calls <code>deletePart</code> to remove the selected part from the <code>allParts</code> list.
     * @param actionEvent Delete button on the Parts side of the Main Screen
     */
    public void onDeletePart(ActionEvent actionEvent) {
        Part selectedPart = (Part) mainPartsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("Delete Error");
            noSelection.setContentText("You have not selected a part to delete.");
            noSelection.showAndWait();
        }
        else {
            Alert delPartAlert = new Alert(Alert.AlertType.CONFIRMATION);
            delPartAlert.setTitle("Confirm Delete");
            delPartAlert.setContentText("Are you sure you want to delete " + selectedPart.getName() + "?");
            delPartAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Inventory.deletePart(selectedPart);
                }
            });
        }
    }

    /**
     * Checks to see if a product is selected, confirms with the user, and calls <code>deleteProduct</code> to remove the selected product from the <code>allProducts</code> list.
     * @param actionEvent Delete button clicked
     */
    public void onDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = (Product) mainProductsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("Unable to Delete");
            noSelection.setContentText("You have not selected a product to delete.");
            noSelection.showAndWait();
        }
       else if(!(selectedProduct.getAllAssociatedParts()).isEmpty()) {
           Alert stillHasParts = new Alert(Alert.AlertType.ERROR);
           stillHasParts.setTitle("Unable to Delete");
           stillHasParts.setContentText("Please remove all associated parts before deleting product.");
           stillHasParts.showAndWait();
        }
       else{
            Alert delProdAlert = new Alert(Alert.AlertType.CONFIRMATION);
            delProdAlert.setTitle("Confirm Delete");
            delProdAlert.setContentText("Are you sure you want to delete " + selectedProduct.getName() + "?");
            delProdAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Inventory.deleteProduct(selectedProduct);
                }
            });
       }
    }

    /**
     * Allows a user to confirm their intent to exit, and terminates the program if appropriate.
     * @param actionEvent Exit button clicked
     * @throws IOException
     */
    public void onExit(ActionEvent actionEvent) throws IOException {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Confirm Exit");
        exitAlert.setContentText("Are you sure you want to exit?");
        exitAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                exit();
            }
        });
    }
}