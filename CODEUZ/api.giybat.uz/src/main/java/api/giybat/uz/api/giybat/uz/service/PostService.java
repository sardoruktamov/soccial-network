package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.dto.FilterResultDTO;
import api.giybat.uz.api.giybat.uz.dto.ProfileDTO;
import api.giybat.uz.api.giybat.uz.dto.post.PostCreateDTO;
import api.giybat.uz.api.giybat.uz.dto.post.PostDTO;
import api.giybat.uz.api.giybat.uz.dto.post.PostFilterDTO;
import api.giybat.uz.api.giybat.uz.entity.PostEntity;
import api.giybat.uz.api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.api.giybat.uz.enums.ProfileRole;
import api.giybat.uz.api.giybat.uz.exps.AppBadException;
import api.giybat.uz.api.giybat.uz.repository.CustomRepository;
import api.giybat.uz.api.giybat.uz.repository.PostRepository;
import api.giybat.uz.api.giybat.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AttachService attachService;
    @Autowired
    private CustomRepository customRepository;

    public PostDTO create(PostCreateDTO dto){
        PostEntity entity = new PostEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setPhotoId(dto.getPhoto().getId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        postRepository.save(entity);

        return toInfoDto(entity);
    }

    public Page<PostDTO> getProfilePostList(int page, int size){
        PageRequest pageRequest = PageRequest.of(page,size);
        Integer profId = SpringSecurityUtil.getCurrentUserId();
        Page<PostEntity> result = postRepository.getAllByProfileIdAndVisibleTrueOrderByCreatedDateDesc(profId, pageRequest);
        List<PostDTO> dtoList = result.getContent().stream()
                .map(dto -> toInfoDto(dto))
                .toList();

        return new PageImpl<PostDTO>(dtoList, pageRequest, result.getTotalElements());
    }

    public PostDTO getById(String id){
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return toDto(entity);
    }

    public PostDTO update(String id, PostCreateDTO dto){
        PostEntity entity = get(id);
        if (!SpringSecurityUtil.hazRole(ProfileRole.ROLE_ADMIN) && !entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())){
            throw new AppBadException("You do not have permission to update this post");
        }
        String deletePhotoId = null;
        if (!dto.getPhoto().getId().equals(entity.getPhotoId())){
            deletePhotoId = entity.getPhotoId();
        }
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setPhotoId(dto.getPhoto().getId());
        postRepository.save(entity);
        // delete old image
        if(deletePhotoId != null){
            attachService.delete(deletePhotoId);
        }
        return toInfoDto(entity);
    }

    public Boolean delete(String id){
        PostEntity entity = get(id);
        if (!SpringSecurityUtil.hazRole(ProfileRole.ROLE_ADMIN) && !entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())){
            throw new AppBadException("You do not have permission to update this post");
        }
        postRepository.delete(id);
        return true;
    }

    public PageImpl<PostDTO> filter(PostFilterDTO dto, int page, int size){
        FilterResultDTO<PostEntity> resultDto = customRepository.filter(dto, page, size);
        List<PostDTO> dtoList = resultDto.getList().stream()
                .map(postEntity -> toInfoDto(postEntity))
                .toList();
        return new PageImpl<>(dtoList, PageRequest.of(page,size), resultDto.getTotalCount());
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

    public PostDTO toInfoDto(PostEntity entity){
        PostDTO dto = new PostDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPhoto(attachService.attachDTO(entity.getPhotoId()));
        return dto;
    }

    public PostEntity get(String id){
        return postRepository.findById(id).orElseThrow(() -> {
            throw new AppBadException("Post not found: " + id);
        });
    }
}
