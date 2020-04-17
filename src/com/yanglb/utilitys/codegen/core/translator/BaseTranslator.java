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
package com.yanglb.utilitys.codegen.core.translator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import com.yanglb.utilitys.codegen.core.model.ParamaModel;
import com.yanglb.utilitys.codegen.core.model.WritableModel;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.utility.MsgUtility;
import com.yanglb.utilitys.codegen.utility.ObjectUtility;
import com.yanglb.utilitys.codegen.utility.StringUtility;

public class BaseTranslator<T> implements ITranslator<T> {
	protected T model;
	protected WritableModel writableModel;
	protected HashMap<String, String> settingMap;
	protected ParamaModel paramaModel;
	
	protected BaseTranslator() {
		this.writableModel = new WritableModel();
	}
	
	/**
	 * 进行翻译处理
	 * @param settingMap 配置信息
	 * @param model 等待翻译的Model
	 * @param paramaModel 参数/选项模型
	 * @return WritableModel 一个可写的Model
	 * @throws CodeGenException 翻译出错时抛出此异常
	 */
	public WritableModel translate(HashMap<String, String> settingMap, ParamaModel paramaModel, T model) 
			throws CodeGenException {
		this.settingMap = settingMap;
		this.model = model;
		this.paramaModel = paramaModel;
		
		this.doTranslate();
		return this.writableModel;
	}
	
	/**
	 * 预翻译器（子类如果重写，则必须显示调用超类的此方法）
	 * @throws CodeGenException 错误信息
	 */
	protected void onBeforeTranslate() throws CodeGenException {
		// 设置文件名、等初始化操作
		this.writableModel.setEncode("utf-8");
		this.writableModel.setExtension(this.paramaModel.getLang().name());
		this.writableModel.setFileName("untitled");
		this.writableModel.setFilePath("");
		this.writableModel.setLang(this.paramaModel.getLang());
		this.writableModel.setData(new StringBuilder());
		this.writableModel.setOutputDir(this.paramaModel.getOut());
		
		return;
	}
	
	/**
	 * 进行转换操作
	 * @throws CodeGenException 错误信息
	 */
	protected void onTranslate() throws CodeGenException{
		// 父类没什么可做的了
	}
	
	/**
	 * 编码转换等操作
	 * @throws CodeGenException 错误信息
	 */
	protected void onAfterTranslate() throws CodeGenException {
		// 编码转换在写入器完成
		// 标记替换
		this.replaceFlags();
		
		// TODO: 代码格式调整（后期完善）
	}
	
	// 翻译操作
	private void doTranslate() throws CodeGenException{
		this.onBeforeTranslate();
		this.onTranslate();
		this.onAfterTranslate();
	}
	
	/**
	 * 替换{标记}
	 * 如果
	 * @throws CodeGenException 
	 */
	private void replaceFlags() throws CodeGenException {
		Object replaceModel = this.getReplaceModel();
		List<String> flags = StringUtility.findFlags(this.writableModel.getData());
		String data = this.writableModel.getData().toString();
		for(String key:flags) {
			// Map元素
			String value = null;
			boolean hasKey = false;
			if(this.settingMap.containsKey(key)) {
				hasKey = true;
				value = this.settingMap.get(key);
			} else if(replaceModel != null) {
				// Model元素
				Field field = null;
				hasKey = true;
				// 在replaceModel查找key属性，如果有则使用该属性值。
				try {
					field = ObjectUtility.getDeepField(replaceModel.getClass(), key);
					field.setAccessible(true);
					if(field.get(replaceModel) != null) {
						value = field.get(replaceModel).toString();
					}
				} catch (NoSuchFieldException e) {
					// 没有此属性
					hasKey = false;
				} catch (SecurityException |IllegalArgumentException | IllegalAccessException e) {
					throw new CodeGenException(String.format(MsgUtility.getString("E_006"),
							replaceModel.getClass().toString(),
							field.getName(),
							e.getMessage()));
				}
			}
			
			// 如果有值则替换
			if(hasKey) {
				String f = String.format("\\$\\{%s\\}", key);
				data = data.replaceAll(f, value);
			}
		}
		this.writableModel.setData(new StringBuilder(data));
	}
	
	/**
	 * 取得用于替换的Model
	 * @return
	 */
	protected Object getReplaceModel() {
		return this.model;
	}
}
