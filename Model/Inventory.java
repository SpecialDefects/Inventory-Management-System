package Model;
/**
 *
 * @author Cody Biller
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** represents an Inventory of products and parts **/
public class Inventory {
    /** ObservableList of all parts currently in the Inventory **/
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /** ObservableList of all products currently in Inventory **/
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart Part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @param newProduct Product to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * @param partId to look up
     * @return Part
     * @throws Exception if part is not found
     */
    public static Part lookupPart(int partId) throws Exception {
        for(Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        throw new Exception("Missing Part");
    }
    /**
     * @param name to be searched for
     * @return observableList of Products whose names contain a full or partial match of name param
     */
    public static ObservableList<Part> lookupPart(String name) {
        ObservableList<Part> results = FXCollections.observableArrayList();
        for(Part part: allParts) {
            if (part.getName().contains(name)) {
                results.add(part);
            }
        }
        return results;
    }
    /**
     * @param productId Product to look up
     * @return Product
     * @throws Exception if product is not found
     */
    public static Product lookupProduct(int productId) throws Exception {
        for(Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        throw new Exception("Missing Product");
    }
    /**
     * @param name to be searched for
     * @return observableList of Products whose names contain a full or partial match of name param
     */
    public static ObservableList<Product> lookupProduct(String name) {
        ObservableList<Product> results = FXCollections.observableArrayList();
        for(Product product: allProducts) {
            if (product.getName().contains(name)) {
                results.add(product);
            }
        }
        return results;
    }
    /**
     * @param index of Part to update
     * @param selectedPart Updated Part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.remove(index);
        allParts.add(index, selectedPart);
    }

    /**
     * @param index of Product to update
     * @param selectedProduct Updated Product
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.remove(index);
        allProducts.add(index, selectedProduct);
    }

    /**
     * @param selectedPart to delete
     * @return true if Part is deleted, false if it does not exist
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * @param selectedProduct to delete
     * @return true if Product is deleted, false if it does not exist
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return list of all Parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return list of all Products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
