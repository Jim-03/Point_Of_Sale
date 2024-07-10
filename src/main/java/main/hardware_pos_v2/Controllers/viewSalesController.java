package main.hardware_pos_v2.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import main.hardware_pos_v2.Database.DAO.SalesDAO;
import main.hardware_pos_v2.Database.Entity.Sale;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class viewSalesController {

    @FXML
    private TableColumn<Sale, String> customerNameColumn;

    @FXML
    private TableColumn<Sale, String> customerPhoneColumn;

    @FXML
    private TableColumn<Sale, Double> changeColumn;

    @FXML
    private TableColumn<Sale, String> itemsColumn;

    @FXML
    private TableView<Sale> salesTable;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TableColumn<Sale, Double> totalPaidColumn;

    @FXML
    private TableColumn<Sale, String> dateColumn;

    @FXML
    private TableColumn<Sale, String> servedByColumn;

    @FXML
    private TableColumn<Sale, Double> totalAmountColumn;
    @FXML
    private VBox vbox;

    public void initialize() {
        customerNameColumn.setCellValueFactory(sale -> new SimpleStringProperty(sale.getValue().getCustomerName()));
        customerPhoneColumn.setCellValueFactory((phone -> new SimpleStringProperty(phone.getValue().getPhoneNumber())));
        itemsColumn.setCellValueFactory(items -> new SimpleStringProperty(items.getValue().getItemList()));
        totalAmountColumn.setCellValueFactory(total -> new SimpleDoubleProperty(total.getValue().getListTotal()).asObject());
        totalPaidColumn.setCellValueFactory(payment -> new SimpleDoubleProperty(payment.getValue().getTotalPaid()).asObject());
        changeColumn.setCellValueFactory(debt -> new SimpleDoubleProperty(debt.getValue().getDebt()).asObject());
        dateColumn.setCellValueFactory(date -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return new SimpleStringProperty(date.getValue().getDate().format(formatter));
        });
        servedByColumn.setCellValueFactory(server -> new SimpleStringProperty(server.getValue().getServerName()));
        salesTable.prefWidthProperty().bind(vbox.widthProperty());
        fetchAll();
    }

    private void fetchAll() {
        ObservableList<Sale> saleObservableList = FXCollections.observableArrayList();
        SalesDAO salesDAO = new SalesDAO();
        List<Sale> list = salesDAO.getAll();
        saleObservableList.addAll(list);
        salesTable.setItems(saleObservableList);
    }
}
