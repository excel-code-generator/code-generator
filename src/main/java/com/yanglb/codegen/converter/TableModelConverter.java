/**
 * Copyright 2015-2021 yanglb.com
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
package com.yanglb.codegen.converter;

import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.model.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TableModelConverter<T> {

	/**
	 * 将TableModel对象转换为指定的model对象
	 * @param cls Model类
	 * @param model 数据模型
	 * @return cls Bean的列表
	 * @throws CodeGenException 转换出错时抛出异常
	 */
	public List<T> convert(Class<T> cls, TableModel model) throws CodeGenException {
		List<T> result = new ArrayList<T>();
		List<Map<String, String>> modelList = model.toList();
		
		BeanMapConverter<T> converter = new BeanMapConverter<T>();
		for(Map<String, String> itm : modelList) {
			T res = converter.convert(cls, itm);
			
			// 添加到结果集中
			result.add(res);
		}
		return result;
	}
}
