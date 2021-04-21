package uz.pdp.appmoneytransfer.payload;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {

    @NotNull(message ="firstName must not be null")
    @Size(min = 3, max = 50)
    private String  firstName;

    @NotNull(message ="lastName must not be null")
    @Length(min = 3, max = 50)
    private String  lastName;

    @NotNull(message ="email must not be null")
    @Email
    private String email;

    @NotNull(message ="password must not be null")
    private String password;
}
