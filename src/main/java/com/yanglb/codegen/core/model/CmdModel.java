package com.yanglb.codegen.core.model;

import java.util.Map;

public class CmdModel {
    private String parser;
    private String generator;
    private String reader;
    private String translator;

    public CmdModel() {
    }
    public CmdModel(Map<String, String> values){
        parser = values.get("parser");
        generator = values.get("generator");
        reader = values.get("reader");
        translator = values.get("translator");
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }
}
