package Controller;
/**
 *
 * @author Cody Biller
 */
import Model.Inventory;
import Model.Part;
import Model.Product;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;



/** controls the modifyProductForm view
 * <p><b>ERROR/LOGIC FIX</b>
 *  * My first iteration of the add and remove part methods involved side effects from removing and adding parts from the Products associated
 *  * parts list directly before any saves were committed. To solve this issue, I introduced a second ObservableArrayList to hold
 *  * temporary changes. So, in the case that there were many changes but the user cancels the modifications, the
 *  * original associatedParts list is preserved.</p>**/
public class ModifyProductFormController implements Initializable {
    /** product id **/
    public TextField productId;
    /** product name **/
    public TextField productName;
    /** product inventory **/
    public TextField productInv;
    /** product cost **/
    public TextField productCost;
    /** maximum amount of part to be held in inventory **/
    public TextField productMax;
    /** minimum amount of part to be held in inventory **/
    public TextField productMin;

    /** table view for associated parts **/
    public TableView associatedPartsTable;
    /** table view for all parts **/
    public TableView allPartsTable;
    /** table column for all parts id **/
    public TableColumn allPartsId;
    /** table column for all parts name **/
    public TableColumn allPartsName;
    /** table column for all parts inventory **/
    public TableColumn allPartsInventory;
    /** table column for all parts cost **/
    public TableColumn allPartsCost;
    /** table column for associated parts id **/
    public TableColumn associatedPartsId;
    /** table column for associated parts name **/
    public TableColumn associatedPartsName;
    /** table column for associated parts inventory **/
    public TableColumn associatedPartsInventory;
    /** table column for associated parts cost **/
    public TableColumn associatedPartsCost;
    /** part search field **/
    public TextField allPartsSearch;

    /** all associatedParts for the product **/
    private ObservableList<Part> associatedParts;
    /** stores all unsaved changes to associatedParts **/
    private ObservableList<Part> tempParts;
    /** index of product in Inventory **/
    private int index;

    /**
     * set up associatedParts and allParts tables
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPartsTable.setItems(Inventory.getAllParts());
        /** populate associatedPartsTable with associatedParts **/
        associatedPartsTable.setItems(associatedParts);

        /** set up initial column cells for allParts **/
        allPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        /** set up initial column cells for associatedParts **/
        associatedPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    /**
     * handle logic for adding part to associated parts of the product
     * @param actionEvent
     */
    public void onActionPartAddButton(ActionEvent actionEvent) {
        Part selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();
        /** notify user if there is no selected part **/
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select a part to add");
            alert.showAndWait();
        } else {
            tempParts.add(selectedPart);
        }
    }

    /**
     * handle logic for removing associated parts from the product
     * @param actionEvent
     */
    public void onActionRemovePartButton(ActionEvent actionEvent) {
        Part selectedPart = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();
        /** notify user if there is no selected part **/
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select an associated part to remove");
            alert.showAndWait();
        } else {
            /** ask user to confirm the deletion of selected part **/
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> option = alert.showAndWait();
            /** if user confirms, delete part **/
            if (option.get() == ButtonType.OK) {
                tempParts.remove(selectedPart);
            }
        }
    }


    /**
     * handle saving modified product to Inventory
     * perform error and logic checking on user input
     * use tempParts to preserve product associatedParts in event of no save
     * after successful save, transition to main menu
     * @param actionEvent
     * @throws Exception
     */
    public void onActionSaveButton(ActionEvent actionEvent) throws Exception {
        /** product to add **/
        Product productToAdd;
        /** product name **/
        String name;
        int id;
        /** product inventory/stock **/
        int inv;
        /** product cost/price **/
        double cost;
        /** product min **/
        int min;
        /** product max **/
        int max;

        try {
            /** parse current products ID **/
            id = Integer.parseInt(productId.getText());
            name = productName.getText();
            /** check if name field is populated **/
            if (name.length() == 0) {
                /** if name field is blank throw an exception to warn user **/
                throw new Exception("Name field must not be blank");
            }
            try {
                /** attempt to parse user input in inv text field as int **/
                inv = Integer.parseInt(productInv.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Stock field must be an integer");
            }
            try {
                /** attempt to parse user input in price field as double **/
                cost = Double.parseDouble(productCost.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as a double then throw an exception to warn user **/
                throw new Exception("Price field must be in the format of dollars and cents. Ex. 4.30");
            }
            try {
                /** attempt to parse user input in min text field as int **/
                min = Integer.parseInt(productMin.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Min field must be an integer");
            }
            try {
                /** attempt to parse user input in max text field as int **/
                max = Integer.parseInt(productMax.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Max field must be an integer");
            }
            /** throw exception if min > than max **/
            if (min > max) {
                throw new Exception("Min must be less than Max");
            }
            /** throw exception if min less than 0 **/
            if (min < 0) {
                throw new Exception("min must be greater than or equal to 0");
            }
            /** throw exception if inv is less than min or inv is greater than max **/
            if (inv < min || inv > max) {
                throw new Exception("Inventory must be greater than or equal to minimum and less than or equal to maximum");
            }
            /** create new product **/
            productToAdd = new Product(id, name, cost, inv, min, max);
            /** add all parts from associated parts to new Product **/
            for (Part part : tempParts) {
                productToAdd.addAssociatedPart(part);
            }
            /** update product **/
            Inventory.updateProduct(index, productToAdd);
            /** return to main menu **/
            try {
                Parent mainMenu = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                Scene scene = new Scene(mainMenu, 900, 500);
                stage.setTitle("Main Menu");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println("Main Menu stage failed to load");
            }
        } catch (Exception e ) {
            displayError(e.getMessage());
        }
    }

    /**
     * returns to main menu view
     * @param actionEvent
     */
    public void onActionCancelButton(ActionEvent actionEvent) {
        try {
            Parent mainMenu = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(mainMenu, 900, 500);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Main Menu stage failed to load");
        }
    }

    /**
     * check if query is numerical, and lookupPart for ID. If query is not numerical, an exception will be thrown from parseInt and caught.
     * in case of exception, lookupPart will be called with a string instead.
     * @param keyEvent triggers search based on query in parts text field
     */
    public void onInputPartSearch(KeyEvent keyEvent) {
        ObservableList<Part> resultParts = FXCollections.observableArrayList();
        try {
            resultParts.add(Inventory.lookupPart(Integer.parseInt(allPartsSearch.getText())));
        } catch (Exception e) {
            resultParts = Inventory.lookupPart(allPartsSearch.getText());
        }
        allPartsTable.setItems(resultParts);
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
     * load all product information into class,
     * utilized by main menu to pass information to modify product view
     * @param product to be modified
     * @param index of product in Inventory
     */
    public void loadProduct(Product product, int index) {
        this.associatedParts = product.getAllAssociatedParts();
        this.tempParts = FXCollections.observableArrayList(associatedParts);
        this.productId.setText(Integer.toString(product.getId()));
        this.productName.setText(product.getName());
        this.productInv.setText(Integer.toString(product.getStock()));
        this.productCost.setText(Double.toString(product.getPrice()));
        this.productMin.setText(Integer.toString(product.getMin()));
        this.productMax.setText(Integer.toString(product.getMax()));
        this.index = index;
        this.associatedPartsTable.setItems(tempParts);
    }
}
