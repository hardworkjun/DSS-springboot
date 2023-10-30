package dss.gx1.dss;

import dss.gx1.dss.dao.DataMapper;
import dss.gx1.dss.entity.PVModel;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@MapperScan
class DssApplicationTests {

    @Resource
    private DataMapper dataMapper;

    @Test
    void contextLoads() {
        String basePath = "C:\\Users\\lenovo\\Desktop\\DSS作业\\数据";
        File dir = new File(basePath);

        List<File> allFileList = new ArrayList<>();

        // 判断文件夹是否存在
        if (!dir.exists()) {
            System.out.println("目录不存在");
            return;
        }

        getAllFile(dir, allFileList);


        for (File file : allFileList) {

            try (Workbook workbook = WorkbookFactory.create(file)) {

                for (Sheet sheet : workbook) {
                    boolean isStart = false;
                    for (Row row : sheet) {
                        if (!isStart) {
                            isStart = true;
                            continue;
                        }

                        for (int i = 1; i < 13 ; i++) {
                            PVModel model = new PVModel();

                            model.setStatisticsYear(Integer.valueOf(sheet.getSheetName()));
                            model.setStatisticsMonth(i);

                            model.setArea(row.getCell(0).getStringCellValue());

                            model.setVolume((int) Double.parseDouble(row.getCell(i)+""));
                            dataMapper.insertModel(model);
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void getAllFile(File fileInput, List<File> allFileList) {
        // 获取文件列表
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                // 递归处理文件夹
                // 如果不想统计子文件夹则可以将下一行注释掉
                getAllFile(file, allFileList);
            } else {
                // 如果是文件则将其加入到文件数组中
                if (file.getName().matches(".*xlsx$")) {
                    allFileList.add(file);
                }

            }
        }
    }

}
