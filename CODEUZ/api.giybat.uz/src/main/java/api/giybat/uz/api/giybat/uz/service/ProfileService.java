package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.dto.AppResponse;
import api.giybat.uz.api.giybat.uz.dto.profile.ProfileDetailUpdateDTO;
import api.giybat.uz.api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.api.giybat.uz.enums.AppLanguage;
import api.giybat.uz.api.giybat.uz.exps.AppBadException;
import api.giybat.uz.api.giybat.uz.repository.ProfileRepository;
import api.giybat.uz.api.giybat.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    ResourceBundleService bundleService;

    public AppResponse<String> updateDetail(ProfileDetailUpdateDTO dto, AppLanguage lang){
        Integer userId = SpringSecurityUtil.getCurrentUserId();
        profileRepository.updateDetail(userId, dto.getName());
        return new AppResponse<>(bundleService.getMessage("profile.update.detail.success", lang));
    }

    public ProfileEntity getById(int id){
        // 1-usul
//        Optional<ProfileEntity> optional = profileRepository.findByIdAndVisibleTrue(id);
//        if(optional.isEmpty()){
//            throw new AppBadException("Profile not found");
//        }
//        return optional.get();
        // 2-usul
        return profileRepository.findByIdAndVisibleTrue(id).orElseThrow( () -> {
        throw new AppBadException("Profile not found");
        });
    }
}
