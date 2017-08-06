package com.cubedrive.sheet.service.sheet;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Service;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentFileMapper;
import com.cubedrive.base.utils.DateTimeUtil;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.base.utils.Triple;
import com.cubedrive.sheet.SheetTableSequence;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabObj;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;
import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;

@Service
public class SheetDbTableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private DocumentFileMapper documentFileMapper;

    @Autowired
    private SheetTabMapper sheetTabMapper;
    
    public DocumentFile createDocumentByDbTable(String tableName, User author) throws SQLException{
        DocumentFile file = null;
        TableInfo tableInfo = getTableInfo(tableName);
        if(tableInfo != null){
            file = new DocumentFile(author);
            file.setCreateDate(new Date());
            file.setUpdateDate(new Date());
            file.setName(tableName);
            file.setExname(BaseAppConstants.SHEET_DB_TABLE_EXNAME);
            documentFileMapper.insertDocumentFile(file);
            
            String cellTable = SheetTableSequence.instance().sheetCellTable();
            SheetTab ssTab = new SheetTab();
            ssTab.setDocumentFile(file);
            ssTab.setName("Sheet1");
            ssTab.setTabOrder(1);
            ssTab.setActive(true);
            ssTab.setCellTable(cellTable);
            sheetTabMapper.insertSheetTab(ssTab);
            
        }
        return file;
    }
    
    
    public Triple<Integer,List<SheetCell>, TableInfo> loadCells(DocumentFile documentFile, final Integer tabId, final Integer start, final Integer limit) throws SQLException{
        TableInfo tableInfo = getTableInfo(documentFile.getName());
        final String sql = "select * from " + tableInfo.getTableName() + " limit ? offset ? ";
        final List<SheetCell> cellList = new ArrayList<>(limit);
        final List<TableColumnInfo> tableColumns =  tableInfo.getTableColumns();
        int rowIndex = (int)jdbcTemplate.query(sql, new Object[]{limit, start}, new ResultSetExtractor<Object>() {

            private int rowIndex = 0;

            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                while(rs.next()){
                    for(int i=0; i<tableColumns.size(); i++){
                        int row = start + rowIndex + 1 ;
                        TableColumnInfo tableColumnInfo = tableColumns.get(i);
                        Object content = rs.getObject(tableColumnInfo.getIndex());
                        if(content != null){
                            String contentString = content.toString();
                            SheetCell cell = new SheetCell(tabId, row, tableColumnInfo.getIndex(), null);
                            cell.setRawData(contentString);
                            cell.setContent(contentString);
                            cellList.add(cell);
                        }
                    }
                    rowIndex ++;
                }
                return rowIndex;
            }
        });
        
        return new Triple<>(rowIndex,cellList, tableInfo);
    }
    
    public SheetTabObj getSheetTabObj(Integer tabId, TableInfo tableInfo){
        SheetTabObj tabObj = new SheetTabObj(tableInfo.getTableName(), tabId, true);
        final String sql = "select count(*) from " + tableInfo.getTableName();
        int rowSize = jdbcTemplate.queryForInt(sql);
        tabObj.setMaxCol(tableInfo.getTableColumns().size());
        tabObj.setMaxRow(rowSize);
        tabObj.setTotalCellNum(tabObj.getMaxCol() * rowSize);
        return tabObj;
    }
    

    private String getCellContentJson(Object content, String dataType){
        Map<String,Object> contentMap = new HashMap<>();
        switch (dataType){
            case "bool":{
                contentMap.put("data", content.toString());
                contentMap.put("fm", "bool");
                break;
            }
            case "string":
            case "text":{
                contentMap.put("data", SpreadsheetImportHelper.toJavascriptCompatibleString(content.toString()));
                break;
            }
            case "short":
            case "int":
            case "long":{
                contentMap.put("data", content);
                break;
            }
            case "float":
            case "double":{
                contentMap.put("data", content.toString());
                contentMap.put("fm", "number");
                contentMap.put("dfm", null);
                break;
            }
            case "bits":{
                contentMap.put("data", SpreadsheetImportHelper.toJavascriptCompatibleString(new String((byte[])content, BaseAppConstants.utf8)));
                break;
            }
            case "date":{
                contentMap.put("data", DateTimeUtil.formatAsYYYYMMdd((java.sql.Date)content ));
                contentMap.put("fm", "date");
                contentMap.put("dfm", "Y-m-d");
                break;
            }
            case "time":{
                contentMap.put("data", content.toString());
                contentMap.put("fm", "time");
                contentMap.put("dfm", "H:i:s");
                break;
            }
            case "datetime":{
                contentMap.put("data", DateTimeUtil.formatAsYYYYMMddHHmmss(( java.sql.Timestamp) content));
                contentMap.put("fm", "datetime");
                contentMap.put("tfm", "H:i:s");
                contentMap.put("dfm", "Y-m-d H:i:s");
                contentMap.put("dtfm", "Y-m-d H:i:s");
                break;
            }
        }
        return JsonUtil.toJson(contentMap);
    }
    
    
    public TableInfo getTableInfo(String tableName)throws SQLException{
        GetTableInfoCallback callback = new GetTableInfoCallback(tableName);
        try{
            TableInfo tableInfo =(TableInfo)JdbcUtils.extractDatabaseMetaData(jdbcTemplate.getDataSource(), callback);
            return tableInfo;
        }catch (MetaDataAccessException ex ){
            throw new SQLException(ex);
        }
    }

    
   

    static class GetTableInfoCallback implements DatabaseMetaDataCallback {

        private final String tableName;

        GetTableInfoCallback(String tableName){
            this.tableName = tableName;
        }

        @Override
        public TableInfo processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {
            ResultSet rs = dbmd.getColumns(null, null, tableName, null);
            List<TableColumnInfo> tableColumns = new ArrayList<>();
            int index =1;
            while(rs.next()){
                String columnName = rs.getString(4);
                int columnType = rs.getInt(5);
                int columnSize = rs.getInt(7);
                boolean nullable = rs.getInt(11) == ResultSetMetaData.columnNoNulls ? false : true;
                TableColumnInfo columnInfo = new TableColumnInfo(index, columnName, sqlTypeName(columnType), columnSize, nullable);
                tableColumns.add(columnInfo);
                index ++;
            }

            if(tableColumns.isEmpty()){
                return null;
            }else{
                return new TableInfo(tableName, tableColumns);
            }
        }
    }

    public static class TableColumnInfo {
        private Integer index;
        private String columnName;
        private String dataType;
        private int columnSize;
        private boolean nullable;

        public TableColumnInfo(){

        }

        public TableColumnInfo(Integer index,String columnName, String dataType, int columnSize, boolean nullable) {
            this.index = index;
            this.columnName = columnName;
            this.dataType = dataType;
            this.columnSize = columnSize;
            this.nullable = nullable;
        }
        

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public int getColumnSize() {
            return columnSize;
        }

        public void setColumnSize(int columnSize) {
            this.columnSize = columnSize;
        }

        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }

        
        public SheetCell toSheetCellOfRawData(Integer sheetId){
            SheetCell cell = new SheetCell(sheetId, 0, index, null);
            cell.setRawData(columnName);
            return cell;
        }
        
        public SheetCell toSheetCellOfContent(Integer sheetId){
            SheetCell cell = new SheetCell(sheetId, 0, index, null);
            Map<String,Object> jsonMap = new HashMap<>();
            jsonMap.put("data", columnName);
            Integer width =columnSize;
            Integer dt = null;
            String fm = null;
            String dfm = null;
            switch(dataType){
                case "short":
                case "int":
                case "long":
                case "float":
                case "double": dt = 0; break;
                case "string":
                case "bits":  dt = 1; break;
                case "text":  dt = 1; width = 1024;break;
                case "date":  dt = 2;  fm="date"; dfm="Y-m-d" ;break;
                default: break;
            }
            jsonMap.put("width", width);
            if(dt != null){
                jsonMap.put("dcfg", "{dt:"+dt+"}");
            }
            if(fm != null && dfm != null){
                jsonMap.put("fm", fm);
                jsonMap.put("dfm", dfm);
            }
            String json = JsonUtil.toJson(jsonMap);
            cell.setContent(json);
            return cell;
        }
        
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TableColumnInfo)) return false;
            TableColumnInfo that = (TableColumnInfo) o;
            return Objects.equals(columnName, that.columnName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(columnName);
        }
        
        
        
    }

    public static class TableInfo {
        private String tableName;
        private List<TableColumnInfo> tableColumns = new ArrayList<>();

        public TableInfo(){
        }

        public TableInfo(String tableName, List<TableColumnInfo> tableColumns) {
            this.tableName = tableName;
            this.tableColumns = tableColumns;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public List<TableColumnInfo> getTableColumns() {
            return tableColumns;
        }

        public void setTableColumns(List<TableColumnInfo> tableColumns) {
            this.tableColumns = tableColumns;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TableInfo)) return false;
            TableInfo tableInfo = (TableInfo) o;
            return Objects.equals(tableName, tableInfo.tableName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tableName);
        }
    }

    static String sqlTypeName(int sqlType){
        String dataType;
        switch (sqlType){
            case Types.BIT:
            case Types.BOOLEAN:
                dataType = "bool"; break;
            case Types.TINYINT:
            case Types.SMALLINT:
                dataType ="short"; break;
            case Types.INTEGER:
                dataType ="int"; break;
            case Types.BIGINT:
                dataType = "long";break;
            case Types.REAL:
            case Types.FLOAT:
                dataType = "float";break;
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                dataType = "double";break;
            case Types.VARCHAR:
            case Types.CHAR:
                dataType = "string";break;
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.CLOB:
            case Types.NCLOB: 
                dataType = "text";break;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
            case Types.JAVA_OBJECT:
                dataType = "bits"; break;
            case Types.DATE:
                dataType = "date"; break;
            case Types.TIME:
                dataType = "time"; break;
            case Types.TIMESTAMP:
                dataType = "datetime";break;
            default:
                dataType = "unhandled";break;

        }
        return dataType;
    }
}
