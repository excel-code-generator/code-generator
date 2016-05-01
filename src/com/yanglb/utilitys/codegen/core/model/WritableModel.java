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

import com.yanglb.utilitys.codegen.support.SupportLang;

public class WritableModel {
	private String encode;
	private SupportLang lang;
	private String extension;
	private String filePath;
	private String fileName;
	private String outputDir;
	private String fullPath;
	private StringBuilder data;
	
	
	/**
	 * @return the fullPath
	 */
	public String getFullPath() {
		return fullPath;
	}
	/**
	 * @param fullPath the fullPath to set
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	/**
	 * @return the outputDir
	 */
	public String getOutputDir() {
		return outputDir;
	}
	/**
	 * @param outputDir the outputDir to set
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	/**
	 * @return the encode
	 */
	public String getEncode() {
		return encode;
	}
	/**
	 * @param encode the encode to set
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}
	/**
	 * @return the lang
	 */
	public SupportLang getLang() {
		return lang;
	}
	/**
	 * @param lang the lang to set
	 */
	public void setLang(SupportLang lang) {
		this.lang = lang;
	}
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}
	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	/**
	 * @return the data
	 */
	public StringBuilder getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(StringBuilder data) {
		this.data = data;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
