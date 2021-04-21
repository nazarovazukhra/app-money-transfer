package uz.pdp.appmoneytransfer.service;

import org.springframework.stereotype.Service;
import uz.pdp.appmoneytransfer.entity.Card;
import uz.pdp.appmoneytransfer.entity.User;
import uz.pdp.appmoneytransfer.payload.ApiResponse;
import uz.pdp.appmoneytransfer.payload.CardDto;
import uz.pdp.appmoneytransfer.repository.CardRepository;
import uz.pdp.appmoneytransfer.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {

    final CardRepository cardRepository;
    final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public List<Card> get() {
        return cardRepository.findAll();
    }

    public Card getById(UUID id) {

        Optional<Card> optionalCard = cardRepository.findById(id);
        return optionalCard.orElse(null);
    }


    public ApiResponse delete(UUID id) {

        boolean exists = cardRepository.existsById(id);
        if (!exists)
            return new ApiResponse("Such card not found", false);

        cardRepository.deleteById(id);
        return new ApiResponse("Card deleted", true);
    }

    public ApiResponse add(CardDto cardDto) {

        boolean exists = cardRepository.existsByCardNumberAndCardBank(cardDto.getCardNumber(), cardDto.getCardBank());
        if (exists)
            return new ApiResponse("Such card already exists", false);

        Card card = new Card();
        card.setCardNumber(cardDto.getCardNumber());
        card.setCardBank(cardDto.getCardBank());
        card.setBalance(cardDto.getBalance());

        Optional<User> optionalUser = userRepository.findById(cardDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Such user not found", false);

        card.setUser(optionalUser.get());

        cardRepository.save(card);
        return new ApiResponse("Card saved", true);
    }


    public ApiResponse edit(UUID id, CardDto cardDto) {

        Optional<Card> optionalCard = cardRepository.findById(id);
        if (!optionalCard.isPresent())
            return new ApiResponse("Such card not found", false);

        Optional<User> optionalUser = userRepository.findById(cardDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Such user not found", false);

        Card editingCard = optionalCard.get();
        editingCard.setBalance(cardDto.getBalance());
        editingCard.setCardBank(cardDto.getCardBank());
        editingCard.setCardNumber(cardDto.getCardNumber());
        editingCard.setUser(optionalUser.get());
        cardRepository.save(editingCard);
        return new ApiResponse("Card edited", true);
    }
}
