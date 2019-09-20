package cat.proven.petAlert.model;

import cat.proven.petAlert.logger.ServerLogger;
import cat.proven.petAlert.model.persist.DistanceDAO;
import cat.proven.petAlert.model.token.AuthToken;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 * Model DAO
 *
 * @author Pet Alert
 */
public class Model {

    EntityManager em;
    private ServerLogger logger;
    private DistanceDAO dDao;

    /**
     * Model class constructor
     *
     * @param logger ServerLogger object
     */
    public Model(ServerLogger logger) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PetAlertPU");
        this.em = emf.createEntityManager();
        this.logger = logger;
        dDao = new DistanceDAO();
    }

    //****** METHODS *****//
    //****** DISTANCE *****//
    /**
     * Looks for all alerts order by distance
     *
     * @param latitude north south position
     * @param longitude east west position
     * @return list of distances or null in case of error.
     */
    public List<Distance> findAlertsByCoordinates(Double latitude, Double longitude) {
        List<Distance> found;
        found = dDao.selectWhereDistance(latitude, longitude);
        return found;
    }

    //****** ALERTS *****//
    /**
     * Looks for all alerts in the datebase.
     *
     * @return list of alerts or null in case of error.
     */
    public List<Alert> findAllAlerts() {
        List<Alert> alerts;
        try {
            Query query = em.createNamedQuery("Alert.findAll");
            alerts = query.getResultList();
        } catch (PersistenceException ex) {
            alerts = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return alerts;
    }

    /**
     * Looks for an alert by id in the database.
     *
     * @param id primary key to search.
     * @return an alert or null in case of error.
     */
    public Alert findAlertById(int id) {
        Alert alert;
        try {
            Query query = em.createNamedQuery("Alert.findById");
            query.setParameter("id", id);
            alert = (Alert) query.getSingleResult();
        } catch (NumberFormatException | PersistenceException ex) {
            alert = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return alert;
    }

    /**
     * Looks for alerts by animal kind in the database.
     *
     * @param kind animal kind to search.
     * @return list of alerts or null in case of error.
     */
    public List<Alert> findAlertsByAnimalKind(String kind) {
        List<Alert> alerts;
        try {
            Query query = em.createNamedQuery("Alert.findByAnimalKind");
            query.setParameter("kind", kind);
            alerts = query.getResultList();
        } catch (PersistenceException ex) {
            alerts = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return alerts;
    }

    /**
     * Looks for alerts by animal kind and race in the database.
     *
     * @param kind animal kind to search.
     * @param race animal race to search.
     * @return list of alerts or null in case of error.
     */
    public List<Alert> findAlertsByAnimalKindAndRace(String kind, String race) {
        List<Alert> alerts;
        try {
            Query query = em.createNamedQuery("Alert.findByAnimalKind_Race");
            query.setParameter("kind", kind);
            query.setParameter("race", race);
            alerts = query.getResultList();
        } catch (PersistenceException ex) {
            alerts = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return alerts;
    }

    /**
     * Looks for alerts by animal kind, race and sex in the database.
     *
     * @param kind animal kind to search.
     * @param race animal race to search.
     * @param sex animal sex to search.
     * @return list of alerts or null in case of error.
     */
    public List<Alert> findAlertsByAnimalKindRaceAndSex(String kind, String race, String sex) {
        List<Alert> alerts;
        try {
            Query query = em.createNamedQuery("Alert.findByAnimalKind_Race_Sex");
            query.setParameter("kind", kind);
            query.setParameter("race", race);
            query.setParameter("sex", sex);
            alerts = query.getResultList();
        } catch (PersistenceException ex) {
            alerts = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return alerts;
    }

    /**
     * Looks for alerts reported 3 or more times.
     *
     * @return list of alerts or null in case of error.
     */
    public List<Alert> findAlertsByReport() {
        List<Alert> alerts;
        try {
            Query query = em.createNamedQuery("Alert.findByReportNumber");
            alerts = query.getResultList();
        } catch (PersistenceException ex) {
            alerts = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return alerts;
    }

    /**
     * Adds an alert in database.
     *
     * @param a alert to insert.
     * @return 1 if alert added successfully, 0 if user dont exist, -1 in case
     * of error.
     */
    public int addAlert(Alert a) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User toFind = em.find(User.class, a.getUserId().getId());
            if (toFind != null) {
                a.setUserId(toFind);
                em.persist(a);
                tx.commit();
                result = 1;
            } else {
                result = 0;
            }
        } catch (PersistenceException | DatabaseException ex) {
            tx.rollback();
            result = -1;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    /**
     * Modify an alert in database.
     *
     * @param update alert to modify.
     * @return 1 if alert modified successfully, -1 in case of error.
     */
    public int modifyAlert(Alert update) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            Alert toModify = em.find(Alert.class, update.getId());
            tx.begin();
            em.detach(toModify);
            toModify.setCreationDate(update.getCreationDate());
            toModify.setActive(update.getActive());
            toModify.setReportNumber(update.getReportNumber());

            Coordinate c = em.getReference(Coordinate.class, update.getCoordId().getId());
            c.setLatitude(update.getCoordId().getLatitude());
            c.setLatitude(update.getCoordId().getLatitude());
            toModify.setCoordId(c);

            Animal a = em.getReference(Animal.class, update.getAnimalId().getId());
            a.setChipNum(update.getAnimalId().getChipNum());
            a.setName(update.getAnimalId().getName());
            a.setKind(update.getAnimalId().getKind());
            a.setHairColor(update.getAnimalId().getHairColor());
            a.setRace(update.getAnimalId().getRace());
            a.setHalfBlood(update.getAnimalId().getHalfBlood());
            a.setAge(update.getAnimalId().getAge());
            a.setSex(update.getAnimalId().getSex());
            a.setImage(update.getAnimalId().getImage());
            toModify.setAnimalId(a);

            Description d = em.getReference(Description.class, update.getDescId().getId());
            d.setTitle(update.getDescId().getTitle());
            d.setLostDayHour(update.getDescId().getLostDayHour());
            d.setDescription(update.getDescId().getDescription());
            d.setPhone(update.getDescId().getPhone());
            toModify.setDescId(d);

            em.merge(toModify);
            tx.commit();
            result = 1;
        } catch (PersistenceException | DatabaseException ex) {
            tx.rollback();
            result = -1;
            System.out.println(ex.getMessage());
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    /**
     * Mark the alert as finished.
     *
     * @param alert alert to finish.
     * @return 1 if alert finished successfully, -1 in case of error.
     */
    public int finishAlert(Alert alert) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            alert.setActive(false);
            tx.commit();
            result = 1;
        } catch (PersistenceException | DatabaseException ex) {
            tx.rollback();
            result = -1;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    /**
     * Report an alert.
     *
     * @param alert to report.
     * @return 1 if alert reported successfully, -1 in case of error.
     */
    public int reportAlert(Alert alert) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            int reportNum = alert.getReportNumber();
            reportNum++;
            alert.setReportNumber(reportNum);
            tx.commit();
            result = 1;
        } catch (PersistenceException | DatabaseException ex) {
            tx.rollback();
            result = -1;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    //****** COORDINATES ******//
    /**
     * Adds a coordinate in database.
     *
     * @param coordinate to add.
     * @return 1 if coordinate added successfully, -1 in case of error.
     */
    public int addCoordinate(Coordinate coordinate) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(coordinate);
            tx.commit();
            result = 1;
        } catch (PersistenceException | DatabaseException ex) {
            tx.rollback();
            result = -1;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    //****** USERS ******//
    /**
     * Looks for all users in database.
     *
     * @return list of users or null in case of error.
     */
    public List<User> findAllUsers() {
        List<User> users;
        try {
            Query query = em.createNamedQuery("User.findAll");
            users = query.getResultList();
        } catch (PersistenceException ex) {
            users = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return users;
    }

    /**
     * Looks for an user by id in the database.
     *
     * @param id primary key to search.
     * @return an user or null in case of error.
     */
    public User findUserById(int id) {
        User user;
        try {
            Query query = em.createNamedQuery("User.findById");
            query.setParameter("id", id);
            user = (User) query.getSingleResult();
        } catch (NumberFormatException | PersistenceException ex) {
            user = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return user;
    }
    
    /**
     * Looks for an user by mail in the database.
     *
     * @param mail param to search.
     * @return an user or null in case of error.
     */
    public User findUserByMail(String mail){
        User user;
        try {
            Query query = em.createNamedQuery("User.findByMail");
            query.setParameter("mail", mail);
            user = (User) query.getSingleResult();
        } catch (NumberFormatException | PersistenceException ex) {
            user = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return user;
    }

    /**
     * Looks for an user by token in the database.
     *
     * @param token param to search.
     * @return an user or null in case of error.
     */
    public User findUserByToken(String token) {
        User user;
        try {
            Query query = em.createNamedQuery("User.findByToken");
            query.setParameter("token", token);
            user = (User) query.getSingleResult();
        } catch (NumberFormatException | PersistenceException ex) {
            user = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return user;
    }

    /**
     * Looks for an user by username in the database.
     *
     * @param username param to search.
     * @return an user or null in case of error.
     */
    public User findUserByUsername(String username) {
        User user;
        try {
            Query query = em.createNamedQuery("User.findByUsername");
            query.setParameter("username", username);
            user = (User) query.getSingleResult();
        } catch (PersistenceException ex) {
            user = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return user;
    }

    /**
     * Adds an user in database.
     *
     * @param user to add.
     * @return 1 if user added succesfully, -1 in case of error.
     */
    public int addUser(User user) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
            result = 1;
        } catch (PersistenceException | DatabaseException ex) {
            result = -1;
            tx.rollback();
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    /**
     * Modify an user in database.
     *
     * @param update user to modify.
     * @return 1 if user modified succesfully, -1 in case of error.
     */
    public int modifyUser(User update) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            User toModify = em.find(User.class, update.getId());
            tx.begin();
            em.detach(toModify);
            toModify.setName(update.getName());
            toModify.setPassword(update.getPassword());
            toModify.setPopulation(update.getPopulation());
            em.merge(toModify);
            tx.commit();
            result = 1;
        } catch (PersistenceException | DatabaseException ex) {
            result = -1;
            tx.rollback();
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    /**
     * Modify password of an user in database.
     *
     * @param update user to modify.
     * @param password new password.
     * @return 1 if password modified successfully, -1 in case of error.
     */
    public int modifyUserPassword(User update, String password) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            User toModify = em.find(User.class, update.getId());
            tx.begin();
            em.detach(toModify);
            toModify.setPassword(password);
            em.merge(toModify);
            tx.commit();
            result = 1;
        } catch (PersistenceException | DatabaseException ex) {
            result = -1;
            tx.rollback();
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    /**
     * Block an user.
     *
     * @param update user to block.
     * @return 1 if user blocked successfully, -1 in case of error.
     */
    public int blockUser(User update) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            User toModify = em.find(User.class, update.getId());
            tx.begin();
            em.detach(toModify);
            toModify.setActive(false);
            em.merge(toModify);
            tx.commit();
            result = 1;
        } catch (PersistenceException | DatabaseException ex) {
            result = -1;
            tx.rollback();
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    //****** MESSAGES *****//
    /**
     * Looks for all messages in database.
     *
     * @return list of messages or null in case of error.
     */
    public List<Message> findAllMessages() {
        List<Message> messages;
        try {
            Query query = em.createNamedQuery("Message.findAll");
            messages = query.getResultList();
        } catch (PersistenceException ex) {
            messages = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return messages;
    }

    /**
     * Looks for a message in database.
     *
     * @param id primary key to search.
     * @return a message or null in case of error.
     */
    public Message findMessage(int id) {
        Message message;
        try {
            Query query = em.createNamedQuery("Message.findById");
            query.setParameter("id", id);
            message = (Message) query.getSingleResult();
        } catch (PersistenceException ex) {
            message = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return message;
    }

    /**
     * Looks for messages sent by an user.
     *
     * @param id user primary key to search.
     * @return list of messages or null in case of error.
     */
    public List<Message> findUserMessagesSent(int id) {
        List<Message> messages;
        try {
            Query query = em.createNamedQuery("Message.findByUserSent");
            query.setParameter("id", id);
            messages = query.getResultList();
        } catch (PersistenceException ex) {
            messages = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return messages;
    }

    /**
     * Looks for messages received by an user.
     *
     * @param id user primary key to search.
     * @return list of messages or null in case of error.
     */
    public List<Message> findUserMessagesReceived(int id) {
        List<Message> messages;
        try {
            Query query = em.createNamedQuery("Message.findByUserReceived");
            query.setParameter("id", id);
            messages = query.getResultList();
        } catch (PersistenceException ex) {
            messages = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return messages;
    }

    /**
     * Adds a message in database.
     *
     * @param message to add.
     * @return 1 if message added successfully, -1 in case of error.
     */
    public int addMessage(Message message) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(message);
            tx.commit();
            result = 1;
        } catch (DatabaseException | PersistenceException ex) {
            tx.rollback();
            result = -1;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    /**
     * Marks message as hide in database.
     *
     * @param message to hide.
     * @param who for the sender or receiver.
     * @return 1 if message marked as hide, -1 in case of error.
     */
    public int hideMessage(Message message, String who) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            switch (who) {
                case "sent":
                    message.setSenderActive(false);
                    break;
                case "received":
                    message.setReceiverActive(false);
                    break;
            }
            tx.commit();
            result = 1;
        } catch (DatabaseException | PersistenceException ex) {
            tx.rollback();
            result = -1;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    //****** LOGIN *****//
    /**
     * Adds token to user.
     *
     * @param user user to assign token.
     * @param token to add.
     * @return 1 if successfully or -1 in case of error.
     */
    public int addToken(User user, AuthToken token) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            user.setToken(token.getToken());
            user.setExpiration(token.getExpiration());
            user.setSourceIP(token.getSourceIP());
            tx.commit();
            result = 1;
        } catch (DatabaseException | PersistenceException ex) {
            tx.rollback();
            result = -1;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    /**
     * Looks for expiration time of an user in database.
     *
     * @param token to search.
     * @return user expiration or null in case of error.
     */
    public Date findUserExpiration(String token) {
        Date expiration;
        try {
            Query query = em.createNamedQuery("User.findUserExpiration");
            query.setParameter("token", token);
            expiration = (Date) query.getSingleResult();
        } catch (PersistenceException ex) {
            expiration = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return expiration;
    }

    /**
     * Looks for all tokens in database.
     *
     * @return list of token or null in case of error.
     */
    public List<String> findAllTokens() {
        List<String> tokens;
        try {
            Query query = em.createNamedQuery("User.findAllTokens");
            tokens = query.getResultList();
        } catch (PersistenceException ex) {
            tokens = null;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return tokens;
    }

    /**
     * Removes token of an user in database.
     *
     * @param user to seach.
     * @return 1 if token successfully removed or -1 in case of error.
     */
    public int removeToken(User user) {
        int result;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            user.setToken(null);
            tx.commit();
            result = 1;
        } catch (DatabaseException | PersistenceException ex) {
            tx.rollback();
            result = -1;
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

}
