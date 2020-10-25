package saaf.common.fmw.message.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DemoUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoUtils.class);
    public DemoUtils() {
        super();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException {
        meetquery("403", "e:\\Excel\\1火灾三级预案处置流程.xlsx", 3);
    }

    private static List<Map<String, Object>> meetquery(InputStream inputStream, String fileName, int fromLine) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Workbook workbook = null; // 一个workbook对象，就是一个Excel文件
            Sheet sheet = null; // sheet页，因excel总有多个sheet页，需要判断具体取值哪一个
            Row row1 = null; // Sheet页数中的某一行
            int colNum = 0; // Sheet总行数
            Cell cell = null; // 第一列内容
            Cell cell1 = null; // 第二列内容
            String meetname = null; // 要点名称
            String meetid = null; // 要点编号
            String meethine = null; // 要点提示内容
            String meettime = null; // 处置时间
            //MeetBean meet = null;
            Map<String, Object> meet = null;
            //判断文件是什么格式 2003/2007 根据版本不同 处置对象也不同
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream); // Excel 2003
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream); // Excel 2007
            } else {
                return list;
            }
            sheet = workbook.getSheetAt(0);
            colNum = sheet.getLastRowNum(); // 总行数 不包括标题内容
            LOGGER.info("共有：" + colNum + "行");
            for (int i = fromLine; i <= colNum; i++) {
                meet = new HashMap<String, Object>();
                row1 = sheet.getRow(i); // 要解析的行数
                cell = row1.getCell((short)2); // 要解析要点名称的列数
                cell1 = row1.getCell((short)4); // 要解析要点提示内容的列数
                if (cell != null && cell1 != null) {
                    meetname = cell.getStringCellValue();
                    meethine = cell1.getStringCellValue();
                    meetid = "YD" + i;
                    // 如果处置要点名称为空，则是循环到了最后一个处置要点，则返回。。。
                    if (!meetname.equals("")) {
                        String intstr = String.valueOf((int)(Math.random() * 10 + 1)); // 生成1-10的随机数
                        // 如果是1-9随机数，则需要自动补零 时间格式为00:00:00
                        if (intstr.length() < 2) {
                            String min = "0" + intstr;
                            meettime = "00:" + min + ":00";
                        } else {
                            meettime = "00:" + intstr + ":00";
                        }
                        meet.put("meetid", meetid);
                        meet.put("meetname", meetname);
                        meet.put("meethine", meethine);
                        meet.put("meettime", meettime);
                        meet.put("meetlevel", "401");
                        list.add(meet);
                    } else {
                        return list;
                    }
                } else {
                    return list;
                }
            }
            inputStream.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return list;
    }

    private static List<Map<String, Object>> meetquery(String level, String fileName, int fromLine) throws FileNotFoundException {
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        return meetquery(inputStream, fileName, fromLine);
    }
}
