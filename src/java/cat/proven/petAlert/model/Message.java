package cat.proven.petAlert.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ADT Message
 *
 * @author Pet Alert
 */
@Entity
@Table(name = "messages")
@XmlRootElement
/* QUERIES */
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m")
    , @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id")
    , @NamedQuery(name = "Message.findByTitle", query = "SELECT m FROM Message m WHERE m.title = :title")
    , @NamedQuery(name = "Message.findByContent", query = "SELECT m FROM Message m WHERE m.content = :content")
    , @NamedQuery(name = "Message.findBySenderActive", query = "SELECT m FROM Message m WHERE m.senderActive = :senderActive")
    , @NamedQuery(name = "Message.findByReceiverActive", query = "SELECT m FROM Message m WHERE m.receiverActive = :receiverActive")
    , @NamedQuery(name = "Message.findBySendDate", query = "SELECT m FROM Message m WHERE m.sendDate = :sendDate")
    , @NamedQuery(name = "Message.findByReceiptDate", query = "SELECT m FROM Message m WHERE m.receiptDate = :receiptDate")
    , @NamedQuery(name = "Message.findByUserSent", query = "SELECT m FROM Message m JOIN m.senderId u WHERE m.senderActive = true and u.id = :id")
    , @NamedQuery(name = "Message.findByUserReceived", query = "SELECT m FROM Message m JOIN m.receiverId u WHERE m.receiverActive = true and u.id = :id")})
public class Message implements Serializable {

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
    @Column(name = "title")
    @Expose
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "content")
    @Expose
    private String content;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sender_active")
    @Expose
    private boolean senderActive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "receiver_active")
    @Expose
    private boolean receiverActive;
    @Basic(optional = false)
    @Column(name = "send_date")

    @Expose
    private long sendDate;
    @Column(name = "receipt_date")

    @Expose
    private long receiptDate;
    @Expose
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User receiverId;
    @Expose
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User senderId;

    /* CONSTRUCTORS */
    public Message() {

    }

    public Message(Integer id) {
        this.id = id;
    }

    public Message(String title, String content, User receiverId, User senderId) {
        this.title = title;
        this.content = content;
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

    public Message(String title, String content, long sendDate, User senderId, User receiverId) {
        this.title = title;
        this.content = content;
        this.sendDate = sendDate;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    /* GETTERS AND SETTERS */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getSenderActive() {
        return senderActive;
    }

    public void setSenderActive(boolean senderActive) {
        this.senderActive = senderActive;
    }

    public boolean getReceiverActive() {
        return receiverActive;
    }

    public void setReceiverActive(boolean receiverActive) {
        this.receiverActive = receiverActive;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public long getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(long receiptDate) {
        this.receiptDate = receiptDate;
    }

    public User getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(User receiverId) {
        this.receiverId = receiverId;
    }

    public User getSenderId() {
        return senderId;
    }

    public void setSenderId(User senderId) {
        this.senderId = senderId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Message other = (Message) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Message{");
        sb.append("id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", senderActive=").append(senderActive);
        sb.append(", receiverActive=").append(receiverActive);
        sb.append(", sendDate=").append(sendDate);
        sb.append(", receiptDate=").append(receiptDate);
        sb.append(", senderId=").append(senderId);
        sb.append(", receiverId=").append(receiverId);
        sb.append('}');

        return sb.toString();
    }

}
