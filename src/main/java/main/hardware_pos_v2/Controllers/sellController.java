package main.hardware_pos_v2.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.hardware_pos_v2.Database.DAO.CategoryDAO;
import main.hardware_pos_v2.Database.DAO.ItemDAO;
import main.hardware_pos_v2.Database.DAO.SalesDAO;
import main.hardware_pos_v2.Database.Entity.DatabaseItem;
import main.hardware_pos_v2.Database.Entity.Sale;
import main.hardware_pos_v2.Models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class sellController {
    @FXML
    private Button addButton;

    @FXML
    private TextField customerNameInput;

    @FXML
    private TextField dateOfPurchaseOutput;

    @FXML
    private TextField changeOutput;

    @FXML
    private TextField phoneInput;

    @FXML
    private Button sellButton;

    @FXML
    private TextField serverNameInput;


    @FXML
    private TextField paymentsInput;

    @FXML
    private TextField totalPaymentsOutput;

    @FXML
    private Label feedBackLabel;

    @FXML
    private VBox inputArea;

    @FXML
    private TextField itemNameInput;

    @FXML
    private TableColumn<Item, String> itemsColumn;

    @FXML
    private TableColumn<Item, Double> priceColumn;

    @FXML
    private TableView<Item> purchaseTable;

    @FXML
    private TableColumn<Item, Integer> quantityColumn;

    @FXML
    private TextField quantityInput;

    @FXML
    private TableColumn<Item, Double> totalPriceColumn;

    ObservableList<Item> list = FXCollections.observableArrayList();
    private ArrayList<Item> shoppingCart;

    public void initialize() {
        itemsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSellingPrice()).asObject());
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        totalPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getQuantity() * cellData.getValue().getSellingPrice()).asObject());
        purchaseTable.setItems(list);
    }

    @FXML
    /*
    Creates an item object to be sold
     */
    public void createItem() {
        // CLear anything from the feedback
        String name = itemNameInput.getText();
        int quantity;

        //Check if a name was entered
        if (name == null || name.trim().isEmpty()) {
            setFeedBackLabel(false, "Input Item name");
            return;
        }
        try {
            quantity = Integer.parseInt(quantityInput.getText());

            // Check if the right quantity is provided
            if (quantity <= 0) {
                setFeedBackLabel(false, "Please enter a valid quantity");
                return;
            }
        } catch (NumberFormatException e) {
            setFeedBackLabel(false, "Please enter quantity as a number");
            return;
        }
        setFeedBackLabel(true, "Searching...");
        // Fetch the data from database
        ItemDAO itemDAO = new ItemDAO();
        Map<Boolean, String> result = itemDAO.readItem(name);

        // Check if the item was found
        if (result.containsKey(false)){
            setFeedBackLabel(false, result.get(false));
            return;
        } else {
            setFeedBackLabel(true, "Item found");
        }
        Item item = itemDAO.getItem(name);
        if (quantity > item.getQuantity()){
            setFeedBackLabel(false, "The specified quantity(" + quantity + ") is more than the available stock of (" + item.getQuantity() + ")");
            return;
        }
        // Add the item to the shopping cart
        if (shoppingCart == null){
            shoppingCart = new ArrayList<>();
        }

        // Check if the item already exists in the shopping cart
        boolean itemExistsInCart = false;
        for (Item cartItem : shoppingCart) {
            if (cartItem.getName().equals(item.getName())) {
                // Update the quantity of the existing item in the cart
                int newQuantity = cartItem.getQuantity() + quantity;
                if (newQuantity > item.getQuantity()) {
                    setFeedBackLabel(false, "The combined quantity exceeds the available stock of (" + item.getQuantity() + ")");
                    return;
                }
                cartItem.setQuantity(newQuantity);
                setFeedBackLabel(true, "Item quantity updated in shopping list");
                itemExistsInCart = true;
                break;
            }
        }

        // If the item doesn't exist in the cart, add it
        if (!itemExistsInCart) {
            item.setQuantity(quantity);
            shoppingCart.add(item);
            setFeedBackLabel(true, "Item added to shopping list");
        }
        itemNameInput.clear();
        quantityInput.clear();
        updateTable();
        totalPaymentsOutput.setText(calculateTotal() + "Ksh");
        return;
    }

    // Calculates the total amount of items in the table
    public double calculateTotal() {
        double total = 0;
        for (Item item : shoppingCart) {
            total += item.getQuantity() * item.getSellingPrice();
        }
        return total;
    }

    @FXML
    //Removes item from the shopping cart to be sold
    public void sellItems() {
        // Check if the list is empty
        if (shoppingCart.isEmpty()){
            setFeedBackLabel(false, "The shopping cart is empty");
            return;
        }
        reduceStock();
        // Remove any data in the input fields
        customerNameInput.clear();
        phoneInput.clear();
        serverNameInput.clear();
        return;
    }

    // Reduces the number of items in the database
    public void reduceStock() {
        ItemDAO itemDAO = new ItemDAO();
        List<Item> itemsToRemove = new ArrayList<>();

        // Iterate through each item in the list and reduce in the database
        for (Item item : shoppingCart) {
            Map<Boolean, String> result = itemDAO.reduceStock(item.getName(), item.getQuantity());
            if (result.containsKey(true)) {
                setFeedBackLabel(true, item.getName() + " successfully sold");
                itemsToRemove.add(item);
            } else {
                setFeedBackLabel(false, result.get(false));
                return;
            }
        }
        List<String> itemList = new ArrayList<>();
        // Calculate total before deleting for sale storage
        double total = calculateTotal();
        // remove items from the list
        for (Item item : itemsToRemove) {
            DatabaseItem databaseItem = new DatabaseItem();
            CategoryDAO categoryDAO = new CategoryDAO();
            databaseItem.setName(item.getName());
            databaseItem.setSellingPrice(item.getSellingPrice());
            databaseItem.setStock(item.getQuantity());
            databaseItem.setCategory(categoryDAO.getCategory(item.getCategoryName()));
            databaseItem.setBuyingPrice(item.getBuyingPrice());
            itemList.add(
                    "Item:" + databaseItem.getName() + " Quantity:"
                            + databaseItem.getStock() + " Selling:"
                            + databaseItem.getSellingPrice() + " Total:"
                            + databaseItem.getStock() * databaseItem.getSellingPrice()
            );
        }
        // Remove items from the shopping cart after iteration
        shoppingCart.removeAll(itemsToRemove);
        // Add the sale to database
        makeSale(total, itemList);
        // Update the items table
        updateTable();


    }

    // Adds new sale to the database
    public void makeSale(double total, List<String> itemList) {
        // Fetch data input
        String server = serverNameInput.getText();
        String customer = customerNameInput.getText();
        String phone = phoneInput.getText();
        // Validate input
        if (customer.trim().isEmpty()) {
            setFeedBackLabel(false, "Please enter customer's name");
            return;
        }
        if (phone.trim().isEmpty()) {
            setFeedBackLabel(false, "Please enter customer's phone number");
            return;
        }
        if (server.trim().isEmpty())
        {
            setFeedBackLabel(false, "Please enter the name of the one serving");
            return;
        }
        double payment;
        // Check if the payment is entered
        try{
            payment = Double.parseDouble(paymentsInput.getText());
            if (payment <= 0) {
                setFeedBackLabel(false, "Payment amount should be above 0");
                return;
            }
        } catch (NumberFormatException e) {
            setFeedBackLabel(false, "Please enter valid payments");
            return;
        }
        double text = payment -total;
        changeOutput.setText(text + "Ksh");
        // Update the sales table
        Sale sale = new Sale();
        sale.setCustomerName(customer);
        sale.setDebt(text);
        sale.setPhoneNumber(phone);
        sale.setServerName(server);
        sale.setItemList(itemList.toString());
        sale.setTotalPaid(payment);
        sale.setListTotal(total);
        SalesDAO salesDAO = new SalesDAO();
        Map<Boolean, String> result = salesDAO.addNewSale(sale);
        if (result.containsKey(false)) {
            setFeedBackLabel(false, result.get(false));
        } else {
            setFeedBackLabel(true, result.get(true));
        }
    }

    // Updates table depending on the list
    public void updateTable(){
        list.setAll(shoppingCart);
    }

    /**
     * updates the feedback with a message
     * @param state boolean to determine the color of output
     * @param message the message to display
     */
    public void setFeedBackLabel(boolean state, String message){
        feedBackLabel.setVisible(false);
        feedBackLabel.setText(message);
        feedBackLabel.setStyle(state ? "-fx-text-fill: green" : "-fx-text-fill: red");
        feedBackLabel.setVisible(true);
    }
}
