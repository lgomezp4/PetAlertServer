package cat.proven.petAlert.model;

import cat.proven.petAlert.model.Animal;
import cat.proven.petAlert.model.Coordinate;
import cat.proven.petAlert.model.Description;
import cat.proven.petAlert.model.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-29T18:55:00")
@StaticMetamodel(Alert.class)
public class Alert_ { 

    public static volatile SingularAttribute<Alert, Coordinate> coordId;
    public static volatile SingularAttribute<Alert, Description> descId;
    public static volatile SingularAttribute<Alert, Integer> reportNumber;
    public static volatile SingularAttribute<Alert, Boolean> active;
    public static volatile SingularAttribute<Alert, Integer> id;
    public static volatile SingularAttribute<Alert, Long> creationDate;
    public static volatile SingularAttribute<Alert, Animal> animalId;
    public static volatile SingularAttribute<Alert, User> userId;

}