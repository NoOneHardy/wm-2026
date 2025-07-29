package ch.no1hardy.service.front;

import lombok.Builder;

@Builder
public record BaseResponse<T>(
        T data,
        GlobalData globalData
) {
}
