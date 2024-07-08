package main.hardware_pos_v2.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import main.hardware_pos_v2.Database.DAO.SalesDAO;
import main.hardware_pos_v2.Database.Entity.Sale;

import java.util.List;

public class viewSalesController {

    @FXML
    private ListView<String> listView;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbox;

    public void initialize() {
        listView.prefWidthProperty().bind(vbox.widthProperty());
        fetchAll();
    }

    public void fetchAll() {
        SalesDAO salesDAO = new SalesDAO();
        List<Sale> sales = salesDAO.getAll();
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Sale sale: sales) {
            list.add(sale.toString());
        }
        listView.setItems(list);
    }
}