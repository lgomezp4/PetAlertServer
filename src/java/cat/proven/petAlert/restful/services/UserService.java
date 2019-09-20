package cat.proven.petAlert.restful.services;

import cat.proven.petAlert.logger.ServerLogger;
import cat.proven.petAlert.model.Alert;
import cat.proven.petAlert.model.User;
import cat.proven.petAlert.model.Model;
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
 * REST Web Service for users.
 *
 * @author Pet Alert
 */
@Path("/users")
public class UserService {

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
    public UserService(@Context ServletContext context) {
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
     * Show all users.
     *
     * @return Json with list of users and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public String showUsers() {
        List<User> users;
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            users = model.findAllUsers();
            if (users != null) {
                if (!users.isEmpty()) {
                    result = new RequestResult(users, 1);
                } else {
                    result = new RequestResult("No results", 0);
                }
            } else {
                result = new RequestResult("Database error", -1);
            }
        } catch (IllegalStateException ex) {
            result = new RequestResult("Error", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show users by id.
     *
     * @param id to search
     * @return Json with user and result code. Code: 1 OK, 0 user doesnt exist,
     * -1 Error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showUsersById(@PathParam("id") String id) {
        User user;
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            user = model.findUserById(Integer.parseInt(id));
            if (user != null) {
                result = new RequestResult(user, 1);
            } else {
                result = new RequestResult("User ID doesnt exist", 0);
            }
        } catch (NumberFormatException | IllegalStateException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show users by token
     *
     * @param token to search
     * @return Json user and result code. Code: 1 OK, 0 user doesnt exist, -1
     * Error
     */
    @GET
    @Path("/token/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showUsersByToken(@PathParam("token") String token) {
        User user;
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            user = model.findUserByToken(token);
            if (user != null) {
                result = new RequestResult(user, 1);
            } else {
                result = new RequestResult("User not found", 0);
            }
        } catch (NumberFormatException | IllegalStateException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show users by username.
     *
     * @param username to search
     * @return Json with user and result code. Code: 1 OK, 0 username doesnt
     * exist, -1 Error
     */
    @GET
    @Path("/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showUsersByUsername(@PathParam("username") String username) {
        User user;
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            user = model.findUserByUsername(username);
            if (user != null) {
                result = new RequestResult(user, 1);
            } else {
                result = new RequestResult("Username doesnt exist", 0);
            }
        } catch (IllegalStateException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Show user by alert id.
     *
     * @param id alert id
     * @return Json with user and result code. Code: 1 OK, 0 user doesnt exist,
     * -1 Error
     */
    @GET
    @Path("/alert/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String showUsersByAlert(@PathParam("id") String id) {
        User user;
        RequestResult result;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            Alert alert = model.findAlertById(Integer.parseInt(id));
            if (alert != null) {
                user = alert.getUserId();
                result = new RequestResult(user, 1);
            } else {
                result = new RequestResult("User ID doesnt exist", 0);
            }
        } catch (NumberFormatException | IllegalStateException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return gson.toJson(result);
    }

    /**
     * Add an user.
     *
     * @param user to add
     * @return Json with message and result code. Code: 1 OK, -1 error adding
     * user or error in parameters
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String addUser(@FormParam("user") String user) {
        RequestResult result;
        try {
            User newUser = new Gson().fromJson(user, User.class);
            if (newUser != null) {
                User toCheck = model.findUserByMail(newUser.getMail());
                if (toCheck == null) {
                    int code = model.addUser(newUser);
                    if (code == 1) {
                        result = new RequestResult("User added successfully", 1);
                    } else {
                        result = new RequestResult("Error adding user", -1);
                    }
                } else {
                    result = new RequestResult("Mail already exists", -2);
                }
            } else {
                result = new RequestResult("Error in parameters", -1);
            }
        } catch (IllegalStateException | JsonSyntaxException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }

        return new Gson().toJson(result);
    }

    /**
     * Modify user
     *
     * @param user to modify
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK, -1 error, 0 user
     * doesnt exist, -10 invalid token, -11 expired token, -12 Error assigning
     * token.
     */
    @POST
    @Path("/modify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String modifyUser(@FormParam("user") String user, @FormParam("token") String token) {
        RequestResult result;
        int option;
        try {
            User update = new Gson().fromJson(user, User.class);
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        if (update != null) {
                            int code = model.modifyUser(update);
                            if (code == 1) {
                                result = new RequestResult("User modified successfully", 1);
                            } else {
                                result = new RequestResult("Error modifying user", -1);
                            }
                        } else {
                            result = new RequestResult("User doesnt exist", 0);
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
        } catch (IllegalStateException | JsonSyntaxException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return new Gson().toJson(result);
    }

    /**
     * Modify an user password.
     *
     * @param userId to search user
     * @param password to change
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK, -1 error, 0 user
     * id doesnt exist, -10 invalid token, -11 expired token, -12 Error
     * assigning token.
     */
    @POST
    @Path("/modifyPassword")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String modifyUserPwd(@FormParam("userId") String userId,
            @FormParam("password") String password, @FormParam("token") String token) {
        RequestResult result;
        User user;
        int option;
        try {
            int uId = new Gson().fromJson(userId, Integer.class);
            String uPass = new Gson().fromJson(password, String.class);
            user = model.findUserById(uId);
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        if (user != null) {
                            if (user.getToken().equals(tokenToCheck)) {
                                result = new RequestResult("Invalid token", -10);
                            } else {
                                if (uPass != null) {
                                    int code = model.modifyUserPassword(user, uPass);
                                    if (code == 1) {
                                        result = new RequestResult("Password modified successfully", 1);
                                    } else {
                                        result = new RequestResult("Error modifying password", -1);
                                    }
                                }else{
                                    result = new RequestResult("Error in parameters", -1);
                                }
                            }
                        } else {
                            result = new RequestResult("User ID doesnt exist", 0);
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
        } catch (JsonSyntaxException | IllegalStateException | IllegalArgumentException ex) {
            result = new RequestResult("Error in parameters", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return new Gson().toJson(result);
    }

    /**
     * Block user
     *
     * @param userId to search user
     * @param token secure the method for logged in user
     * @return Json with message and result code. Code: 1 OK, -1 error, 0 user
     * id doesnt exist, -10 invalid token, -11 expired token, -12 Error
     * assigning token.
     */
    @POST
    @Path("/blockUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String blockUser(@FormParam("userId") String userId, @FormParam("token") String token) {
        RequestResult result;
        User user;
        int option;
        try {
            user = model.findUserById(Integer.parseInt(userId));
            String tokenToCheck = new Gson().fromJson(token, String.class);
            if (tokenToCheck != null) {
                option = securePost(tokenToCheck);
                switch (option) {
                    case 1: //token ok
                        if (user != null) {
                            int code = model.blockUser(user);
                            if (code == 1) {
                                result = new RequestResult("User blocked successfully", 1);
                            } else {
                                result = new RequestResult("Error blocking password", -1);
                            }
                        } else {
                            result = new RequestResult("User ID doesnt exist", 0);
                        }
                        break;
                    case 0: //token caducado
                        result = new RequestResult("Expired token", -11);
                        break;
                    case -10: //token no valido
                        result = new RequestResult("Invalid token", -10);
                        break;
                    default: //error al asignar token
                        result = new RequestResult("Error assigning token", -12);
                        break;
                }
            } else {
                result = new RequestResult("Error in token parameters", -1);
            }
        } catch (JsonSyntaxException | IllegalStateException | NumberFormatException ex) {
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
        if (!userTime.before(ts)) {
            result = 1;
        } else {
            //password is nedded
            result = 0;
        }
        return result;
    }

}
