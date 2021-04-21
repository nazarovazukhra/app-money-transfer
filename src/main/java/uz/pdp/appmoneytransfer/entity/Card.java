package uz.pdp.appmoneytransfer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Card {

    @Id
    @GeneratedValue()
    private UUID id; // card ning takrorlanmas qismi


    @Column(unique = true, nullable = false, length = 16)
    private String cardNumber; // card raqami

    @Column(nullable = false)
    private String cardBank;

    private Float balance;

    @OneToOne
    private User user;


}
