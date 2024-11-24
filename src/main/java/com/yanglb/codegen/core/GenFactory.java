/**
 * Copyright 2015-2023 yanglb.com
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

import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.ObjectUtil;
import com.yanglb.codegen.utils.Resources;

public class GenFactory<T> {
	private GenFactory() {
	}

	/**
	 * 根据配置文件中配置的名字创建
	 * @param <T> 创建接口类型
	 * @return T的接口实例
	 * @throws CodeGenException 错误信息
	 */
	public static <T> T createByName(String className) throws CodeGenException {
		T result = null;
		try {
			GenFactory<T> factory = new GenFactory<T>();
			result = factory.create(className);
		} catch (Exception e) {
			throw new CodeGenException(String.format(Resources.getString("E_011"), className, e.getMessage()));
		}
		return result;
	}

	private T create(String implName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException  {
		if (implName.startsWith(".")) implName = "com.yanglb.codegen" + implName;

		Object instance = Class.forName(implName).newInstance();
		return ObjectUtil.cast(instance);
	}
}
