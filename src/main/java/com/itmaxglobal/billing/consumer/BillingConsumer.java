package com.itmaxglobal.billing.consumer;

import com.itmaxglobal.billing.dto.BillingStatusRequestDTO;
import com.itmaxglobal.billing.entity.Session;
import com.itmaxglobal.billing.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Slf4j
public class BillingConsumer {
    private final SessionRepository sessionRepository;

    public BillingConsumer(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @RabbitListener(queues = {"${rabbitmq.billing-queue}"})
    public void consumeMessage(BillingStatusRequestDTO billingStatusRequestDTO) throws IOException {
        try{
            Session lastSession = sessionRepository.findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDesc(billingStatusRequestDTO.getImei(), billingStatusRequestDTO.getImsi(), billingStatusRequestDTO.getMsisdn());

            LocalDateTime updateDate = LocalDateTime.parse(billingStatusRequestDTO.getDateTobeUpdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"));
            if (Objects.nonNull(lastSession)) {
                lastSession.setUpdatedAt(updateDate);
                lastSession.setLastActivityDate(updateDate);
                sessionRepository.save(lastSession);
                log.info("Last_activity_date updated for  - IMEI [{}] IMSI [{}] MSISDN [{}] MODEL-TYPE [{}]", billingStatusRequestDTO.getImei(),
                        billingStatusRequestDTO.getImsi(), billingStatusRequestDTO.getMsisdn(), billingStatusRequestDTO.getModelType());
            }
        } catch (Exception ex){
            log.error(ex.getMessage());
        }
    }
}
