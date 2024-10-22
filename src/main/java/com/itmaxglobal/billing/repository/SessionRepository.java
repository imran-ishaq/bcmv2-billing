package com.itmaxglobal.billing.repository;

import com.itmaxglobal.billing.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDesc(String imei, Long imsi, String msisdn);

    @Query(value = "/**IDENTIFIER**/select s.* from session_new_2 s where s.imei=:imei and s.imsi=:imsi and s.msisdn=:msisdn order by s.updated_date desc limit 1", nativeQuery = true)
    Optional<Session> findFirstByImeiAndImsiAndMsisdnOrderByUpdatedAtDescWithIdentifier(@Param("imei") String imei, @Param("imsi") Long imsi, @Param("msisdn") String msisdn);

}
