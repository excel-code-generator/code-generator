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
package com.yanglb.codegen.core.translator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yanglb.codegen.model.DdlDetail;
import com.yanglb.codegen.model.DdlModel;
import com.yanglb.codegen.model.ForeignDetailModel;
import com.yanglb.codegen.model.ForeignModel;
import com.yanglb.codegen.exceptions.CodeGenException;
import com.yanglb.codegen.utils.StringUtil;

public class BaseDdlTranslator extends BaseSqlTranslator<DdlModel> {
	// 外键关系列表
	protected List<ForeignModel> foreignKeyList = new ArrayList<ForeignModel>();
	
	@Override
	protected void onBeforeTranslate() throws CodeGenException {
		super.onBeforeTranslate();
		this.writableModel.setExtension("ddl");

		// 设置文件名（同Excel名）
		String fileName = this.model.get(0).getExcelFileName();
		File file = new File(fileName);
		fileName = file.getName();
		
		int index = fileName.lastIndexOf(".");
		if(index != -1) {
			fileName = fileName.substring(0, index);
		}
		this.writableModel.setFileName(fileName);
	}
	
	@Override
	protected void onTranslate() throws CodeGenException {
		super.onTranslate();
		
		// 计算需要处理的外键
		for(DdlModel model : this.model) {
			for(DdlDetail detail : model.getDetail()) {
				// 外键表信息
				ForeignDetailModel foreignDetailModel = this.getForeignByDdlDetail(model, detail);
				
				// 有外键
				if(foreignDetailModel != null) {
					// 检查当前表中有无相同的外键
					ForeignModel foreignModel = this.findUnionForeignKey(model, foreignDetailModel);
					List<ForeignDetailModel> foreignColumns = null;
					
					// 为null时不是联合主键的外键，新建一个外键模型
					if(foreignModel == null) {
						// 外键模型
						foreignModel = new ForeignModel();
						foreignColumns = new ArrayList<ForeignDetailModel>();
						foreignModel.setDdlModel(model);
						foreignModel.setForeignColumns(foreignColumns);
						
						// 添加列外键列表中
						this.foreignKeyList.add(foreignModel);
					} else {
						foreignColumns = foreignModel.getForeignColumns();
					}
					
					// 保存外键关系
					foreignColumns.add(foreignDetailModel);
				}
			}
		}
	}
	
	/**
	 * 查询联合外键
	 * 标准：同表不同列视为联合外键
	 * @param tableModel 表模型
	 * @param foreignDetailModel 外键信息模型
	 * @return 是联合主键的外键时返回外键模型，否则返回null
	 */
	protected ForeignModel findUnionForeignKey(DdlModel tableModel, ForeignDetailModel foreignDetailModel) {
		// 从后往前查找
		for(int i=this.foreignKeyList.size()-1; i>=0; i--) {
			ForeignModel foreignModel = this.foreignKeyList.get(i);
			// 不同表时直接跳过（所处理的表）
			if(!foreignModel.getDdlModel().equals(tableModel)) {
				continue;
			}
			
			// 当前表之前已经处理过的外键List
			List<ForeignDetailModel> savedForeignDetail = foreignModel.getForeignColumns();
			if(!savedForeignDetail.get(0).getForeignDdlModel().equals(foreignDetailModel.getForeignDdlModel())) {
				// 不同的主键引用表跳过
				continue;
			}
			
			// 是否找到同表不同列的外键
			boolean hasSameKey = false;
			for(ForeignDetailModel itm:savedForeignDetail) {
				// 同主键表下查找
				if(itm.getForeignDdlDetail().equals(foreignDetailModel.getForeignDdlDetail())) {
					// 找到相同的主键引用
					hasSameKey = true;
				}
			}
			
			// 没有相同的主键引用表明当前列的外键与找到的是联合主键（合并到一起）
			// 否则表示没找到联合主键的外键
			if(!hasSameKey) {
				return foreignModel;
			}
		}
		return null;
	}
		
	/**
	 * 获取外键信息
	 * @param model 表模型
	 * @param detail 列模型
	 * @return 外键模型（没有外键时返回null）
	 * @throws CodeGenException
	 */
	protected ForeignDetailModel getForeignByDdlDetail(DdlModel model, DdlDetail detail)
			throws CodeGenException {
		// 有外键
		if(!StringUtil.isNullOrEmpty(detail.getColForeign())) {
			// 外键表信息
			ForeignDetailModel foreignDetailModel = new ForeignDetailModel();
			
			boolean result = true;
			do{
				// 外键名
				String referenceName = detail.getColForeign();
				String[] foreigns = referenceName.split("\\.");
				
				// 外键表
				DdlModel foreignTableModel = null;
				// 外键表
				DdlDetail foreignColumnModel = null;
				
				if(foreigns==null || foreigns.length !=2) {
					result = false;
					break;
				}
				
				// 获取外键表
				foreignTableModel = this.findFullTableModel(foreigns[0]);
				if(foreignTableModel == null) {
					result = false;
					break;
				}
				
				// 获取外键表
				for(DdlDetail item:foreignTableModel.getDetail()) {
					if(item.getColName().equals(foreigns[1])) {
						foreignColumnModel = item;
						break;
					}
				}
				
				// 转为ForeignDetailModel
				foreignDetailModel.setDdlDetail(detail);
				foreignDetailModel.setForeignDdlModel(foreignTableModel);
				foreignDetailModel.setForeignDdlDetail(foreignColumnModel);
			}while(false);
			
			// 失败时直接抛出异常
			if(!result) {
				throw new CodeGenException(String.format("invalid foreign key name: [%s] At %s", 
						detail.getColForeign(),
						model.getSheetName()));
			}
			
			return foreignDetailModel;
		}
		
		// 没有外键时返回null
		return null;
	}

	/**
	 * 根据前缀查找全表名
	 * @param prefix 前缀
	 * @return 表名
	 */
	protected DdlModel findFullTableModel(String prefix) {
		// 创建别名与表名关系
		for(DdlModel itm : this.model) {
			if(itm.getName().startsWith(prefix)) {
				return itm;
			}
		}
		return null;
	}
	
	/**
	 * 生成完整的全表名
	 * @param model
	 * @return
	 */
	protected String genFullTableName(DdlModel model) {
		String space = "";
		if(!StringUtil.isNullOrEmpty(model.getNameSpace()) && !"-".equals(model.getNameSpace())) {
			space = String.format("%s%s%s.", this.sqlColumnStart, model.getNameSpace(), this.sqlColumnEnd);
		}
		return String.format("%s%s%s%s", space, this.sqlColumnStart, model.getName(), this.sqlColumnEnd);
	}
}
