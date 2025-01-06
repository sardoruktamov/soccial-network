package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.dto.AppResponse;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfileDetailUpdateDTO;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfilePasswordUpdateDTO;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfileUsernameUpdateDTO;
import api.giybat.uz.api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.api.giybat.uz.enums.AppLanguage;
import api.giybat.uz.api.giybat.uz.exps.AppBadException;
import api.giybat.uz.api.giybat.uz.repository.ProfileRepository;
import api.giybat.uz.api.giybat.uz.util.EmailUtil;
import api.giybat.uz.api.giybat.uz.util.PhoneUtil;
import api.giybat.uz.api.giybat.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    ResourceBundleService bundleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private EmailSendingService emailSendingService;

    public AppResponse<String> updateDetail(ProfileDetailUpdateDTO dto, AppLanguage lang){
        Integer userId = SpringSecurityUtil.getCurrentUserId();
        profileRepository.updateDetail(userId, dto.getName());
        return new AppResponse<>(bundleService.getMessage("profile.update.detail.success", lang));
    }

    public AppResponse<String> updatePassword(ProfilePasswordUpdateDTO dto, AppLanguage lang) {
        Integer userId = SpringSecurityUtil.getCurrentUserId();
        ProfileEntity profile = getById(userId, lang);
        if (!bCryptPasswordEncoder.matches(dto.getCurrentPswd(), profile.getPassword())){
            throw new AppBadException(bundleService.getMessage("current.password.incorrectly",lang));
        }

        profileRepository.updatePassword(userId, bCryptPasswordEncoder.encode(dto.getNewPswd()));
        return new AppResponse<String>(bundleService.getMessage("password.changed.successfully",lang));
    }

    public AppResponse<String> updateUsername(ProfileUsernameUpdateDTO dto, AppLanguage lang){
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        // check
        if (optional.isPresent()){
            return new AppResponse<>(bundleService.getMessage("username.already.registered", lang));
        }
        if(optional.isEmpty()){
            throw new AppBadException("Profile not found");
        }

        // send sms
        if (EmailUtil.isEmail(dto.getUsername())){
            // send email
            emailSendingService.sendEmailForRegistration(dto.getUsername(), optional.get().getId(), lang);
        }else if (PhoneUtil.isPhone(dto.getUsername())){
            // send SMS
            smsSendService.sendUsernameChangeConfirmSms(dto.getUsername(), lang);
        }
        Integer userId = SpringSecurityUtil.getCurrentUserId();
        return new AppResponse<>(bundleService.getMessage("profile.update.detail.success", lang));
    }
    public ProfileEntity getById(int id, AppLanguage lang){
        // 1-usul
//        Optional<ProfileEntity> optional = profileRepository.findByIdAndVisibleTrue(id);
//        if(optional.isEmpty()){
//            throw new AppBadException("Profile not found");
//        }
//        return optional.get();
        // 2-usul
        return profileRepository.findByIdAndVisibleTrue(id).orElseThrow( () -> {
        throw new AppBadException(bundleService.getMessage("profile.not.found", lang));
        });
    }


}
