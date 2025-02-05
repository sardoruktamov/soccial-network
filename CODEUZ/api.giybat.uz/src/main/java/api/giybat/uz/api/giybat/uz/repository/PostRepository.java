package api.giybat.uz.api.giybat.uz.repository;

import api.giybat.uz.api.giybat.uz.entity.PostEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, String> {
}
