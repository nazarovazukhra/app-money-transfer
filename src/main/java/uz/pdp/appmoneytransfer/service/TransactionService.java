package uz.pdp.appmoneytransfer.service;

import org.springframework.stereotype.Service;
import uz.pdp.appmoneytransfer.entity.Card;
import uz.pdp.appmoneytransfer.entity.Operation;
import uz.pdp.appmoneytransfer.entity.Transaction;
import uz.pdp.appmoneytransfer.payload.ApiResponse;
import uz.pdp.appmoneytransfer.payload.CardInfo;
import uz.pdp.appmoneytransfer.payload.TransactionDto;
import uz.pdp.appmoneytransfer.repository.CardRepository;
import uz.pdp.appmoneytransfer.repository.OperationRepository;
import uz.pdp.appmoneytransfer.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    final TransactionRepository transactionRepository;
    final CardRepository cardRepository;
    final OperationRepository operationRepository;

    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository, OperationRepository operationRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.operationRepository = operationRepository;
    }

    public ApiResponse transaction(TransactionDto transactionDto) {

        if (transactionDto.getOperationId() == 1) {

            Optional<Card> ownerCard = cardRepository.findByCardNumber(transactionDto.getOwnerCardId());
            if (!ownerCard.isPresent())
                return new ApiResponse("Such owner's card not found", false);

            Optional<Card> transferCard = cardRepository.findByCardNumber(transactionDto.getTransferCardId());
            if (!transferCard.isPresent())
                return new ApiResponse("Such transfer card not found", false);

            Optional<Operation> optionalOperation = operationRepository.findById(transactionDto.getOperationId());
            if (optionalOperation.isPresent()) {
                Operation operation = optionalOperation.get();
                Float commission = operation.getCommission();

                Card cardFrom = ownerCard.get();
                Card cardTo = transferCard.get();
                Float balanceBefore = cardFrom.getBalance();

                Float amount = transactionDto.getAmount();

                if (balanceBefore > (amount + (amount * commission))) {

                    Transaction transaction = new Transaction();
                    transaction.setOwnerCard(ownerCard.get());
                    transaction.setTransferCard(transferCard.get());
                    transaction.setOperation(operation);
                    transaction.setAmount(amount);
                    transactionRepository.save(transaction);

                    Float balanceAfter = balanceBefore - (amount + (amount * commission));
                    cardFrom.setBalance(balanceAfter);
                    cardRepository.save(cardFrom);

                    Float balanceBeforeCardTo = cardTo.getBalance();
                    cardTo.setBalance(balanceBeforeCardTo + amount);
                    cardRepository.save(cardTo);
                    return new ApiResponse("Transaction successfully completed", true);
                } else {
                    return new ApiResponse("There is no enough money", false);
                }


            }
            return new ApiResponse("Such  operation not found", false);

        }

        if (transactionDto.getOperationId() == 2) {
            Optional<Operation> optionalOperation = operationRepository.findById(transactionDto.getOperationId());
            if (!optionalOperation.isPresent())
                return new ApiResponse("Such operation not found", false);

            Float amount = transactionDto.getAmount();
            if (amount >= 1000) {
                Optional<Card> cardTo = cardRepository.findByCardNumber(transactionDto.getOwnerCardId());
                if (!cardTo.isPresent())
                    return new ApiResponse("Such card not found", false);

                Card card = cardTo.get();

                Float balance = card.getBalance();
                Float balanceAfter = balance + amount;

                Transaction transaction = new Transaction();
                transaction.setAmount(amount);
                transaction.setOperation(optionalOperation.get());
                transaction.setOwnerCard(card);
                transaction.setTransferCard(null);

                transactionRepository.save(transaction);

                card.setBalance(balanceAfter);
                cardRepository.save(card);
                return new ApiResponse("Card successfully charged", true);

            }
            return new ApiResponse("The smallest amount of transfer should be at least 1000 so'm", false);

        }

        if (transactionDto.getOperationId() == 3) {
            Optional<Operation> optionalOperation = operationRepository.findById(transactionDto.getOperationId());
            if (!optionalOperation.isPresent())
                return new ApiResponse("Such operation not found", false);

            Operation operation = optionalOperation.get();

            Optional<Card> ownerCard = cardRepository.findByCardNumber(transactionDto.getOwnerCardId());
            if (!ownerCard.isPresent())
                return new ApiResponse("Such card not found", false);

            Card cardFrom = ownerCard.get();
            Float amount = transactionDto.getAmount();
            if (amount >= 1000) {

                Float balanceBefore = cardFrom.getBalance();
                if (balanceBefore >= amount) {
                    Float balanceAfter = balanceBefore - amount;

                    Transaction transaction = new Transaction();
                    transaction.setOperation(operation);
                    transaction.setOwnerCard(cardFrom);
                    transaction.setTransferCard(null);
                    transaction.setAmount(amount);
                    transactionRepository.save(transaction);

                    cardFrom.setBalance(balanceAfter);
                    cardRepository.save(cardFrom);

                    return new ApiResponse("The money was withdrawn successfully", true);

                } else {
                    return new ApiResponse("There is no enough money in card", false);
                }
            } else {
                return new ApiResponse("The smallest amount of transfer should be at least 1000 so'm ", false);
            }

        }

        return new ApiResponse("Such operation not found", false);
    }

    public List<Transaction> getAll() {

        return transactionRepository.findAll();
    }

    public Transaction getById(UUID id) {

        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        return optionalTransaction.orElse(null);
    }

    public List<CardInfo> getInfoIncome(UUID cardId) {

//        Optional<Card> optionalCard = cardRepository.findByCardNumber(cardNumber);
//        if (optionalCard.isPresent()) {
//
//            //            return new ApiResponse("Such card not found", false);
//
//            Card card = optionalCard.get();
//            UUID cardId = card.getId();
//            return transactionRepository.findInfoIncome(cardId);
//        }
//        return null;
        return transactionRepository.findInfoIncome(cardId);


    }

    public List<CardInfo> getInfoOutcome(UUID cardId) {

//        Optional<Card> optionalCard = cardRepository.findByCardNumber(cardNumber);
//        if (optionalCard.isPresent()) {
//
//            //            return new ApiResponse("Such card not found", false);
//
//            Card card = optionalCard.get();
//            UUID cardId = card.getId();
//            return transactionRepository.findOutcome(cardId);
//        }
        return transactionRepository.findInfoOutcome(cardId);

    }
}


