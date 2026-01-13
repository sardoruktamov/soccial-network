package api.giybat.uz.api.giybat.uz.repository;

import api.giybat.uz.api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.api.giybat.uz.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {
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

    @Modifying
    @Transactional
    @Query("update ProfileEntity set name =?2 where id =?1")
    void updateDetail(Integer id, String name);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set tempUsername =?2 where id =?1")
    void updateTempUsername(Integer id, String tempUsername);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set username =?2 where id =?1")
    void updateUsername(Integer id, String username);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set photoId =?2 where id =?1")
    void updatePhoto(Integer id, String photoId);

    Page<ProfileEntity> findAllByVisibleIsTrueOrderByCreatedDateDesc(PageRequest pageRequest);

    @Query("From ProfileEntity where (lower(username) like ?1 or lower(name) like ?1) and visible is true")
    Page<ProfileEntity> filterByQuery(String query, PageRequest pageRequest);
}
