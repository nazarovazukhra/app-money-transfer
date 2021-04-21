package uz.pdp.appmoneytransfer.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    @NotNull(message ="cardNumber must not be null")
    private String cardNumber;

    @NotNull(message ="cardBank must not be null")
    private String cardBank;

    private Float balance;

    private UUID userId;
}
