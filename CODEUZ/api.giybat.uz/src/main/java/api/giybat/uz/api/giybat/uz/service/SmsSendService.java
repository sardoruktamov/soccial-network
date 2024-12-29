package api.giybat.uz.api.giybat.uz.service;

import api.giybat.uz.api.giybat.uz.dto.sms.SmsAuthDTO;
import api.giybat.uz.api.giybat.uz.dto.sms.SmsAuthResponseDTO;
import api.giybat.uz.api.giybat.uz.entity.SmsProviderTokenHolderEntity;
import api.giybat.uz.api.giybat.uz.repository.SmsProviderTokenHolderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private SmsProviderTokenHolderRepository smsProviderTokenHolderRepository;


    public String sendSms(String phoneNuber, String message){
        return null;
    }

    public String getToken(){
        Optional<SmsProviderTokenHolderEntity> optional = smsProviderTokenHolderRepository.findTop1();
        if (optional.isEmpty()){
            String token = getTokenFromProvider();
            SmsProviderTokenHolderEntity entity = new SmsProviderTokenHolderEntity();
            entity.setToken(token);
            entity.setCreatedDate(LocalDateTime.now());
            smsProviderTokenHolderRepository.save(entity);
            return token;
        }
        SmsProviderTokenHolderEntity entity = optional.get();
        LocalDateTime expDate = entity.getCreatedDate().plusMonths(1);
        if (LocalDateTime.now().isBefore(expDate)){
            return entity.getToken();
        }
        // update token (agar tokenni vaqti tugagan bo`lsa )
        String token = getTokenFromProvider();
        entity.setToken(token);
        entity.setCreatedDate(LocalDateTime.now());
        return token;
    }

    public String getTokenFromProvider(){

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
