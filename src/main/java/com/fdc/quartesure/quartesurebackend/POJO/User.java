package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NamedQuery(name = "User.findByEmail",
        query = "select u from User u where u.email =:email")

@NamedQuery(name = "User.findById",
        query = "select u from User u where u.user_id =:user_id")

@NamedQuery(name = "User.getAllUser",
        query = "select new com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper(" +
                "u.user_id,u.firstName,u.email,u.phoneNumber,u.status,u.lastConnexion," +
                "u.subscribeDate,u.selectedOptions) " +
                "from User u where u.role='user'")

@NamedQuery(name = "User.updateStatus",
        query = "update User u set u.status =:status where u.user_id =:id")

@NamedQuery(name = "User.updateSelectedOptions",
        query = "update User u set u.selectedOptions =:selectedOptions where u.user_id =:id")

@NamedQuery(name = "User.getAllAdmin",
        query = "select u.email from User u where u.role='admin'")

@Getter
@Setter
@Builder
@Entity
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "winner_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===================== EXISTANT =====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "first_name")
    @NotEmpty(message = "firstname is required !!")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    private String lastName;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(unique = true, name = "email")
    @NotEmpty
    private String email;

    @Column(name = "password")
    @NotEmpty
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "credit")
    private Integer credit;

    @Column(name = "lastConnexion")
    private String lastConnexion;

    @Column(name = "subscribeDate")
    private String subscribeDate;

    @Column(name = "role")
    private String role;

    @Column(name = "selectedOptions")
    private String selectedOptions;

    @Column(name = "parrainer_par")
    private String parrainerby;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Fillol> fillols;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Subscription subscription;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    // ===================== FORGOT PASSWORD =====================

    @Column(name = "reset_password_token", unique = true)
    private String resetPasswordToken;

    @Column(name = "reset_password_expiry")
    private LocalDateTime resetPasswordExpiry;

    // ===================== UTILS =====================

    public String fullName() {
        return firstName + " " + lastName;
    }
}
