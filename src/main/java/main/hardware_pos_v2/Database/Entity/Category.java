package main.hardware_pos_v2.Database.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<DatabaseItem> itemList;

    public Category(int id, String name, String description, List<DatabaseItem> itemList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.itemList = itemList;
    }

    public Category() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DatabaseItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<DatabaseItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", itemList=" + itemList +
                '}';
    }
}
