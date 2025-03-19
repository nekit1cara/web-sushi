package com.example.web_sushi.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false)
    private String clientFullName;

    @Column(name = "phone", nullable = false)
    private String clientPhoneNumber;

    private String deliveryAddress;

    @JsonIgnore
    @OneToMany(mappedBy = "clients")
    private List<Orders> orders;




}
