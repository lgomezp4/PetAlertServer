package cat.proven.petAlert.model;

import cat.proven.petAlert.model.Alert;
import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-29T18:55:00")
@StaticMetamodel(Animal.class)
public class Animal_ { 

    public static volatile SingularAttribute<Animal, byte[]> image;
    public static volatile SingularAttribute<Animal, String> race;
    public static volatile SingularAttribute<Animal, Boolean> halfBlood;
    public static volatile SingularAttribute<Animal, BigInteger> chipNum;
    public static volatile SingularAttribute<Animal, String> kind;
    public static volatile SingularAttribute<Animal, String> sex;
    public static volatile SingularAttribute<Animal, String> name;
    public static volatile SingularAttribute<Animal, Integer> id;
    public static volatile SingularAttribute<Animal, String> hairColor;
    public static volatile SingularAttribute<Animal, Integer> age;
    public static volatile ListAttribute<Animal, Alert> alertsList;

}