package cat.proven.petAlert.restful.services;

import cat.proven.petAlert.logger.ServerLogger;
import cat.proven.petAlert.model.Message;
import cat.proven.petAlert.model.Model;
import cat.proven.petAlert.model.User;
import cat.proven.petAlert.restful.RequestResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
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
 * REST Web Service for messages.
 *
 * @author Pet Alert
 */
@Path("/messages")
public class MessagesService {

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
    public MessagesService(@Context ServletContext context) {
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
     * Show all messages.
     *
     * @return Json with list of messages and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public String showMessages() {
        List<Message> messages;
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            messages = model.findAllMessages();
            if (messages != null) {
                if (!messages.isEmpty()) {
                    result = new RequestResult(messages, 1);
                } else {
                    result = new RequestResult("No results", 0);
                }
            } else {
                result = new RequestResult("Error", -1);
            }
        } catch (IllegalStateException ex) {
            result = new RequestResult("Error", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show user messages sent.
     *
     * @param userID to search
     * @return Json with list of messages and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/sent/{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showUserMessagesSent(@PathParam("userID") String userID) {
        List<Message> messages;
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            messages = model.findUserMessagesSent(Integer.parseInt(userID));
            if (messages != null) {
                if (!messages.isEmpty()) {
                    result = new RequestResult(messages, 1);
                } else {
                    result = new RequestResult("No messages sent", 0);
                }
            } else {
                result = new RequestResult("Error", -1);
            }
        } catch (NumberFormatException | IllegalStateException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show user messages received.
     *
     * @param userID to search
     * @return Json with list of messages and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/received/{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showUserMessagesReceived(@PathParam("userID") String userID) {
        List<Message> messages;
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            messages = model.findUserMessagesReceived(Integer.parseInt(userID));
            if (messages != null) {
                if (!messages.isEmpty()) {
                    result = new RequestResult(messages, 1);
                } else {
                    result = new RequestResult("No messages received", 0);;
                }
            } else {
                result = new RequestResult("Error", -1);
            }
        } catch (NumberFormatException | IllegalStateException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Add a message.
     *
     * @param message to add
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK, 0 sender and
     * receiver can not be the same user, -1 error sending, -2 receiver not
     * found, -3 sender not found, -10 invalid token, -11 expired token, -12
     * error assigning token.
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String addMessage(@FormParam("message") String message, @FormParam("token") String token) {
        RequestResult result;
        int option;
        try {
            Message newMessage = new Gson().fromJson(message, Message.class);
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        if (newMessage != null) {
                            User sender = model.findUserById(newMessage.getSenderId().getId());
                            User receiver = model.findUserById(newMessage.getReceiverId().getId());
                            if (sender != null) {
                                if (receiver != null) {
                                    if (!sender.equals(receiver)) {
                                        newMessage.setSenderId(sender);
                                        newMessage.setReceiverId(receiver);
                                        int code = model.addMessage(newMessage);
                                        if (code == 1) {
                                            result = new RequestResult("Message sent!", 1);
                                        } else {
                                            result = new RequestResult("Error sending message", -1);
                                        }
                                    } else {
                                        result = new RequestResult("Sender and receiver can not be the same user", 0);
                                    }
                                } else {
                                    result = new RequestResult("User receiver not found", -2);
                                }
                            } else {
                                result = new RequestResult("User sender not found", -3);
                            }
                        } else {
                            result = new RequestResult("Error in message parameters", -1);
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
        } catch (IllegalStateException | JsonSyntaxException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return new Gson().toJson(result);
    }

    /**
     * Hide a message
     *
     * @param id message id
     * @param who who is hiding
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK, 0 message doesnt
     * exist, -1 error hiding message, -10 invalid token, -11 expired token, -12
     * error assigning token.
     */
    @POST
    @Path("/hide")
    @Produces(MediaType.APPLICATION_JSON)
    public String hideMessage(@FormParam("id") String id,
            @FormParam("who") String who, @FormParam("token") String token) {
        RequestResult result;
        int option;
        int code;
        Message message;
        try {
            int messageId = new Gson().fromJson(id, Integer.class);
            String whoCheck = new Gson().fromJson(who, String.class);
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        message = model.findMessage(messageId);
                        if (message != null) {
                            code = model.hideMessage(message, whoCheck);
                            if (code == 1) {
                                result = new RequestResult("Message hidden!", 1);
                            } else {
                                result = new RequestResult("Error hiding message", -1);
                            }
                        } else {
                            result = new RequestResult("Message doesnt exist", 0);
                        }
                        break;
                    case 0: //token expired
                        result = new RequestResult("Expired token", -11);
                        break;
                    case -10: //token not valid
                        result = new RequestResult("Invalid token", -10);
                        break;
                    default: //error assigning token
                        result = new RequestResult("Error assigning token", -12);
                        break;
                }
            } else {
                result = new RequestResult("Error in token parameters", -1);
            }
        } catch (IllegalStateException | JsonSyntaxException ex) {
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
