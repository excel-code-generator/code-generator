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
package com.yanglb.codegen.core;

import com.yanglb.codegen.core.generator.IGenerator;
import com.yanglb.codegen.core.model.ParamaModel;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.support.SupportGen;
import com.yanglb.codegen.utils.Conf;
import com.yanglb.codegen.utils.Conf;
import com.yanglb.codegen.utils.MsgUtility;

public class GenFactory<T> {
	// 外部不可创建实例
	private GenFactory() {
		
	}

	/**
	 * 创建生成器
	 * @param paramaModel 参数模型
	 * @return IGenerator
	 * @throws CodeGenException
	 */
	public static IGenerator createGenerator(ParamaModel paramaModel) throws CodeGenException{
		IGenerator generator = null;
		try {
			GenFactory<IGenerator> factory = new GenFactory<IGenerator>(); 
			generator = factory.createFromProperties(paramaModel.getCmd());
			generator.init(paramaModel);
		} catch (Exception e) {
			throw new CodeGenException(MsgUtility.getString("E_010"));
		}
		return generator;
	}
	
	/**
	 * 根据配置文件中配置的名字创建
	 * @param <T> 创建接口类型
 	 * @param name 名称
	 * @return T的接口实例
	 * @throws CodeGenException 错误信息
	 */
	public static <T> T createByName(SupportGen name) throws CodeGenException {
		T result = null;
		try {
			GenFactory<T> factory = new GenFactory<T>();
			result = factory.createFromProperties(name.name());
		} catch (Exception e) {
			throw new CodeGenException(String.format(MsgUtility.getString("E_011"), name, e.getMessage()));
		}
		return result;
	}
	
	
	/**
	 * 根据类名创建
	 * @param implName
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected T create(String implName) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException  {
		T instance = null;
		instance = (T) Class.forName(implName).newInstance();
		return instance;
	}
	
	/**
	 * 根据配置文件创建
	 * @param key
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected T createFromProperties(String key) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String className = "com.yanglb.codegen.core." + Conf.getString(Conf.CATEGORY_GENERATOR, key);
		return this.create(className);
	}
}
