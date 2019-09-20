package cat.proven.petAlert.restful.services;

import cat.proven.petAlert.logger.ServerLogger;
import cat.proven.petAlert.model.Alert;
import cat.proven.petAlert.model.Distance;
import cat.proven.petAlert.model.Model;
import cat.proven.petAlert.model.User;
import cat.proven.petAlert.restful.RequestResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service for alerts.
 *
 * @author Pet Alert
 */
@Path("/alerts")
public class AlertsService {

    private Model model;
    private ServerLogger logger;
    private Gson gson;
    private final int sec = 3600 * 24; //24 hours

    /**
     * Constructor. It gets a reference to the Model and ServerLogger, saves it
     * in the application context to
     *
     * @param context the application context
     */
    public AlertsService(@Context ServletContext context) {
        if (context.getAttribute("model") != null) {
            logger = (ServerLogger) context.getAttribute("logger");
            model = (Model) context.getAttribute("model");
        } else {
            logger = new ServerLogger();
            model = new Model(logger);
            context.setAttribute("logger", logger);
            context.setAttribute("model", model);
        }
    }

    /**
     * Shows all alerts of five on five.
     *
     * @param number from the position to be displayed
     * @return Json with list of alerts and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/all/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showAlerts(@PathParam("number") String number) {
        List<Alert> alerts;
        List<Alert> shortAlerts = new ArrayList<>();
        //exclude fields object without @Expose annotation
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        RequestResult result;
        try {
            int n = Integer.parseInt(number); //n = list position
            int listSize;
            alerts = model.findAllAlerts(); //alerts in database
            if (alerts != null) {
                if (!alerts.isEmpty()) {
                    if (n > alerts.size()) { //if n is greater than five
                        result = new RequestResult("No results", 0);
                    } else {
                        if (alerts.size() < n + 5) { //if the rest is less than five
                            listSize = alerts.size();
                        } else { //five alerts
                            listSize = n + 5;
                        }
                        for (int i = n; i < listSize; i++) {
                            shortAlerts.add(alerts.get(i)); //saves alerts from the given position
                        }
                        result = new RequestResult(shortAlerts, 1);
                    }
                } else {
                    result = new RequestResult("No results", 0);
                }
            } else {
                result = new RequestResult("Database error", -1);
            }
        } catch (IllegalStateException | JsonSyntaxException | NumberFormatException ex) {
            result = new RequestResult("Error", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Shows all alerts order by distance of five on five.
     *
     * @param latitude north south position
     * @param longitude east west position
     * @param number from the position to be displayed
     * @return Json with list of alerts and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/distance/{latitude}/{longitude}/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showAlertsByDistance(@PathParam("latitude") String latitude,
            @PathParam("longitude") String longitude, @PathParam("number") String number) {
        List<Distance> distances;
        List<Alert> shortAlerts = new ArrayList<>();
        RequestResult result;
        List<Alert> alerts;
        //exclude fields object without @Expose annotation
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            int n = Integer.parseInt(number); //n = list position
            int listSize;
            distances = model.findAlertsByCoordinates(Double.parseDouble(latitude),
                    Double.parseDouble(longitude)); //id alerts order by distance
            alerts = model.findAllAlerts(); //alerts in database
            if (distances != null && alerts != null) {
                if (!distances.isEmpty()) {
                    if (n > alerts.size()) { //if n is greater than five
                        result = new RequestResult("No results", 0);
                    } else {
                        if (distances.size() < n + 5) { //if the rest is less than five
                            listSize = distances.size();
                        } else { //five alerts
                            listSize = n + 5;
                        }
                        for (int i = n; i < listSize; i++) {
                            int x = distances.get(i).getId();
                            int index = alerts.indexOf(new Alert(x));
                            shortAlerts.add(alerts.get(index)); //saves alerts from the given position
                        }
                        result = new RequestResult(shortAlerts, 1);
                    }
                } else {
                    result = new RequestResult("No results", 0);
                }
            } else {
                result = new RequestResult("Database error", -1);
            }
        } catch (IllegalStateException | JsonSyntaxException | NumberFormatException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show alert by id.
     *
     * @param id to filter
     * @return Json with alert and result code. Code: 1 OK, 0 No results, -1
     * Error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showAlertsById(@PathParam("id") String id) {
        Alert alert;
        RequestResult result;
        //exclude fields object without @Expose annotation
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            alert = model.findAlertById(Integer.parseInt(id)); //retrieve alert from database
            if (alert != null) {
                result = new RequestResult(alert, 1);
            } else {
                result = new RequestResult("Alert ID doesnt exist", 0);
            }
        } catch (IllegalStateException | JsonSyntaxException | NumberFormatException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show alerts by animal kind.
     *
     * @param kind animal kind, dog, cat or other
     * @param number from the position to be displayed
     * @return Json with list of alerts and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/animal/{kind}/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showAlertsByAnimalKind(@PathParam("kind") String kind,
            @PathParam("number") String number) {
        //POR AQUÍ que devuelva de 5 en 5
        List<Alert> alerts;
        List<Alert> shortAlerts = new ArrayList<>();
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            int n = Integer.parseInt(number); //n = list position
            int listSize;
            alerts = model.findAlertsByAnimalKind(kind);
            if (alerts != null) {
                if (!alerts.isEmpty()) {
                    if (n > alerts.size()) { //if n is greater than five
                        result = new RequestResult("No results", 0);
                    } else {
                        if (alerts.size() < n + 5) { //if the rest is less than five
                            listSize = alerts.size();
                        } else { //five alerts
                            listSize = n + 5;
                        }
                        for (int i = n; i < listSize; i++) {
                            shortAlerts.add(alerts.get(i)); //saves alerts from the given position
                        }
                        result = new RequestResult(shortAlerts, 1);
                    }
                } else {
                    result = new RequestResult("No results", 0);
                }
            } else {
                result = new RequestResult("Database error", -1);
            }
        } catch (IllegalStateException | JsonSyntaxException | NumberFormatException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show alerts by animal kind and race.
     *
     * @param kind animal kind, dog, cat or other
     * @param race animal race to filter
     * @param number from the position to be displayed
     * @return Json with list of alerts and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/animal/{kind}/race/{race}/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showAlertsByAnimalKindAndRace(@PathParam("kind") String kind,
            @PathParam("race") String race, @PathParam("number") String number) {
        List<Alert> alerts;
        List<Alert> shortAlerts = new ArrayList<>();
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            int n = Integer.parseInt(number); //n = list position
            int listSize;
            alerts = model.findAlertsByAnimalKindAndRace(kind, race);
            if (alerts != null) {
                if (!alerts.isEmpty()) {
                    if (n > alerts.size()) { //if n is greater than five
                        result = new RequestResult("No results", 0);
                    } else {
                        if (alerts.size() < n + 5) { //if the rest is less than five
                            listSize = alerts.size();
                        } else { //five alerts
                            listSize = n + 5;
                        }
                        for (int i = n; i < listSize; i++) {
                            shortAlerts.add(alerts.get(i)); //saves alerts from the given position
                        }
                        result = new RequestResult(shortAlerts, 1);
                    }
                } else {
                    result = new RequestResult("No results", 0);
                }
            } else {
                result = new RequestResult("Database error", -1);
            }
        } catch (IllegalStateException | JsonSyntaxException | NumberFormatException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show alerts by animal kind, race and sex.
     *
     * @param kind animal kind, dog, cat or other
     * @param race animal race to filter
     * @param sex animal sex to filter
     * @param number from the position to be displayed
     * @return Json with list of alerts and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/animal/{kind}/race/{race}/sex/{sex}/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showAlertsByAnimalKindRaceAndSex(@PathParam("kind") String kind,
            @PathParam("race") String race, @PathParam("sex") String sex,
            @PathParam("number") String number) {
        List<Alert> alerts;
        RequestResult result;
        List<Alert> shortAlerts = new ArrayList<>();
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            int n = Integer.parseInt(number); //n = list position
            int listSize;
            alerts = model.findAlertsByAnimalKindRaceAndSex(kind, race, sex);
            if (alerts != null) {
                if (!alerts.isEmpty()) {
                    if (n > alerts.size()) { //if n is greater than five
                        result = new RequestResult("No results", 0);
                    } else {
                        if (alerts.size() < n + 5) { //if the rest is less than five
                            listSize = alerts.size();
                        } else { //five alerts
                            listSize = n + 5;
                        }
                        for (int i = n; i < listSize; i++) {
                            shortAlerts.add(alerts.get(i)); //saves alerts from the given position
                        }
                        result = new RequestResult(shortAlerts, 1);
                    }
                } else {
                    result = new RequestResult("No results", 0);
                }
            } else {
                result = new RequestResult("Database error", -1);
            }
        } catch (IllegalStateException | JsonSyntaxException | NumberFormatException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show alerts reported 3 or more times.
     *
     * @param number from the position to be displayed
     * @return Json with list of alerts and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/reported/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showAlertsByReport(@PathParam("number") String number) {
        List<Alert> alerts;
        RequestResult result;
        List<Alert> shortAlerts = new ArrayList<>();
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            int n = Integer.parseInt(number); //n = list position
            int listSize;
            alerts = model.findAlertsByReport();
            if (alerts != null) {
                if (!alerts.isEmpty()) {
                    if (n > alerts.size()) { //if n is greater than five
                        result = new RequestResult("No results", 0);
                    } else {
                        if (alerts.size() < n + 5) { //if the rest is less than five
                            listSize = alerts.size();
                        } else { //five alerts
                            listSize = n + 5;
                        }
                        for (int i = n; i < listSize; i++) {
                            shortAlerts.add(alerts.get(i)); //saves alerts from the given position
                        }
                        result = new RequestResult(shortAlerts, 1);
                    }
                } else {
                    result = new RequestResult("No results", 0);
                }
            } else {
                result = new RequestResult("Database error", -1);
            }
        } catch (IllegalStateException | JsonSyntaxException | NumberFormatException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Add an alert
     *
     * @param alert to add
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK,
     * -1 Parameter error, -10 invalid token, -11 expired token, -12
     * Error assigning token.
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String addAlert(@FormParam("alert") String alert, @FormParam("token") String token) {
        RequestResult result;
        int option;
        try {
            Alert newAlert = new Gson().fromJson(alert, Alert.class);
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        if (newAlert != null) {
                            int code = model.addAlert(newAlert);
                            if (code == 1) {
                                result = new RequestResult("Alert added successfully", 1);
                            } else {
                                result = new RequestResult("Error adding alerts", -1);
                            }
                        } else {
                            throw new JsonSyntaxException("Error in parameters");
                        }
                        break;
                    case 0: //token expired
                        result = new RequestResult("Expired token", -11);
                        break;
                    case -10: //not valid token
                        result = new RequestResult("Invalid token", -10);
                        break;
                    default: //error assigning token
                        result = new RequestResult("Error assigning token", -12);
                        break;
                }
            } else {
                result = new RequestResult("Error in token parameters", -1);
            }
        } catch (IllegalArgumentException | IllegalStateException | JsonSyntaxException ex) {
            result = new RequestResult(ex.getMessage(), -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return new Gson().toJson(result);
    }

    /**
     * Modify an alert
     *
     * @param alert to modify
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK,
     * -1 error, -10 invalid token, -11 expired token, -12
     * Error assigning token.
     */
    @POST
    @Path("/modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String modifyAlert(@FormParam("alert") String alert, @FormParam("token") String token) {
        RequestResult result;
        int option;
        try {
            Alert update = new Gson().fromJson(alert, Alert.class);
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        if (update != null) {
                            int code = model.modifyAlert(update);
                            if (code == 1) {
                                result = new RequestResult("Alert modified successfully", 1);
                            } else {
                                result = new RequestResult("Error modifying alert", -1);
                            }
                        } else {
                            throw new JsonSyntaxException("Error in parameters");
                        }
                        break;
                    case 0: //token expired
                        result = new RequestResult("Expired token", -11);
                        break;
                    case -10: //not valid token
                        result = new RequestResult("Invalid token", -10);
                        break;
                    default: //error assigning token
                        result = new RequestResult("Error assigning token", -12);
                        break;
                }
            } else {
                result = new RequestResult("Error in token parameters", -1);
            }
        } catch (IllegalArgumentException | IllegalStateException | JsonSyntaxException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }

        return new Gson().toJson(result);
    }

    /**
     * Finish an alert
     *
     * @param id alert id
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK,
     * -1 Parameter error, -10 invalid token, -11 expired token, -12
     * Error assigning token.
     */
    @POST
    @Path("/finish")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String finishAlert(@FormParam("id") String id, @FormParam("token") String token) {
        RequestResult result;
        int option;
        try {
            int alertId = new Gson().fromJson(id, Integer.class);
            Alert alert = model.findAlertById(alertId);
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        if (alert != null) {
                            int code = model.finishAlert(alert);
                            if (code == 1) {
                                result = new RequestResult("Alert finished", 1);
                            } else {
                                result = new RequestResult("Error ending alert", -1);
                            }
                        } else {
                            result = new RequestResult("Alert not found", -1);
                        }
                        break;
                    case 0: //token expired
                        result = new RequestResult("Expired token", -11);
                        break;
                    case -10: //token not valid
                        result = new RequestResult("Invalid token", -10);
                        break;
                    default: //error when assigning token
                        result = new RequestResult("Error assigning token", -12);
                        break;
                }
            } else {
                result = new RequestResult("Error in token parameters", -1);
            }
        } catch (IllegalArgumentException | IllegalStateException | JsonSyntaxException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return new Gson().toJson(result);
    }

    /**
     * Report an alert
     *
     * @param id alert id
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK,
     * -1 Parameter error, -10 invalid token, -11 expired token, -12
     * Error assigning token.
     */
    @POST
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String reportAlert(@FormParam("id") String id, @FormParam("token") String token) {
        RequestResult result;
        int option;
        try {
            int alertId = new Gson().fromJson(id, Integer.class);
            Alert alert = model.findAlertById(alertId);
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        if (alert != null) {
                            int code = model.reportAlert(alert);
                            if (code == 1) {
                                result = new RequestResult("Alert reported succesfully!", 1);
                            } else {
                                result = new RequestResult("Error reporting alert", -1);
                            }
                        } else {
                            result = new RequestResult("Alert not found", -1);
                        }
                        break;
                    case 0: //token expired
                        result = new RequestResult("Expired token", -11);
                        break;
                    case -10: //token not valid
                        result = new RequestResult("Invalid token", -10);
                        break;
                    default: //error when assigning token
                        result = new RequestResult("Error assigning token", -12);
                        break;
                }
            } else {
                result = new RequestResult("Error in token parameters", -1);
            }
        } catch (IllegalArgumentException | IllegalStateException | JsonSyntaxException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return new Gson().toJson(result);
    }

    /**
     * Cheks user token for POST methods.
     *
     * @param token to check
     * @return 1 if token ok, 0 if token expired, -10 if token does not match
     * the user's token
     */
    private int securePost(String token) {
        int code;
        User user = model.findUserByToken(token);
        if (user != null) {
            if (user.getToken().equals(token)) {
                code = validateToken(user);
            } else {
                code = -10;
            }
        } else {
            code = -10;
        }
        return code;
    }

    /**
     * Checks if token is alive or expired, if the token is correct it will be
     * renewed.
     *
     * @param user to check
     * @return 1 OK, -1 error assigning token, 0 token expired.
     */
    private int validateToken(User user) {
        int result;
        Date userDate = user.getExpiration();
        Timestamp userTime = new Timestamp(userDate.getTime());
        //adds 24 hours to the last registered date of the user
        Instant instant = userTime.toInstant().plusSeconds(sec);
        userTime = userTime.from(instant);
        //current server time        
        Date d = new Date();
        Timestamp ts = new Timestamp(d.getTime());
        //check expiration session
        if (!userTime.before(ts)) { //User's last registered date + 24 hours are before the current time
            result = 1;
        } else {
            //password is nedded
            result = 0;
        }
        return result;
    }

}
