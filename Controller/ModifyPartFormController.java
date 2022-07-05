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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** controls the modifyPartForm view **/
public class ModifyPartFormController implements Initializable {
    /** cancel button **/
    public Button cancel;
    /** save button **/
    public Button save;
    /** inHouse button **/
    public RadioButton inHouseButton;
    /** outsourced button **/
    public RadioButton outsourcedButton;
    /** part id**/
    public TextField partID;
    /** part name **/
    public TextField partName;
    /** part price **/
    public TextField partPrice;
    /** part inventory/stock **/
    public TextField partInv;
    /** minimum amount of part to be held in inventory **/
    public TextField partMin;
    /** maximum amount of part to be held in inventory **/
    public TextField partMax;
    /** hold MachineID or CompanyName depending on radio button state **/
    public TextField partTypeValue;
    /** part type label **/
    public Label partTypeLabel;
    /** index of modified part in Inventory **/
    private int index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify View initiated");
    }

    /** handle logic in case that outsourced radio button is clicked **/
    public void onActionOutsourcedButton(ActionEvent actionEvent) throws IOException {
        /** if InHouseButton is active, deselect it **/
        if (inHouseButton.isSelected()) {
            inHouseButton.setSelected(false);           /** deselect inHouseButton **/
            outsourcedButton.setSelected(true);         /** select outSourcedButton **/
            partTypeLabel.setText("Company Name");      /** rename partTypeLabel **/
            partTypeValue.clear();                      /** clear partTypeValue of previous input **/
        } else {
            outsourcedButton.setSelected(true);         /** else, maintain selected state on outsourced button **/
        }
    }

    /** handle logic in case that inHouse radio button is clicked **/
    public void onActionInHouseButton(ActionEvent actionEvent) {
        /** if InHouseButton is active, deselect it **/
        if (outsourcedButton.isSelected()) {
            outsourcedButton.setSelected(false);     /** deselect outsourcedButton **/
            inHouseButton.setSelected(true);         /** select inHouseButton **/
            partTypeLabel.setText("Machine ID");     /** rename partTypeLabel **/
            partTypeValue.clear();                   /** clear partTypeValue of previous input **/
        } else {
            inHouseButton.setSelected(true);         /** else, maintain selected state on inHouse button **/
        }
    }

    /** helper function to pass part data from main menu to ModifyPart **/
    /**
     *
     * @param part to be loaded into modify part view
     * @param index of part in Inventory
     */
    public void loadPart(Part part, int index) {
        this.index = index; /** hold index for inventory reference **/

        /** populate form text fields with information from part to be modified **/
        partID.setText(Integer.toString(part.getId()));
        partName.setText(part.getName());
        partPrice.setText(Double.toString(part.getPrice()));
        partInv.setText(Integer.toString(part.getStock()));
        partMin.setText(Integer.toString(part.getMin()));
        partMax.setText(Integer.toString(part.getMax()));

        /** if part is an InHouse part, set InHouse to selected and partTypeLabel to Machine ID **/
        if (part instanceof InHouse) {
            partTypeValue.setText(Integer.toString(((InHouse) part).getMachineId()));
            inHouseButton.setSelected(true);
            outsourcedButton.setSelected(false);
            partTypeLabel.setText("Machine ID");
        } else {
            /** if part is not an InHouse part, then it is an Outsourced Part
             * set outsourcedButton to selected and partTypeLabel to companyName
             */
            partTypeValue.setText(((Outsourced) part).getCompanyName());
            inHouseButton.setSelected(false);
            outsourcedButton.setSelected(true);
            partTypeLabel.setText("Company Name");
        }
    }

    /**
     * handle saving of modified part
     * @param actionEvent
     */
    public void onActionSave(ActionEvent actionEvent) {
        String modifiedName;        /** holds modified part name **/
        String modifiedTypeValue;   /** modified part type value **/
        double modifiedPrice;       /** modified part price **/
        int modifiedInv;            /** modified part stock/inventory **/
        int modifiedMin;            /** modified part minimum **/
        int modifiedMax;            /** modified part maximum **/

        try {
            modifiedName = partName.getText();
            /** check if name field is populated **/
            if (modifiedName.length() < 1) {
                /** if name field is blank throw an exception to warn user **/
                throw new Exception("Name field must not be blank");
            }
            try {
                /** attempt to parse user input in inv text field as int **/
                modifiedInv = Integer.parseInt(partInv.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Stock field must be an integer");
            }
            try {
                /** attempt to parse user input in price field as double **/
                modifiedPrice = Double.parseDouble(partPrice.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as a double then throw an exception to warn user **/
                throw new Exception("Price field must be in the format of dollars and cents. Ex. 4.30");
            }
            try {
                /** attempt to parse user input in min text field as int **/
                modifiedMin = Integer.parseInt(partMin.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Min field must be an integer");
            }
            try {
                /** attempt to parse user input in max text field as int **/
                modifiedMax = Integer.parseInt(partMax.getText());
            } catch (Exception e) {
                /** if user input cannot be parsed as an int then throw an exception to warn user **/
                throw new Exception("Max field must be an integer");
            }
            /** throw exception if min > than max **/
            if (modifiedMin > modifiedMax) {
                throw new Exception("Min must be less than Max");
            }
            /** throw exception if min less than 0 **/
            if (modifiedMin < 0) {
                throw new Exception("min must be greater than or equal to 0");
            }
            /** throw exception if inv is less than min or inv is greater than max **/
            if (modifiedInv < modifiedMin || modifiedInv > modifiedMax) {
                throw new Exception("Inventory must be greater than or equal to minimum and less than or equal to maximum");
            }
            /** retrieve modifiedTypeValue for processing **/
            modifiedTypeValue = partTypeValue.getText();
            /** check if in house button is selected **/
            if (inHouseButton.isSelected()) {
                /** process modifiedPartTypeValue to conform to InHouse part structure **/
                int machineID;
                try {
                    /** attempt to parse user input **/
                    machineID = Integer.parseInt(modifiedTypeValue);
                } catch (Exception e) {
                    throw new Exception("Machine ID must be an Integer");
                }
                /** create new InHouse part **/
                InHouse newPart = new InHouse(Integer.parseInt(partID.getText()), modifiedName, modifiedPrice, modifiedInv, modifiedMin, modifiedMax, machineID);
                /** add part to Inventory **/
                Inventory.updatePart(index, newPart);
            } else {
                /** in the case of an Outsourced part **/
                /** check if company name is not blank **/
                if (modifiedTypeValue.length() < 1) {
                    /** if user input is blank then throw an exception to warn user **/
                    throw new Exception("Company Name cannot be blank");
                }
                /** create new Outsourced part **/
                Outsourced newPart = new Outsourced(Integer.parseInt(partID.getText()), modifiedName, modifiedPrice, modifiedInv, modifiedMin, modifiedMax, modifiedTypeValue);
                /** add part to Inventory **/
                Inventory.updatePart(index, newPart);
            }
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
        } catch (Exception e) {
            /** display error messages for any malformed or incorrect inputs during the modify process **/
            displayError(e.getMessage());
        }
    }

    /** handle interaction when cancel button is clicked, return to main menu **/
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

    /**
     *
     * @param message message to be displayed in alert dialog
     */
    private void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setContentText(message);

        alert.showAndWait();
    }
}
