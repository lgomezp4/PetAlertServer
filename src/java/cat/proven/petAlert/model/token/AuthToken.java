package cat.proven.petAlert.model.token;

import java.util.Date;
import java.util.Objects;

/**
 * ADT AuthToken
 *
 * @author Pet Alert
 */
public class AuthToken {

    /* ATTRIBUTES */
    private String token;
    private String username;
    private Date expiration;
    private String sourceIP;

    /* CONSTRUCTORS */
    public AuthToken(String token, String username, Date expiration, String sourceIP) {
        this.token = token;
        this.username = username;
        this.expiration = expiration;
        this.sourceIP = sourceIP;
    }

    /* GETTERS AND SETTERS */
    public AuthToken() {
    }

    public AuthToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.token);
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
        final AuthToken other = (AuthToken) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthToken{");
        sb.append("token=").append(token);
        sb.append(", username=").append(username);
        sb.append(", expiration=").append(expiration);
        sb.append(", sourceIP=").append(sourceIP);
        sb.append('}');
        return  sb.toString();
    }

    /**
     * Generates a token with the user's name, expiration date, a random number
     * and applies a Hash function.
     *
     * @return token
     */
    public String generate() {
        String tok = this.username + this.expiration.toString() + Math.random();
        this.token = Hash.md5(tok);
        return this.token;
    }

}
