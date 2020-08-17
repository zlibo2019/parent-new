package com.weds.core.utils.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {
    private Document doc;

    /**
     * This method is used to init a Document by a xml-string.
     *
     * @return the return value is .
     */
    public XmlParser(String filename) throws Exception {
        try {
            DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
            DocumentBuilder DB = DBF.newDocumentBuilder();
            doc = DB.parse(filename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("未找到文件" + filename);
        }
    }

    public XmlParser(InputStream in) throws Exception {
        try {
            DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
            DocumentBuilder DB = DBF.newDocumentBuilder();
            doc = DB.parse(in, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("输入流不合法");
        }
    }

    /**
     * 在指定元素指定位置下插入值为value的item元素
     *
     * @param name
     * @param row
     * @param item
     * @param value
     * @throws Exception
     */
    public void setItemValue(String name, int row, String item, String value)
            throws Exception {
        int rowcount;
        rowcount = this.getRowCount(name);
        if (rowcount >= row) {
            NodeList nodelist = doc.getElementsByTagName(name);
            Element elm = (Element) nodelist.item(row);
            nodelist = elm.getElementsByTagName(item);

            if (nodelist.getLength() != 0) {
                Node node = nodelist.item(0);
                nodelist = node.getChildNodes();
                if (nodelist.getLength() != 0)
                    node.removeChild(nodelist.item(0));
                node.appendChild(doc.createTextNode(value));
            } else {
                elm = (Element) elm.appendChild(doc.createElement(item));
                elm.appendChild(doc.createTextNode(value));
            }
        } else {
            throw new Exception("增加元素失败");
        }
    }

    public void setItemAttr(String name, int row, String item, String value)
            throws Exception {
        int rowcount;
        rowcount = this.getRowCount(name);
        if (rowcount >= row) {
            NodeList nodelist = doc.getElementsByTagName(name);
            Element elm = (Element) nodelist.item(row);
            nodelist = elm.getElementsByTagName(item);

            if (nodelist.getLength() != 0) {
                Element node = (Element) nodelist.item(0);
                node.setAttribute(item, value);

            } else {
                elm.setAttribute(item, value);
            }
        } else {
            throw new Exception("增加元素失败");
        }
    }

    /**
     * 取得name元素下第row个元素中节点名为item的值
     *
     * @param name
     * @param row
     * @param item
     * @return
     * @throws Exception
     */
    public String getItemValue(String name, int row, String item)
            throws Exception {
        String ItemValue = new String();
        int rowcount = this.getRowCount(name);
        if (rowcount >= row) {
            NodeList nodelist = doc.getElementsByTagName(name);
            Element elm = (Element) nodelist.item(row);
            nodelist = elm.getElementsByTagName(item);
            if (nodelist.getLength() != 0) {
                if (nodelist.item(0).getChildNodes().item(0) == null) {
                    return "";
                } else
                    ItemValue = nodelist.item(0).getChildNodes().item(0)
                            .getNodeValue();
            }
            return ItemValue;
        } else {
            throw new Exception("不存在元素:" + name);
        }
    }

    /**
     * 取得第row个name元素下节点为item的属性值
     *
     * @param name
     * @param row
     * @param item
     * @return
     * @throws Exception
     */
    public String getItemAttrValue(String name, int row, String item,
                                   String attr) throws Exception {
        String ItemValue = new String();
        int rowcount = this.getRowCount(name);
        if (rowcount >= row) {
            NodeList nodelist = doc.getElementsByTagName(name);
            Element elm = (Element) nodelist.item(row);
            nodelist = elm.getElementsByTagName(item);
            if (nodelist.getLength() != 0) {
                if (nodelist.item(0).getChildNodes().item(0) == null) {
                    return "";
                } else {
                    Element el = (Element) nodelist.item(0);
                    ItemValue = el.getAttribute(attr);
                    if (ItemValue == null) {
                        throw new Exception("不存在属性:" + attr);
                    }
                }
            }
            return ItemValue;
        } else {
            throw new Exception("不存在元素:" + name);
        }
    }

    public String getItemAttrValue(String name, int row, String attr)
            throws Exception {
        String ItemValue = new String();
        int rowcount = this.getRowCount(name);
        if (rowcount >= row) {
            NodeList nodelist = doc.getElementsByTagName(name);
            Element elm = (Element) nodelist.item(row);
            ItemValue = elm.getAttribute(attr);
            if (ItemValue == null) {

                throw new Exception("不存在属性:" + attr);

            }
            return ItemValue;
        } else {
            throw new Exception("不存在元素:" + name);
        }
    }

    /**
     * 取得name元素下，所有item节点的的值
     *
     * @param name
     * @param item
     * @return
     * @throws Exception
     */

    public String[] getItemValues(String name, String item) throws Exception {
        int rowcount = this.getRowCount(name);
        String[] values = new String[rowcount];

        for (int i = 0; i < rowcount; i++)
            values[i] = this.getItemValue(name, i, item);

        return values;
    }

    public Map<String, String> getMapItemValues(String name, String item)
            throws Exception {
        NodeList nd = doc.getElementsByTagName(name);
        int rowcount = nd.getLength();
        Map<String, String> values = new HashMap<String, String>();

        for (int i = 0; i < rowcount; i++) {
            // List value = new ArrayList();
            Element chileElement = (Element) nd.item(i);
            NodeList child = chileElement.getElementsByTagName(item);
            for (int p = 0; p < child.getLength(); p++) {
                Node concrectchild = child.item(p);
                for (Node tempnode = concrectchild.getFirstChild(); tempnode != null; tempnode = tempnode
                        .getNextSibling()) {
                    if (tempnode.getNodeType() == Node.ELEMENT_NODE) {
                        if (tempnode.getFirstChild() != null)
                            values.put(tempnode.getNodeName(), tempnode
                                    .getFirstChild().getNodeValue());
                        else
                            values.put(tempnode.getNodeName(), "");
                    }
                }
            }
        }

        return values;
    }

    public Map<String, String> getMapValues(String name) throws Exception {
        NodeList nd = doc.getElementsByTagName(name);
        Map<String, String> values = new HashMap<String, String>();

        for (Node tempnode = nd.item(0).getFirstChild(); tempnode != null; tempnode = tempnode
                .getNextSibling()) {
            if (tempnode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempnode.getFirstChild() != null)
                    values.put(tempnode.getNodeName(), tempnode.getFirstChild()
                            .getNodeValue());
                else
                    values.put(tempnode.getNodeName(), "");
            }
        }

        return values;
    }

    /**
     * 取得name元素的个数
     *
     * @param name
     * @return
     */
    public int getRowCount(String name) {
        int rowcount = 0;

        NodeList nodelist = doc.getElementsByTagName(name);
        if (nodelist != null)
            rowcount = nodelist.getLength();
        else
            rowcount = -1;

        return rowcount;
    }

    /**
     * 取得name元素下，item节上中值为value的个数
     *
     * @param name
     * @param item
     * @param value
     * @return
     * @throws Exception
     */
    public int getRow(String name, String item, String value) throws Exception {
        int rowcount, row = 0;

        rowcount = this.getRowCount(name);
        for (int i = 0; i < rowcount; i++) {
            if (getItemValue(name, i + 1, item).equals(value))
                row = i;
        }

        return row;
    }

    /**
     * This method is used to add a new row by "name".
     *
     * @return the return value is .
     */
    public int addRow(String name) {
        int row = 0;
        NodeList nodelist = doc.getElementsByTagName("report");
        Element elm = (Element) nodelist.item(0);
        elm = (Element) elm.appendChild(doc.createElement(name));
        row = getRowCount(name);
        return row;
    }

    public int addRow(String name, int row, String item, String value)
            throws Exception {
        NodeList nodelist = doc.getElementsByTagName(name);
        if (nodelist.getLength() <= row) {
            throw new Exception("元素:" + name + " 不存在");
        } else {
            Element elm = (Element) nodelist.item(row);
            Element temp = doc.createElement(item);
            temp.appendChild(doc.createTextNode(value));
            elm.appendChild(temp);
        }
        row = getRowCount(name);
        return row;
    }

    /**
     * This method is used to delete a row by row's "name" & "rownum".
     *
     * @return the return value is .
     */
    public int delRow(String name, int row) {
        int rowdel = row;
        if (rowdel < 0)
            return -1;

        NodeList nodelist = doc.getElementsByTagName(name);
        if (nodelist.getLength() < rowdel)
            return -1;

        Node node = nodelist.item(rowdel);
        node.getParentNode().removeChild(node);

        return rowdel;
    }

    /**
     * This method is delete all row whose name is "name"
     */
    public int delRow(String name) {
        int iDelSum = 0;
        int iRowCount = getRowCount(name);

        for (int i = 0; i < iRowCount; i++) {
            delRow(name, 1);
            iDelSum++;
        }
        return iDelSum;
    }
}