package com.itmaxglobal.billing.consumer;

import com.itmaxglobal.billing.dto.BillingStatusRequestDTO;
import com.itmaxglobal.billing.entity.Session;
import com.itmaxglobal.billing.repository.SessionRepository;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class BillingConsumer {
    private final SessionRepository sessionRepository;

    public BillingConsumer(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @RabbitListener(queues = {"${rabbitmq.billing-queue}"}, ackMode = "MANUAL")
    public void consumeMessage(BillingStatusRequestDTO billingStatusRequestDTO, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try{
            Session lastSession = sessionRepository.findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDesc(billingStatusRequestDTO.getImei(), billingStatusRequestDTO.getImsi(), billingStatusRequestDTO.getMsisdn());

            if (Objects.nonNull(lastSession)) {
                lastSession.setUpdatedAt(LocalDateTime.now());
                lastSession.setLastActivityDate(LocalDateTime.now());
                sessionRepository.save(lastSession);
                log.info("Last_activity_date updated for  - IMEI [{}] IMSI [{}] MSISDN [{}] MODEL-TYPE [{}]", billingStatusRequestDTO.getImei(),
                        billingStatusRequestDTO.getImsi(), billingStatusRequestDTO.getMsisdn(), billingStatusRequestDTO.getModelType());
                channel.basicAck(tag, false);
            }
        } catch (Exception ex){
            log.error(ex.getMessage());
            try {
                // Optionally, reject and requeue the message for further processing
                channel.basicNack(tag, false, true);
            } catch (Exception nackEx) {
                log.error(nackEx.getMessage());
            }
        }
    }
}
