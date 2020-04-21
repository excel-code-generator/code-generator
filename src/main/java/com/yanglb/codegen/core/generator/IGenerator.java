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
package com.yanglb.codegen.core.generator;

import java.util.List;

import com.yanglb.codegen.core.model.OptionModel;
import com.yanglb.codegen.core.model.ParamaModel;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.exceptions.ParamaCheckException;

public interface IGenerator {
	
	/**
	 * 初始化
	 * @param paramaModel 参数模型
	 */
	public void init(ParamaModel paramaModel);
	
//	/**
//	 * 检查输入参数是否正确
//	 * @throws ParamaCheckException 出错信息
//	 */
//	public void check() throws ParamaCheckException;
	
	/**
	 * 执行生成工作
	 * @throws CodeGenException 出错信息
	 */
	public void invoke() throws CodeGenException, ParamaCheckException;
	
	/**
	 * 取得该生成器支持的选项
	 * @return 该生成器支持的选项
	 */
	public List<OptionModel> getSupportOptions();
}
