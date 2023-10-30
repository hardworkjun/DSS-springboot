package dss.gx1.dss;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.junit.jupiter.api.Test;

public class DataFittingTest {

    @Test
    public void testTF(){
        // 输入数据
        double[] x = {10, 20, 30, 40,50,60,70,80,90,100,110,120};  // 自变量
        double[] y = {908, 1073, 667, 432, 664, 1032,1701,1700,950,1159,704,568};  // 因变量

        // 创建WeightedObservedPoints对象并添加数据点
        WeightedObservedPoints obs = new WeightedObservedPoints();
        for (int i = 0; i < x.length; i++) {
            obs.add(x[i], y[i]);
        }

        // 使用PolynomialCurveFitter进行拟合
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);  // 拟合三次函数
        double[] coefficients = fitter.fit(obs.toList());

        // 打印拟合参数
        System.out.println("拟合参数:");
        for (int i = 0; i < coefficients.length; i++) {
            System.out.println("a" + i + " = " + coefficients[i]);
        }
    }

    @Test
    public void testRa(){
        int year = 2019;
        int month = 1;

        System.out.println((year + (month << 5)) & year);
    }

}
