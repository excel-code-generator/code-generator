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
package com.yanglb.codegen.core.reader;

import com.yanglb.codegen.exceptions.CodeGenException;

import java.util.HashMap;


public interface ISettingReader extends IReader<HashMap<String, String>> {
    /**
     * 读取配置项目
     */
    HashMap<String, String> settingReader() throws CodeGenException;

    /**
     * 读取一个配置项目（会将Infos内容添加到结果集中，且属性添加generator前缀）
     */
    HashMap<String, String> settingReader(String settingSheet) throws CodeGenException;

    /**
     * 读取多个配置项目（会将Infos内容添加到结果集中，且属性添加generator前缀）
     */
    HashMap<String, String> settingReader(String[] settingSheets) throws CodeGenException;
}
