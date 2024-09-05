package com.itmaxglobal.billing.repository;

import com.itmaxglobal.billing.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends RevisionRepository<Session,Long, Integer>, JpaRepository<Session, Long> {

    Session findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDesc(String imei, String imsi, String msisdn);

}
