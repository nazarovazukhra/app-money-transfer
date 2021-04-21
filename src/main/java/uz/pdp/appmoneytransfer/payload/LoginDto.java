package uz.pdp.appmoneytransfer.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotNull(message ="username must not be null")
    @Email
    private String username;

    @NotNull(message ="password must not be null")
    private String password;
}
