package com.itmaxglobal.billing.repository;

import com.itmaxglobal.billing.entity.SessionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionHistoryRepository extends JpaRepository<SessionHistory, Long> {

    Optional<SessionHistory> findFirstByImeiAndImsiAndMsisdnOrderByUpdatedDateDesc(String imei, Long imsi, String msisdn);

}
