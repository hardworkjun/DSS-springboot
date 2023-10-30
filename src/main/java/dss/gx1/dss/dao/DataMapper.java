package dss.gx1.dss.dao;

import dss.gx1.dss.entity.PVModel;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper {

    List<PVModel> getPVModelsByDate(@Param("year") Integer year , @Param("month") Integer month);

    PVModel getMaxVolume(@Param("year") Integer year , @Param("month") Integer month);

    List<PVModel> getPVModelsByArea(String area);

    long insertModel(PVModel model);

    List<PVModel> getSortModelByAreaAndMonth(@Param("area") String area , @Param("month") Integer month);

    List<PVModel> getModelsByYearAndMonth(@Param("beYear") Integer beYear , @Param("beMonth") Integer beMonth , @Param("laYear") Integer laYear , @Param("laMonth") Integer laMonth);

    List<Map<String , Object>> getStatistics(@Param("year") Integer year , @Param("month") Integer month);

}
