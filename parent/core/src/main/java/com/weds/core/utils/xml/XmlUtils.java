package com.weds.core.utils.xml;

import com.weds.core.annotation.XmlElement;
import com.weds.core.annotation.XmlRoot;
import com.weds.core.utils.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author sxm
 * @Description
 * @Date 2018年3月22日
 */
public class XmlUtils {

    /**
     * xml转
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Map<?, ?> xmlToMap(String xml) throws DocumentException {
        Map<String, Object> map = new HashMap<String, Object>();
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        xmlToMap(root, map);
        return map;
    }

    /**
     * 递归处理
     *
     * @param element
     * @param map
     * @return
     */
    private static void xmlToMap(Element element, Map<String, Object> map) {
        List<?> eleList = element.elements();
        if (eleList.size() == 0) {
            map.put(element.getName(), element.getTextTrim());
        } else {
            Map<String, Object> subMap = new HashMap<String, Object>();
            map.put(element.getName(), subMap);
            for (Object obj : eleList) {
                xmlToMap((Element) obj, subMap);
            }
        }
    }

    /**
     * Map对象转XML
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static String mapToXml(Map<?, ?> obj) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置XML文件的编码格式
        format.setEncoding("UTF-8");
        StringWriter sw = new StringWriter();
        XMLWriter xw = new XMLWriter(sw, format);
        xw.setEscapeText(false);
        Document document = DocumentHelper.createDocument();
        mapToXml(obj, document);
        xw.write(document);
        xw.flush();
        xw.close();
        return sw.toString();
    }

    /**
     * 递归处理
     *
     * @param obj
     * @param root
     */
    private static void mapToXml(Map<?, ?> obj, Branch root) {
        Iterator<?> iterator = obj.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object subObj = obj.get(key);
            if (subObj instanceof String) {
                StringBuffer sb = new StringBuffer();
                sb.append("<![CDATA[");
                sb.append(subObj.toString());
                sb.append("]]>");
                root.addElement(key).setText(sb.toString());
            } else if (subObj instanceof Map) {
                Element element = root.addElement(key);
                mapToXml((Map<?, ?>) subObj, element);
            }
        }
    }

    public static String beanToXml(Object obj) throws Exception {
        return beanToXml(obj, "UTF-8", true, true, true);
    }

    /**
     * 实体类转化XML
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String beanToXml(Object obj, String encoding, boolean headFlag, boolean transFlag, boolean formatFlag) throws Exception {
        OutputFormat format = null;
        if (formatFlag) {
            format = OutputFormat.createPrettyPrint();
        } else {
            format = OutputFormat.createCompactFormat();
        }
        // 设置XML文件的编码格式
        format.setEncoding(encoding);
        format.setTrimText(false);
        format.setSuppressDeclaration(!headFlag);
        StringWriter sw = new StringWriter();
        XMLWriter xw = new XMLWriter(sw, format);
        xw.setEscapeText(false);
        Document document = DocumentHelper.createDocument();
        beanToXml(obj, document, transFlag, null);
        xw.write(document);
        xw.flush();
        xw.close();
        return sw.toString();
    }

    /**
     * 递归处理
     *
     * @param obj
     * @param root
     * @throws Exception
     */
    private static void beanToXml(Object obj, Branch root, boolean transFlag, String index) {
        XmlRoot xmlRoot = obj.getClass().getAnnotation(XmlRoot.class);
        if (xmlRoot == null) {
            return;
        }

        root = root.addElement(xmlRoot.value().replace("#", index == null ? "" : index));

        String attribute = xmlRoot.attribute();
        if (!StringUtils.isBlank(attribute)) {
            String[] temp = attribute.split(":");
            root = ((Element) root).addAttribute(temp[0], temp[1]);
        }

        String subRoot = xmlRoot.child();
        if (!StringUtils.isBlank(subRoot)) {
            root = root.addElement(subRoot);
        }

        Field[] properties = obj.getClass().getDeclaredFields();
        for (Field property : properties) {
            Element el = (Element) root;
            XmlElement element = property.getAnnotation(XmlElement.class);
            Object fieldObj = ClassUtils.getFieldValueByName(property.getName(), obj);

            // (property.getType().isAnnotationPresent(XmlRoot.class)
            if (fieldObj != null && fieldObj.getClass().isAnnotationPresent(XmlRoot.class)) {
                if (element != null) {
                    el = root.addElement(element.value());
                }
                beanToXml(fieldObj, el, transFlag, null);
                continue;
            }

            if (fieldObj instanceof List) {
                if (element != null) {
                    el = root.addElement(element.value());
                }
                int listIndex = 1;
                for (Object subObj : ((List<?>) fieldObj)) {
                    beanToXml(subObj, el, transFlag, listIndex + "");
                    listIndex++;
                }
                continue;
            }

            if (element == null) {
                continue;
            }

            String value = fieldObj == null ? "" : fieldObj.toString();
            if (element.length() != -1) {
                int len = value.length();
                if (len > element.length()) {
                    value = value.substring(0, element.length());
                } else {
                    value = String.format("%-" + element.length() + "s", value);
                }
            }

            StringBuilder sb = new StringBuilder();
            if (transFlag) {
                sb.append("<![CDATA[");
                sb.append(value);
                sb.append("]]>");
            } else {
                sb.append(value);
            }

            // 为节点添加属性，属性值为对应属性的值
            root.addElement(element.value()).setText(sb.toString());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> getElementByPath(String xml, String xPath) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element el_root = document.getRootElement();
        XPath xpath = el_root.createXPath(xPath);
        List<Element> listNode = xpath.selectNodes(el_root);
        return getElementAttr(listNode);
    }

    public static Map<String, String> getOneElementByPath(String xml, String xPath) throws DocumentException {
        return getElementByPath(xml, xPath).get(0);
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, String>> getElementAttr(List<Element> listNode) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Element el : listNode) {
            Map<String, String> map = new HashMap<String, String>();
            List<Element> nodeName = el.elements();
            for (Element element : nodeName) {
                map.put(element.getName(), element.getText());
            }
            list.add(map);
        }
        return list;
    }
}
