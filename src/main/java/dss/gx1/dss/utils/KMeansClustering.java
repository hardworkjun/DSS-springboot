package dss.gx1.dss.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansClustering {
    private List<Double> dataPoints;
    private int k;
    private List<Double> centroids;
    private List<List<Double>> clusters;

    public KMeansClustering(List<Double> dataPoints, int k) {
        this.dataPoints = dataPoints;
        this.k = k;
        this.centroids = new ArrayList<>();
        this.clusters = new ArrayList<>();
    }

    public void run() {
        initializeCentroids();
        boolean isConverged = false;

        while (!isConverged) {
            assignDataPointsToClusters();
            double previousCentroidSum = calculateCentroidSum();

            updateCentroids();
            double currentCentroidSum = calculateCentroidSum();

            if (Math.abs(previousCentroidSum - currentCentroidSum) < 0.0001) {
                isConverged = true;
            }
        }
    }

    //随机选择k个数据点作为初始聚类中心
    private void initializeCentroids() {
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int randomIndex = random.nextInt(dataPoints.size());
            centroids.add(dataPoints.get(randomIndex));
        }
    }

    //根据每个数据点与聚类中心的距离将数据点分配到最近的聚类中
    private void assignDataPointsToClusters() {
        clusters.clear();
        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }

        for (Double dataPoint : dataPoints) {
            int clusterIndex = getNearestCentroidIndex(dataPoint);
            clusters.get(clusterIndex).add(dataPoint);
        }
    }

    //
    private int getNearestCentroidIndex(double dataPoint) {
        int nearestIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < centroids.size(); i++) {
            double distance = Math.abs(dataPoint - centroids.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                nearestIndex = i;
            }
        }

        return nearestIndex;
    }

    //根据每个聚类中的数据点计算新的聚类中心
    private void updateCentroids() {
        for (int i = 0; i < k; i++) {
            List<Double> cluster = clusters.get(i);
            if (!cluster.isEmpty()) {
                double sum = 0;
                for (Double dataPoint : cluster) {
                    sum += dataPoint;
                }
                centroids.set(i, sum / cluster.size());
            }
        }
    }

    //计算当前聚类中心与所属聚类中的数据点之间的距离之和，用于判断算法是否收敛
    private double calculateCentroidSum() {
        double sum = 0;
        for (int i = 0; i < k; i++) {
            List<Double> cluster = clusters.get(i);
            for (Double dataPoint : cluster) {
                sum += Math.abs(dataPoint - centroids.get(i));
            }
        }
        return sum;
    }

    public List<Double> getCentroids() {
        return centroids;
    }

    public List<List<Double>> getClusters() {
        return clusters;
    }

    public static void main(String[] args) {
        // Example usage
        List<Double> dataPoints = new ArrayList<>();
        dataPoints.add(1.0);
        dataPoints.add(2.0);
        dataPoints.add(3.0);
        dataPoints.add(7.0);
        dataPoints.add(8.0);
        dataPoints.add(9.0);

        int k = 2;
        KMeansClustering kMeans = new KMeansClustering(dataPoints, k);
        kMeans.run();

        List<Double> centroids = kMeans.getCentroids();
        List<List<Double>> clusters = kMeans.getClusters();

        System.out.println("Centroids:");
        for (Double centroid : centroids) {
            System.out.println(centroid);
        }

        System.out.println("Clusters:");
        for (List<Double> cluster : clusters) {
            System.out.println(cluster);
        }
    }
}
