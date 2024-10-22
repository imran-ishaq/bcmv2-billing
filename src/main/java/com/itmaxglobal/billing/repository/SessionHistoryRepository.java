package com.itmaxglobal.billing.repository;

import com.itmaxglobal.billing.entity.SessionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionHistoryRepository extends JpaRepository<SessionHistory, Long> {

    Optional<SessionHistory> findFirstByImeiAndImsiAndMsisdnOrderByUpdatedDateDesc(String imei, Long imsi, String msisdn);

    @Query(value = "/**IDENTIFIER**/select s.* from session_history s where s.imei=:imei and s.imsi=:imsi and s.msisdn=:msisdn order by s.updated_date desc limit 1", nativeQuery = true)
    Optional<SessionHistory> findFirstByImeiAndImsiAndMsisdnOrderByUpdatedDateDescWithIdentifier(String imei, Long imsi, String msisdn);

}
