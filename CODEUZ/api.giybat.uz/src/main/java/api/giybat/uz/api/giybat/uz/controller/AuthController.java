package api.giybat.uz.api.giybat.uz.controller;

import api.giybat.uz.api.giybat.uz.dto.AppResponse;
import api.giybat.uz.api.giybat.uz.dto.AuthDTO;
import api.giybat.uz.api.giybat.uz.dto.ProfileDTO;
import api.giybat.uz.api.giybat.uz.dto.RegistrationDTO;
import api.giybat.uz.api.giybat.uz.dto.sms.SmsVerificationDTO;
import api.giybat.uz.api.giybat.uz.enums.AppLanguage;
import api.giybat.uz.api.giybat.uz.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<AppResponse<String>> registration(@Valid @RequestBody RegistrationDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(authService.registration(dto,lang));
    }

    @GetMapping("/registration/email-verification/{token}")
    public ResponseEntity<String> emailVerification(@PathVariable("token") String token,
                                                  @RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(authService.registrationEmailVerification(token,lang));
    }

    @PostMapping("/registration/sms-verification")
    public ResponseEntity<ProfileDTO> smsVerification(@Valid @RequestBody SmsVerificationDTO dto,
                                                  @RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(authService.registrationSmsVerification(dto,lang));
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthDTO dto,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(authService.login(dto,lang));
    }
}
