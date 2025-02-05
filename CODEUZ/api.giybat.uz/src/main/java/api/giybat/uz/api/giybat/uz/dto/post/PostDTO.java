package api.giybat.uz.api.giybat.uz.dto.post;

import api.giybat.uz.api.giybat.uz.dto.AttachDTO;
import api.giybat.uz.api.giybat.uz.entity.AttachEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {

    private String id;

    private String title;

    private String content;

    private AttachDTO photo;

    private Boolean visible;

    private LocalDateTime createdDate;

}
