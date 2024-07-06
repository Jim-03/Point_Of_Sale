package main.hardware_pos_v2.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.hardware_pos_v2.Database.DAO.ItemDAO;
import main.hardware_pos_v2.Models.Item;

import java.io.IOException;
import java.util.Map;

public class mainMenuController {

    @FXML
    private Button restockInventoryButton;

    @FXML
    private Button addButton;

    @FXML
    private VBox contentArea;

    @FXML
    private TextField customerNameInput;

    @FXML
    private TextField dateOfPurchaseOutput;

    @FXML
    private TextField debtsButton;

    @FXML
    private HBox header;

    @FXML
    private Label heading;

    @FXML
    private Button homeButton;

    @FXML
    private VBox inputArea;

    @FXML
    private TextField itemNameInput;

    @FXML
    private TableColumn<Item, String> itemsColumn;

    @FXML
    private Button logoutButton;

    @FXML
    private HBox navigationBar;

    @FXML
    private TextField phoneInput;

    @FXML
    private TableColumn<Item, Double> priceColumn;

    @FXML
    private Button productsButton;

    @FXML
    private TableView<Item> purchaseTable;

    @FXML
    private TableColumn<Item, Integer> quantityColumn;

    @FXML
    private TextField quantityInput;

    @FXML
    private Button salesButton;

    @FXML
    private Label feedBackLabel;

    @FXML
    private Button sellButton;

    @FXML
    private TextField serverNameInput;

    @FXML
    private Button submitButton;

    @FXML
    private TextField totalPaymentsOutput;

    @FXML
    private TableColumn<Item, Double> totalPriceColumn;

    ObservableList<Item> list = FXCollections.observableArrayList();

    /**
     * initialize the table view
     */
    public void initialize() {
        itemsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSellingPrice()).asObject());
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        totalPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getQuantity() * cellData.getValue().getSellingPrice()).asObject());
        purchaseTable.setItems(list);
    }

    public void createItem() {
        // CLear anything from the feedback
        feedBackLabel.setVisible(false);
        String name = itemNameInput.getText();

        //Check if a name was entered
        if (name == null || name.trim().isEmpty()) {
            feedBackLabel.setText("Input Item name");
            feedBackLabel.setStyle("-fx-text-fill: red");
            feedBackLabel.setVisible(true);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityInput.getText());

            // Check if the right quantity is provided
            if (quantity <= 0) {
                feedBackLabel.setText("Please enter a valid quantity");
                feedBackLabel.setStyle("-fx-text-fill: red");
                feedBackLabel.setVisible(true);
                return;
            }
        } catch (NumberFormatException e) {
            feedBackLabel.setText("Please enter quantity as a number");
            feedBackLabel.setStyle("-fx-text-fill: red");
            feedBackLabel.setVisible(true);
            return;
        }
        feedBackLabel.setVisible(true);
        feedBackLabel.setText("Searching...");
        feedBackLabel.setStyle("-fx-text-fill: green");
        // Fetch the data from database
        ItemDAO itemDAO = new ItemDAO();
        Map<Boolean, String> result = itemDAO.readItem(name);

        // Check if the item was found
        if (result.containsKey(false)){
            feedBackLabel.setText(result.get(false));
            feedBackLabel.setStyle("-fx-text-fill: red");
            feedBackLabel.setVisible(true);
            return;
        } else {
            feedBackLabel.setText("Item found");
            feedBackLabel.setStyle("-fx-text-fill: green");
            feedBackLabel.setVisible(true);
        }
    }

    @FXML
    public void addToInventory() {
        changeContentArea("add-to-inventory.fxml");
    }

    @FXML
    public void sellScene() {
        changeContentArea("sell.fxml");
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
