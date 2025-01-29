package api.giybat.uz.api.giybat.uz.controller;

import api.giybat.uz.api.giybat.uz.dto.AppResponse;
import api.giybat.uz.api.giybat.uz.dto.CodeConfirmDTO;
import api.giybat.uz.api.giybat.uz.dto.PostDTO;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfileDetailUpdateDTO;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfilePasswordUpdateDTO;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfilePhotoUpdateDTO;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfileUsernameUpdateDTO;
import api.giybat.uz.api.giybat.uz.enums.AppLanguage;
import api.giybat.uz.api.giybat.uz.service.ProfileService;
import api.giybat.uz.api.giybat.uz.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Slf4j
@Tag(name = "ProfileController", description = "API list for working with Profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PutMapping("/detail")
    public ResponseEntity<AppResponse<String>> updateDetail(@Valid @RequestBody ProfileDetailUpdateDTO dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        AppResponse<String> res = profileService.updateDetail(dto, lang);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/photo")
    public ResponseEntity<AppResponse<String>> updatePhoto(@Valid @RequestBody ProfilePhotoUpdateDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        AppResponse<String> res = profileService.updatePhoto(dto.getPhotoId(), lang);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/password")
    public ResponseEntity<AppResponse<String>> updatePassword(@Valid @RequestBody ProfilePasswordUpdateDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        AppResponse<String> res = profileService.updatePassword(dto, lang);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/username")
    public ResponseEntity<AppResponse<String>> updateUsername(@Valid @RequestBody ProfileUsernameUpdateDTO dto,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        AppResponse<String> res = profileService.updateUsername(dto, lang);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/username/confirm")
    public ResponseEntity<AppResponse<String>> updateUsernameConfirm(@Valid @RequestBody CodeConfirmDTO dto,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        AppResponse<String> res = profileService.updateUsernameConfirm(dto, lang);
        return ResponseEntity.ok(res);
    }
}
