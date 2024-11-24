/**
 * Copyright 2015-2023 yanglb.com
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
package com.yanglb.codegen.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableModel extends BaseModel {
	private final List<Map<String, String>> metaData;
	private String[] columns;
	
	public TableModel() {
		this.columns = null;
		this.metaData = new ArrayList<Map<String, String>>();
	}
	
	public Map<String, String> getAt(int index) {
		return this.metaData.get(index);
	}
	
	public void insert(Map<String, String> row) {
		this.metaData.add(row);
	}
	
	public void removeAt(int index) {
		this.metaData.remove(index);
	}
	
	public void clear() {
		this.metaData.clear();
	}
	
	public int size() {
		return this.metaData.size();
	}
	
	public List<Map<String, String>> toList() {
		return this.metaData;
	}
	/**
	 * 获取列对象
	 * @return
	 */
	public String[] getColumns() {
		return this.columns;
	}
	
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
}
