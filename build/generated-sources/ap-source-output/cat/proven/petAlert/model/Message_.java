package cat.proven.petAlert.model;

import cat.proven.petAlert.model.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-29T18:55:00")
@StaticMetamodel(Message.class)
public class Message_ { 

    public static volatile SingularAttribute<Message, User> senderId;
    public static volatile SingularAttribute<Message, User> receiverId;
    public static volatile SingularAttribute<Message, Long> sendDate;
    public static volatile SingularAttribute<Message, Boolean> senderActive;
    public static volatile SingularAttribute<Message, Long> receiptDate;
    public static volatile SingularAttribute<Message, Integer> id;
    public static volatile SingularAttribute<Message, Boolean> receiverActive;
    public static volatile SingularAttribute<Message, String> title;
    public static volatile SingularAttribute<Message, String> content;

}