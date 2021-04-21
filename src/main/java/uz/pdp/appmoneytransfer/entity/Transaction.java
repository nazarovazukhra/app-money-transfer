package uz.pdp.appmoneytransfer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue()
    private UUID id; // transaction ning takrorlanmas qismi

    @ManyToOne
    private Card ownerCard;

    @ManyToOne
    private Card transferCard;

    @ManyToOne
    private Operation operation;


    private Float amount;

    @CreationTimestamp
    private Timestamp date;
}
