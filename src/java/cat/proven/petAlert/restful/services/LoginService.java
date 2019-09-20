package cat.proven.petAlert.restful.services;

import cat.proven.petAlert.logger.ServerLogger;
import cat.proven.petAlert.model.Model;
import cat.proven.petAlert.model.User;
import cat.proven.petAlert.model.token.AuthToken;
import cat.proven.petAlert.restful.RequestResult;
import com.google.gson.Gson;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Login Web Service
 *
 * @author Pet Alert
 */
@Path("/token")
public class LoginService {

    private Model model;
    private ServerLogger logger;
    private final int sec = 3600 * 24; //24 hours

    /**
     * Constructor
     *
     * @param context
     */
    public LoginService(@Context ServletContext context) {
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
     * Retrieves all tokens.
     *
     * @return Json with list of tokens and result code. Code: 1 OK, 0 No
     * results, -1 Error
     */
    @GET
    @Path("/")
    public String getTokens() {
        List<String> list;
        RequestResult result;
        try {
            list = model.findAllTokens();
            if (list != null) {
                if (!list.isEmpty()) {
                    result = new RequestResult(list, 1);
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
        return new Gson().toJson(result);
    }

    /**
     * Login with username and password
     *
     * @param username username
     * @param password password
     * @return Json with token and result code. Code: 1 OK, -1 error assigning
     * token, -2 user doenst exist, -3 password does not match, -5 login fail.
     */
    @GET
    @Path("/login/{username}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String login(@PathParam("username") String username,
            @PathParam("password") String password) {
        //validate user credentials
        int code;
        RequestResult result;
        try {
            User user = model.findUserByUsername(username);
            //user exist
            if (user != null) {
                //check password
                boolean valid = user.getPassword().equals(password);               
                if (valid) {
                    if (user.getToken() != null) { //have token                                                            
                        code = validateToken(user, 1); //check expiration
                        if (code == 1) {
                            result = new RequestResult(user.getToken(), 1);
                        } else {
                            code = generateToken(user); //generate token
                            switch (code) {
                                case 1:
                                    result = new RequestResult(user.getToken(), 1);
                                    break;
                                default:
                                    result = new RequestResult("Error assigning token", -1);
                                    break;
                            }
                        }
                    } else { //user dont have token                                                
                        code = generateToken(user); //generate authorization token
                        switch (code) {
                            case 1:
                                result = new RequestResult(user.getToken(), 1);
                                break;
                            default:
                                result = new RequestResult("Error assigning token", -1);
                                break;
                        }
                    }
                } else {
                    //incorrect password
                    result = new RequestResult("Password does not match", -3);
                }
            } else {
                //user doesn't exist
                result = new RequestResult("User doenst exist", -2);
            }
        } catch (IllegalStateException ex) {
            result = new RequestResult("Server error", -5);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return new Gson().toJson(result);
    }

    /**
     * Login with user token
     *
     * @param token to search
     * @return Json with token and result code. Code: 1 OK, 0 password required,
     * -1 no match token, -2 user dont have token, -5 login fail.
     */
    @GET
    @Path("/loginToken/{token}")
    public String loginToken(@PathParam("token") String token) {
        RequestResult result;       
        int code;
        try {
            User user = model.findUserByToken(token);
            if (user != null) {
                if (user.getToken() != null) {
                    //validate token
                    code = validateToken(user, 2);
                    if (code == 1) {
                        result = new RequestResult(user.getToken(), 1);                       
                    } else {
                        //password required              
                        result = new RequestResult("Password required", 0);
                    }
                } else {
                    //user doesn't exist
                    result = new RequestResult("User dont have token", -2);
                }
            } else {
                result = new RequestResult("No user with this token", -1);
            }
        } catch (IllegalStateException ex) {
            result = new RequestResult("Login fail", -5);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }

        return new Gson().toJson(result);
    }

    /**
     * Check if token is valid and belongs to the user.
     *
     * @param user to check
     * @param option 1 generates new token, 0 nothing
     * @return result: 1 OK, -1 NOK, 0 session expired.
     */
    private int validateToken(User user, int option) {
        int result;
        Date userDate = user.getExpiration();
        Timestamp userTime = new Timestamp(userDate.getTime());
        //adds 24 hours to the last registered date of the user
        Instant instant = userTime.toInstant().plusSeconds(sec);
        userTime = userTime.from(instant);
        //current time       
        Date d = new Date();
        Timestamp ts = new Timestamp(d.getTime());
        //check expiration session
        //user's last registered date + 24 hours are before the current time
        if (!userTime.before(ts)) {          
            //renew token
            int code = generateToken(user);
            if (code == 1) {
                result = 1; //OK
            } else {
                result = -1; //NOK
            }
        } else {
            if (option == 1) {
                int code = generateToken(user);
                if (code == 1) {
                    result = 1; //OK
                } else {
                    result = -1; //NOK
                }
            } else {
                result = 0; //password is needed
            }
        }

        return result;
    }

    /**
     * Removes user token
     *
     * @param token to destroy
     * @return 1 if successfully removed, -2 token not found, -1 otherwise.
     */
    @GET
    @Path("/logout/{token}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String logout(@PathParam("token") String token) {
        RequestResult result;
        User user;
        try {
            user = model.findUserByToken(token);
            if (user != null) {
                int code = model.removeToken(user);
                if (code == 1) {
                    result = new RequestResult("Token removed succesfully", 1);
                } else {
                    result = new RequestResult("Error removing token", -1);
                }
            } else {
                result = new RequestResult("Token not found", -2);
            }
        } catch (IllegalStateException ex) {
            result = new RequestResult("Error logout", -1);
            logger.getLogger().log(Level.SEVERE, ex.getMessage());
        }
        return new Gson().toJson(result);
    }

    /**
     * Generates token for user
     *
     * @param user
     * @return 1 if success or -1 in case of error
     */
    private int generateToken(User user) {
        AuthToken tokenObj = null;
        int code;
        if (user != null) {
            Date date = new Date();
            Timestamp expiration = new Timestamp(date.getTime());
            tokenObj = new AuthToken(null, user.getUsername(),
                    expiration, "0.0.0.0");
            tokenObj.generate();
            code = model.addToken(user, tokenObj);
        } else {
            code = -1;
        }
        return code;
    }
}
