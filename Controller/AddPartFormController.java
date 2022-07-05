package Controller;
/**
 *
 * @author Cody Biller
 */
import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/** controls the addPartForm view **/
public class AddPartFormController implements Initializable {
    /** part name **/
    public TextField partName;
    /** part inventory/stock **/
    public TextField partInv;
    /** part cost **/
    public TextField partCost;
    /** maximum amount of part to be held in inventory **/
    public TextField partMax;
    /** minimum amount of part to be held in inventory **/
    public TextField partMin;
    /** hold MachineID or CompanyName depending on radio button state **/
    public TextField partTypeValue;
    /** part form save button **/
    public Button save;
    /** part cancel button **/
    public Button cancel;
    /** inHouse button **/
    public RadioButton inHouseButton;
    /** outsourced button **/
    public RadioButton outsourcedButton;
    /** part type label **/
    public Label partTypeLabel;
    /** part id **/
    static public int partID = 33;


    /** handle logic in case that outsourced radio button is clicked **/
    public void onActionOutsourcedButton(ActionEvent actionEvent) {
        /** if InHouseButton is active, deselect it **/
        if (inHouseButton.isSelected()) {
            inHouseButton.setSelected(false);        /** deselect inHouseButton **/
            outsourcedButton.setSelected(true);      /** select outSourcedButton **/
            partTypeLabel.setText("Company Name");   /** rename partTypeLabel **/
        } else {
            outsourcedButton.setSelected(true);      /** else, maintain selected state on outsourced button **/
        }
    }
    /** handle logic in case that inHouse radio button is clicked **/
    public void onActionInHouseButton(ActionEvent actionEvent) {
        /** if outsourcedButton is active, deselect it **/
        if (outsourcedButton.isSelected()) {
            outsourcedButton.setSelected(false);       /** deselect outsourcedButton **/
            inHouseButton.setSelected(true);           /** select inHouseButton **/
            partTypeLabel.setText("Machine ID");       /** rename partTypeLabel **/
        } else {
            inHouseButton.setSelected(true);           /** else, maintain selected state on inHouseButton **/
        }
    }

    /**
     * Handle Logic for saving parts based on user input in form
     * @param actionEvent
     */
    public void onActionSavePart(ActionEvent actionEvent) {
        /** part to be added **/
        Part partToAdd;
        /** name of part **/
        String name;
        /** company that makes part **/
        String companyName;
        /** part inventory/stock **/
        int inv;
        /** part cost/price **/
        double cost;
        /** part max **/
        int max;
        /** part min **/
        int min;
        /** part machineID **/
        int machineID = 0;
        try {
            name = partName.getText();
            /** check if name field is populated **/
            if (name.length() == 0) {
                /** if name field is blank throw an exception to warn user **/
                throw new Exception("Name field must not be blank");
            }
            try {
                /** attempt to parse user input in inv text field as int **/
                inv = Integer.parseInt(partInv.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Stock field must be an integer");
            }
            try {
                /** attempt to parse user input in price field as double **/
                cost = Double.parseDouble(partCost.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as a double then throw an exception to warn user **/
                throw new Exception("Price field must be in the format of dollars and cents. Ex. 4.30");
            }
            try {
                /** attempt to parse user input in min text field as int **/
                min = Integer.parseInt(partMin.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Min field must be an integer");
            }
            try {
                /** attempt to parse user input in max text field as int **/
                max = Integer.parseInt(partMax.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Max field must be an integer");
            }
            /** throw exception if min > than max **/
            if (min > max) {
                throw new Exception("Min must be less than Max");
            }
            /** throw exception if min less than 0 **/
            if (min < 0 ) {
                throw new Exception("min must be greater than or equal to 0");
            }
            /** throw exception if inv is less than min or inv is greater than max **/
            if (inv < min || inv > max) {
                throw new Exception("Inventory must be greater than or equal to minimum and less than or equal to maximum");
            }
            /** check if part to be added is InHouse or Outsourced based on partTypeLabel **/
            if (partTypeLabel.getText().equals("Machine ID")) {
                try {
                    /** attempt to parse machineID **/
                    machineID = Integer.parseInt(partTypeValue.getText());
                    partToAdd = new InHouse(partID, name, cost, inv, min, max, machineID);
                } catch (Exception e) {
                    /** throw exception if user input for machineID is not parsable to integer **/
                    throw new Exception("MachineID field must be an integer");
                }
            } else {
                companyName = partTypeValue.getText();
                /** check if companyName is blank **/
                if (companyName.length() == 0) {
                    /** throw an exception if company is blank **/
                    throw new Exception("Company Name cannot be blank");
                }
                partToAdd = new Outsourced(partID, name, cost, inv, min, max, companyName);
            }
            /** increment partID to ensure unique ids for future parts **/
            partID++;
            /** add new part **/
            Inventory.addPart(partToAdd);
            /** load main menu **/
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
        } catch (Exception e) {
            displayError(e.getMessage());
        }
    }

    /**
     * Returns to main menu
     * @param actionEvent
     */
    public void onActionCancel(ActionEvent actionEvent) {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Prompts user with dialog box containing error message
     * @param message to be displayed in dialog box
     */
    private void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
