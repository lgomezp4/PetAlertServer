package cat.proven.petAlert.model;

import cat.proven.petAlert.model.Alert;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-29T18:55:00")
@StaticMetamodel(Description.class)
public class Description_ { 

    public static volatile SingularAttribute<Description, String> phone;
    public static volatile SingularAttribute<Description, String> description;
    public static volatile SingularAttribute<Description, Integer> id;
    public static volatile SingularAttribute<Description, String> title;
    public static volatile SingularAttribute<Description, Long> lostDayHour;
    public static volatile ListAttribute<Description, Alert> alertsList;

}