package api.giybat.uz.api.giybat.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_provider_token_holder_entity")
@Getter
@Setter
public class SmsProviderTokenHolderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
