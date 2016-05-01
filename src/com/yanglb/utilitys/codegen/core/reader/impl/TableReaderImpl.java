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
package com.yanglb.utilitys.codegen.core.reader.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.yanglb.utilitys.codegen.core.model.TableModel;
import com.yanglb.utilitys.codegen.core.reader.BaseModelReader;
import com.yanglb.utilitys.codegen.core.reader.ITableReader;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.utility.StringUtility;

public class TableReaderImpl extends BaseModelReader<TableModel> implements ITableReader {
	
	/**
	 * 读取DB定义Sheet
	 * @throws CodeGenException
	 */
	@Override
	protected TableModel onReader(XSSFSheet sheet) throws CodeGenException {
		TableModel model = super.onReader(sheet, TableModel.class);
		
		// 记录列的KEY
		String[] keys = null;
		List<String> listKey = new ArrayList<String>();
		Map<String, String> curValue = null; // 当前行的值
		
		for(int rowNo=this.startRowNo; rowNo<=sheet.getLastRowNum(); rowNo++) {
			XSSFRow row = sheet.getRow(rowNo);
			// 第一行为KEY
			if(rowNo == this.startRowNo) {
				keys = new String[row.getLastCellNum()];
			} else {
				curValue = new HashMap<String, String>();
			}
			
			// 如果当前行 所有列均为空白，则不添加到结果集中。
			boolean allBlank = true;
			
			// 开始读取
			if(row == null) {
				throw new CodeGenException(String.format("error: sheet(%s), row(%d)", 
						sheet.getSheetName(), rowNo));
			}
			
			/*
			int colNo = 0;
			for(Iterator<Cell> iter = row.cellIterator(); iter.hasNext();) {
				XSSFCell cell = (XSSFCell) iter.next();
				colNo = cell.getColumnIndex();
			/*	
			}
			*/
			
			for(int colNo=this.startColNo; colNo<row.getLastCellNum(); colNo++) {
				XSSFCell cell = row.getCell(colNo);
				if(cell == null) {
					throw new CodeGenException(String.format("error: sheet(%s), row(%d), col(%d)", 
							sheet.getSheetName(), rowNo, colNo));
				}
				if(cell.getCellType() != XSSFCell.CELL_TYPE_BLANK
						&& cell.getCellType() != XSSFCell.CELL_TYPE_ERROR) {
					allBlank = false;
				}
				String value = this.getCellStringValue(cell);

				// 第一行为KEY
				if(rowNo == this.startRowNo) {
					if(StringUtility.isNullOrEmpty(value) 
							|| cell.getCellType() ==XSSFCell.CELL_TYPE_BLANK
							|| "-".equals(value)) {
						// 空白/忽略的列无视
						continue;
					}
					keys[colNo] = value;
					listKey.add(value);
				} else {
					if(colNo >= keys.length) continue;
					
					// 添加值
					String key = keys[colNo];
					if(!StringUtility.isNullOrEmpty(key)) {
						curValue.put(keys[colNo], value);
					}
				}
			}
			
			// 全部是空白列时不添加到结果集中
			if(!allBlank && curValue != null) {
				model.insert(curValue);
			}
		}
		
		// 设置列
		model.setColumns((String[]) listKey.toArray(new String[0]));
		return model;
	}
	
	/**
	 * 根据已知的某个Sheet进行读取
	 * @param sheet
	 * @return
	 * @throws CodeGenException 
	 */
	public TableModel reader(XSSFSheet sheet) throws CodeGenException {
		return this.onReader(sheet);
	}
}
