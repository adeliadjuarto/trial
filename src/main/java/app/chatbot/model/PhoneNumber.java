package app.chatbot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by adeliadjuarto on 6/19/17.
 */
@Entity
@Table(name="phone_number")
@Setter
@Getter
public class PhoneNumber {
    protected PhoneNumber () {}
    public PhoneNumber (String contactId, String type, String number, String extension, String icon) {
        this.contactId = contactId;
        this.type = type;
        this.number = number;
        this.extension = extension;
        this.icon = icon;
    }
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(
            name = "system-uuid",
            strategy = "uuid2")
    @Column(name="id")
    private String id;
    @Column(name="contact_id")
    private String contactId;
    @Column(name="type")
    private String type;
    @Column(name="number")
    private String number;
    @Column(name="extension")
    private String extension;
    @Column(name="icon")
    private String icon;

}
