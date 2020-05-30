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
package com.yanglb.codegen.core.translator;

import java.util.List;

import com.yanglb.codegen.exceptions.CodeGenException;

public class BaseSqlTranslator<T> extends BaseTranslator<List<T>> {
	// 列引号字符，如：SQL Server为[], MySQL为`等
	protected String sqlColumnStart = "\"";
	protected String sqlColumnEnd = "\"";
}
