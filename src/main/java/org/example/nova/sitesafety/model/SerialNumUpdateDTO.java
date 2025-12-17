package org.example.nova.sitesafety.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SerialNumUpdateDTO {

    private String regionCode;
    private String cityCode;
    private Integer lastSerialNum;
}
