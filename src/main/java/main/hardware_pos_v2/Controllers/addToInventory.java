package main.hardware_pos_v2.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.hardware_pos_v2.Database.DAO.CategoryDAO;
import main.hardware_pos_v2.Database.DAO.ItemDAO;
import main.hardware_pos_v2.Database.Entity.Category;
import main.hardware_pos_v2.Models.Item;

import java.util.Map;

public class addToInventory {

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button addItemInput;

    @FXML
    private TextField buyingPriceInput;

    @FXML
    private TextField categoryDescriptionInput;

    @FXML
    private TextField categoryNameInput;

    @FXML
    private Label feedback;

    @FXML
    private TextField itemCategoryInput;

    @FXML
    private TextField itemNameInput;

    @FXML
    private TextField sellingPriceInput;

    @FXML
    private TextField stockInput;

    @FXML
    /*
     * Adds an item to the item's database
     */
    public void addItemToInventory() {
        // Assign the data input
        String name = itemNameInput.getText();
        String itemCategory = itemCategoryInput.getText();
        double buyingPrice;
        double sellingPrice;
        int stock;

        // Check if the names were input first
        if (name.trim().isEmpty()) {
            setFeedback(false, "Please enter the item's name");
            return;
        }
        if (itemCategory.trim().isEmpty()) {
            setFeedback(false, "Please enter a valid category name");
            return;
        }

        // Validate buying price input
        try {
            buyingPrice = Double.parseDouble(buyingPriceInput.getText());
        } catch (NumberFormatException e) {
            setFeedback(false, "Please enter a valid buying price");
            return;
        }

        // Validate selling price input
        try {
            sellingPrice = Double.parseDouble(sellingPriceInput.getText());
        } catch (NumberFormatException e) {
            setFeedback(false, "Please enter a valid selling price");
            return;
        }

        // Validate stock input
        try {
            stock = Integer.parseInt(stockInput.getText());
        } catch (NumberFormatException e) {
            setFeedback(false, "Please enter a valid stock number");
            return;
        }

        // Check for validity in remaining input
        if (buyingPrice <= 0) {
            setFeedback(false, "Please enter a valid buying price");
            return;
        }
        if (sellingPrice <= 0) {
            setFeedback(false, "Please enter a valid selling price");
            return;
        }
        if (stock <= 0) {
            setFeedback(false, "Please enter a valid stock size");
            return;
        }

        // If all validations pass
        setFeedback(true, "Searching for category");

        // Build item object
        Item item = new Item();
        item.setName(name.toLowerCase());
        item.setCategoryName(itemCategory.toLowerCase());
        item.setBuyingPrice(buyingPrice);
        item.setSellingPrice(sellingPrice);
        item.setQuantity(stock);

        // Add item to database
        ItemDAO itemDAO = new ItemDAO();
        Map<Boolean, String> result = itemDAO.addItem(item);

        // Check for result
        // If a similar product is found
        if (result.get(false).equals("A similar item already exists in the database")){
            Item updatedItem = itemDAO.getItem(item.getName());
            updatedItem.setQuantity(updatedItem.getQuantity() + item.getQuantity());
            Map<Boolean, String> update = itemDAO.updateItem(updatedItem, item.getName());
            // Check the result for updating
            if (update.containsKey(true)) {
                setFeedback(true, update.get(true));
                return;
            }
            else {
                setFeedback(false, update.get(false));
                return;
            }
        } else if (result.containsKey(false)) {
            setFeedback(false, result.get(false));
            return;
        } else {
            setFeedback(true, result.get(true));

            // Clear the input fields
            itemNameInput.clear();
            itemCategoryInput.clear();
            buyingPriceInput.clear();
            sellingPriceInput.clear();
            stockInput.clear();
            return;
        }
    }

    @FXML
    /*
     * adds a new category to the database
     */
    public void addCategoryToInventory() {
        // Capture the input
        String name = categoryNameInput.getText();
        String description = categoryDescriptionInput.getText();

        // Check for empty inputs
        if (name.trim().isEmpty()) {
            setFeedback(false, "Please enter a category name");
            return;
        }
        if (description.trim().isEmpty()) {
            setFeedback(false, "Please enter the category's description");
            return;
        }

        setFeedback(true, "processing...");

        // Build a category object
        Category category = new Category();
        category.setName(name.trim().toLowerCase());
        category.setDescription(description.trim().toLowerCase());

        // Add to database
        CategoryDAO categoryDAO = new CategoryDAO();
        Map<Boolean,String> result = categoryDAO.addCategory(category);

        // Check the result
        if (result.containsKey(false)) {
            setFeedback(false, result.get(false));
            return;
        } else {
            setFeedback(true, result.get(true));
            return;
        }
    }

    /**
     * Assigns data to the feedback label
     * @param state a boolean to determine color (true for green, false for red)
     * @param message the message to display
     */
    public void setFeedback(boolean state, String message) {
        feedback.setVisible(false);
        feedback.setText(message);
        feedback.setStyle(state ? "-fx-text-fill: green" : "-fx-text-fill: red");
        feedback.setVisible(true);
    }
}
