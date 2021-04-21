package uz.pdp.appmoneytransfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.appmoneytransfer.entity.User;
import uz.pdp.appmoneytransfer.entity.enums.RoleName;
import uz.pdp.appmoneytransfer.payload.ApiResponse;
import uz.pdp.appmoneytransfer.payload.LoginDto;
import uz.pdp.appmoneytransfer.payload.RegisterDto;
import uz.pdp.appmoneytransfer.repository.RoleRepository;
import uz.pdp.appmoneytransfer.repository.UserRepository;
import uz.pdp.appmoneytransfer.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse registerUser(RegisterDto registerDto) {

        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Bunday email allaqachon mavjud", false);

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findAllByRoleName(RoleName.ROLE_USER)));

        user.setEmailCode(UUID.randomUUID().toString());

        userRepository.save(user);

        // EMAILGA YUBORISH METHOD I
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Muvaffaqiyatli ro'yxatdan o'tdingiz ,account ingizni aktivlashtirilishi uchun email ingizni tasdqilang", true);

    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("test@Pdp.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Account ni Tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Tasdiqlang</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findAllByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Account tasdiqlandi", true);
        }
        return new ApiResponse("Account allaqachon tasdiqlangan", false);

    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginDto.getUsername(), loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Parol yoki login xato", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findAllByEmail(username);
//        if (optionalUser.isPresent()) {
//            return optionalUser.get();
//        }
//        throw new UsernameNotFoundException(username + "topilmadi");

        return userRepository.findAllByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "topilmadi"));
    }
}
