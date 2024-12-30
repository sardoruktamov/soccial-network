package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.entity.SmsHistoryEntity;
import api.giybat.uz.api.giybat.uz.enums.SmsType;
import api.giybat.uz.api.giybat.uz.repository.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsHistoryService {

    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    public void created(String phone, String message, String code, SmsType smsType){
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhoneNumber(phone);
        entity.setMessage(message);
        entity.setCode(code);
        entity.setSmsType(smsType);
        entity.setCreatedDate(LocalDateTime.now());
        smsHistoryRepository.save(entity);
    }

    public Long getSmsCount(String phone){
        LocalDateTime now = LocalDateTime.now();
        return smsHistoryRepository.countByPhoneNumberAndCreatedDateBetween(phone,now.minusMinutes(1),now);
    }
}
