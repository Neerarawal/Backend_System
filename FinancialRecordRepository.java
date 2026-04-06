package com.zorvyn.finance.repository;

import com.zorvyn.finance.entity.FinancialRecord;
import com.zorvyn.finance.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    Optional<FinancialRecord> findByIdAndDeletedFalse(Long id);

    Page<FinancialRecord> findAllByDeletedFalse(Pageable pageable);

    // Filter by type
    Page<FinancialRecord> findAllByTypeAndDeletedFalse(RecordType type, Pageable pageable);

    // Filter by category (case-insensitive)
    Page<FinancialRecord> findAllByCategoryIgnoreCaseAndDeletedFalse(String category, Pageable pageable);

    // Filter by date range
    Page<FinancialRecord> findAllByDateBetweenAndDeletedFalse(LocalDate from, LocalDate to, Pageable pageable);

    // Filter by type + category
    Page<FinancialRecord> findAllByTypeAndCategoryIgnoreCaseAndDeletedFalse(
            RecordType type, String category, Pageable pageable);

    // Aggregates for dashboard
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = :type AND r.deleted = false")
    BigDecimal sumByType(@Param("type") RecordType type);

    @Query("SELECT r.category, SUM(r.amount) FROM FinancialRecord r WHERE r.deleted = false GROUP BY r.category")
    List<Object[]> sumGroupedByCategory();

    @Query("""
            SELECT YEAR(r.date), MONTH(r.date), r.type, SUM(r.amount)
            FROM FinancialRecord r
            WHERE r.deleted = false
            GROUP BY YEAR(r.date), MONTH(r.date), r.type
            ORDER BY YEAR(r.date) DESC, MONTH(r.date) DESC
            """)
    List<Object[]> monthlyTrendRaw();

    // Recent 10 records
    List<FinancialRecord> findTop10ByDeletedFalseOrderByDateDesc();
}
