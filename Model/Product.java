package Model;
/**
 *
 * @author Cody Biller
 */
import Model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** represents a product with associated parts **/
public class Product {
    /** Observable list of all parts associated to the product **/
    private ObservableList<Part> associatedParts;
    /** product id **/
    private int id;
    /** product name **/
    private String name;
    /** product price **/
    private double price;
    /** product stock **/
    private int stock;
    /** product min **/
    private int min;
    /** product max **/
    private int max;

    /**
     *
     * @param id product id
     * @param name product name
     * @param price product price
     * @param stock product stock
     * @param min minimum amount of product to hold in inventory
     * @param max maximum amount of product to hold in inventory
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.stock = stock;
        this.name = name;
        this.price = price;
        this.min = min;
        this.max = max;
        this.associatedParts = FXCollections.observableArrayList();
    }

    /**
     * @param id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /** @param name the name to set **/
    public void setName(String name) {
        this.name = name;
    }
    /** @param price the price to set **/
    public void setPrice(double price) {
        this.price = price;
    }
    /** @param stock the stock to set **/
    public void setStock(int stock) {
        this.stock = stock;
    }
    /** @param min the min to set **/
    public void setMin(int min) {
        this.min = min;
    }
    /** @param max the max to set **/
    public void setMax(int max) {
        this.max = max;
    }

    /** @return the id **/
    public int getId() {
        return id;
    }
    /** @return the name **/
    public String getName() {
        return name;
    }
    /** @return the price **/
    public double getPrice() {
        return price;
    }
    /** @return the stock **/
    public int getStock() {
        return stock;
    }
    /** @return the min **/
    public int getMin() {
        return min;
    }
    /** @return the max **/
    public int getMax() {
        return max;
    }
    /**
     * @param part part to add to associatedParts
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    /**
     * @param selectedAssociatedPart part to remove from associatedParts
     * @return true if associated part exists and is remove, else returns false
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }
    /**
     * @return observableList of type parts associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
