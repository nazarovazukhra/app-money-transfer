package uz.pdp.appmoneytransfer.service;

import org.springframework.stereotype.Service;
import uz.pdp.appmoneytransfer.entity.Operation;
import uz.pdp.appmoneytransfer.payload.ApiResponse;
import uz.pdp.appmoneytransfer.repository.OperationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OperationService {

    final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public ApiResponse add(Operation operation) {

        boolean existsByType = operationRepository.existsByType(operation.getType());
        if (existsByType)
            return new ApiResponse("Such operation already exists", false);

        Operation newOperation = new Operation();
        newOperation.setType(operation.getType());
        newOperation.setCommission(operation.getCommission());

        operationRepository.save(newOperation);
        return new ApiResponse("Operation added", true);
    }

    public List<Operation> getAll() {

        return operationRepository.findAll();
    }

    public Operation getById(Integer id) {

        Optional<Operation> optionalOperation = operationRepository.findById(id);
        return optionalOperation.orElse(null);
    }

    public ApiResponse delete(Integer id) {

        boolean exists = operationRepository.existsById(id);
        if (!exists)
            return new ApiResponse("Such operation not found", false);
        operationRepository.deleteById(id);
        return new ApiResponse("Operation deleted", true);
    }

    public ApiResponse edit(Integer id, Operation operation) {

        Optional<Operation> optionalOperation = operationRepository.findById(id);
        if (!optionalOperation.isPresent())
            return new ApiResponse("Such operation not found", false);

        Operation editingOperation = optionalOperation.get();
        editingOperation.setType(operation.getType());
        editingOperation.setCommission(operation.getCommission());
        operationRepository.save(editingOperation);
        return new ApiResponse("Operation edited", true);
    }
}
