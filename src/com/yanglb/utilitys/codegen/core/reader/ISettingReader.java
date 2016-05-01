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
package com.yanglb.utilitys.codegen.core.reader;

import java.util.HashMap;

import com.yanglb.utilitys.codegen.exceptions.CodeGenException;


public interface ISettingReader extends IReader<HashMap<String, String>> {
	/**
	 * 读取common配置项目
	 * @return
	 * @throws CodeGenException
	 */
	public HashMap<String, String> settingReader() throws CodeGenException;
	
	/**
	 * 读取一个配置项目（会另外添加common Sheet的内容）
	 * @param settingSheet
	 * @return
	 * @throws CodeGenException
	 */
	public HashMap<String, String> settingReader(String settingSheet) throws CodeGenException;
	
	/**
	 * 读取多个配置项目（会另外添加common Sheet的内容）
	 * @param settingSheets
	 * @return
	 * @throws CodeGenException
	 */
	public HashMap<String, String> settingReader(String[] settingSheets) throws CodeGenException;
}
