package uz.pdp.appmoneytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appmoneytransfer.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findAllByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findAllByEmail(String email);
}
