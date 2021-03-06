/*
 * Copyright © 2009-2016 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.11.03 at 05:04:23 PM CET 
//

package org.yawlfoundation.yawlschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for LayoutLabelFactsType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="LayoutLabelFactsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userobject" type="{http://www.yawlfoundation.org/yawlschema}LayoutUserObjectHTMLType" minOccurs="0"/>
 *         &lt;element name="attributes" type="{http://www.yawlfoundation.org/yawlschema}LayoutAttributesFactsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LayoutLabelFactsType", propOrder = { "userobject",
		"attributes" })
public class LayoutLabelFactsType {

	protected LayoutUserObjectHTMLType userobject;
	@XmlElement(required = true)
	protected LayoutAttributesFactsType attributes;

	/**
	 * Gets the value of the userobject property.
	 * 
	 * @return possible object is {@link LayoutUserObjectHTMLType }
	 * 
	 */
	public LayoutUserObjectHTMLType getUserobject() {
		return userobject;
	}

	/**
	 * Sets the value of the userobject property.
	 * 
	 * @param value
	 *            allowed object is {@link LayoutUserObjectHTMLType }
	 * 
	 */
	public void setUserobject(LayoutUserObjectHTMLType value) {
		this.userobject = value;
	}

	/**
	 * Gets the value of the attributes property.
	 * 
	 * @return possible object is {@link LayoutAttributesFactsType }
	 * 
	 */
	public LayoutAttributesFactsType getAttributes() {
		return attributes;
	}

	/**
	 * Sets the value of the attributes property.
	 * 
	 * @param value
	 *            allowed object is {@link LayoutAttributesFactsType }
	 * 
	 */
	public void setAttributes(LayoutAttributesFactsType value) {
		this.attributes = value;
	}

}
