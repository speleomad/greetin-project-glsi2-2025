package com.fsb.greeting.dao.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

 import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table; 


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name="persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
     @Column(name="persons_name",length = 30, nullable=false, unique = true) 
    private String name;
    private short age;
    private String photo; 
    @ManyToOne()
    @JoinColumn(name="age_group_id")
    //si on ne veut pas que les personnes serons supprimées suite à la supression de ageGroup
    /*@ManyToOne(optional = true)
    @JoinColumn(name = "age_group_id", nullable = true)*/
    private AgeGroup ageGroup;
    public Person(Long id, String name, short age, String photo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.photo = photo;
    }
}