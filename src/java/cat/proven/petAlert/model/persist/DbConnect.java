package cat.proven.petAlert.model.persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection
 *
 * @author Pet Alert
 */
public class DbConnect {

    private static DbConnect instance;
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String BD_URL = "jdbc:mysql://" + "localhost:3306/dam1905?noAccessToProcedureBodies=true";
    private final String USUARI = "dam1905";
    private final String PASSWORD = "Ew5kaer9!";

    /**
     * Singleton constructor.
     */
    private DbConnect() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.append("Instalation error. Contact to the admin");
            System.exit(-1);
        }
    }

    /**
     * Singelton Instance for conection to the data base.
     *
     * @return unique instance.
     */
    public static DbConnect getInstance() {
        if (instance == null) {
            instance = new DbConnect();
        }
        return instance;
    }

    /**
     * Conection for the data base.
     *
     * @return connection or null in case of error.
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(BD_URL, USUARI, PASSWORD);
        } catch (SQLException e) {
            conn = null;
        }
        return conn;
    }

}
