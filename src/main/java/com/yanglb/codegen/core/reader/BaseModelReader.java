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

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.yanglb.codegen.core.model.BaseModel;
import com.yanglb.codegen.exceptions.CodeGenException;

/**
 * 如果需要写入操作(IWriter) 请使用此类的派生类
 * @author yanglibing
 *
 * @param <T>
 */
public class BaseModelReader<T> extends BaseReader<T> {
	protected BaseModelReader() {
		
	}
	
	protected T onReader(XSSFSheet sheet, Class<T> cls) throws CodeGenException {
		T model = null;
		try {
			model = cls.newInstance();
			((BaseModel)model).setSheetName(sheet.getSheetName());
			((BaseModel)model).setExcelFileName(this.excelFile);
		} catch (Exception e) {
			throw new CodeGenException(e.getMessage());
		}
		return model;
	}
}
