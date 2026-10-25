package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.time.LocalDateTime;
@NamedQuery(name = "Fillol.findByEmail",query = "select f from Fillol f where f.email =:email")
@NamedQuery(name = "Fillol.findById",query = "select f from Fillol f where f.id =:id")
// @NamedQuery(name = "Fillol.getAllFillol", query = "select new com.fdc.quartesure.quartesurebackend.POJO.Fillol(f.id,f.email,f.status) from Fillol")
@NamedQuery(name = "Fillol.updateStatus", query = "update User f set f.status =: status where f.id =: id")
@Getter
@Setter
@Builder
@Entity
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "fillol")
public class Fillol {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    @ManyToOne
    private User user;

    /**
     * Le filleul peut se déclarer indisponible depuis son compte : le parrain ne peut alors plus lui attribuer de
     * combinaisons. null = anciennes lignes, considérées comme disponibles (valeur par défaut true en base).
     */
    @Column(name = "disponible", columnDefinition = "boolean default true")
    private Boolean disponible;

    @Column(name = "disponible_depuis")
    private LocalDateTime disponibleDepuis;

    public boolean isDisponibleOuNonRenseigne() {
        return disponible == null || disponible;
    }
}
