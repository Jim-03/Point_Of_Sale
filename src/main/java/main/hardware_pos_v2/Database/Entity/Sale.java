package main.hardware_pos_v2.Database.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "phone")
    private String phoneNumber;

    @Column
    private String itemList;

    @Column(name = "reference_number")
    private String refNo;

    @Column
    private LocalDateTime date;

    @Column(name = "total_paid")
    private double totalPaid;

    @Column(name = "list_total")
    private double listTotal;

    @Column(name = "server_name")
    private String serverName;

    @Column
    private double debt;

    public Sale() {
        setRefNo();
        setDate();
    }

    public Sale(int id, String customerName, String phoneNumber, String itemList, String refNo, LocalDateTime date, double totalPaid, double listTotal, String serverName, double debt) {
        this.id = id;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.itemList = itemList;
        this.refNo = refNo;
        this.date = date;
        this.totalPaid = totalPaid;
        this.listTotal = listTotal;
        this.serverName = serverName;
        this.debt = debt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public void setRefNo() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        this.refNo = timestamp + uniqueID;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getListTotal() {
        return listTotal;
    }

    public void setListTotal(double listTotal) {
        this.listTotal = listTotal;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate() {
        this.date = LocalDateTime.now();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", itemList='" + itemList + '\'' +
                ", refNo='" + refNo + '\'' +
                ", date=" + date +
                ", totalPaid=" + totalPaid +
                ", listTotal=" + listTotal +
                ", serverName='" + serverName + '\'' +
                ", debt=" + debt +
                '}';
    }
}
