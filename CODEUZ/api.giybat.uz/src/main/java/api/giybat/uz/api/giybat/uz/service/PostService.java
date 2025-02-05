package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.dto.ProfileDTO;
import api.giybat.uz.api.giybat.uz.dto.post.PostCreateDTO;
import api.giybat.uz.api.giybat.uz.dto.post.PostDTO;
import api.giybat.uz.api.giybat.uz.entity.PostEntity;
import api.giybat.uz.api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.api.giybat.uz.repository.PostRepository;
import api.giybat.uz.api.giybat.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AttachService attachService;

    public PostDTO create(PostCreateDTO dto){
        PostEntity entity = new PostEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setPhotoId(dto.getPhoto().getId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        postRepository.save(entity);

        return toDto(entity);
    }

    public PostDTO toDto(PostEntity entity){
        PostDTO dto = new PostDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPhoto(attachService.attachDTO(entity.getPhotoId()));
        return dto;
    }
}
