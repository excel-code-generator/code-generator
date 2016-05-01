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
package com.yanglb.utilitys.codegen.support;

/**
 * 支持的生成类型
 * @author yanglibing
 *
 */
public enum SupportType {
	/**
	 * 生成数据库设计
	 */
	//db,
	
	/**
	 * 生成Services
	 */
	//services,
	
	/**
	 * 生成DDL(SQL)
	 */
	ddl_mysql,
	
	/**
	 * 生成DDL(SQL Server)
	 */
	ddl_sqlserver,
	
	/**
	 * 生成DDL(Sqlite)
	 */
	ddl_sqlite,
	
	/**
	 * 生成DML(SQL)
	 */
	dml,
	
	/**
	 * 生成Message文件（properties/js）
	 */
	msg,
	
	/**
	 * 生成PO Model
	 */
	po,
}
