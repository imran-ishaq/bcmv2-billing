package com.itmaxglobal.billing.consumer;

import com.itmaxglobal.billing.dto.BillingStatusRequestDTO;
import com.itmaxglobal.billing.entity.Session;
import com.itmaxglobal.billing.entity.SessionHistory;
import com.itmaxglobal.billing.repository.SessionHistoryRepository;
import com.itmaxglobal.billing.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class BillingConsumer {
    private final SessionRepository sessionRepository;
    private final SessionHistoryRepository sessionHistoryRepository;

    public BillingConsumer(SessionRepository sessionRepository, SessionHistoryRepository sessionHistoryRepository) {
        this.sessionRepository = sessionRepository;
        this.sessionHistoryRepository = sessionHistoryRepository;
    }

    @RabbitListener(id = "billingActivity", queues = {"${rabbitmq.billing-queue}"}, containerFactory = "rabbitListenerContainerFactory")
    public void consumeMessage(BillingStatusRequestDTO billingStatusRequestDTO) {
        try {

            Long imsi = Long.parseLong(billingStatusRequestDTO.getImsi());
            Optional<Session> lastSession = sessionRepository.findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDesc(billingStatusRequestDTO.getImei(), imsi, billingStatusRequestDTO.getMsisdn());

            LocalDateTime updateDate = LocalDateTime.parse(billingStatusRequestDTO.getDateTobeUpdate());
            if (lastSession.isPresent()) {
                lastSession.get().setUpdatedAt(updateDate);
                lastSession.get().setLastActivityDate(updateDate);
                sessionRepository.save(lastSession.get());

            } else {
                Optional<SessionHistory> lastSessionHistory = sessionHistoryRepository.findFirstByImeiAndImsiAndMsisdnOrderByUpdatedDateDesc(billingStatusRequestDTO.getImei(), imsi, billingStatusRequestDTO.getMsisdn());
                if (lastSessionHistory.isPresent()) {
                    lastSessionHistory.get().setUpdatedDate(updateDate);
                    lastSessionHistory.get().setLastActivityDate(updateDate);
                    sessionHistoryRepository.save(lastSessionHistory.get());
                }
            }

            log.info("Last_activity_date updated for  - IMEI [{}] IMSI [{}] MSISDN [{}] MODEL-TYPE [{}]", billingStatusRequestDTO.getImei(),
                    billingStatusRequestDTO.getImsi(), billingStatusRequestDTO.getMsisdn(), billingStatusRequestDTO.getModelType());
        } catch (NumberFormatException ex) {
            log.error("Invalid IMSI value: [{}] for IMEI [{}] MSISDN [{}]", billingStatusRequestDTO.getImsi(), billingStatusRequestDTO.getImei(), billingStatusRequestDTO.getMsisdn());
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
