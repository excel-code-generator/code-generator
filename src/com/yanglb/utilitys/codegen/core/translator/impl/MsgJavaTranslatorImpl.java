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
package com.yanglb.utilitys.codegen.core.translator.impl;

import java.util.List;
import java.util.Map;

import com.yanglb.utilitys.codegen.core.model.TableModel;
import com.yanglb.utilitys.codegen.core.translator.BaseTranslator;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.utility.StringUtility;

public class MsgJavaTranslatorImpl extends BaseTranslator<List<TableModel>> {
	protected String msgLang = "";

	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		
		// 当前生成的国际化语言
		this.msgLang = this.settingMap.get("MsgLang");
		
		this.writableModel.setExtension("properties");
		this.writableModel.setFileName("message");
		if(!this.msgLang.equals("default")) {
			this.writableModel.setFileName("message_"+this.msgLang);
		}
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
				if(StringUtility.isNullOrEmpty(id)) continue;
				// TODO: 对字符串进行编码转换
				String value = itm.get(this.msgLang);
				sb.append(String.format("%s=%s\r\n", id, value));
			}
		}
	}

	@Override
	protected void onAfterTranslate() throws CodeGenException {
//		super.onAfterTranslate();
		// 不需要替换标记
		
		// TODO: 调整格式（\u1234）
	}
}
