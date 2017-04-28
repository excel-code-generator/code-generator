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
package com.yanglb.utilitys.codegen.converter;

import java.lang.reflect.Field;
import java.util.Map;

import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.utility.MsgUtility;
import com.yanglb.utilitys.codegen.utility.ObjectUtility;
import com.yanglb.utilitys.codegen.utility.StringUtility;

public class BeanMapConverter<T> {
	
	/**
	 * 将Map转换为 cls类的对象
	 * @param cls 将要转换为的类
	 * @param map 保存数据的Map
	 * @return 转换结果
	 * @throws CodeGenException
	 */
	public T convert(Class<T> cls, Map<String, String> map) throws CodeGenException {
		T result = null;
		try {
			result = cls.newInstance();
			for(String key:map.keySet()) {
				Field field = ObjectUtility.getDeepField(result.getClass(), key);
				field.setAccessible(true);
				
				String mapValue = map.get(key);
				Object value = null;
				
				// 类型转换
				Class<?> type = field.getType();
				if(boolean.class.equals(type)) {
					value = false;
					if(!StringUtility.isNullOrEmpty(mapValue)) {
						value = this.toBoolean(mapValue);
					}
				} else if (Boolean.class.equals(type)) {
					value = null;
					if(!StringUtility.isNullOrEmpty(mapValue)) {
						value = this.toBoolean(mapValue);
					}
				} else if (int.class.equals(type)) {
					// 直接转换，出错时向外抛异常
					try{
						value = Integer.parseInt(mapValue);
					}catch(NumberFormatException e) {
						throw new CodeGenException(String.format(MsgUtility.getString("E_001"), key, e.getMessage()));
					}
				} else if ( Integer.class.equals(type)) {
					// 可以为Null的整型
					if(StringUtility.isNullOrEmpty(mapValue)) {
						value = null;
					} else {
						try{
							value = Integer.parseInt(mapValue); 
						}catch(NumberFormatException e) {
							throw new CodeGenException(String.format(MsgUtility.getString("E_001"), key, e.getMessage()));
						}
					}
				} else {
					// TODO: 其它类型的转换需要添加
					value = mapValue;
				}
				field.set(result, value);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new CodeGenException(e.getMessage());
		} catch (NoSuchFieldException e) {
			throw new CodeGenException(String.format(MsgUtility.getString("E_002"), e.getMessage()));
		} catch (SecurityException e) {
			throw new CodeGenException(e.getMessage());
		}
		return result;
	}
	
	private boolean toBoolean(String v) {
		boolean value = false;
		v = v.toLowerCase();
		if("y".equals(v) 
				|| "√".equals(v)
				|| "yes".equals(v)
				|| "true".equals(v)){
			value = true;
		}
		return value;
	}
}
