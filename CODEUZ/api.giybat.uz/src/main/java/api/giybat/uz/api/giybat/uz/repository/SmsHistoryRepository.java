package api.giybat.uz.api.giybat.uz.repository;

import api.giybat.uz.api.giybat.uz.entity.SmsHistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity,String> {

    //select count(*) from sms_history where phone_number =? and created_date between ? and ?
    Long countByPhoneNumberAndCreatedDateBetween(String phoneNumber, LocalDateTime from, LocalDateTime to);
}
