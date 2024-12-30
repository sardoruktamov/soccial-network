package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.dto.sms.SmsAuthDTO;
import api.giybat.uz.api.giybat.uz.dto.sms.SmsAuthResponseDTO;
import api.giybat.uz.api.giybat.uz.dto.sms.SmsRequestDTO;
import api.giybat.uz.api.giybat.uz.dto.sms.SmsSendResponseDTO;
import api.giybat.uz.api.giybat.uz.entity.SmsProviderTokenHolderEntity;
import api.giybat.uz.api.giybat.uz.enums.SmsType;
import api.giybat.uz.api.giybat.uz.exps.AppBadException;
import api.giybat.uz.api.giybat.uz.repository.SmsProviderTokenHolderRepository;
import api.giybat.uz.api.giybat.uz.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class SmsSendService {
    @Value("${eskiz.url}")
    private String smsUrl;
    @Value("${eskiz.login}")
    private String accountLogin;
    @Value("${eskiz.password}")
    private String accountPassword;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SmsHistoryService smsHistoryService;

    @Autowired
    private SmsProviderTokenHolderRepository smsProviderTokenHolderRepository;
    Integer smsLimit = 1;

    public void sendRegistrationSms(String phoneNumber){
        String code = RandomUtil.getRandomSmsCode();
        String message = "MarketZone.uz portali, ro'yxatdan o'tishni tasdiqlash kodi: %s";
        message = String.format(message,code);
        sendSms(phoneNumber,message,code,SmsType.REGISTRATION);
    }
    private SmsSendResponseDTO sendSms(String phoneNuber, String message, String code, SmsType smsType){
        // check
        Long count = smsHistoryService.getSmsCount(phoneNuber);
        if (count >= smsLimit){
            throw new AppBadException("SMS limit reached");
        }
        SmsSendResponseDTO result = sendSms(phoneNuber, message);
        // sms save
        smsHistoryService.created(phoneNuber,message, code, smsType);
        return result;

    }

    private SmsSendResponseDTO sendSms(String phoneNuber, String message){
        // get token
        String token = getToken();
        // header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("Authorization","Bearer " + token);
        // body
        SmsRequestDTO body = new SmsRequestDTO();
        body.setMobile_phone(phoneNuber);
        // message -> vaqtincha o'zgaruvchi TODO message
        message = "Bu Eskiz dan test";
        body.setMessage(message);
        body.setFrom("4546");
        // send request
        HttpEntity<SmsRequestDTO> entity = new HttpEntity<>(body,headers);
        try {
            ResponseEntity<SmsSendResponseDTO> response = restTemplate.exchange(
                    smsUrl + "/message/sms/send",
                    HttpMethod.POST,
                    entity,
                    SmsSendResponseDTO.class);//smsUrlga POST request yubor ENTITYni va Stringga konvert qil

            System.out.println(response.toString());
            System.out.println("-------------SMS yuborildiiiii---------------");
            SmsSendResponseDTO responseDTO = new SmsSendResponseDTO();
            return response.getBody();
        }catch (RuntimeException e){
            e.printStackTrace();
            throw new AppBadException("SMS yuborishda xatolik bo'ldi!");

        }
    }

    private String getToken(){
        Optional<SmsProviderTokenHolderEntity> optional = smsProviderTokenHolderRepository.findTop1By();
        if (optional.isEmpty()){
            String token = getTokenFromProvider();
            SmsProviderTokenHolderEntity entity = new SmsProviderTokenHolderEntity();
            entity.setToken(token);
            entity.setCreatedDate(LocalDateTime.now());
            entity.setExpiredDate(LocalDateTime.now().plusMonths(1));
            smsProviderTokenHolderRepository.save(entity);
            System.out.println(token);
            return token;
        }
        SmsProviderTokenHolderEntity entity = optional.get();
//        LocalDateTime expDate = entity.getCreatedDate().plusMonths(1);
        if (LocalDateTime.now().isBefore(entity.getExpiredDate())){
            return entity.getToken();
        }
        // update token (agar tokenni vaqti tugagan bo`lsa yangi token oladi va update qiladi)
        String token = getTokenFromProvider();
        entity.setToken(token);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setExpiredDate(LocalDateTime.now().plusMonths(1));
        return token;
    }

    private String getTokenFromProvider(){

        SmsAuthDTO smsAuthDTO = new SmsAuthDTO();
        smsAuthDTO.setEmail(accountLogin);
        smsAuthDTO.setPassword(accountPassword);

        //1-usulda token olish
//        String response = restTemplate.postForObject(smsUrl + "/auth/login", smsAuthDTO, String.class);
//        JsonNode jsonNode = new ObjectMapper().readTree(response);
//        JsonNode data = jsonNode.get("data");
//        String token = data.get("token").asText();
//        log.info("TOKEN: " + token);
//        return token;

        try {
            // 2-usulda token olish
                SmsAuthResponseDTO response = restTemplate.postForObject(smsUrl + "/auth/login", smsAuthDTO, SmsAuthResponseDTO.class);
            System.out.println(response);
            return response.getData().getToken();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }



    }
}
