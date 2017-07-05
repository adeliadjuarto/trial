package app.chatbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by adeliadjuarto on 7/5/17.
 */
@Entity
@Table(name="service_type")
@Setter
@Getter
public class ServiceType {
    public ServiceType(){}
    public ServiceType(String name){
        this.name = name;
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
}
