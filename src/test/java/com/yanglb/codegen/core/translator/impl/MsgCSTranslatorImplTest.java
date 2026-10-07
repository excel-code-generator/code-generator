package com.yanglb.codegen.core.translator.impl;

import com.yanglb.codegen.model.ParameterModel;
import com.yanglb.codegen.model.TableModel;
import com.yanglb.codegen.model.WritableModel;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class MsgCSTranslatorImplTest {
    @Parameterized.Parameters(name = "language={0}")
    public static Collection<Object[]> languages() {
        return Arrays.asList(new Object[][]{{"default"}, {"en"}, {"ko"}});
    }

    private final String language;

    public MsgCSTranslatorImplTest(String language) {
        this.language = language;
    }

    @Test
    public void preservesAllCommasInLastResourceValue() throws Exception {
        Document document = translate(new String[][]{
                {"Probe.First", "First, comma"},
                {"Probe.Last", "Last, multiple, commas,"}
        });

        assertEquals("First, comma", value(document, "data", "Probe.First"));
        assertEquals("Last, multiple, commas,", value(document, "data", "Probe.Last"));
    }

    @Test
    public void preservesCommaWhenLastResourceHasNoComma() throws Exception {
        Document document = translate(new String[][]{
                {"Probe.First", "First, comma"},
                {"Probe.Last", "No comma"}
        });

        assertEquals("First, comma", value(document, "data", "Probe.First"));
        assertEquals("No comma", value(document, "data", "Probe.Last"));
    }

    @Test
    public void preservesCommasInResourceKeys() throws Exception {
        Document document = translate(new String[][]{{"Probe,Key", "No comma"}});

        assertEquals("No comma", value(document, "data", "Probe,Key"));
    }

    @Test
    public void preservesHeadersWhenResourceValuesContainNoCommas() throws Exception {
        Document document = translate(new String[][]{{"Probe", "No comma"}});

        assertEquals("System.Resources.ResXResourceWriter, System.Windows.Forms, "
                        + "Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089",
                value(document, "resheader", "writer"));
        assertEquals("No comma", value(document, "data", "Probe"));
    }

    @Test
    public void preservesCommasAlongsideXmlSpecialCharacters() throws Exception {
        String original = "<tag>, & \"quoted\", 한글, 中文";
        Document document = translate(new String[][]{{"Probe", original}});

        assertEquals(original, value(document, "data", "Probe"));
    }

    private Document translate(String[][] resources) throws Exception {
        TableModel table = new TableModel();
        table.setSheetName("CommaProbe");
        for (String[] resource : resources) {
            Map<String, String> row = new HashMap<>();
            row.put("id", resource[0]);
            row.put(language, resource[1]);
            table.insert(row);
        }

        ParameterModel parameters = new ParameterModel();
        parameters.setFile("CommaProbe.xlsx");
        parameters.setOptions(new DefaultParser().parse(new Options(), new String[0]));
        HashMap<String, String> settings = new HashMap<>();
        settings.put("MsgLang", language);

        List<WritableModel> outputs = new MsgCSTranslatorImpl().translate(
                settings, parameters, Collections.singletonList(table));
        assertEquals(1, outputs.size());
        assertEquals("resx", outputs.get(0).getExtension());
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                new InputSource(new StringReader(outputs.get(0).getData().toString())));
    }

    private String value(Document document, String tag, String name) {
        NodeList elements = document.getElementsByTagName(tag);
        for (int index = 0; index < elements.getLength(); index++) {
            Element element = (Element) elements.item(index);
            if (name.equals(element.getAttribute("name"))) {
                return element.getElementsByTagName("value").item(0).getTextContent();
            }
        }
        throw new AssertionError("Missing " + tag + " named " + name);
    }
}
