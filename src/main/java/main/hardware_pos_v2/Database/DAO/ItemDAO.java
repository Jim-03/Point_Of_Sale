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
import java.util.List;
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
            Query<Category> query = session.createQuery("FROM Category WHERE name = :categoryName", Category.class);
            query.setParameter("categoryName", newItem.getCategoryName());
            Category itemCategory = query.uniqueResult();

            // Check for duplicate item
            Query<DatabaseItem> databaseItemQuery = session.createQuery("FROM DatabaseItem WHERE name = :itemName", DatabaseItem.class);
            databaseItemQuery.setParameter("itemName", newItem.getName().trim().toLowerCase());
            if (databaseItemQuery.uniqueResult() != null) {
                result.put(false, "A similar item already exists in the database");
                return result;
            }

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
            databaseItem.setStock(newItem.getQuantity());

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

    /**
     * retrieves an item from the database
     * @param itemName the name of the item to retrieve
     * @return true/false mapped with result string
     */
    public Map<Boolean, String> readItem(String itemName){
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<DatabaseItem> query = session.createQuery("FROM DatabaseItem WHERE name = :itemName", DatabaseItem.class);
            query.setParameter("itemName", itemName);
            DatabaseItem item = query.uniqueResult();
            // Check if item exists
            if (item == null) {
                result.put(false, "Item not found");
                return result;
            }
            // Return the item
            result.put(true, item.toString());
            return result;
        } catch (Exception e) { // In case of any errors
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred: " + e.getMessage());
            return result;
        } finally {
            if (session != null){
                session.close();
            }
        }
    }

    /**
     * updates an item in the database
     * @param newItemData the new data to update to
     * @param currentItemName the name of the item to update
     * @return map of true/false with message
     */
    public Map<Boolean, String> updateItem(Item newItemData, String currentItemName){
        Map<Boolean, String> result = new HashMap<>();
        Transaction transaction = null;
        // Check if a name was provided
        if (newItemData == null) {
            result.put(false, "Item data can't be empty");
            return result;
        } else {
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                // Fetch the outdated data
                Query<DatabaseItem> query = session.createQuery("FROM DatabaseItem WHERE name=:itemName", DatabaseItem.class);
                query.setParameter("itemName", currentItemName);
                DatabaseItem oldItemData = query.uniqueResult();
                // If data isn't found
                if (oldItemData == null) {
                    result.put(false, "Item data not found");
                    return result;
                }
                // Check if the new category exists
                Query<Category> categoryQuery = session.createQuery("FROM Category WHERE name = :categoryName", Category.class);
                categoryQuery.setParameter("categoryName", newItemData.getCategoryName());
                Category itemCategory = categoryQuery.uniqueResult();
                // If category doesn't exist
                if (itemCategory == null) {
                    result.put(false, "The specified category wasn't found");
                    return result;
                }
                // Update to the new data
                oldItemData.setName(newItemData.getName());
                oldItemData.setCategory(itemCategory);
                oldItemData.setBuyingPrice(newItemData.getBuyingPrice());
                oldItemData.setSellingPrice(newItemData.getSellingPrice());
                oldItemData.setStock(newItemData.getQuantity());
                // Save the new data
                session.saveOrUpdate(oldItemData);
                transaction.commit();
                result.put(true, "Item's data has updated successfully");
                return result;
            } catch (Exception e) { // In case  of errors
                if (transaction != null) {
                    transaction.rollback();
                }
                result.put(false, e.getMessage());
                return result;
            }
        }
    }

    /**
     * deletes an item from the database
     * @param itemName the name of the item to delete
     * @return map of true/false and a message
     */
    public Map<Boolean, String> deleteItem(String itemName) {
        Map<Boolean, String> result = new HashMap<>();
        Transaction transaction = null;
        // Check if name was provided
        if (itemName == null) {
            result.put(false, "Item name cant be empty");
            return result;
        }
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // Fetch the item's data
            Query<DatabaseItem> query = session.createQuery("FROM DatabaseItem WHERE name= :itemName", DatabaseItem.class);
            query.setParameter("itemName", itemName);
            DatabaseItem item = query.uniqueResult();
            // Data not found
            if (item == null) {
                result.put(false, "Specified item not found");
            } else {
                // Delete the item
                session.delete(item);
                transaction.commit();
                result.put(true, "Item successfully deleted");
            }
            return result;
        } catch (Exception e) { // In case of errors
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, e.getMessage());
            return result;
        }
    }

    /**
     * reduces the stock of a specific item
     * @param itemName the name of the item
     * @param quantity the amount to reduce from the stock
     * @return map of true/false with a message
     */
    public Map<Boolean, String> reduceStock(String itemName, int quantity){
        Map<Boolean, String> result = new HashMap<>();
        Transaction transaction = null;
        // Check if item name was specified
        if (itemName == null) {
            result.put(false, "Item's name can't be empty");
            return result;
        } else if (quantity <= 0) { // Check if the amount to reduce is reasonable
            result.put(false, "The quantity should be above 0");
            return result;
        } else {
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                // Fetch the item's data
                Query<DatabaseItem> query = session.createQuery("FROM DatabaseItem WHERE name = :itemName", DatabaseItem.class);
                query.setParameter("itemName", itemName);
                DatabaseItem item = query.uniqueResult();
                // Item not found
                if (item == null) {
                    result.put(false, "Item not found in database");
                    return result;
                } else if (quantity > item.getStock()) { // Result in negative stock count
                    result.put(false, "The entered quantity is more than the available stock" +
                            "Number of items = " + quantity +
                            "Remaining stock =" + item.getStock());
                    return result;
                }
                // Reduce the stock
                item.setStock(item.getStock() - quantity);
                // Update the new stock size
                session.saveOrUpdate(item);
                transaction.commit();
                result.put(true, "Item stock reduced");
                return result;
            } catch (Exception e) { // In case of errors
                if (transaction != null) {
                    transaction.rollback();
                }
                result.put(false, "An error occured: " + e.getMessage());
                return result;
            }
        }
    }

    public Item getItem(String name){
        Transaction transaction = null;

        // Check if name is provided
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Query for an item
            Query<DatabaseItem> query = session.createQuery("FROM DatabaseItem WHERE name = :itemName", DatabaseItem.class);
            query.setParameter("itemName", name);
            DatabaseItem databaseItem = query.uniqueResult();

            // Check if the item was found
            if (databaseItem == null){
                return null;
            }
            Item item = new Item();
            item.setName(databaseItem.getName());
            item.setBuyingPrice(databaseItem.getBuyingPrice());
            item.setSellingPrice(databaseItem.getSellingPrice());
            item.setCategoryName(databaseItem.getCategory().getName());
            item.setQuantity(databaseItem.getStock());
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * returns a list of all items in a specific category
     * @param category the category data
     * @return the list, null otherwise
     */
    public List<DatabaseItem> fetchItemsInCategory(Category category) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM DatabaseItem WHERE category.id = :categoryId", DatabaseItem.class)
                    .setParameter("categoryId", category.getId())
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}