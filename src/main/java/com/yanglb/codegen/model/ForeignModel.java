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
package com.yanglb.codegen.model;

import java.util.List;

public class ForeignModel {
	// 表模型
	private DdlModel ddlModel;
	
	// 外键列（每一外键一条数据，联合主键时该列表才可能有多条数据）
	private List<ForeignDetailModel> foreignColumns;

	public DdlModel getDdlModel() {
		return ddlModel;
	}

	public void setDdlModel(DdlModel ddlModel) {
		this.ddlModel = ddlModel;
	}

	public List<ForeignDetailModel> getForeignColumns() {
		return foreignColumns;
	}

	public void setForeignColumns(List<ForeignDetailModel> foreignColumns) {
		this.foreignColumns = foreignColumns;
	}
	
	
}
