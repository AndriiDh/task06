package com.task06.dto;

import java.io.Serializable;

public class Response implements Serializable {
    private Integer statusCode;
    private String message;

    private Response() {
    }

    public static class Builder {
        private Integer statusCode;
        private String message;

        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Response build() {
            Response response = new Response();
            response.setStatusCode(statusCode);
            response.setMessage(message);
            return response;
        }
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}