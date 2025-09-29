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
package com.yanglb.codegen.core.translator;

import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.model.ParameterModel;
import com.yanglb.codegen.model.WritableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BaseTranslator<T> implements ITranslator<T> {
    protected T model;
    protected final List<WritableModel> writableModel;
    protected HashMap<String, String> settingMap;
    protected ParameterModel parameterModel;

    protected BaseTranslator() {
        this.writableModel = new ArrayList<>();
    }

    /**
     * 文件名，优先使用--fn参数指定的文件名，如不指定使用excel名称
     *
     * @return 文件名
     */
    protected String getFileName() {
        String fileName = this.parameterModel.getFileName();
        if (fileName != null) {
            fileName.replaceAll("\"", "");
        } else {
            fileName = this.parameterModel.getFile();
            File file = new File(fileName);
            fileName = file.getName();

            int index = fileName.lastIndexOf(".");
            if (index != -1) {
                fileName = fileName.substring(0, index);
            }
        }

        return fileName;
    }

    /**
     * 进行翻译处理
     *
     * @param settingMap     配置信息
     * @param model          等待翻译的Model
     * @param parameterModel 参数/选项模型
     * @return WritableModel 一个可写的Model
     * @throws CodeGenException 翻译出错时抛出此异常
     */
    @Override
    public List<WritableModel> translate(HashMap<String, String> settingMap, ParameterModel parameterModel, T model)
            throws CodeGenException {
        this.settingMap = settingMap;
        this.model = model;
        this.parameterModel = parameterModel;

        this.doTranslate();
        return this.writableModel;
    }

    /**
     * 预翻译器（子类如果重写，则必须显示调用超类的此方法）
     *
     * @throws CodeGenException 错误信息
     */
    protected void onBeforeTranslate() throws CodeGenException {
        // 设置文件名、等初始化操作
        WritableModel model = new WritableModel();
        model.setEncode("utf-8");
        model.setFileName(getFileName());
        model.setData(new StringBuilder());
        model.setOutputDir(this.parameterModel.getOptDir());
        this.writableModel.add(model);
    }

    /**
     * 进行转换操作
     *
     * @throws CodeGenException 错误信息
     */
    protected void onTranslate(WritableModel writableModel) throws CodeGenException {
        // 父类没什么可做的了
    }

    /**
     * 编码转换等操作
     *
     * @throws CodeGenException 错误信息
     */
    protected void onAfterTranslate(WritableModel writableModel) throws CodeGenException {
        // 编码转换在写入器完成
    }

    // 翻译操作
    private void doTranslate() throws CodeGenException {
        this.onBeforeTranslate();
        for (WritableModel writableModel : writableModel) {
            this.onTranslate(writableModel);
            this.onAfterTranslate(writableModel);
        }
    }
}
