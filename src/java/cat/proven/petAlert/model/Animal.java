package cat.proven.petAlert.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * ADT Animal
 *
 * @author Pet Alert
 */
@Entity
@Table(name = "animals")
@XmlRootElement
/* QUERIES */
@NamedQueries({
    @NamedQuery(name = "Animal.findAll", query = "SELECT a FROM Animal a")
    , @NamedQuery(name = "Animal.findById", query = "SELECT a FROM Animal a WHERE a.id = :id")
    , @NamedQuery(name = "Animal.findByChipNum", query = "SELECT a FROM Animal a WHERE a.chipNum = :chipNum")
    , @NamedQuery(name = "Animal.findByName", query = "SELECT a FROM Animal a WHERE a.name = :name")
    , @NamedQuery(name = "Animal.findByKind", query = "SELECT a FROM Animal a WHERE a.kind = :kind")
    , @NamedQuery(name = "Animal.findByHairColor", query = "SELECT a FROM Animal a WHERE a.hairColor = :hairColor")
    , @NamedQuery(name = "Animal.findByRace", query = "SELECT a FROM Animal a WHERE a.race = :race")
    , @NamedQuery(name = "Animal.findByHalfBlood", query = "SELECT a FROM Animal a WHERE a.halfBlood = :halfBlood")
    , @NamedQuery(name = "Animal.findByAge", query = "SELECT a FROM Animal a WHERE a.age = :age")
    , @NamedQuery(name = "Animal.findBySex", query = "SELECT a FROM Animal a WHERE a.sex = :sex")})
public class Animal implements Serializable {

    /* ATTRIBUTES */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Column(name = "chip_num")
    @Expose
    private BigInteger chipNum;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "name")
    @Expose
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "kind")
    @Expose
    private String kind;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sex")
    @Expose
    private String sex;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "hair_color")
    @Expose
    private String hairColor;
    @Size(max = 50)
    @Column(name = "race")
    @Expose
    private String race;
    @Column(name = "half_blood")
    @Expose
    private Boolean halfBlood;
    @Column(name = "age")
    @Expose
    private Integer age;
    @Lob
    @Column(name = "image")
    @Expose
    byte[] image;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animalId", fetch = FetchType.LAZY)
    private List<Alert> alertsList;

    /* CONSTRUCTORS */
    public Animal() {
    }

    public Animal(Integer id) {
        this.id = id;
    }

    public Animal(String name, String kind, String sex, String hairColor, String race, Boolean halfBlood, Integer age) {
        this.name = name;
        this.kind = kind;
        this.sex = sex;
        this.hairColor = hairColor;
        this.race = race;
        this.halfBlood = halfBlood;
        this.age = age;
    }

    public Animal(Integer id, String name, String kind, String hairColor, String sex) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.hairColor = hairColor;
        this.sex = sex;
    }

    /* GETTERS AND SETTERS */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getChipNum() {
        return chipNum;
    }

    public void setChipNum(BigInteger chipNum) {
        this.chipNum = chipNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Boolean getHalfBlood() {
        return halfBlood;
    }

    public void setHalfBlood(Boolean halfBlood) {
        this.halfBlood = halfBlood;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @XmlTransient
    public List<Alert> getAlertsList() {
        return alertsList;
    }

    public void setAlertsList(List<Alert> alertsList) {
        this.alertsList = alertsList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Animal other = (Animal) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Animal{");
        sb.append("id=").append(id);
        sb.append(", chipNum=").append(chipNum);
        sb.append(", name=").append(name);
        sb.append(", kind=").append(kind);
        sb.append(", sex=").append(sex);
        sb.append(", hairColor=").append(hairColor);
        sb.append(", race=").append(race);
        sb.append(", halfBlood=").append(halfBlood);
        sb.append(", age=").append(age);
        sb.append(", image=").append(image);
        sb.append('}');

        return sb.toString();
    }

}
