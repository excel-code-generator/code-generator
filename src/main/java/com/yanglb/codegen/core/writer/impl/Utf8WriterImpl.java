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
package com.yanglb.codegen.core.writer.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.yanglb.codegen.core.writer.BaseWriter;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.Resources;

/**
 * UTF-8文件写入器
 * @author yanglibing
 *
 */
public class Utf8WriterImpl extends BaseWriter {

	@Override
	protected void onWriter() throws CodeGenException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(this.writableModel.getFullPath());
			osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
	        osw.write(this.writableModel.getData().toString()); 
	        osw.flush();
	        
		} catch (IOException e) {
			throw new CodeGenException(String.format(Resources.getString("E_007"), e.getMessage()));
		} finally {
			try{if(osw != null) osw.close();
			} catch (Exception e){}
			
			try{if(fos != null) fos.close();
			} catch (Exception e){}
		}
	}
}
