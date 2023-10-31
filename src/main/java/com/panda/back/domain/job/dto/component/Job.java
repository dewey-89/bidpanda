package com.panda.back.domain.job.dto.component;

import lombok.Getter;

@Getter
public class Job {
    private String url;
    private String title;
    private boolean enabled;
    private boolean saveResponse;
    private Schedule schedule;
    private Integer requestMethod;

    public Job(String url, String title, Schedule schedule) {
        this.url = url;
        this.title = title;
        this.enabled = true;
        this.saveResponse = true;
        this.schedule = schedule;
        this.requestMethod = 0;
    }
}
