package dss.gx1.dss;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class sjxlTest {

    @Test
    public void zsph(){
        double[] data = {10, 12, 13, 14, 16, 18, 20}; // 输入的时间序列数据
        double alpha = 0.5; // 平滑系数

        double[] smoothedData = exponentialSmoothing(data, alpha , data.length);

        System.out.println("原始数据： " + Arrays.toString(data));
        System.out.println("平滑后的数据： " + Arrays.toString(smoothedData));
    }

    @Test
    public void ydpj(){
        double[] data = {10, 12, 13, 14, 16, 18, 20}; // 输入的时间序列数据
        int windowSize = 3; // 移动窗口大小

        double[] smoothedData = movingAverage(data, windowSize);

        System.out.println("原始数据： " + Arrays.toString(data));
        System.out.println("平滑后的数据： " + Arrays.toString(smoothedData));
    }



    //指数平滑
    public double[] exponentialSmoothing(double[] data, double alpha , int n) {
        double[] smoothedData = new double[n];

        smoothedData[0] = data[0]; // 初始化平滑后的数据的第一个值为原始数据的第一个值

        for (int i = 1; i < n; i++) {
            smoothedData[i] = alpha * data[i] + (1 - alpha) * smoothedData[i - 1];
        }

        return smoothedData;
    }

    //移动平均
    public static double[] movingAverage(double[] data, int windowSize) {
        int n = data.length;
        double[] smoothedData = new double[n];

        for (int i = 0; i < n; i++) {
            int startIndex = Math.max(0, i - windowSize + 1);
            int endIndex = i + 1;
            double sum = 0;

            for (int j = startIndex; j < endIndex; j++) {
                sum += data[j];
            }

            smoothedData[i] = sum / (endIndex - startIndex);
        }

        return smoothedData;
    }
}



