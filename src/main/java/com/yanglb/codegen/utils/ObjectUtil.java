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
package com.yanglb.codegen.utils;

import java.lang.reflect.Field;

public class ObjectUtil {
	
	/**
	 * 深层次
	 * 获取指定Class的属性
	 * @param cls 类名
	 * @param name 属性史
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Field getDeepField(Class<?> cls, String name) 
			throws NoSuchFieldException, SecurityException {
		Field field = null;
		try {
			field = cls.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			if(cls.equals(Object.class)) {
				throw e;
			}
			return getDeepField(cls.getSuperclass(), name);
		}
		
		return field;
	}
}
