package com.panda.back.domain.job.dto;

import com.panda.back.domain.job.dto.component.Job;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobClientRequestDto {
    private Job job;
    public JobClientRequestDto(Job job) {
        this.job = job;
    }
}
