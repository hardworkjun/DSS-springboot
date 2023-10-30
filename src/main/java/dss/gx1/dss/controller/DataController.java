package dss.gx1.dss.controller;

import dss.gx1.dss.service.DataService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class DataController {

    @Resource
    private DataService dataService;

    @GetMapping("/pv/{year}/{month}")
    public Map<String , Object> getPassengerVolume(
         @PathVariable("month") Integer month ,
         @PathVariable("year") Integer year
    ){
        return dataService.getPassengerVolume(year , month);
    }

    @GetMapping("/statistics/{year}/{month}")
    public Map<String , Object> getStatistics(
            @PathVariable("month") Integer month ,
            @PathVariable("year") Integer year
    ){
        return dataService.getStatistics(year , month);
    }

    @GetMapping("/volume/{area}")
    public Map<String, Object> getVolumeByArea(
            @PathVariable("area") String area
    ){
        return dataService.getVolumeByArea(area);
    }

    @GetMapping("/vol/{area}/{month}")
    public Map<String, Object> getVolumeByAreaAndMonth(
            @PathVariable("area") String area ,
            @PathVariable("month") Integer month
    ){
        return dataService.getVolumeByAreaAndMonth(area , month);
    }

    @GetMapping("/max/{year}/{month}")
    public Map<String , Object> getMaxVolume(
            @PathVariable("month") Integer month ,
            @PathVariable("year") Integer year
    ){
        return dataService.getMaxVolume(year , month);
    }


    @GetMapping("/fitting/{area}/{year}/{month}/{n}")
    public Map<String , Object> getDataFitting(
            @PathVariable("area") String area,
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month,
            @PathVariable("n") Integer n
    ){
        return dataService.getDataFitting(area , year , month , n);
    }

    @GetMapping("/series/{area}/{alpha}/{year}/{month}/{type}")
    public Map<String , Object> getTimeSeries(
            @PathVariable("area") String area,
            @PathVariable("alpha") Double alpha,
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month,
            @PathVariable("type") String type
    ){
        return dataService.getTimeSeries(area , alpha , year , month , type);
    }

    @GetMapping("/cluster/{beforeYear}/{beforeMonth}/{lateYear}/{lateMonth}/{k}")
    public Map<String , Object> getClusterRes(
            @PathVariable("beforeYear") Integer beforeYear,
            @PathVariable("beforeMonth") Integer beforeMonth,
            @PathVariable("lateYear") Integer lateYear,
            @PathVariable("lateMonth") Integer lateMonth,
            @PathVariable("k") Integer k
    ){
        return dataService.getClusterRes(beforeYear , beforeMonth , lateYear , lateMonth , k);
    }

}
