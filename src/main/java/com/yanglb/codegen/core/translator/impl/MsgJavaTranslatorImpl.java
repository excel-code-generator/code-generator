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
package com.yanglb.codegen.core.translator.impl;

import java.util.Map;

import com.yanglb.codegen.core.model.TableModel;
import com.yanglb.codegen.core.translator.BaseMsgTranslator;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.StringUtil;

public class MsgJavaTranslatorImpl extends BaseMsgTranslator {
	@Override
	protected String getSplitString() {
		return "_";
	}
	
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		
		this.writableModel.setExtension("properties");
		this.writableModel.setFilePath("msg/properties");
	}

	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		StringBuilder sb = this.writableModel.getData();
		
		int cnt = 0;
		for(TableModel tblModel : this.model) {
			sb.append(String.format("%s# %s\r\n", (cnt++ == 0)?"":"\r\n", tblModel.getSheetName()));
			for(Map<String, String> itm : tblModel.toList()) {
				String id = itm.get("id");
				if(StringUtil.isNullOrEmpty(id)) continue;
				// TODO: 对字符串进行编码转换
				String value = itm.get(this.msgLang);
				sb.append(String.format("%s=%s\r\n", id, value));
			}
		}
	}
}
