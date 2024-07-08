package main.hardware_pos_v2.Database.DAO;

import main.hardware_pos_v2.Database.Entity.Sale;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesDAO {
    private SessionFactory sessionFactory;

    public SalesDAO() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * adds a new sale to the database
     * @param sale the new sales data
     * @return map of true/false with the message
     */
    public Map<Boolean, String> addNewSale(Sale sale) {
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();

        // Check if a sale is provided
        if (sale == null) {
            result.put(false, "Please input valid data");
            return result;
        }

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            sale.setCustomerName(sale.getCustomerName().trim().toLowerCase());
            // Check if the list is provided
            if (sale.getItemList().isEmpty()) {
                result.put(false, "The item list can't be empty");
                return result;
            }
            // Add it to the database
            session.save(sale);
            transaction.commit();
            result.put(true, "New sale was made");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred due to: " + e.getMessage());
            return result;
        }
    }

    /**
     * updates an existing sales data
     * @param newSaleData the new sale data
     * @param refNo the previous sales reference number
     * @return map of boolean/string with a message
     */
    public Map<Boolean, String> updateSale(Sale newSaleData, String refNo){
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();

        try (Session session = sessionFactory.openSession()) {
            // Fetch old data
            Sale oldSaleData = session.createQuery("FROM Sale WHERE refNO = :reference", Sale.class)
                    .setParameter("reference", refNo)
                    .uniqueResult();

            // Check if the sale exists
            if (oldSaleData == null) {
                result.put(false, "Sale doesn't exist");
                return result;
            }

            // Update sale details
            oldSaleData.setCustomerName(newSaleData.getCustomerName());
            oldSaleData.setPhoneNumber(newSaleData.getPhoneNumber());
            oldSaleData.setDebt(newSaleData.getDebt());
            oldSaleData.setItemList(newSaleData.getItemList());
            oldSaleData.setListTotal(newSaleData.getListTotal());
            oldSaleData.setTotalPaid(newSaleData.getTotalPaid());
            session.saveOrUpdate(oldSaleData);
            transaction.commit();
            result.put(true, "The sale data has updated successfully");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred while updating: " + e.getMessage());
            return result;
        }

    }

    /**
     * retrieves data on a specific sale
     * @param refNo the sale''s unique reference number
     * @return a map of true/false with a message
     */
        public Map<Boolean, String> readSale(String refNo) {
            Transaction transaction = null;
            Map<Boolean, String> result = new HashMap<>();

            if (refNo.trim().isEmpty()) {
                result.put(false, "Provide the reference number");
                return result;
            }

            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                Sale sale = session.createQuery("FROM Sale WHERE refNo = :reference", Sale.class)
                        .setParameter("reference", refNo)
                        .uniqueResult();

                // Check if the data was found
                if (sale == null) {
                    result.put(false, "The sale with the provided reference number wasn't found");
                    return result;
                }

                result.put(true, sale.toString());
                return result;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                result.put(false, "Error fetching info due to: " + e.getMessage());
                return result;
            }
        }

    /**
     * deletes a sale from the database
     * @param refNo the reference number
     * @return map of true/false with a message
     */
    public Map<Boolean, String> deleteSale(String refNo) {
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();

        if (refNo.trim().isEmpty()) {
            result.put(false, "Please provide a reference number");
            return result;
        }

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Sale sale = session.createQuery("FROM Sale WHERE refNO = : reference", Sale.class)
                    .setParameter("reference", refNo)
                    .uniqueResult();

            // Check if sale was found
            if (sale == null) {
                result.put(false, "The sale with the provided reference number wasn't found");
                return result;
            }

            // Delete the sale
            session.delete(sale);
            transaction.commit();
            result.put(true, "Sale was successfully deleted");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred while deleting: " + e.getMessage());
            return result;
        }
    }

    /**
     * retrieves a list of all sales made
     * @return a list of sales
     */
    public List<Sale> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Sale", Sale.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
