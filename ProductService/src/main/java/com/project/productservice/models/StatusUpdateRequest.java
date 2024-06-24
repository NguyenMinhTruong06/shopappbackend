package com.project.productservice.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusUpdateRequest {
        @JsonProperty(namespace = "status")
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

