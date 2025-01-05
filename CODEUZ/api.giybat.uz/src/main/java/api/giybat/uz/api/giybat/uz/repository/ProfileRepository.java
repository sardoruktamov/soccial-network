package api.giybat.uz.api.giybat.uz.repository;

import api.giybat.uz.api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.api.giybat.uz.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {
    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);
    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer id);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set status =?2 where id =?1")
    void changeStatus(Integer id, GeneralStatus status);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set password =?2 where id =?1")
    void updatePassword(Integer id, String password);
}
