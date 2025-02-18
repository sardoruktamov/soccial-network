package api.giybat.uz.api.giybat.uz.repository;

import api.giybat.uz.api.giybat.uz.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, String> {

    //select * from post where profile_id = ? and visible = true
    List<PostEntity> getAllByProfileIdAndVisibleTrue(Integer profileId);

    @Transactional
    @Modifying
    @Query("update PostEntity set visible = false where id = ?1")
    void delete(String id);
}
