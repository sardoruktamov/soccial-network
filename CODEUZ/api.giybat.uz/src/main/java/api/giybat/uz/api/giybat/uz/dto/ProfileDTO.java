package api.giybat.uz.api.giybat.uz.dto;

import api.giybat.uz.api.giybat.uz.enums.ProfileRole;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileDTO {

    private String name;
    private String username;
    private List<ProfileRole> roleList;
    private String jwt;
}
