package com.example.moyiza_be.event.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {
    // 정리필요
    OK("정상", HttpStatus.OK),
    FILE_SAVE_FAIL("파일 저장에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    WRONG_IMAGE_FORMAT("지원하지 않는 파일 형식입니다.", HttpStatus.BAD_REQUEST),

    DELETE_COMMENT( "댓글 삭제 성공", HttpStatus.OK),
    DELETED_COMMENT( "삭제된 댓글입니다.", HttpStatus.OK),
    CREATE_COMMENT( "댓글 작성 성공", HttpStatus.OK),
    UPDATE_COMMENT( "댓글 수정 성공", HttpStatus.OK),
    CREATE_MEETING( "모임 글 작성 성공", HttpStatus.OK),
    UPDATE_MEETING( "모임 글 수정 성공", HttpStatus.OK),
    UPDATE_MEETING_IMAGE( "모임 글 이미지 수정 성공", HttpStatus.OK),
    GET_UPDATE_PAGE( "모임 글 수정 페이지 불러오기 성공", HttpStatus.OK),
    UPDATE_LINK( "모임 링크 추가 성공", HttpStatus.OK),
    DELETE_MEETING( "모임 글 삭제 성공", HttpStatus.OK),
    CREATE_ENTER("모임 입장 성공", HttpStatus.OK),
    UPDATE_PROFILE( "프로필 내용 수정 성공", HttpStatus.OK),
    UPDATE_PROFILE_URL( "프로필 이미지 변경 성공", HttpStatus.OK),
    GET_PROFILE_UPDATE_PAGE( "프로필 수정 페이지 불러오기 성공", HttpStatus.OK),
    DELETE_PROFILE_URL( "프로필 이미지 삭제 성공", HttpStatus.OK),
    NO_IMAGE("프로필 이미지 파일을 선택해주세요.",HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("Invalid parameter included",HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("Bad request",HttpStatus.BAD_REQUEST),
    NO_MEETING("모임 글이 존재하지 않습니다", HttpStatus.NOT_FOUND),
    NO_COMMENT("댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NO_ATTENDANT("해당 모임에 찾으시는 참석자가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_USER("작성자만 삭제/수정할 수 있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_DELETE("작성자만 할 수 있습니다.", HttpStatus.BAD_REQUEST),
    DELETE_USER( "회원 탈퇴 성공", HttpStatus.OK),
    NOT_FOUND_AUTHORIZATION_IN_SECURITY_CONTEXT("Security Context에 인증 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_SIGNUP_SUCCESS("회원가입 성공", HttpStatus.OK),
    USER_SIGNUP_FAIL("회원가입 실패", HttpStatus.BAD_REQUEST),
    USER_LOGIN_SUCCESS("로그인 성공", HttpStatus.OK),
    USER_FOLLOW_SUCCESS("유저 팔로우 성공", HttpStatus.OK),
    USER_FOLLOW_FAIL("유저 팔로우 실패", HttpStatus.OK),
    USER_UNFOLLOW_SUCCESS("유저 언팔로우 성공", HttpStatus.OK),
    ONLY_FOR_ADMIN("관리자만 가능합니다.", HttpStatus.BAD_REQUEST),
    ONLY_FOR_USER("관리자는 불가능합니다.", HttpStatus.BAD_REQUEST),
    WRONG_EMAIL_PATTERN("올바른 이메일 형식이 아닙니다.", HttpStatus.BAD_REQUEST),
    WRONG_USERNAME_PATTERN("닉네임은 최소 5자 이상, 10자이하 이어야 합니다.", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD_PATTERN("비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다.", HttpStatus.BAD_REQUEST),
    FOLLOWING_LIST_LOED("팔로잉 리스트 불러오기 성공",HttpStatus.OK),
    FOLLOWERS_LIST_LOED("팔로워 리스트 불러오기 성공",HttpStatus.OK),
    EMAIL_CODE("이메일인증코드 발송완료", HttpStatus.OK),
    EMAIL_CONFIRM_BAD("이메일을 확인해주세요",HttpStatus.BAD_REQUEST),
    EMAIL_CONFIRM_CODE_BAD("인증코드를 확인해주세요",HttpStatus.BAD_REQUEST),
    EMAIL_CONFIRM_SUCCESS("인증완료",HttpStatus.OK),

    WRONG_SECRET_PASSWORD("비밀번호는 4글자여야 합니다.", HttpStatus.BAD_REQUEST),

    WRONG_PASSWORD("비밀번호를 다시 입력해주세요", HttpStatus.BAD_REQUEST),
    TOO_LONG_TITLE("제목은 최대 20글자입니다.", HttpStatus.BAD_REQUEST),
    NO_USER("회원을 찾을 수 없습니다.",HttpStatus.BAD_REQUEST),
    WRONG_ADMIN_TOKEN("관리자 암호가 틀려 등록이 불가능합니다.", HttpStatus.BAD_REQUEST),
    OVERLAPPED_USERNAME("중복된 username 입니다.", HttpStatus.BAD_REQUEST),
    OVERLAPPED_EMAIL("중복된 email 입니다",HttpStatus.BAD_REQUEST),
    NO_SUCH_CATEGORY("존재하지 않는 카테고리입니다.", HttpStatus.BAD_REQUEST),
    NO_SUCH_PLATFORM("존재하지 않는 플랫폼입니다.", HttpStatus.BAD_REQUEST),
    NO_MORE_SEAT("정원 초과되었습니다.", HttpStatus.BAD_REQUEST),
    NO_MORE_REVIEW("이미 후기를 작성하였습니다.", HttpStatus.BAD_REQUEST),
    NO_AUTH_REVIEW("후기를 작성할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_ATTENDANCE_YET("아직 참석하지 않은 모임입니다.", HttpStatus.BAD_REQUEST),
    INVALID_MEETING("강퇴당한 모임입니다.", HttpStatus.BAD_REQUEST),
    IS_EXIST_ALARMS("알림 존재 여부 확인 성공",HttpStatus.OK),
    GET_ALARMS("알림 목록 조회 성공",HttpStatus.OK),
    GET_ALARM_COUNT("알림 개수 조회 성공",HttpStatus.OK),
    DELETE_ALARM("알림 삭제 성공",HttpStatus.OK),
    DELETE_ALARMS("알림 전체 삭제 성공",HttpStatus.OK),
    NO_ALARM("존재하지 않는 알림입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    UPDATE_PASSWORD("비밀번호 변경완료",HttpStatus.OK );

    private final String StatusMsg;
    private final HttpStatus statusCode;

    public String getStatusMsg(Throwable e){
        return this.getStatusMsg(this.getStatusMsg() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }
    public String getStatusMsg(String message){
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getStatusMsg());
    }

    public static Code valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new IllegalArgumentException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getStatusCode() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return Code.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return Code.INTERNAL_SERVER_ERROR;
                    } else {
                        return Code.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getStatusCode().value());
    }
}
