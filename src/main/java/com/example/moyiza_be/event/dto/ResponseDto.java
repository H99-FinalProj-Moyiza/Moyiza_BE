package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.errorcode.Code;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@RequiredArgsConstructor
public class ResponseDto {

    private final Boolean success;
    private final Integer statusCode;
    private final String statusMsg;

    public static ResponseDto of(Boolean success, Code code) {
        return new ResponseDto(success, code.getStatusCode().value(), code.getStatusMsg());
    }

    public static ResponseDto of(Boolean success, Code errorCode, Exception e) {
        return new ResponseDto(success, errorCode.getStatusCode().value(), errorCode.getStatusMsg(e));
    }

    public static ResponseDto of(Boolean success, Code errorCode, String message) {
        return new ResponseDto(success, errorCode.getStatusCode().value(), errorCode.getStatusMsg(message));
    }

}