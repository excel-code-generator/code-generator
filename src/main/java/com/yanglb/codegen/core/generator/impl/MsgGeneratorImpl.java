/**
 * Copyright 2015-2023 yanglb.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanglb.codegen.core.generator.impl;


import com.yanglb.codegen.core.GenFactory;
import com.yanglb.codegen.core.generator.BaseGenerator;
import com.yanglb.codegen.core.reader.ITableReader;
import com.yanglb.codegen.core.translator.ITranslator;
import com.yanglb.codegen.core.writer.IWriter;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.model.TableModel;
import com.yanglb.codegen.model.WritableModel;
import com.yanglb.codegen.utils.Conf;
import com.yanglb.codegen.utils.GenTypes;
import com.yanglb.codegen.utils.Resources;

import java.util.Arrays;
import java.util.List;


public class MsgGeneratorImpl extends BaseGenerator {
    @Override
    protected void onGeneration() throws CodeGenException {
        super.onGeneration();

        // 读取DB信息表
        ITableReader tableReader = GenFactory.createByName(parameterModel.getCmdModel().getReader());
        tableReader.setStartPoint(3, 2);
        List<TableModel> list = tableReader.reader(this.parameterModel.getFile(), this.parameterModel.getSheets());
        if (list.isEmpty()) {
            throw new CodeGenException(Resources.getString("E_003"));
        }

        // 获取语言（每种语言翻译一次）
        TableModel tableModel = list.get(0);
        for (String key : tableModel.getColumns()) {
            if (needOutput(key)) {
                settingMap.put("MsgLang", key);
                String trans = parameterModel.getCmd();

                // 转换为可写入的Model（单个文件）
                ITranslator<List<TableModel>> translator = GenFactory.createByName(parameterModel.getCmdModel().getTranslator());
                List<WritableModel> writableModels = translator.translate(settingMap, parameterModel, list);

                for (WritableModel writableModel : writableModels) {
                    // 默认使用UTF-8编码
                    GenTypes.Writer supportWriter = GenTypes.Writer.utf8;
                    if ("ascii".equals(writableModel.getEncode())) supportWriter = GenTypes.Writer.ascii;

                    // 写入到文件中
                    IWriter writer = GenFactory.createByName(Conf.getString(Conf.CATEGORY_WRITER, supportWriter.name()));
                    writer.writer(writableModel);
                }
            }
        }
    }

    private boolean needOutput(String key) {
        if ("id".equals(key)) return false;
        if ("default".equals(key)) return true;
        String[] lang = parameterModel.getOptions().getOptionValues("lang");
        if (lang == null || lang.length == 0) return true;
        return Arrays.asList(lang).contains(key);
    }
}
