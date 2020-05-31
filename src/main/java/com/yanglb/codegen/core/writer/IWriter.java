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

import com.yanglb.codegen.model.WritableModel;
import com.yanglb.codegen.exceptions.CodeGenException;

public interface IWriter {
	
	/**
	 * 将WritableModel对象写入文件中
	 * @param writableModel
	 * @throws CodeGenException
	 */
    void writer(WritableModel writableModel) throws CodeGenException;
}
