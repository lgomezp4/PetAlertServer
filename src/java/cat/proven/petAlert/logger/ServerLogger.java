package cat.proven.petAlert.logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * ServerLogger class.
 *
 * @author Pet Alert
 */
public class ServerLogger {

    private Logger logger;

    public ServerLogger() {
        loadConfig();
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * Configuration of logger properties. Will add the information in a file
     * with a maximum size of 10k bytes on a rotating basis in 3 files.
     */
    private void loadConfig() {
        String file = "/home/alumnes/dam1905/serverLogs/log_%.txt";
        logger = Logger.getLogger("cat.proven.petAlert.services");
        logger.setUseParentHandlers(false);
        try {
            FileHandler handler;
            handler = new FileHandler(file, 10000, 3, true);
            handler.setLevel(Level.WARNING);
            handler.setFormatter(new SimpleFormatter()); //new XMLFormatter()
            logger.addHandler(handler);
        } catch (FileNotFoundException ex) {
            logger.setUseParentHandlers(true);
            Logger.getLogger("cat.proven.petAlert.services").log(Level.SEVERE, ex.getMessage());
        } catch (IOException | SecurityException ex) {
            logger.setUseParentHandlers(true);
            Logger.getLogger("cat.proven.petAlert.services").log(Level.SEVERE, ex.getMessage());
        }
    }
}
