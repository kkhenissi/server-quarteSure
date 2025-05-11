package com.fdc.quartesure.quartesurebackend.POJO;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "User.findByEmail",query = "select u from User u where u.email =:email")

@NamedQuery(name = "User.getAllUser", query = "select new com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper(u.id,u.firstName,u.email,u.phoneNumber,u.status,u.lastConnexion,u.subscribeDate,u.selectedOptions) from User u where u.role='user'")

@NamedQuery(name = "User.updateStatus", query = "update User u set u.status =: status where u.id =: id")

@NamedQuery(name = "User.updateSelectedOptions", query = "update User u set u.selectedOptions =: selectedOptions where u.id =: id")

@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role='admin'")

@Getter
@Setter
@Builder
@ToString
@Entity
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "winner_user")
public class User implements Serializable {
    private  static  final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer user_id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "status")
    private String status;
    @Column(name = "credit")
    private Integer credit;
    @Column(name = "lastConnexion")
    private String lastConnexion;
    @Column(name= "subscribeDate")
    private String subscribeDate;
    @Column(name = "role")
    private String role;
    @Column(name = "selectedOptions")
    private String selectedOptions;

    public String fullName() {return firstName + " " + lastName;}
}
