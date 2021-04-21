package uz.pdp.appmoneytransfer.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {

    private Float amount;
    private Date date;
    private Integer operationId;
    private String fromCardNumber;
    private String toCardNumber;


}
