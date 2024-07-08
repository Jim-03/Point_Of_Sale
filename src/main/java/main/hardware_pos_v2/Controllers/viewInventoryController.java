package main.hardware_pos_v2.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import main.hardware_pos_v2.Database.DAO.CategoryDAO;
import main.hardware_pos_v2.Database.DAO.ItemDAO;
import main.hardware_pos_v2.Database.Entity.Category;
import main.hardware_pos_v2.Database.Entity.DatabaseItem;

import java.util.List;

public class viewInventoryController {

    @FXML
    private ListView<String> listView;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbox;

    @FXML
    public void initialize() {
        listView.prefWidthProperty().bind(vbox.widthProperty());
        fetchAll();
    }

    public void fetchAll() {
        CategoryDAO categoryDAO = new CategoryDAO();
        ItemDAO itemDAO = new ItemDAO();
        List<Category> categories = categoryDAO.fetchAll();
        ObservableList<String> dataList = FXCollections.observableArrayList();

        // Fetch items in each category
        for (Category category : categories) {
            dataList.add("Category: " + category.getName());
            int itemNumber = 1;
            List<DatabaseItem> databaseItems = itemDAO.fetchItemsInCategory(category);
            for (DatabaseItem item : databaseItems) {
                dataList.add("\t" + itemNumber + ": " + item.getName() + " (" + item.getStock() + ")");
                itemNumber++;
            }
        }

        listView.setItems(dataList);
    }
}