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
package com.yanglb.codegen.core.reader.impl;

import com.yanglb.codegen.utils.Conf;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.yanglb.codegen.core.GenFactory;
import com.yanglb.codegen.core.model.DmlModel;
import com.yanglb.codegen.core.model.TableModel;
import com.yanglb.codegen.core.reader.BaseModelReader;
import com.yanglb.codegen.core.reader.ITableReader;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.support.SupportGen;

public class DmlReaderImpl extends BaseModelReader<DmlModel> {
	/**
	 * 读取DB定义Sheet
	 * @throws CodeGenException 
	 */
	@Override
	protected DmlModel onReader(XSSFSheet sheet) throws CodeGenException {
		DmlModel model = super.onReader(sheet, DmlModel.class);
		XSSFRow row = null;
		
		// 读取基本信息
		row = sheet.getRow(1);
		model.setNameSpace(this.getCellStringValue(row.getCell(2)));
		model.setAuthor(this.getCellStringValue(row.getCell(4)));
		model.setVersion(this.getCellStringValue(row.getCell(6)));
		model.setDescription(this.getCellStringValue(row.getCell(8)));
		
		row = sheet.getRow(2);
		model.setName(this.getCellStringValue(row.getCell(2)));
		model.setResponsibility(this.getCellStringValue(row.getCell(4)));
		model.setRenewDate(this.getCellStringValue(row.getCell(6)));
		
		// 列详细信息
		model.setDmlData(this.readerTable(sheet));
		return model;
	}
	
	/**
	 * 读取Table
	 * @param sheet
	 * @return
	 * @throws CodeGenException 
	 */
	private TableModel readerTable(XSSFSheet sheet) throws CodeGenException {
		// 通过 TableReader读取表格内容
		ITableReader tableReader = GenFactory.createByName(Conf.CATEGORY_READER, "table");
		tableReader.setStartPoint(7, 2);
		return tableReader.reader(sheet);
	}
}
