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

public enum SupportGen {
	// reader
	ddl_reader, // com.yanglb.utilitys.codegen.core.reader.impl.DdlReaderImpl
	dml_reader, // com.yanglb.utilitys.codegen.core.reader.impl.DmlReaderImpl
	hashmap_reader, // com.yanglb.utilitys.codegen.core.reader.impl.HashMapReaderImpl
	setting_reader, // com.yanglb.utilitys.codegen.core.reader.impl.SettingReaderImpl
	table_reader, // com.yanglb.utilitys.codegen.core.reader.impl.TableReaderImpl

	// translator
	ddl_mysql_translator, // com.yanglb.utilitys.codegen.core.translator.impl.DdlMysqlTranslatorImpl
	ddl_sqlite_translator, // com.yanglb.utilitys.codegen.core.translator.impl.DdlSqliteTranslatorImpl
	ddl_sqlserver_translator, // com.yanglb.utilitys.codegen.core.translator.impl.DdlSqlServerTranslatorImpl
	dml_translator, // com.yanglb.utilitys.codegen.core.translator.impl.DmlTranslatorImpl
	msg_js_translator, // com.yanglb.utilitys.codegen.core.translator.impl.MsgJsTranslatorImpl
	msg_json_translator, // com.yanglb.utilitys.codegen.core.translator.impl.MsgJsonTranslatorImpl
	msg_java_translator, // com.yanglb.utilitys.codegen.core.translator.impl.MsgJavaTranslatorImpl
	msg_cs_translator,

	// writer 
	utf8_writer, // com.yanglb.utilitys.codegen.core.writer.impl.Utf8WriterImpl
	ascii_writer, // com.yanglb.utilitys.codegen.core.writer.impl.AsciiWriterImpl
}
