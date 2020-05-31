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
package com.yanglb.codegen.core.writer;

import java.io.File;

import com.yanglb.codegen.model.WritableModel;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.Resources;

public class BaseWriter implements IWriter{
	protected WritableModel writableModel;
	
	@Override
	public void writer(WritableModel writableModel) throws CodeGenException {
		this.writableModel = writableModel;
		
		this.doWriter();
	}
	
	/**
	 * 进行写入
	 * @throws CodeGenException
	 */
	private void doWriter() throws CodeGenException{
		// 生成目录 
		this.onCreateDir();
		
		// 字节编码转换
		this.onCharEncodeTranslator();
		
		// 进行写入
		this.onWriter();
	}
	
	/**
	 * 生成必要的目录（如包名等）
	 * @throws CodeGenException
	 */
	protected void onCreateDir() throws CodeGenException {
		String outPath = this.writableModel.getOutputDir();
		outPath = outPath.replace('\\', '/');
		if(outPath.lastIndexOf("/") == outPath.length()-1) {
			outPath = outPath.substring(0, outPath.length()-1);
		}
		
		outPath = String.format("%s/%s.%s",
				outPath, 
				this.writableModel.getFileName(),
				this.writableModel.getExtension());
		
		this.writableModel.setFullPath(outPath);
		
		File file = new File(outPath);
		if(file.isDirectory()) {
			// 文件已存在且是目录，不可继续处理
			throw new CodeGenException(String.format(Resources.getString("E_008"), outPath));
		}
		
		// 目录不存在时创建
		if(!file.getParentFile().exists()) {
			if(!file.getParentFile().mkdirs()) {
				throw new CodeGenException(String.format(Resources.getString("E_009"), outPath));
			}
		}
	}
	
	/**
	 * 进行写入
	 * @throws CodeGenException
	 */
	protected void onWriter() throws CodeGenException {
		// 父类不做任何处理
	}
	
	/**
	 * 字符编辑转换（如有必要子类进行实现）
	 * @throws CodeGenException
	 */
	protected void onCharEncodeTranslator() throws CodeGenException {
		// 子类实现
	}
}
