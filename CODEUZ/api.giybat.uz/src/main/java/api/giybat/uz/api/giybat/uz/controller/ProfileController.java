package api.giybat.uz.api.giybat.uz.controller;

import api.giybat.uz.api.giybat.uz.dto.AppResponse;
import api.giybat.uz.api.giybat.uz.dto.PostDTO;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfileDetailUpdateDTO;
import api.giybat.uz.api.giybat.uz.enums.AppLanguage;
import api.giybat.uz.api.giybat.uz.service.ProfileService;
import api.giybat.uz.api.giybat.uz.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Slf4j
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PutMapping("/detail")
    public ResponseEntity<AppResponse<String>> updateDetail(@Valid @RequestBody ProfileDetailUpdateDTO dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        AppResponse<String> res = profileService.updateDetail(dto, lang);
        return ResponseEntity.ok(res);
    }
}
