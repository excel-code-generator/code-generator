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
package com.yanglb.codegen.core.generator.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yanglb.codegen.core.GenFactory;
import com.yanglb.codegen.core.generator.BaseGenerator;
import com.yanglb.codegen.core.model.TableModel;
import com.yanglb.codegen.core.model.WritableModel;
import com.yanglb.codegen.core.reader.ISettingReader;
import com.yanglb.codegen.core.reader.ITableReader;
import com.yanglb.codegen.core.translator.ITranslator;
import com.yanglb.codegen.core.writer.IWriter;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.support.SupportGen;
import com.yanglb.codegen.support.SupportLang;
import com.yanglb.codegen.utils.MsgUtility;

public class MsgGeneratorImpl extends BaseGenerator {
	@Override
	protected void onGeneration() throws CodeGenException {
		super.onGeneration();
		
		// 读取必要的配置数据
		ISettingReader settingReader = GenFactory.createByName(SupportGen.setting_reader);
		// TODO: paramaModel
		String genKey = null; //String.format("%s_%s", paramaModel.getType().name(), paramaModel.getLang().name());
		HashMap<String, String> map = settingReader.settingReader(genKey);
		settingMap.putAll(map);
		
		// 读取DB信息表
		ITableReader tableReader = GenFactory.createByName(SupportGen.table_reader);
		tableReader.setStartPoint(3, 2);
		// TODO: paramaModel
		List<TableModel> list = null;// tableReader.reader(this.paramaModel.getIn(), this.paramaModel.getSheets());
		if(list.size() == 0) {
			throw new CodeGenException(MsgUtility.getString("E_003"));
		}
		
		// 获取语言（每种语言翻译一次）
		List<String> langList = new ArrayList<String>();
		TableModel tableModel = list.get(0);
		for(String key : tableModel.getColumns()) {
			if(!"id".equals(key)) {
				langList.add(key);
				
				settingMap.put("MsgLang", key);
				SupportGen supportTrans = SupportGen.msg_js_translator;
				SupportGen supportWriter = SupportGen.utf8_writer;
				// TODO: paramaModel
//				if(this.paramaModel.getLang() == SupportLang.java) {
//					supportTrans = SupportGen.msg_java_translator;
//				} else if (this.paramaModel.getLang() == SupportLang.json) {
//					supportTrans = SupportGen.msg_json_translator;
//				} else if (this.paramaModel.getLang() == SupportLang.cs) {
//					supportTrans = SupportGen.msg_cs_translator;
//				} else if (this.paramaModel.getLang() == SupportLang.ios) {
//					supportTrans = SupportGen.msg_ios_translator;
//				} else if (this.paramaModel.getLang() == SupportLang.android) {
//					supportTrans = SupportGen.msg_android_translator;
//				}
				
				// 转换为可写入的Model（单个文件）
				ITranslator<List<TableModel>> translator = GenFactory.createByName(supportTrans);
				WritableModel writableModel = translator.translate(settingMap, this.paramaModel, list);
				
				// 写入到文件中
				IWriter writer = GenFactory.createByName(supportWriter);
				writer.writer(writableModel);
			}
		}
	}

}
