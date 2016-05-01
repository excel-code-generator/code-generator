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
package com.yanglb.utilitys.codegen.core.writer.impl;

import java.io.FileOutputStream;
import java.io.IOException;

import com.yanglb.utilitys.codegen.core.writer.BaseWriter;
import com.yanglb.utilitys.codegen.exceptions.CodeGenException;
import com.yanglb.utilitys.codegen.utility.MsgUtility;

/**
 * ANSI文件写入器
 * @author yanglibing
 *
 */
public class AsciiWriterImpl extends BaseWriter {
	
	@Override
	protected void onCharEncodeTranslator() throws CodeGenException {
//		super.onCharEncodeTranslator();
		
	}

	@Override
	protected void onWriter() throws CodeGenException {
//		super.onWriter();
		
		FileOutputStream fos = null;
		try {
			// TODO: ANSI形式的文件写入未完
			fos = new FileOutputStream(this.writableModel.getFullPath());
			fos.write(this.writableModel.getData().toString().getBytes("ASCII"));
			fos.flush();
	        
		} catch (IOException e) {
			throw new CodeGenException(String.format(MsgUtility.getString("E_007"), e.getMessage()));
		} finally {
			try{if(fos != null) fos.close();
			} catch (Exception e){}
		}
	}
}
