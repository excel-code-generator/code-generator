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
package com.yanglb.codegen.core.reader;

import com.yanglb.codegen.exceptions.CodeGenException;
import java.util.List;


public interface IReader<T> {
	/**
	 * 读取Excel中全部可读取Sheet
	 * @param excelFile
	 * @return
	 * @throws CodeGenException 
	 */
	List<T> reader(String excelFile) throws CodeGenException;
	
	/**
	 * 读取Excel中指定的几个Sheet
	 * @param excelFile
	 * @param sheets
	 * @return
	 * @throws CodeGenException 
	 */
	List<T> reader(String excelFile, String[] sheets) throws CodeGenException;
	
	/**
	 * 读取Excel中指定的一个Sheet
	 * @param excelFile
	 * @param sheet
	 * @return
	 * @throws CodeGenException 
	 */
	T reader(String excelFile, String sheet) throws CodeGenException;

	/**
	 * 设置开始位置
	 * @param startRowNo 开始行
	 * @param startColNo 开始列
	 */
	void setStartPoint(int startRowNo, int startColNo);
}
