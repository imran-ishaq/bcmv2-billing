package com.itmaxglobal.billing.repository;

import com.itmaxglobal.billing.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDesc(String imei, Long imsi, String msisdn);

}
