package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.VwTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VwTransactionRepository extends JpaRepository<VwTransaction, Long> {

    @Query(value = "SELECT set_config('app.current_user_id', CAST(:userId AS text), false)", nativeQuery = true)
    String setCurrentUserId(@Param("userId") Long userId);

    @Query(value = """
        SELECT * FROM vw_transaction
        WHERE (CAST(:category AS TEXT) IS NULL OR category ILIKE CONCAT('%', :category, '%'))
          AND (CAST(:subCategory AS TEXT) IS NULL OR sub_category ILIKE CONCAT('%', :subCategory, '%'))
          AND (CAST(:topic AS TEXT) IS NULL OR topic ILIKE CONCAT('%', :topic, '%'))
          AND (CAST(:type AS TEXT) IS NULL OR type ILIKE CONCAT('%', :type, '%'))
          AND (CAST(:level AS TEXT) IS NULL OR level ILIKE CONCAT('%', :level, '%'))
          AND (CAST(:question AS TEXT) IS NULL OR question ILIKE CONCAT('%', :question, '%'))
          AND (CAST(:bookmarked AS TEXT) IS NULL OR bookmarked = :bookmarked)
        """,
            countQuery = """
        SELECT COUNT(*) FROM vw_transaction
        WHERE (CAST(:category AS TEXT) IS NULL OR category ILIKE CONCAT('%', :category, '%'))
          AND (CAST(:subCategory AS TEXT) IS NULL OR sub_category ILIKE CONCAT('%', :subCategory, '%'))
          AND (CAST(:topic AS TEXT) IS NULL OR topic ILIKE CONCAT('%', :topic, '%'))
          AND (CAST(:type AS TEXT) IS NULL OR type ILIKE CONCAT('%', :type, '%'))
          AND (CAST(:level AS TEXT) IS NULL OR level ILIKE CONCAT('%', :level, '%'))
          AND (CAST(:question AS TEXT) IS NULL OR question ILIKE CONCAT('%', :question, '%'))
          AND (CAST(:bookmarked AS TEXT) IS NULL OR bookmarked = :bookmarked)
        """,
            nativeQuery = true)
    Page<VwTransaction> findAllTransactions(
            @Param("category") String category,
            @Param("subCategory") String subCategory,
            @Param("topic") String topic,
            @Param("type") String type,
            @Param("level") String level,
            @Param("bookmarked") String bookmarked,
            @Param("question") String question,
            Pageable pageable
    );
}
