package com.kari.sdds.entity;

import java.time.Instant;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Measurement(name = "Ms700")
public class Ms700 {

    @Column(timestamp = true)
    Instant time;

    @Column(tag = true)
    String type;

    @Column
    Double value;

    @Builder
    public Ms700(Instant time, String type, Double value) {
        this.time = time;
        this.type = type;
        this.value = value;
    }
}
