package ma.hariti.asmaa.wrm.inventivit.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponseDTO<T> {
    private final boolean success;
    private final T data;
    private final String error;
    private final int status;

    public static <T> ApiResponseDTO<T> success(T data, int totalElements) {
        return new ApiResponseDTO<>(true, data, null, totalElements);
    }

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, data, null, 0);
    }
    public static <T> ApiResponseDTO<T> error(String message, String errorCode, int status) {
        return new ApiResponseDTO<>(false, null, message, status);
    }





}