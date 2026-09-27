package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.TempEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempRepository extends JpaRepository<TempEntity, Long> {
}
