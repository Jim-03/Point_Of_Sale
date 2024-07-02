package main.hardware_pos_v2.Database.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class DatabaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "buying_price")
    private Double buyingPrice;

    @Column(name = "selling_price")
    private Double sellingPrice;
    private int stock;

    public DatabaseItem() {}

    public DatabaseItem(int id, String name, Category category, Double buyingPrice, Double sellingPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setStock(int quantity) {
        this.stock = quantity;
    }

    public int getStock() {
        return stock;
    }
}
