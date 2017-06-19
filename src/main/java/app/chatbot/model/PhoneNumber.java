package app.chatbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by adeliadjuarto on 6/19/17.
 */
@Entity
@Table(name="phone_number")
@Setter
@Getter
public class PhoneNumber {
    protected PhoneNumber () {}
    public PhoneNumber (String id, String contactId, String type, String number, String extension) {
        this.id = id;
        this.contactId = contactId;
        this.type = type;
        this.number = number;
        this.extension = extension;
    }
    @Id
    @Column(name="id")
    String id;
    @Column(name="contact_id")
    String contactId;
    @Column(name="type")
    String type;
    @Column(name="number")
    String number;
    @Column(name="extension")
    String extension;


}
