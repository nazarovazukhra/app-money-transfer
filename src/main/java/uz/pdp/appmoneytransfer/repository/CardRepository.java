package uz.pdp.appmoneytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appmoneytransfer.entity.Card;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    boolean existsByCardNumberAndCardBank(String cardNumber, String cardBank);

    Optional<Card> findByCardNumber(String cardNumber);

}
