package uz.pdp.appmoneytransfer.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    @NotNull(message = "Amount of money must not be null and the smallest amount of transfer should be at least 1000 so'm ")
    private Float amount;

    private Integer operationId;

    private String transferCardId;

    @NotNull(message = "Owner card id must not be null ")
    private String ownerCardId;

}
