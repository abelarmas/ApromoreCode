
package org.wfmc._2008.xpdl2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.wfmc.org/2008/XPDL2.1}Category" maxOccurs="unbounded"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "category",
    "any"
})
@XmlRootElement(name = "Categories")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2010-04-22T11:04:04+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-833")
public class Categories {

    @XmlElement(name = "Category", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-04-22T11:04:04+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-833")
    protected List<Category> category;
    @XmlAnyElement(lax = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-04-22T11:04:04+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-833")
    protected List<java.lang.Object> any;
    @XmlAnyAttribute
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-04-22T11:04:04+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-833")
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the category property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the category property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Category }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-04-22T11:04:04+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-833")
    public List<Category> getCategory() {
        if (category == null) {
            category = new ArrayList<Category>();
        }
        return this.category;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link java.lang.Object }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-04-22T11:04:04+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-833")
    public List<java.lang.Object> getAny() {
        if (any == null) {
            any = new ArrayList<java.lang.Object>();
        }
        return this.any;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-04-22T11:04:04+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-833")
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}