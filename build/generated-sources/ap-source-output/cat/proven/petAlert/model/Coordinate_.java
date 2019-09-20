package cat.proven.petAlert.model;

import cat.proven.petAlert.model.Alert;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-05-29T18:55:00")
@StaticMetamodel(Coordinate.class)
public class Coordinate_ { 

    public static volatile SingularAttribute<Coordinate, BigDecimal> latitude;
    public static volatile SingularAttribute<Coordinate, Integer> id;
    public static volatile SingularAttribute<Coordinate, BigDecimal> longitude;
    public static volatile ListAttribute<Coordinate, Alert> alertsList;

}