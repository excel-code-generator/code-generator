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

import java.util.HashMap;
import java.util.List;

public interface ITranslator<T> {
    /**
     * 进行翻译处理
     *
     * @param settingMap     配置信息
     * @param model          等待翻译的Model
     * @param parameterModel 参数/选项模型
     * @return WritableModel 一个可写的Model
     * @throws CodeGenException 翻译出错时抛出此异常
     */
    List<WritableModel> translate(HashMap<String, String> settingMap, ParameterModel parameterModel, T model) throws CodeGenException;
}
