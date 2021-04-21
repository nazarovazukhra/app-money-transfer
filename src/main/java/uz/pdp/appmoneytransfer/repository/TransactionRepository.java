package uz.pdp.appmoneytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.appmoneytransfer.entity.Transaction;
import uz.pdp.appmoneytransfer.payload.CardInfo;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("select new uz.pdp.appmoneytransfer.payload.CardInfo(t.amount,t.date,t.operation.id,t.ownerCard.cardNumber,t.transferCard.cardNumber) from Transaction t where (t.operation.id=2 and t.ownerCard.id=:cardId)or (t.operation.id=1 and t.transferCard.id=:cardId) ")
    List<CardInfo> findInfoIncome(@Param("cardId") UUID cardId);

    @Query("select new uz.pdp.appmoneytransfer.payload.CardInfo(t.amount,t.date,t.operation.id,t.ownerCard.cardNumber,t.transferCard.cardNumber) from Transaction t where (t.operation.id=1 and t.ownerCard.id=?1)or (t.operation.id=3 and t.ownerCard.id=?1) ")
    List<CardInfo> findInfoOutcome(@Param("cardId") UUID cardId);

//    @Query(value = "selectt.amount, t.date, t.operation.id,t.owner_card_id,t.transfer_card_id from transaction t\n" +
//            "         join operation o on t.operation_id = o.id\n" +
//            "         join card c on c.id = t.owner_card_id or t.transfer_card_id = c.id\n" +
//            "where (o.id=1 and t.transfer_card_id =?1) or (o.id=2 and t.owner_card_id=?1)",nativeQuery = true)
//    List<CardIncomeInfo> findInfoIncome(UUID cardId);


//    @Query(value = "select * from transaction\n" +
//            "join operation o on o.id = transaction.operation_id\n" +
//            "join card c on c.id = transaction.owner_card_id or transaction.transfer_card_id = c.id\n" +
//            "where o.id=1 and transaction.owner_card_id=:cardId or o.id=3 and transaction.owner_card_id='04fceba1-120f-4009-9d91-9b91a53f43f3'", nativeQuery = true)
//    List<CardInfo> findOutcome(UUID cardId);

}
