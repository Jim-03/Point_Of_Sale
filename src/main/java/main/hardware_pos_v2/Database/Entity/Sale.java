package main.hardware_pos_v2.Database.Entity;

import jakarta.persistence.*;
import main.hardware_pos_v2.Models.Item;

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

    @Column(name = "customer-name")
    private String customerName;

    @Column(name = "phone")
    private String phoneNumber;

    @JoinTable(
            name = "shopped-items",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns =  @JoinColumn(name = "items")
    )
    List<Item> itemList;

    @Column(name = "reference-number")
    private String refNo;

    @Column(name = "total-paid")
    private double totalPaid;

    @Column(name = "list-total")
    private double listTotal;

    @Column
    private double debt;

    public Sale() {}

    public Sale(int id, String customerName, String phoneNumber, List<Item> itemList, String refNo, double totalPaid, double listTotal, double debt) {
        this.id = id;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.itemList = itemList;
        this.refNo = refNo;
        this.totalPaid = totalPaid;
        this.listTotal = listTotal;
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

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void setRefNo() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        this.refNo = timestamp + uniqueID;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
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

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", itemList=" + itemList +
                ", refNo='" + refNo + '\'' +
                ", totalPaid=" + totalPaid +
                ", listTotal=" + listTotal +
                ", debt=" + debt +
                '}';
    }
}
