package com.patsnap.insights.trickydata.manager;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.Gson;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelManager extends BaseManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelManager.class);

    @Override
    public void generalJsonFile(String data) {

    }

    public String readFile() {
        String data = readExcelFile();
        generalJsonFile(data);
        return "success" + data;
    }

    /**
     * get json data from excel file
     *
     * @return
     */
    private String readExcelFile() {
        String data = "";
        File file = new File("/Users/caoliang/Downloads/20180521101235777.XLSX");
        List<LinkedHashMap<String, String>> listMaps = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(file);
            Workbook workbook = null;
            if (file.getName().toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (file.getName().toLowerCase().endsWith("xlsx")) {
                //2007
                workbook = new XSSFWorkbook(inputStream);
            }

            if(workbook != null){
                Sheet sheet = workbook.getSheetAt(0);
                int firstLine = sheet.getFirstRowNum();
                int lastLine = sheet.getLastRowNum();
                Map<String, String> titleMap = new HashMap<>();
                //read title
                Row row = sheet.getRow(firstLine);
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数
                int lastCellNum = row.getPhysicalNumberOfCells();

                LinkedHashMap<String, String> map = null;
                for(int rowNum = firstLine;rowNum <= lastLine;rowNum++){
                    map = new LinkedHashMap<>();
                    for (int index = firstCellNum; index <= lastCellNum; index++){
                        if (rowNum == firstLine) {
                            if (sheet.getRow(rowNum).getCell(index) != null){
                                titleMap.put(index + "", sheet.getRow(rowNum).getCell(index).getStringCellValue());
                            }
                        }else{
                            if (sheet.getRow(rowNum).getCell(index) != null){
                                switch (sheet.getRow(rowNum).getCell(index).getCellType()){
                                    case Cell.CELL_TYPE_BLANK:
                                        map.put(titleMap.get(index + ""), "");
                                        break;
                                    case Cell.CELL_TYPE_STRING:
                                        map.put(titleMap.get(index + ""), sheet.getRow(rowNum).getCell(index).getStringCellValue());
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC:
                                        map.put(titleMap.get(index + ""), BigDecimal.valueOf(sheet.getRow(rowNum).getCell(index).getNumericCellValue()).toPlainString());
                                        break;
                                    case Cell.CELL_TYPE_BOOLEAN:
                                        map.put(titleMap.get(index + ""), String.valueOf(sheet.getRow(rowNum).getCell(index).getBooleanCellValue()));
                                        break;
                                    default:
                                        map.put(titleMap.get(index + ""), "");
                                        break;
                                }
                            }
                        }
                    }
                    if (!map.isEmpty()) {
                        listMaps.add(map);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("############ cant find file! ", e);
        } catch (IOException e) {
            LOGGER.error("############ an io error occoured! ", e);
        }
        data = new Gson().toJson(listMaps);
        return data;
    }

}
