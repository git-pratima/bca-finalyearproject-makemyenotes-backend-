package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.UserRevisionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRevisionStatusRepository extends JpaRepository<UserRevisionStatus, Long> {
    //Optional<UserRevisionStatus> findByQidAndUseridAndCreatedDate(Long qid, Long userid, Date createdDate);

    @Query("SELECT u FROM UserRevisionStatus u " +
            "WHERE u.qid = :qid " +
            "AND u.userid = :userid " +
            "AND FUNCTION('DATE', u.createdDate) = :createdDate")
    List<UserRevisionStatus> findByQidAndUseridAndCreatedDate(
            @Param("qid") Long qid,
            @Param("userid") Long userid,
            @Param("createdDate") Date createdDate
    );


    @Query("SELECT MAX(u.count) FROM UserRevisionStatus u " +
            "WHERE u.userid = :userid " +
            "AND u.createdDate BETWEEN :start AND :end")
    Long findMaxCountByUseridAndDate(
            @Param("userid") Long userid,
            @Param("start") Date start,
            @Param("end") Date end
    );


    @Query("SELECT FUNCTION('DATE', u.createdDate), MAX(u.count) " +
            "FROM UserRevisionStatus u " +
            "WHERE u.userid = :userid " +
            "AND u.createdDate BETWEEN :start AND :end " +
            "GROUP BY FUNCTION('DATE', u.createdDate)")
    List<Object[]> findDailyMaxCountForMonth(
            @Param("userid") Long userid,
            @Param("start") Date start,
            @Param("end") Date end
    );

    @Query(value = """
    SELECT DATE(created_date), type, COUNT(*)
    FROM tbl_revision_status
    WHERE userid = :userId
      AND EXTRACT(MONTH FROM created_date) = EXTRACT(MONTH FROM CURRENT_DATE)
      AND EXTRACT(YEAR FROM created_date) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY DATE(created_date), type
    ORDER BY DATE(created_date)
    """, nativeQuery = true)
    List<Object[]> findTypeStatsByUserId(@Param("userId") Long userId);

}
