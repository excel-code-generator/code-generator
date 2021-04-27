/**
 * Copyright 2015-2021 yanglb.com
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

import com.yanglb.codegen.core.reader.BaseReader;
import com.yanglb.codegen.utils.StringUtil;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;



/**
 * HashMapReaderImpl 及其子类读取的数据不支持写入
 * @author yanglibing
 *
 */
public class HashMapReaderImpl extends BaseReader<HashMap<String, String>> {
	 
	/**
	 * 读取Sheet内容
	 */
	@Override
	protected HashMap<String, String> onReader(XSSFSheet sheet) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		for(int row=this.startRowNo; row <= sheet.getLastRowNum(); row++) {
			XSSFRow xssfRow = sheet.getRow(row);
			String key = this.getCellStringValue(xssfRow.getCell(this.startColNo));
			// key不为空时添加到Map中
			if(xssfRow.getCell(this.startColNo).getCellType() == XSSFCell.CELL_TYPE_BLANK
					|| StringUtil.isNullOrEmpty(key)) {
				continue;
			}
			String value = this.getCellStringValue(xssfRow.getCell(this.startColNo+1));			
			result.put(key, value);
		}
		return result;
	}
	
	/**
	 * 合并
	 * @param listMap
	 * @return
	 */
	public HashMap<String, String> mergeResult(List<HashMap<String, String>> listMap) {
		HashMap<String, String> result = new HashMap<String, String>();
		for(HashMap<String, String> itm : listMap) {
			result.putAll(itm);
		}
		return result;
	}
}
