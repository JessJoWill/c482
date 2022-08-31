package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Holds inventory data.
 */
public abstract class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Searches the allParts list for entered text by partName.
     * @param partName the name of the part the user is searching for
     * @return ObservableList of found parts
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                foundParts.add(part);
            }
        }
        return foundParts;
    }

    /**
     * Searches the allParts list for entered text by partId.
     * @param partId the ID of the part the user is searching for
     * @return ObservableList of found parts
     */
    public static ObservableList<Part> lookupPart(int partId) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getId() == partId) {
                foundParts.add(part);
            }
        }
        return foundParts;
    }
    /**
     * Searches the allProducts list for entered text by productName.
     * @param productName the name of the part the user is searching for
     * @return ObservableList of found parts
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    /**
     * Searches the allProducts list for entered text by productId.
     * @param productId the ID of the product the user is searching for
     * @return ObservableList of found products
     */
    public static ObservableList<Product> lookupProduct(int productId) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    /**
     * Adds a new In-house Part to the allParts list.
     * @param newPart the newPart to add
     */
    public static void addIHPart(InHouse newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new Outsourced Part to the allParts list.
     * @param newPart the newPart to add
     */
    public static void addOSPart(Outsourced newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new Product to the allProducts list.
     * @param newProduct the newProduct to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Returns the contents of the allParts list.
     * @return allParts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns the contents of the allProducts list.
     * @return allProducts
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Removes the selected Part from the allParts list.
     * @param selectedPart the part that is currently selected
     * @return true
     */
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    /**
     * Removes the selected Product from the allProducts list.
     * @param selectedProduct the product that is currently selected
     * @return true
     */
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    }
}
