package ch.no1hardy.service.front.group;

public record GroupReq(
        String name,
        Boolean isKnockout,
        Integer order,
        String thumbnail
) {
}
