package ch.no1hardy.service.front.group;

import lombok.Data;

@Data
public class GroupReq {
    private String name;
    private Boolean isKnockout;
    private Integer order;
    private String thumbnail;
}
