package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.dto.RegistrationDTO;
import api.giybat.uz.api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.api.giybat.uz.enums.GeneralStatus;
import api.giybat.uz.api.giybat.uz.enums.ProfileRole;
import api.giybat.uz.api.giybat.uz.exps.AppBadException;
import api.giybat.uz.api.giybat.uz.repository.ProfileRepository;
import api.giybat.uz.api.giybat.uz.repository.ProfileRoleRepository;
import api.giybat.uz.api.giybat.uz.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ProfileRoleRepository profileRoleRepository;
    @Autowired
    private ProfileRoleService profileRoleService;

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private ProfileService profileService;
    public String registration(RegistrationDTO dto){
    Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
    if (optional.isPresent()){
        ProfileEntity profile = optional.get();
        if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)){
            profileRoleService.deleteRoles(profile.getId());
            // 1-usul
             profileRepository.delete(profile);
            // 2-usul
            //send sms/email orqali ro'yxatdan o'tishini davom ettirish
        }else {
            throw new AppBadException("Username already exists");
        }
    }

    ProfileEntity entity = new ProfileEntity();
    entity.setName(dto.getName());
    entity.setUsername(dto.getUsername());
    entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
    entity.setStatus(GeneralStatus.IN_REGISTRATION);
    entity.setVisible(true);
    entity.setCreatedDate(LocalDateTime.now());
    profileRepository.save(entity);
    // Insert Role
    profileRoleService.create(entity.getId(), ProfileRole.ROLE_USER);

    emailSendingService.sendEmailForRegistration(dto.getUsername(), entity.getId());

        return null;
    }

    public String regVerification(String token) {

        try{
            Integer profileId = JwtUtil.decodeRegVerToken(token);
            ProfileEntity profile = profileService.getById(profileId);
            if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)){
                // 1-usulda barcha fieldlarini update qiladi
//            profile.setStatus(GeneralStatus.ACTIVE);
//            profileRepository.save(profile);
                // 2-usulda faqat status update bo`ladi
                profileRepository.changeStatus(profileId,GeneralStatus.ACTIVE);
                return "Verification finished!";
            }
        }catch (JwtException e){
        }
        throw new AppBadException("Verification failed!");
    }
}
