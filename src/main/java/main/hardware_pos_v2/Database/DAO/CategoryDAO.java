package main.hardware_pos_v2.Database.DAO;

import javafx.collections.ObservableList;
import main.hardware_pos_v2.Database.Entity.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDAO {
    private final SessionFactory sessionFactory;

    /*
    Constructor
     */
    public CategoryDAO () {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * adds a new category to the database
     * @param category the new data to save
     * @return a map of true/false with a message
     */
    public Map<Boolean, String> addCategory(Category category){
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();
        // Check if the category is provided
        if (category == null) {
            result.put(false, "The category can't be empty");
            return result;
        }
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            // Check if the category already exists
            Query<Category> query = session.createQuery("FROM Category WHERE name = :categoryName", Category.class);
            query.setParameter("categoryName", category.getName());
            Category categoryExists = query.uniqueResult();
            if (categoryExists != null) {
                result.put(false, "The category already exists");
                return result;
            }
            // Add the new category
            session.save(category);
            transaction.commit();
            result.put(true, "Category successfully added");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred: " + e.getMessage());
            return result;
        }
    }

    /**
     * retrieves a category from the database
     * @param categoryName the name of the category
     * @return the category details
     */
    public Map<Boolean, String> readCategory(String categoryName){
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();
        //Check if the name is provided
        if (categoryName == null){
            result.put(false, "The category's name can't be null");
            return result;
        }
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // Query for the category
            Query<Category> query = session.createQuery("FROM Category WHERE name = :categoryName", Category.class);
            query.setParameter("categoryName", categoryName);
            Category category = query.uniqueResult();
            // Check if category exists
            if (category == null) {
                result.put(false, "THe specified category doesn't exist");
                return result;
            }
            // Return the data
            result.put(true, category.toString());
            return result;
        } catch (Exception e) {
            // Error while fetching
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred due to: " + e.getMessage());
            return result;
        }
    }

    /**
     * updates a category's data in the database
     * @param newCategoryData the new data to update to
     * @param categoryName the former name of the category
     * @return map of true/false with a message
     */
    public Map<Boolean, String> updateCategory(Category newCategoryData, String categoryName){
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();

        // Check if the name and new data is provided
        if (categoryName == null) {
            result.put(false, "Please provide the initial name");
            return result;
        } else if (newCategoryData == null) {
            result.put(false, "The new data can't be empty");
            return result;

        }
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // Fetch the old category's data
            Query<Category> query = session.createQuery("FROM Category WHERE name = :categoryName", Category.class);
            query.setParameter("categoryName", categoryName);
            Category oldCategoryData = query.uniqueResult();
            // Check if the category exists
            if (oldCategoryData == null) {
                result.put(false, "Category doesn't exist");
                return result;
            }
            // Update the data
            oldCategoryData.setName(newCategoryData.getName());
            oldCategoryData.setDescription(newCategoryData.getDescription());
            oldCategoryData.setItemList(newCategoryData.getItemList());
            // Save the update
            session.saveOrUpdate(oldCategoryData);
            transaction.commit();
            result.put(true, "Item successfully updated");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occured due to: " + e.getMessage());
            return result;
        }
    }

    /**
     * deletes a category from the database
     * @param categoryName the name of the category to be deleted
     * @return a map of true/false with a message
     */
    public Map<Boolean, String> deleteCategory(String categoryName) {
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();

        // Check if the name was provided
        if (categoryName == null) {
            result.put(false, "Please provide the name of the category");
            return result;
        }
        try (Session session = sessionFactory.openSession()) {
            transaction = session.getTransaction();
            // Fetch category data from the database
            Query<Category> query = session.createQuery("FROM Category WHERE name = :categoryName", Category.class);
            query.setParameter("categoryName", categoryName);
            Category category = query.uniqueResult();
            // Check if category exists
            if (category == null) {
                result.put(false, "Category doesn't exist");
                return result;
            }
            // Delete the category
            session.delete(category);
            // Save changes
            transaction.commit();
            result.put(true, "Category successfully deleted");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occured while deleting: "+ e.getMessage());
            return result;
        }
    }

    /**
     * retrieves a list of all categories
     * @return a list of categories
     */
    public List<Category> fetchAll() {
        Transaction transaction = null;
        List<Category> list = null;

        try (Session session =sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            list = session.createQuery("FROM Category", Category.class).list();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return list;
    }


    /**
     * retrieves a category class from the database
     * @param categoryName the name of the category to search for
     * @return the category data
     */
    public Category getCategory(String categoryName) {
        return sessionFactory.openSession().createQuery("FROM Category WHERE name = :categoryName", Category.class)
                .setParameter("categoryName", categoryName)
                .uniqueResult();
    }
}
