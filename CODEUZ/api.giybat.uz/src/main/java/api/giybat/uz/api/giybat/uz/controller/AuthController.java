package api.giybat.uz.api.giybat.uz.controller;

import api.giybat.uz.api.giybat.uz.dto.RegistrationDTO;
import api.giybat.uz.api.giybat.uz.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO dto){
    return ResponseEntity.ok().body(authService.registration(dto));
    }

    @GetMapping("/registration/verification/{token}")
    public ResponseEntity<String> regVerification(@PathVariable("token") String token){
        return ResponseEntity.ok().body(authService.regVerification(token));
    }
}
