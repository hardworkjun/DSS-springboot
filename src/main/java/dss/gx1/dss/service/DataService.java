package dss.gx1.dss.service;

import java.util.List;
import java.util.Map;

public interface DataService {
    Map<String, Object> getPassengerVolume(Integer year, Integer month);

    Map<String, Object> getMaxVolume(Integer year, Integer month);


    Map<String, Object> getDataFitting(String area, Integer year, Integer month , Integer n );

    Map<String, Object> getVolumeByArea(String area);

    Map<String, Object> getTimeSeries(String area, Double alpha, Integer year , Integer month , String type);

    Map<String, Object> getClusterRes(Integer beforeYear, Integer beforeMonth, Integer lateYear, Integer lateMonth , Integer k );

    Map<String, Object> getVolumeByAreaAndMonth(String area, Integer month);

    Map<String, Object> getStatistics(Integer year, Integer month);
}
