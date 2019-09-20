package cat.proven.petAlert.restful;

import java.util.Set;
import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

/**
 * REST Application configuration
 *
 * @author Pet Alert
 */
@ApplicationPath("/services")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(cat.proven.petAlert.restful.services.AlertsService.class);
        resources.add(cat.proven.petAlert.restful.services.LoginService.class);
        resources.add(cat.proven.petAlert.restful.services.MessagesService.class);
        resources.add(cat.proven.petAlert.restful.services.UserService.class);
    }
}
