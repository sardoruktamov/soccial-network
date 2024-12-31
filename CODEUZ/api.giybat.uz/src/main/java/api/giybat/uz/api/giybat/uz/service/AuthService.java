package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.dto.AppResponse;
import api.giybat.uz.api.giybat.uz.dto.AuthDTO;
import api.giybat.uz.api.giybat.uz.dto.ProfileDTO;
import api.giybat.uz.api.giybat.uz.dto.RegistrationDTO;
import api.giybat.uz.api.giybat.uz.dto.sms.SmsVerificationDTO;
import api.giybat.uz.api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.api.giybat.uz.enums.AppLanguage;
import api.giybat.uz.api.giybat.uz.enums.GeneralStatus;
import api.giybat.uz.api.giybat.uz.enums.ProfileRole;
import api.giybat.uz.api.giybat.uz.exps.AppBadException;
import api.giybat.uz.api.giybat.uz.repository.ProfileRepository;
import api.giybat.uz.api.giybat.uz.repository.ProfileRoleRepository;
import api.giybat.uz.api.giybat.uz.repository.SmsHistoryRepository;
import api.giybat.uz.api.giybat.uz.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
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
    @Autowired
    private ResourceBundleService bundleService;
    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private SmsHistoryService smsHistoryService;

    public AppResponse<String> registration(RegistrationDTO dto, AppLanguage lang){

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
                throw new AppBadException(bundleService.getMessage("email.phone.exist", lang));
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

        // send email
//        emailSendingService.sendEmailForRegistration(dto.getUsername(), entity.getId(), lang);
        // send SMS
        smsSendService.sendRegistrationSms(dto.getUsername());

        return new AppResponse<>(bundleService.getMessage("email.confirm.send",lang));
    }

    public String registrationEmailVerification(String token, AppLanguage lang) {

        try{
            Integer profileId = JwtUtil.decodeRegVerToken(token);
            ProfileEntity profile = profileService.getById(profileId);
            if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)){
                // 1-usulda barcha fieldlarini update qiladi
//            profile.setStatus(GeneralStatus.ACTIVE);
//            profileRepository.save(profile);
                // 2-usulda faqat status update bo`ladi
                profileRepository.changeStatus(profileId,GeneralStatus.ACTIVE);
                return bundleService.getMessage("verification.finished",lang);
            }
        }catch (JwtException e){
        }
        throw new AppBadException(bundleService.getMessage("verification.failed",lang));
    }

    public ProfileDTO login(AuthDTO dto, AppLanguage lang){
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()){
            throw new AppBadException(bundleService.getMessage("username.password.wrong",lang));
        }
        ProfileEntity profile = optional.get();
        if(!bCryptPasswordEncoder.matches(dto.getPassword(), profile.getPassword())){
            throw new AppBadException(bundleService.getMessage("username.password.wrong",lang));
        }
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)){
            throw new AppBadException(bundleService.getMessage("status.error.register.again",lang));
        }

        // response
        return getLoginResponse(profile);
    }

    public ProfileDTO registrationSmsVerification(SmsVerificationDTO dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getPhoneNumber());
        if (optional.isEmpty()){
            throw new AppBadException(bundleService.getMessage("profile.not.found",lang));
        }
        ProfileEntity profile = optional.get();
        // checking status
        if (!profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)){
            throw new AppBadException(bundleService.getMessage("verification.failed",lang));
        }
        // checking sms code
        smsHistoryService.check(dto.getPhoneNumber(), dto.getCode(), lang);
        // ACTIVE
        profileRepository.changeStatus(profile.getId(),GeneralStatus.ACTIVE);
        // response
        return getLoginResponse(profile);
    }

    public ProfileDTO getLoginResponse(ProfileEntity profile){
        ProfileDTO response = new ProfileDTO();
        response.setName(profile.getName());
        response.setUsername(profile.getUsername());
        response.setRoleList(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));
        response.setJwt(JwtUtil.encode(profile.getUsername(), profile.getId(),response.getRoleList()));        // jwt
        return response;
    }
}
