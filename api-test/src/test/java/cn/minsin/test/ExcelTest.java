package cn.minsin.test;

import cn.minsin.excel.enums.ExcelVersion;
import cn.minsin.excel.function.ExcelCreateFunctions;
import cn.minsin.excel.function.ExcelParseFunctions;
import cn.minsin.excel.model.ExcelParseResultModel;
import cn.minsin.excel.model.ExcelRowModel;
import cn.minsin.excel.model.ExcelSheetModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 17:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ExcelTest {

    @Autowired
    private ExcelParseFunctions excelParseFunctions;


    @Test
    public void parse() throws IOException {
        ExcelParseResultModel cellValueList = excelParseFunctions
                //init有两个重载
                .init(new File("D://template.xlsx"))
                //获取值
                .getCellValueList(new String[]{"用户"}, 0, new int[]{0,1,2,3,4,5,6,7});
        //查找sheet
        ExcelSheetModel test = cellValueList.findSheet("用户");

        List<ExcelRowModel> rows = test.getRows();
        for (ExcelRowModel row : rows) {
            System.out.println(row);
        }
    }

    @Autowired
    private ExcelCreateFunctions excelCreateFunctions;

    @Test
    public void create() throws IOException {

        excelCreateFunctions
                .init(ExcelVersion.VERSION_2007)
                .sheet(0, "测试")
                .row(5).cell(1, "B")
                .cell(3, "C")
                .export("D://" + System.currentTimeMillis());

        excelCreateFunctions
                //resource下
                .init("temp/templateDemo.xlsx", false)
                .sheet(0, "测试")
                .row(5).cell(1, "B")
                .cell(3, "C")
                .export("D://" + System.currentTimeMillis());
    }
}
