/**
 * Copyright 2015 yanglb.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanglb.codegen.core.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.exceptions.UnImplementException;
import com.yanglb.codegen.utils.DataFormatType;
import com.yanglb.codegen.utils.Resources;

/**
 * 读取类
 * @author yanglibing
 */
public class BaseReader<T> implements IReader<T>{
	protected String excelFile;
	protected String[] sheets;
	protected List<T> results;
	protected int startRowNo;
	protected int startColNo;
	
	protected BaseReader() {
		this.results = new ArrayList<T>();
		this.startRowNo = 0;
		this.startColNo = 0;
	}
	
	/**
	 * 读取Excel中全部可读取Sheet
	 * @param excelFile
	 * @return
	 * @throws CodeGenException 
	 */
	public List<T> reader(String excelFile) throws CodeGenException {
		return this.reader(excelFile, new String[0]);
	}
	
	/**
	 * 读取Excel中指定的几个Sheet
	 * @param excelFile
	 * @param sheets
	 * @return
	 * @throws CodeGenException 
	 */
	public List<T> reader(String excelFile, String[] sheets) throws CodeGenException {
		// 保存传入参数
		this.excelFile = excelFile;
		this.sheets = sheets;
		
		// 进行读取
		this.doReader();
		
		// 返回结果
		return this.results;
	}
	
	/**
	 * 读取Excel中指定的一个Sheet
	 * @param excelFile
	 * @param sheet
	 * @return
	 * @throws CodeGenException 
	 */
	public T reader(String excelFile, String sheet) throws CodeGenException {
		this.reader(excelFile, new String[]{sheet});
		return this.results.size() == 0 ? null : this.results.get(0);
	}
	
	/**
	 * 子类必须实现
	 * @param sheet
	 * @return
	 * @throws UnImplementException 
	 * @throws CodeGenException 
	 */
	protected T onReader(XSSFSheet sheet) throws UnImplementException, CodeGenException {
		throw new UnImplementException();
	}
	
	/**
	 * 进行读取
	 * @throws CodeGenException 
	 */
	private void doReader() throws CodeGenException {
		// 打开文件
		XSSFWorkbook wb = null;
		try {
			// jar中读取时不能使用new File方法
			if(this.excelFile.startsWith("jar://")) {
				String path = this.excelFile.substring(5);
				InputStream is=this.getClass().getResourceAsStream(path);
				wb = new XSSFWorkbook(is);
			} else {
				File file = new File(this.excelFile);
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
				wb = new XSSFWorkbook(in);
			}
			
			// 更新公式
			HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);

			// 逐个读取
			if(this.sheets == null || this.sheets.length == 0) {
				// 全部读取
				for(int i=0; i<wb.getNumberOfSheets(); i++) {
					XSSFSheet sheet = wb.getSheetAt(i);
					
					// 过虑不可读取的Sheet
					if(!this.isReadable(sheet.getSheetName())) {
						continue;
					}
					this.results.add(this.onReader(sheet));
				}
			} else {
				// 读取指定的Sheet
				for(String sheetName:this.sheets) {
					XSSFSheet sheet = wb.getSheet(sheetName);
					if(sheet != null) {
						this.results.add(this.onReader(sheet));
					}
				}
			}
		} catch (FileNotFoundException e) {
			// 该异常已经检查过，不可能还在些出现
			throw new CodeGenException(e.getMessage());
		} catch (UnImplementException e) {
			this.results.clear();
			e.printStackTrace();
		} catch (IOException e) {
			throw new CodeGenException(Resources.getString("E_005"));
		} finally {
			try {
				if(wb != null) wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 检查特定的Sheet名是否可读
	 * @param sheetName
	 * @return
	 */
	private boolean isReadable(String sheetName) {
		if(sheetName.equals("说明") || sheetName.startsWith("#")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 取得Cell的字符串数据，当不是字符串时进行转换
	 * @param cell
	 * @return
	 */
	public String getCellStringValue(XSSFCell cell) {
		String result = null;
    	int type = cell.getCellType();
    	if(type == Cell.CELL_TYPE_FORMULA) type = cell.getCachedFormulaResultType();
    	if(type == Cell.CELL_TYPE_BLANK) return null;
    	if(type == Cell.CELL_TYPE_ERROR) {
    		return "#VALUE!";
    	}
    	
    	switch(type) {
    	case Cell.CELL_TYPE_BOOLEAN:
    		result = String.valueOf(cell.getBooleanCellValue());
    		break;
    		
    	case Cell.CELL_TYPE_STRING:
    		result = cell.getStringCellValue();
    		break;
    		
    	case Cell.CELL_TYPE_NUMERIC:
    	{
    		if(cell.getCellStyle().getDataFormat() == DataFormatType.FORMAT_DATE) {
    			Date date = cell.getDateCellValue();
    			String format = "yyyy/MM/dd";//cell.getCellStyle().getDataFormatString();
    			if(cell.getCellStyle().getDataFormatString().contains(":")) {
    				// 包括时间
    				format = "yyyy/MM/dd HH:mm:ss";
    			}
    			SimpleDateFormat df = null;
    			try{
    				df = new SimpleDateFormat(format);
    			} catch(IllegalArgumentException e) {
    				//df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    			}
    			result = df.format(date);
    		} else {
    			// 如果小数点后有 0则删除
    			Number number = cell.getNumericCellValue();
    			result = number.toString();
    			if(result.indexOf('.') != -1) {
    				result = result.replaceAll("[0]*$", "");
    			}
    			if(result.endsWith(".")) {
    				result = result.substring(0, result.length()-1);
    			}
    		}
    	}
    		break;
    	}
    	return result;
	}
	
	///////////////////////////////////////////////
	//  属性
	///////////////////////////////////////////////
	/**
	 * @return the excelFile
	 */
	protected String getExcelFile() {
		return excelFile;
	}

	/**
	 * @param excelFile the excelFile to set
	 */
	protected void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}

	/**
	 * @return the sheets
	 */
	protected String[] getSheets() {
		return sheets;
	}

	/**
	 * @param sheets the sheets to set
	 */
	protected void setSheets(String[] sheets) {
		this.sheets = sheets;
	}

	/**
	 * @return the results
	 */
	protected List<T> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	protected void setResults(List<T> results) {
		this.results = results;
	}

	/**
	 * @return the startRowNo
	 */
	protected int getStartRowNo() {
		return startRowNo;
	}

	/**
	 * @param startRowNo the startRowNo to set
	 */
	protected void setStartRowNo(int startRowNo) {
		this.startRowNo = startRowNo;
	}

	/**
	 * @return the startColNo
	 */
	protected int getStartColNo() {
		return startColNo;
	}

	/**
	 * @param startColNo the startColNo to set
	 */
	protected void setStartColNo(int startColNo) {
		this.startColNo = startColNo;
	}
	
	/**
	 * 设置开始位置
	 * @param startRowNo 开始行
	 * @param startColNo 开始列
	 */
	public void setStartPoint(int startRowNo, int startColNo) {
		this.startRowNo = startRowNo;
		this.startColNo = startColNo;
	}
}
