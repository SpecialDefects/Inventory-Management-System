package Controller;
/**
 *
 * @author Cody Biller
 */
import Model.InHouse;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


 /**
  * controls the mainMenu view
  *
  * <p><b>FUTURE ENHANCEMENT</b>
  * A useful feature that would extend the inventory management systems functionality, would be the ability
  * to view products for which a part is associated. This could be implemented as a simple dialog box that would pop up upon double clicking a part
  * that already exists within the parts table view. The Dialog box should present a short list of a products name alongside the number of the selected parts that it requires.
  *</p>
  * <p>The code for the desired functionality exists in the commented out function onActionPartAssociation at the bottom of the MainMenuController class(CODE NOT THOROUGHLY TESTED)</p>
**/
public class MainMenuController implements Initializable {

    /** table view for parts **/
    public TableView partsTable;
    /** table column for partID **/
    public TableColumn partId;
    /** table column for part name **/
    public TableColumn partName;
    /** table column for part inventory/stock **/
    public TableColumn partInventory;
    /** table column for part cost **/
    public TableColumn partCost;
    /** table view for products **/
    public TableView productsTable;
    /** table column for productId **/
    public TableColumn productId;
    /** table column for product name **/
    public TableColumn productName;
    /** table column product inventory/stock **/
    public TableColumn productInventory;
    /** table column for product cost **/
    public TableColumn productCost;
    /** text field for part searching **/
    public TextField partSearch;
    /** text field for product search **/
    public TextField productSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /** set up initial table views for parts and products **/
        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProducts());

        /** set up initial column cells for parts **/
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        /** set up initial column cells for products **/
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Opens the add product view
     * @param actionEvent
     * @throws IOException
     */
    public void onActionAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/AddProductForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * handle modify product view transition
     * send data from product that is to be modified to modify product view for use
     * @param actionEvent
     */
    public void onActionModifyProduct(ActionEvent actionEvent) {
        /** retrieve and cast selected product **/
        Product productToModify = (Product) productsTable.getSelectionModel().getSelectedItem();
        /** notify user if there is no selected product **/
        if (productToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select a product to modify");
            alert.showAndWait();
        }
        /** store index of current selected part **/
        int index = productsTable.getSelectionModel().getSelectedIndex();
        /** load modify part view **/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyProductForm.fxml"));
            Parent root = loader.load();
            ModifyProductFormController controller = loader.getController();
            /** pass part and index to modify controller **/
            controller.loadProduct(productToModify, index);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 1000, 625);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Failed to load Modify Part Form");
        }
    }

    /**
     * handle deletion of product,
     * product selected must not have any associated parts in order to be successfully deleted
     * @param actionEvent
     */
    public void onActionDeleteProduct(ActionEvent actionEvent) {
        /** retrieve and cast selected product **/
        try {
            Product productToDelete = (Product) productsTable.getSelectionModel().getSelectedItem();
            if (productToDelete == null) {
                throw new Exception ("You must select a product to delete");
            }
            int partsAssociated = productToDelete.getAllAssociatedParts().size();
            if (partsAssociated > 0) {
                throw new Exception("You cannot delete a Product with associated parts\nRemove all associated parts by modifying the product");
            }
            /** ask user to confirm the deletion of selected product **/
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> option = alert.showAndWait();
            /** if user confirms, delete part **/
            if (option.get() == ButtonType.OK) {
                Inventory.deleteProduct(productToDelete);
            }
        } catch (Exception e) {
            displayError(e.getMessage());
        }
    }

    /**
     * check if query is numerical, and lookupPart for ID. If query is not numerical, an exception will be thrown from parseInt and caught.
     * in case of exception, lookupPart will be called with a string instead.
     * @param inputMethodEvent triggers search based on query in parts text field
     */
    public void onInputPartSearch(KeyEvent inputMethodEvent) {
        ObservableList<Part> resultParts = FXCollections.observableArrayList();
        try {
            resultParts.add(Inventory.lookupPart(Integer.parseInt(partSearch.getText())));
        } catch (Exception e) {
            resultParts = Inventory.lookupPart(partSearch.getText());
        }
        partsTable.setItems(resultParts);
    }

    /**
     * retrieve selected part from part table view, load the modify part view,
     * use loadPart within modifyPartController to pass part information to the view,
     * if no part is selected notify user with dialog
     * @param actionEvent
     */
    public void onActionModifyPart(ActionEvent actionEvent) {
        /** retrieve and cast selected part **/
        Part partToModify = (Part) partsTable.getSelectionModel().getSelectedItem();
        /** notify user if there is no selected part **/
        if (partToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select a part to modify");
            alert.showAndWait();
        }
        /** store index of current selected part **/
        int index = partsTable.getSelectionModel().getSelectedIndex();
        /** load modify part view **/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyPartForm.fxml"));
            Parent root = loader.load();
            ModifyPartFormController controller = loader.getController();
            /** pass part and index to modify controller **/
            controller.loadPart(partToModify, index);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 1000, 600);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Failed to load Modify Part Form");
        }
    }

     /** handle logic for part deletion
     * @param actionEvent
     */
    public void onActionDeletePart(ActionEvent actionEvent) {
        Part partToDelete = (Part) partsTable.getSelectionModel().getSelectedItem();
        /** inform user via dialog if there is no part selected to delete **/
        if (partToDelete == null) {
            displayError("You must select a part to delete");
        } else {
            /** ask user to confirm the deletion of selected part **/
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> option = alert.showAndWait();
            /** if user confirms, delete part **/
            if (option.get() == ButtonType.OK) {
                Inventory.deletePart(partToDelete);
            }
        }
    }

    /**
     * handle transition to add part view
     * @param actionEvent
     * @throws IOException
     */
    public void onActionAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/AddPartForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Check if query is numerical, and lookupProduct for ID. If query is not numerical, an exception will be thrown from parseInt and caught.
     * In case of exception, lookupProduct will be called with a string instead.
     * @param inputMethodEvent triggers search based on query in products text field
     */
    public void onInputProductSearch(KeyEvent inputMethodEvent) {
        ObservableList<Product> resultProducts = FXCollections.observableArrayList();
        try {
            resultProducts.add(Inventory.lookupProduct(Integer.parseInt(productSearch.getText())));
        } catch (Exception e) {
            resultProducts = Inventory.lookupProduct(productSearch.getText());
        }
        productsTable.setItems(resultProducts);
    }

    /**
     * present user with dialog confirmation,
     * if confirmed program will terminate
     * @param event
     */
    public void onActionExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Confirm program termination");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Prompts user with dialog box containing error message
     * @param message to be displayed in dialog box
     */
    private void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);

        alert.showAndWait();
    }

     /**
      public void onActionPartAssociation(ActionEvent event) {
        Part partToView = (Part) partsTable.getSelectionModel().getSelectedItem();
        ObservableList<Product> productList = Inventory.getAllProducts();
        String results = "";
        if (partToView == null){
            displayError("You must select a part to view it's associated products");
        }
        for (Product product : productList) {
            ObservableList<Part> parts = product.getAllAssociatedParts();
            int count = 0;
            for (Part part : parts) {
                if (part.getId() == partToView.getId()) {
                    count++;
                }
            }
            if (count > 0) {
                results = results + (product.getName() + " " + count + "\n");
            }
            count = 0;
        }
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Part Name: " + partToView.getName() + " ID:" + partToView.getId());
        alert.setContentText(results);
        alert.showAndWait();
        }
      **/
}
