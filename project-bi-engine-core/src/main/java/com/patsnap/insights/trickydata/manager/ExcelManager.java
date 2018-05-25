package com.patsnap.insights.trickydata.manager;

import com.patsnap.insights.ContextHolder;
import com.patsnap.insights.trickydata.dao.RedshiftDao;
import com.patsnap.insights.trickydata.entity.DataSourceEntity;
import com.patsnap.insights.trickydata.entity.DataTableEntity;

import com.amazonaws.util.json.Jackson;
import com.google.common.net.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExcelManager extends BaseManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelManager.class);

    @Autowired
    S3Manager s3Manager;

    @Autowired
    RedshiftDao redshiftDao;

    @Autowired
    DataTableManager tableManager;

    @Autowired
    DataSourceManager dataSourceManager;

    public String generalJsonFile(String fileName, InputStream inputStream) {
        List<LinkedHashMap<String, String>> result = readExcelFile(fileName, inputStream);
        List<String> strList = result.stream().map(e -> e.put("id", UUID.randomUUID().toString())).map(e -> Jackson.toJsonString(e)).collect(Collectors.toList());
        String data = StringUtils.join(strList, "");

        String trimFileName = FilenameUtils.removeExtension(fileName);
        String s3Key = s3Manager.putObject(trimFileName, MediaType.JSON_UTF_8, data.getBytes());
        Set<String> keySet = result.get(0).keySet();
        keySet.remove("id");
        String schema = StringUtils.join(keySet, " VARCHAR(100) NOT NULL, ");
        schema = "CREATE TABLE \"" + s3Key + "\" (id VARCHAR(100) NOT NULL primary key," + schema + " VARCHAR(100) NOT NULL);";
        redshiftDao.createTable(schema);
        redshiftDao.copy(s3Key, s3Key);

        DataSourceEntity dataSourceEntity = new DataSourceEntity();
        dataSourceEntity.setName(s3Key);
        dataSourceEntity.setUserId(ContextHolder.USER_ID);
        dataSourceEntity = dataSourceManager.saveDataSource(dataSourceEntity);

        DataTableEntity dataTableEntity = new DataTableEntity();
        dataTableEntity.setDatasourceId(dataSourceEntity.getId());
        // todo check table name
        dataTableEntity.setName(s3Key);
        tableManager.saveDataTable(dataTableEntity);

        return s3Key;
    }

    /**
     * get json data from excel file
     *
     * @return
     */
    private List<LinkedHashMap<String, String>> readExcelFile(String contentType, InputStream inputStream) {
        String data = "";
        List<LinkedHashMap<String, String>> listMaps = new ArrayList<>();
        try {
            Workbook workbook = null;
            if (contentType.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (contentType.toLowerCase().endsWith("xlsx")) {
                //2007
                workbook = new XSSFWorkbook(inputStream);
            }

            if (workbook != null) {
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
                for (int rowNum = firstLine; rowNum <= lastLine; rowNum++) {
                    map = new LinkedHashMap<>();
                    for (int index = firstCellNum; index <= lastCellNum; index++) {
                        if (rowNum == firstLine) {
                            if (sheet.getRow(rowNum).getCell(index) != null) {
                                titleMap.put(index + "", sheet.getRow(rowNum).getCell(index).getStringCellValue());
                            }
                        } else {
                            if (sheet.getRow(rowNum).getCell(index) != null) {
                                switch (sheet.getRow(rowNum).getCell(index).getCellType()) {
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
        return listMaps;

    }

}
