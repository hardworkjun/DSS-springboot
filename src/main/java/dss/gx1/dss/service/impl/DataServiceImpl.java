package dss.gx1.dss.service.impl;

import dss.gx1.dss.dao.DataMapper;
import dss.gx1.dss.entity.PVModel;
import dss.gx1.dss.service.DataService;
import dss.gx1.dss.utils.KMeansClustering;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.StatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.geom.Area;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    @Resource
    private DataMapper dataMapper;
    @Override
    public Map<String, Object> getPassengerVolume(Integer year, Integer month) {
        Map<String, Object> map = new HashMap<>();
        for (PVModel model : dataMapper.getPVModelsByDate(year , month)) {
            map.put(model.getArea() , model.getVolume());
        }
        return map;
    }

    @Override
    public Map<String, Object> getStatistics(Integer year, Integer month) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> statistics = dataMapper.getStatistics(year, month);
        map.put("max" , statistics.get(0));
        for (int i = 1 ; i < statistics.size() ; i++){
            if ((int)statistics.get(i).get("volume") < (int)statistics.get(0).get("volume")){
                map.put("min", statistics.get(i));
                break;
            }
        }

        List<PVModel> models = dataMapper.getPVModelsByDate(year, month);
        double[] data = new double[models.size()];
        for (int i = 0 ; i < data.length ; i++){
            data[i] = models.get(i).getVolume();
        }
        map.put("avg" , String.format("%.2f" , StatUtils.mean(data)));
        map.put("variance" , String.format("%.2f" , StatUtils.variance(data)));

        return map;
    }

    @Override
    public Map<String, Object> getVolumeByArea(String area) {
        Map<String, Object> map = new HashMap<>();
        for (PVModel model : dataMapper.getPVModelsByArea(area)) {
            map.put(model.getStatisticsYear()+ "年" + model.getStatisticsMonth()+ "月" , model.getVolume());
        }

        return map;
    }


    @Override
    public Map<String, Object> getVolumeByAreaAndMonth(String area, Integer month) {
        Map<String, Object> map = new HashMap<>();
        for (PVModel model : dataMapper.getSortModelByAreaAndMonth(area, month)) {
            map.put(model.getStatisticsYear() + "" , model.getVolume());
        }

        return map;
    }

    @Override
    public Map<String, Object> getMaxVolume(Integer year, Integer month) {
        Map<String , Object> map = new HashMap<>();
        PVModel maxVolume = dataMapper.getMaxVolume(year, month);
        System.out.println(maxVolume);
        map.put(maxVolume.getArea() , maxVolume.getVolume());
        return map;
    }



    //数据拟合
    @Override
    public Map<String, Object> getDataFitting(String area, Integer year, Integer month , Integer n) {
        Map<String, Object> map = new HashMap<>();

        // 创建WeightedObservedPoints对象并添加数据点
        WeightedObservedPoints obs = new WeightedObservedPoints();
        List<List<Integer>> rowList = new ArrayList<>();
        List<List<Double>> resList = new ArrayList<>();
        for (PVModel model : dataMapper.getSortModelByAreaAndMonth(area, month)) {
            List<Integer> list = new ArrayList<>();
            list.add(model.getStatisticsYear()-2014);
            list.add(model.getVolume());
            obs.add(model.getStatisticsYear()-2014 , model.getVolume());
            rowList.add(list);
        }
        // 使用PolynomialCurveFitter进行拟合
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(n);
        double[] coefficients = fitter.fit(obs.toList());

        System.out.println(Arrays.toString(coefficients));
        for (int i = 2015 ; i < 2023 ; i++){
            List<Double> list = new ArrayList<>();
            list.add(i - 2014.0);
            list.add(powFunction(coefficients , i));
            resList.add(list);
        }
        map.put("PredictedValue" , powFunction(coefficients , year));
        map.put("RowList" , rowList);
        map.put("PreList" , resList);
        return map;
    }

    @Override
    public Map<String, Object> getTimeSeries(String area, Double alpha, Integer year , Integer month , String type) {
        Map<String, Object> map = new HashMap<>();
        //根据地区和月份获取数据
        List<PVModel> models = dataMapper.getSortModelByAreaAndMonth(area, month);
        double[] data = new double[models.size()];
        for (int i = 0; i < data.length ; i++){
            data[i] = models.get(i).getVolume();
        }
        //对数据进行时间序列分析
        double[] handledData = "ma".equals(type) ? movingAverage(data , alpha.intValue() , year) : exponentialSmoothing(data, alpha);

        double[][] rawList = new double[data.length][2];
        double[][] preList = new double[handledData.length][2];

        for (int i = 0 ; i < handledData.length ; i++){
            if (i < data.length){
                rawList[i][0] = i;
                rawList[i][1] = data[i];
            }
            preList[i][0] = i+1;
            preList[i][1] = Double.parseDouble(String.format("%.2f" , handledData[i]));
        }
        //返回预测值
        map.put("rawList" , rawList);
                                                                                                                                                                                                                                                                                                                                                                        map.put("preList" , preList);
                                                                                                                                                                                                                                                                                                                                                                        return map;
    }

    @Override
    public Map<String, Object> getClusterRes(Integer beforeYear, Integer beforeMonth, Integer lateYear, Integer lateMonth , Integer k ) {
        Map<String, Object> map = new HashMap<>();
        List<PVModel> models = dataMapper.getModelsByYearAndMonth(beforeYear, beforeMonth, lateYear, lateMonth);
        List<Double> data = models.stream().map((model)->(double)model.getVolume()).collect(Collectors.toList());
        KMeansClustering kMeansClustering = new KMeansClustering(data , k);
        kMeansClustering.run();
        map.put("Centroids" , kMeansClustering.getCentroids());
        map.put("Clusters" , kMeansClustering.getClusters());
        return map;
    }

    //幂函数计算
    public double powFunction(double[] coefficients , Integer x){
        double predictedValue = 0;
        for (int i = 0 ; i < coefficients.length ; i++){
            predictedValue += Math.pow(x - 2014 , i) * coefficients[i];
        }
        return Double.parseDouble(String.format("%.2f" , predictedValue));
    }

    //指数平滑
    public double[] exponentialSmoothing(double[] data, double alpha) {
        int n = data.length;
        double[] smoothedData = new double[n];

        smoothedData[0] = data[0]; // 初始化平滑后的数据的第一个值为原始数据的第一个值

        for (int i = 1; i < n; i++) {
            smoothedData[i] = alpha * data[i] + (1 - alpha) * smoothedData[i - 1];
        }

        return smoothedData;
    }

    //移动平均
    public double[] movingAverage(double[] data, int windowSize , int year) {
        int n = year - 2014;
        double[] smoothedData = new double[n];

        for (int i = 0; i < n; i++) {
            int startIndex = Math.max(0, i - windowSize + 1);
            int endIndex = i + 1;
            double sum = 0;

            for (int j = startIndex; j < endIndex; j++) {
                sum += j >= data.length ? smoothedData[j-1] : data[j];
            }

            smoothedData[i] = sum / (endIndex - startIndex);
        }

        return smoothedData;
    }
}
