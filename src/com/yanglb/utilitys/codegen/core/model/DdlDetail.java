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
package com.yanglb.utilitys.codegen.core.model;

public class DdlDetail extends BaseModel {
	// PO 属性
	// 字段名  
	private String fieldName;
	// 属性名  
	private String poName;
	// Java/.NET类型  
	private String poType;

	// 表属性
	// 列名
	private String colName;
	// 类型
	private String colType;
	// 长度
	private Integer colLength;
	// 精度
	private Integer colPrecision;
	// 主键
	private boolean colKey;
	// 允许NULL
	private boolean colNullable;
	// 索引（只是参考，至少目前）
	private String colIndex;
	// 外键（只是参考，至少目前）
	private String colForeign;
	// 默认值
	private String colDefault;
	
	// 说明
	private String description;

	public String getColIndex() {
		return colIndex;
	}

	public void setColIndex(String colIndex) {
		this.colIndex = colIndex;
	}

	public String getColForeign() {
		return colForeign;
	}

	public void setColForeign(String colForeign) {
		this.colForeign = colForeign;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the poName
	 */
	public String getPoName() {
		return poName;
	}

	/**
	 * @param poName the poName to set
	 */
	public void setPoName(String poName) {
		this.poName = poName;
	}

	/**
	 * @return the poType
	 */
	public String getPoType() {
		return poType;
	}

	/**
	 * @param poType the poType to set
	 */
	public void setPoType(String poType) {
		this.poType = poType;
	}

	/**
	 * @return the colName
	 */
	public String getColName() {
		return colName;
	}

	/**
	 * @param colName the colName to set
	 */
	public void setColName(String colName) {
		this.colName = colName;
	}

	/**
	 * @return the colType
	 */
	public String getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(String colType) {
		this.colType = colType;
	}

	/**
	 * @return the colLength
	 */
	public Integer getColLength() {
		return colLength;
	}

	/**
	 * @param colLength the colLength to set
	 */
	public void setColLength(Integer colLength) {
		this.colLength = colLength;
	}

	/**
	 * @return the colPrecision
	 */
	public Integer getColPrecision() {
		return colPrecision;
	}

	/**
	 * @param colPrecision the colPrecision to set
	 */
	public void setColPrecision(Integer colPrecision) {
		this.colPrecision = colPrecision;
	}

	/**
	 * @return the colKey
	 */
	public boolean isColKey() {
		return colKey;
	}

	/**
	 * @param colKey the colKey to set
	 */
	public void setColKey(boolean colKey) {
		this.colKey = colKey;
	}

	/**
	 * @return the colNullable
	 */
	public boolean isColNullable() {
		return colNullable;
	}

	/**
	 * @param colNullable the colNullable to set
	 */
	public void setColNullable(boolean colNullable) {
		this.colNullable = colNullable;
	}

	/**
	 * @return the colDefault
	 */
	public String getColDefault() {
		return colDefault;
	}

	/**
	 * @param colDefault the colDefault to set
	 */
	public void setColDefault(String colDefault) {
		this.colDefault = colDefault;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
