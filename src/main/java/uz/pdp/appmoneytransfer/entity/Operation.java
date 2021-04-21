package uz.pdp.appmoneytransfer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // operation ning takrorlanmas qismi


    @Column(unique = true, nullable = false)
    private String type; // oparation nomi

    private Float commission;

}
