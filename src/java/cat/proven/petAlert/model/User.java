package cat.proven.petAlert.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * ADT User
 *
 * @author Pet Alert
 */
@Entity
@Table(name = "users")
@XmlRootElement
/* QUERIES */
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
    , @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name")
    , @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
    , @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")
    , @NamedQuery(name = "User.findByRol", query = "SELECT u FROM User u WHERE u.rol = :rol")
    , @NamedQuery(name = "User.findByMail", query = "SELECT u FROM User u WHERE u.mail = :mail")
    , @NamedQuery(name = "User.findByPopulation", query = "SELECT u FROM User u WHERE u.population = :population")
    , @NamedQuery(name = "User.findByActive", query = "SELECT u FROM User u WHERE u.active = :active")
    , @NamedQuery(name = "User.findAllTokens", query = "SELECT u.token FROM User u")
    , @NamedQuery(name = "User.findByToken", query = "SELECT u FROM User u WHERE u.token = :token")
    , @NamedQuery(name = "User.findUserExpiration", query = "SELECT u.expiration FROM User u WHERE u.token = :token")})
public class User implements Serializable {

    /* ATTRIBUTES */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    @Expose
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    @Expose
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "rol")
    @Expose
    private String rol;
    @Column(name = "token")
    @Expose
    private String token;
    @Column(name = "expiration")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date expiration;
    @Column(name = "sourceIP")
    @Expose
    private String sourceIP;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "mail")
    @Expose
    private String mail;
    @Size(max = 50)
    @Column(name = "population")
    @Expose
    private String population;
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    @Expose
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId", fetch = FetchType.LAZY)
    private List<Alert> alertsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiverId", fetch = FetchType.LAZY)
    private List<Message> messagesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "senderId", fetch = FetchType.LAZY)
    private List<Message> messagesList1;

    /* CONSTRUCTORS */
    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String name, String username, String password, String rol, String mail) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.mail = mail;
    }

    public User(Integer id, String name, String username, String password, String rol, String mail, boolean active) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.mail = mail;
        this.active = active;
    }

    /* GETTERS AND SETTERS */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    @XmlTransient
    public List<Alert> getAlertsList() {
        return alertsList;
    }

    public void setAlertsList(List<Alert> alertsList) {
        this.alertsList = alertsList;
    }

    @XmlTransient
    public List<Message> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    @XmlTransient
    public List<Message> getMessagesList1() {
        return messagesList1;
    }

    public void setMessagesList1(List<Message> messagesList1) {
        this.messagesList1 = messagesList1;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final User other = (User) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", rol=").append(rol);
        sb.append(", mail=").append(mail);
        sb.append(", population=").append(population);
        sb.append(", active=").append(active);
        sb.append('}');

        return sb.toString();
    }
}
