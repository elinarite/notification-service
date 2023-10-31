package com.example.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketResponse {

    private String c;
    private Data d;
    private String s;
    private Long t;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private List<Deal> deals;
        private String e;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Deal {
        private String p;
        private String v;
        @JsonProperty("S")
        private int status;
        private long t;
    }
}