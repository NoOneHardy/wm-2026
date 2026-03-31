package ch.no1hardy.service.front.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupOptionRes {
    private String id;
    private String name;
    private Boolean isKnockout;
    private Integer order;
    private String thumbnail;
}
