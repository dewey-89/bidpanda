package com.panda.back.domain.job.repository;

import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.job.entity.CronJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CronJobRepository extends JpaRepository<CronJob, Long> {
    Optional<CronJob> findByItem(Item item);
    void deleteCronJobByItem(Item item);
}
