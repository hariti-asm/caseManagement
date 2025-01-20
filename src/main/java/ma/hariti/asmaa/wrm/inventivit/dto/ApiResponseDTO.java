package ma.hariti.asmaa.wrm.inventivit.dto;


public record ApiResponseDTO<T>(
        boolean success,
        T data,
        String error,
        int status
) {
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