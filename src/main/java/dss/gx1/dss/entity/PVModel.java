package dss.gx1.dss.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PVModel {
    private Integer id;
    private Integer statisticsYear;
    private Integer statisticsMonth;
    private String area;
    private Integer volume;

    public PVModel() {
    }



}
