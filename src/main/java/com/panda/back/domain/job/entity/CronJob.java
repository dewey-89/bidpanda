package com.panda.back.domain.job.entity;

import com.panda.back.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "cron_job")
public class CronJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "job_id")
    private Long jobId;

    public CronJob(Item item, Long jobId) {
        this.item = item;
        this.jobId = jobId;
    }
}
