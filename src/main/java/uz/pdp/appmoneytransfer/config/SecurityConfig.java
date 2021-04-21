package uz.pdp.appmoneytransfer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.appmoneytransfer.security.JwtFilter;
import uz.pdp.appmoneytransfer.service.AuthService;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthService authService;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("twinszukhranazarova@gmail.com");
        mailSender.setPassword("z.r.nazarova@.tuit");
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");
//        properties.put("mail.smtp.timeout", "60000");
//        properties.put("mail.smtp.connectiontimeout", "60000");
        return mailSender;
        //mail.smtps.timeout
        //mail.smtps.connectiontimeout
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/register", "/api/auth/verifyEmail",
                        "/api/auth/login").permitAll()
                .anyRequest()
                .authenticated();

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
