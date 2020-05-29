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

import java.util.List;

import com.yanglb.codegen.core.GenFactory;
import com.yanglb.codegen.core.generator.BaseGenerator;
import com.yanglb.codegen.core.model.DdlModel;
import com.yanglb.codegen.core.model.WritableModel;
import com.yanglb.codegen.core.reader.IReader;
import com.yanglb.codegen.core.translator.ITranslator;
import com.yanglb.codegen.core.writer.IWriter;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.support.SupportGen;
import com.yanglb.codegen.utils.Conf;
import com.yanglb.codegen.utils.Resources;

public class DmlGeneratorImpl extends BaseGenerator {

	@Override
	protected void onGeneration() throws CodeGenException {
		super.onGeneration();

		// 读取DML信息表
		IReader<DdlModel> ddlReader = GenFactory.createByName(paramaModel.getCmdModel().getReader());
		List<DdlModel> list = ddlReader.reader(this.paramaModel.getFile(), this.paramaModel.getSheets());
		if(list.size() == 0) {
			throw new CodeGenException(Resources.getString("E_003"));
		}

		// 转换为可写入的Model（单个文件）
		ITranslator<List<DdlModel>> translator = GenFactory.createByName(paramaModel.getCmdModel().getTranslator());
		WritableModel writableModel = translator.translate(settingMap, this.paramaModel, list);

		// 默认使用UTF-8编码
		SupportGen.Writer supportWriter = SupportGen.Writer.utf8;
		if (writableModel.getEncode() == "ascii") supportWriter = SupportGen.Writer.ascii;

		// 写入到文件中
		IWriter writer = GenFactory.createByName(Conf.getString(Conf.CATEGORY_WRITER, supportWriter.name()));
		writer.writer(writableModel);
	}
}
