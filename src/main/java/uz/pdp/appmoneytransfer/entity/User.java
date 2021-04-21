package uz.pdp.appmoneytransfer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sys_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue()
    private UUID id; // userning takrorlanmas qismi


    @Column(nullable = false, length = 50)
    private String firstName; // ism


    @Column(nullable = false, length = 50)
    private String lastName; // familyasi

    //examle@gmail.com
    @Column(unique = true, nullable = false)
    private String email; //user ning email i (USERNAME SIFATIDA)

    @Column(nullable = false)
    private String password; //user ning paroli

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt; // qachon ro'yxatdan o'tganligi

    @UpdateTimestamp
    private Timestamp updatedAt; // oxirgi marta qachon tahrirlanganligi

    @ManyToMany
    private Set<Role> roles;

    private boolean accountNonExpired = true; // bu user ning amal qilish muddati o'tmaganligi

    private boolean accountNonLocked = true; // bu user bloklanmaganligi

    private boolean credentialsNonExpired = true;

    private boolean enabled;

    private String emailCode;


    //------------BU USERDETAILS NING METHOD LARI-----------------


    // BU USER NING HUQUQLARI RO'YXATI
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    // BU USER NING USERNAME NI QAYTARUVCHI METHOD
    @Override
    public String getUsername() {
        return this.email;
    }

    // BU ACCOUNT NING AMAL QILISH MUDDATINI QAYTARADI
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    // ACCOUNT BLOKLANGANLIGI HOLATII QAYTARADI
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ACCOUNT NING ISHONCHLILIK MUDDATI TUGAGAN YOKI TUGAMAGANLIGINI QAYTARADI
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    // ACCOUNT NING YONIQ YOKI O'CHIQLIGINI QAYTARADI
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
