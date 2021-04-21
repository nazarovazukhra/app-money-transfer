package uz.pdp.appmoneytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appmoneytransfer.entity.Operation;
@Repository
public interface OperationRepository extends JpaRepository<Operation,Integer> {

    boolean existsByType(String type);
}
