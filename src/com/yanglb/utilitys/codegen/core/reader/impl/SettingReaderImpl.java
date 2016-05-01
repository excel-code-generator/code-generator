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

import com.yanglb.utilitys.codegen.core.reader.ISettingReader;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;

public class SettingReaderImpl extends HashMapReaderImpl implements ISettingReader {
	private static final String COMMON_SHEET = "common"; 
	
	public SettingReaderImpl() {
		this.excelFile = "jar:/conf/CodeGenerator.xlsx";
		this.startRowNo = 3;
		this.startColNo = 2;
	}
	
	/**
	 * 读取所有配置项目
	 * @return
	 * @throws CodeGenException
	 */
	public HashMap<String, String> settingReader() throws CodeGenException {
		return this.settingReader(new String[0]);
	}
	
	/**
	 * 读取一个配置项目（会另外添加common Sheet的内容）
	 * @param settingSheet
	 * @return
	 * @throws CodeGenException
	 */
	public HashMap<String, String> settingReader(String settingSheet) throws CodeGenException {
		return this.settingReader(new String[]{settingSheet});	
	}
	
	/**
	 * 读取多个配置项目（会另外添加common Sheet的内容）
	 * @param settingSheets
	 * @return
	 * @throws CodeGenException
	 */
	public HashMap<String, String> settingReader(String[] settingSheets) throws CodeGenException {
		boolean hasCommon = false;
		List<String> list = new ArrayList<String>();
		for(String itm:settingSheets) {
			list.add(itm);
			if(SettingReaderImpl.COMMON_SHEET.equals(itm)) {
				hasCommon = true;
			}
		}
		if(!hasCommon) {
			list.add(SettingReaderImpl.COMMON_SHEET);
		}
		settingSheets = list.toArray(new String[0]);
		this.reader(this.excelFile, settingSheets);
		
		// 返回合并后的结果
		return this.mergeResult(this.results);
	}
}
