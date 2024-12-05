package api.giybat.uz.api.giybat.uz.repository;

import api.giybat.uz.api.giybat.uz.entity.ProfileRoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity,Integer> {

    @Transactional
    @Modifying
    void deleteByProfileId(Integer integer);
}
