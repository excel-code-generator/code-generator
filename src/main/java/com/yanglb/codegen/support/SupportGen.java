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
package com.yanglb.codegen.support;

public enum SupportGen {
    // reader
    ddl_reader,
    dml_reader,
    hashmap_reader,
    setting_reader,
    table_reader,

    // translator
    ddl_mysql_translator,
    ddl_sqlite_translator,
    ddl_sqlserver_translator,
    dml_translator,
    msg_js_translator,
    msg_json_translator,
    msg_java_translator,
    msg_cs_translator,
    msg_ios_translator,
    msg_android_translator,

    // writer 
    utf8_writer,
    ascii_writer,
}
