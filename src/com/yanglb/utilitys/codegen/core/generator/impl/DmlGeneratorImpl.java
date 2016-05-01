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
package com.yanglb.utilitys.codegen.core.generator.impl;

import java.util.HashMap;
import java.util.List;

import com.yanglb.utilitys.codegen.core.GenFactory;
import com.yanglb.utilitys.codegen.core.generator.BaseGenerator;
import com.yanglb.utilitys.codegen.core.model.DmlModel;
import com.yanglb.utilitys.codegen.core.model.WritableModel;
import com.yanglb.utilitys.codegen.core.reader.IReader;
import com.yanglb.utilitys.codegen.core.reader.ISettingReader;
import com.yanglb.utilitys.codegen.core.translator.ITranslator;
import com.yanglb.utilitys.codegen.core.writer.IWriter;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.support.SupportGen;
import com.yanglb.utilitys.codegen.utility.MsgUtility;

public class DmlGeneratorImpl extends BaseGenerator {

	@Override
	protected void onGeneration() throws CodeGenException {
		super.onGeneration();

		// 读取必要的配置数据
		ISettingReader settingReader = GenFactory.createByName(SupportGen.setting_reader);
		HashMap<String, String> settingMap = settingReader.settingReader("dml");
		
		// 读取DML信息表
		IReader<DmlModel> reader = GenFactory.createByName(SupportGen.dml_reader);
		List<DmlModel> list = reader.reader(this.paramaModel.getIn(), this.paramaModel.getSheets());
		if(list.size() == 0) {
			throw new CodeGenException(MsgUtility.getString("E_003"));
		}
		
		// 转换为可写入的Model（单个文件）
		ITranslator<List<DmlModel>> translator = GenFactory.createByName(SupportGen.dml_translator);
		WritableModel writableModel = translator.translate(settingMap, this.paramaModel, list);
		
		// 写入到文件中
		IWriter writer = GenFactory.createByName(SupportGen.utf8_writer);
		writer.writer(writableModel);
	}
}
