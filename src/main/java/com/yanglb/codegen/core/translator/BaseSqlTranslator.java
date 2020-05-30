/**
 * Copyright 2015-2020 yanglb.com
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
package com.yanglb.codegen.core.translator;

import java.util.List;

import com.yanglb.codegen.exceptions.CodeGenException;

public class BaseSqlTranslator<T> extends BaseTranslator<List<T>> {
	// 列引号字符，如：SQL Server为[], MySQL为`等
	protected String sqlColumnStart = "\"";
	protected String sqlColumnEnd = "\"";
	
    /**
     * 取得用于替换的Model
     */
    private Object getReplaceModel() {
        if(this.model == null || this.model.size() <= 0) return null;
        
        // 只使用第一个Sheet替换
        return this.model.get(0);
    }

	@Override
	protected void onAfterTranslate() throws CodeGenException {
		super.onAfterTranslate();
		
		// 替换Flag
		String data = this.writableModel.getData().toString();
		data = this.replaceFlags(data, getReplaceModel());
		this.writableModel.setData(new StringBuilder(data));
	}
}
