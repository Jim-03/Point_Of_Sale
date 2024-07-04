package main.hardware_pos_v2.Database.DAO;

import main.hardware_pos_v2.Database.Entity.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.Map;

public class AccountDAO {
    private final SessionFactory sessionFactory;

    public AccountDAO() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * creates a new account in the database
     * @param account the account details
     * @return map of true/false with a message
     */
    public Map<Boolean, String> createAccount(Account account){
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();

        // Check if account details are empty
        if (account == null) {
            result.put(false, "Account details can't be empty");
            return result;
        }
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // Check if username already exists
            Query<Account> query = session.createQuery("FROM Account WHERE username = :name", Account.class);
            query.setParameter("name", account.getUsername());
            Account existingAccount = query.uniqueResult();
            if (existingAccount != null) {
                result.put(false, "An account with the specified username already exists");
                return result;
            }
            // Save the new account
            account.setPassword(hashPassword(account.getPassword()));
            session.save(account);
            transaction.commit();
            result.put(true, "Account successfully created");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred while saving account: " + e.getMessage());
            return result;
        }
    }

    /**
     * retrieves info on an account
     * @param username the account's username
     * @return map of true/false with message
     */
    public Map<Boolean, String> readAccount(String username) {
        Map<Boolean, String> result = new HashMap<>();
        Transaction transaction = null;

        // Check if username was provided
        if (username.trim().isEmpty()) {
            result.put(false, "Please provide a username");
            return result;
        }

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // Query for account data
            Query<Account> query = session.createQuery("FROM Account WHERE username = :name", Account.class);
            query.setParameter("name", username);
            Account account = query.uniqueResult();
            // CHeck if account exists
            if (account == null) {
                result.put(false, "The account with the specified username doesn't exist");
                return result;
            }
            result.put(true, account.toString());
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred while fetching data: " + e.getMessage());
            return result;
        }
    }

    /**
     * updates an existing account's data
     * @param newAccountData the new data to update to
     * @param initialUsername the previous username
     * @return a map of true/false with a message
     */
    public Map<Boolean, String> updateAccount(Account newAccountData, String initialUsername) {
        Map<Boolean, String> result = new HashMap<>();
        Transaction transaction = null;

        // Check if the username is provided
        if (initialUsername.trim().isEmpty()){
            result.put(false, "Please provide the current username for identification");
            return result;
        }
        // Check if new details are empty
        else if (newAccountData == null) {
            result.put(false, "The new details can't be empty");
            return result;
        }

        try (
                Session session = sessionFactory.openSession()
                ) {
            transaction = session.beginTransaction();

            // Fetch the outdated account data
            Query<Account> query = session.createQuery("FROM Account WHERE username = :name", Account.class);
            query.setParameter("name", initialUsername.trim());
            Account outdatedAccount = query.uniqueResult();

            // Check if account exists
            if (outdatedAccount == null) {
                result.put(false, "The account with the specified username wasn't found");
                return result;
            }

            // Update the account
            outdatedAccount.setUsername(newAccountData.getUsername().trim());
            outdatedAccount.setPassword(hashPassword(newAccountData.getPassword()));

            // Save the updated account
            session.saveOrUpdate(outdatedAccount);
            transaction.commit();
            result.put(true, "Account was successfully updated");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred while updating the account: " + e.getMessage());
            return result;
        }
    }

    /**
     * deletes an account
     * @param username the account's username
     * @return a map of true/false with a message
     */
    public Map<Boolean, String> deleteAccount(String username) {
        Transaction transaction = null;
        Map<Boolean, String> result = new HashMap<>();

        // Check if username is provided
        if (username.trim().isEmpty()) {
            result.put(false, "Please provide a username");
            return result;
        }

        try( Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            // Fetch the account's data
            Query<Account> query = session.createQuery("FROM Account WHERE username = :name", Account.class);
            query.setParameter("name", username);
            Account account = query.uniqueResult();

            // Check if account exists
            if (account == null) {
                result.put(false, "The account with the specified username doesn't exist");
                return result;
            }

            // Delete the account
            session.delete(account);
            transaction.commit();
            result.put(true, "Account was successfully deleted");
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            result.put(false, "An error occurred while deleting the account: " + e.getMessage());
            return result;
        }
    }

    /**
     * hashes a password for security
     * @param password the readable password string
     * @return the hashed value
     */
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Retrieves the account entity from the database.
     * @param username The username of the account to retrieve.
     * @return The Account entity if found, null otherwise.
     */
    public Account getAccount(String username) {
        Transaction transaction = null;

        // Check if username is provided
        if (username.trim().isEmpty()) {
            return null;
        }

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Query for account data
            Query<Account> query = session.createQuery("FROM Account WHERE username = :name", Account.class);
            query.setParameter("name", username);
            Account account = query.uniqueResult();

            // Commit transaction
            transaction.commit();

            return account; // Return the Account entity or null if not found
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null; // Return null on exception
        }
    }

    public static void main(String[] args) {
        AccountDAO accountDAO =new AccountDAO();
        Account account = new Account();
        account.setUsername("Jimmy");
        account.setPassword("123");
        accountDAO.createAccount(account);
    }
}
