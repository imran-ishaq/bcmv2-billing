package com.itmaxglobal.billing.repository;

import com.itmaxglobal.billing.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDesc(String imei, String imsi, String msisdn);

}
