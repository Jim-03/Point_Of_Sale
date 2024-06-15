package main.hardware_pos_v2.Database.DAO;

import main.hardware_pos_v2.Database.Entity.Category;
import main.hardware_pos_v2.Database.Entity.DatabaseItem;
import main.hardware_pos_v2.Models.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.Map;

public class ItemDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor
     */
    public ItemDAO() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * adds a new item to the database
     * @param newItem the item to be added
     * @return map containing true/false with  a message
     */
    public Map<Boolean, String> addItem(Item newItem) {
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // Check if the specified category exists
            Query<Category> query = session.createQuery("SELECT FROM categories WHERE name = :categoryName", Category.class);
            query.setParameter("itemName", newItem.getCategoryName());
            Category itemCategory = query.uniqueResult();
            // If category doesn't exist
            if (itemCategory == null) {
                result.put(false, "The specified category wasn't found");
                return result;
            }
            // Create a new database object
            DatabaseItem databaseItem = new DatabaseItem();
            databaseItem.setName(newItem.getName());
            databaseItem.setBuyingPrice(newItem.getBuyingPrice());
            databaseItem.setSellingPrice(newItem.getSellingPrice());
            databaseItem.setCategory(itemCategory);

            // Save the object
            session.save(databaseItem);
            transaction.commit();
            result.put(true, "Item Successfully added");
            return result;
        } catch (Exception e) {  // Captures all errors encountered
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "Error adding to database: " + e.getMessage());
            return result;
        }
    }

}