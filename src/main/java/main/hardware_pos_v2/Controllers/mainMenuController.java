package main.hardware_pos_v2.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class mainMenuController {

    @FXML
    private VBox contentArea;

    @FXML
    private HBox header;

    @FXML
    private Label heading;

    @FXML
    private Button homeButton;

    @FXML
    private Button exitButton;

    @FXML
    private HBox navigationBar;

    @FXML
    private Button productsButton;

    @FXML
    private Button restockInventoryButton;

    @FXML
    private Button salesButton;

    /**
     * initialize to load the content area with sell screen
     */
    public void initialize() {
        changeContentArea("sell.fxml");
    }

    @FXML
    public void addToInventory() {
        changeContentArea("add-to-inventory.fxml");
    }

    @FXML
    public void sellScene() {
        changeContentArea("sell.fxml");
    }

    @FXML
    public void viewInventory() {
        changeContentArea("view-inventory.fxml");
    }

    @FXML
    public void viewSales() {
        changeContentArea("sales.fxml");
    }

    @FXML
    public  void setLogoutButton(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public void changeContentArea(String fileName){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fileName));
            VBox vBox = fxmlLoader.load();
            contentArea.getChildren().setAll(vBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
