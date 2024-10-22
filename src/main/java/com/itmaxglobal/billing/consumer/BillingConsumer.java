package com.itmaxglobal.billing.consumer;

import com.itmaxglobal.billing.dto.BillingStatusRequestDTO;
import com.itmaxglobal.billing.entity.Session;
import com.itmaxglobal.billing.entity.SessionHistory;
import com.itmaxglobal.billing.repository.SessionHistoryRepository;
import com.itmaxglobal.billing.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
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

    @RabbitListener(queues = {"${rabbitmq.billing-queue}"})
    public void consumeMessage(BillingStatusRequestDTO billingStatusRequestDTO) {
        try {
            Optional<Session> lastSession = sessionRepository.findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDesc(billingStatusRequestDTO.getImei(), Long.parseLong(billingStatusRequestDTO.getImsi()), billingStatusRequestDTO.getMsisdn())
                    .or(() -> sessionRepository.findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDescWithIdentifier(billingStatusRequestDTO.getImei(), Long.parseLong(billingStatusRequestDTO.getImsi()), billingStatusRequestDTO.getMsisdn()));

            LocalDateTime updateDate = LocalDateTime.parse(billingStatusRequestDTO.getDateTobeUpdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (lastSession.isPresent()) {
                lastSession.get().setUpdatedAt(updateDate);
                lastSession.get().setLastActivityDate(updateDate);
                sessionRepository.save(lastSession.get());

            } else {
                Optional<SessionHistory> lastSessionHistory = sessionHistoryRepository.findFirstByImeiAndImsiAndMsisdnOrderByUpdatedDateDesc(billingStatusRequestDTO.getImei(), Long.parseLong(billingStatusRequestDTO.getImsi()), billingStatusRequestDTO.getMsisdn())
                        .or(() -> sessionHistoryRepository.findFirstByImeiAndImsiAndMsisdnOrderByUpdatedDateDescWithIdentifier(billingStatusRequestDTO.getImei(), Long.parseLong(billingStatusRequestDTO.getImsi()), billingStatusRequestDTO.getMsisdn()));
                if (lastSessionHistory.isPresent()) {
                    lastSessionHistory.get().setUpdatedDate(updateDate);
                    lastSessionHistory.get().setLastActivityDate(updateDate);
                    sessionHistoryRepository.save(lastSessionHistory.get());
                }
            }

            log.info("Last_activity_date updated for  - IMEI [{}] IMSI [{}] MSISDN [{}] MODEL-TYPE [{}]", billingStatusRequestDTO.getImei(),
                    billingStatusRequestDTO.getImsi(), billingStatusRequestDTO.getMsisdn(), billingStatusRequestDTO.getModelType());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
