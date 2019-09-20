package cat.proven.petAlert.model;

import cat.proven.petAlert.model.Alert;
import cat.proven.petAlert.model.Message;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-29T18:55:00")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> mail;
    public static volatile SingularAttribute<User, Boolean> active;
    public static volatile SingularAttribute<User, String> rol;
    public static volatile SingularAttribute<User, String> token;
    public static volatile SingularAttribute<User, String> population;
    public static volatile ListAttribute<User, Alert> alertsList;
    public static volatile SingularAttribute<User, String> password;
    public static volatile ListAttribute<User, Message> messagesList1;
    public static volatile SingularAttribute<User, String> sourceIP;
    public static volatile ListAttribute<User, Message> messagesList;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, Date> expiration;
    public static volatile SingularAttribute<User, Integer> id;
    public static volatile SingularAttribute<User, String> username;

}