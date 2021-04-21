package uz.pdp.appmoneytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appmoneytransfer.entity.Role;
import uz.pdp.appmoneytransfer.entity.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findAllByRoleName(RoleName roleName);
}
