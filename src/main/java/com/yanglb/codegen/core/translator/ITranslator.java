/**
 * Copyright 2015-2020 yanglb.com
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
package com.yanglb.codegen.core.translator;

import java.util.HashMap;

import com.yanglb.codegen.model.ParamaModel;
import com.yanglb.codegen.model.WritableModel;
import com.yanglb.codegen.exceptions.CodeGenException;

public interface ITranslator<T> {
	/**
	 * 进行翻译处理
	 * @param settingMap 配置信息
	 * @param model 等待翻译的Model
	 * @param paramaModel 参数/选项模型
	 * @return WritableModel 一个可写的Model
	 * @throws CodeGenException 翻译出错时抛出此异常
	 */
    WritableModel translate(HashMap<String, String> settingMap, ParamaModel paramaModel, T model) throws CodeGenException;
	
//	/**
//	 * 预翻译器（子类如果重写，则必须显示调用超类的此方法）
//	 * @throws CodeGenException 错误信息
//	 */
//	protected void onBeforeTranslate() throws CodeGenException;
//	
//	/**
//	 * 进行转换操作
//	 * @throws CodeGenException 错误信息
//	 */
//	protected void onTranslate() throws CodeGenException;
//	
//	/**
//	 * 编码转换等操作
//	 * @throws CodeGenException 错误信息
//	 */
//	protected void onAfterTranslate() throws CodeGenException;
//	
//	// 翻译操作
//	private void doTranslate() throws CodeGenException;
}
