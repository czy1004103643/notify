package xyz.melodyl.pojo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseBuilder {
    public static final ResponseEntity DEFAULT_ERROR_RESPONSE = newBuilder(HttpStatus.INTERNAL_SERVER_ERROR).build();

    private HttpHeaders responseHeader;
    private ResponseEntity.BodyBuilder bodyBuilder;
    private Object body;

    private ResponseBuilder(HttpStatus status) {
        responseHeader = new HttpHeaders();
        bodyBuilder = ResponseEntity.status(status);
    }

    public static ResponseBuilder newBuilder(HttpStatus status) {
        return new ResponseBuilder(status);
    }

    public static ResponseEntity ok(Object body) {
        return newBuilder(HttpStatus.OK).body(body).build();
    }

    public static ResponseEntity created() {
        return newBuilder(HttpStatus.CREATED).build();
    }

    public static ResponseEntity found() {
        return newBuilder(HttpStatus.FOUND).build();
    }

    public static ResponseEntity badRequest() {
        return newBuilder(HttpStatus.BAD_REQUEST).build();
    }

    public static ResponseEntity notFound() {
        return newBuilder(HttpStatus.NOT_FOUND).build();
    }

    public static ResponseEntity conflict() {
        return newBuilder(HttpStatus.CONFLICT).build();
    }

    public static ResponseEntity unAuthorized() {
        return ResponseBuilder.newBuilder(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseBuilder header(String name, String value) {
        responseHeader.add(name, value);
        return this;
    }

    public ResponseBuilder headers(Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseHeader.add(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public ResponseBuilder body(Object body) {
        this.body = body;
        return this;
    }

    public ResponseEntity build() {
        return bodyBuilder.headers(responseHeader).body(body);
    }
}
