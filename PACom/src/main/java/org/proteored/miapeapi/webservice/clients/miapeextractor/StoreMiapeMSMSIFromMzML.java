package org.proteored.miapeapi.webservice.clients.miapeextractor;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for storeMiapeMSMSIFromMzML complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="storeMiapeMSMSIFromMzML">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="arg1" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="arg2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="arg3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="arg4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="arg5" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="arg6" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "storeMiapeMSMSIFromMzML", propOrder = { "arg0", "arg1",
		"arg2", "arg3", "arg4", "arg5", "arg6" })
public class StoreMiapeMSMSIFromMzML {

	protected String arg0;
	@XmlElementRef(name = "arg1", type = JAXBElement.class)
	protected JAXBElement<byte[]> arg1;
	protected String arg2;
	protected String arg3;
	protected String arg4;
	protected boolean arg5;
	protected boolean arg6;

	/**
	 * Gets the value of the arg0 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getArg0() {
		return arg0;
	}

	/**
	 * Sets the value of the arg0 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setArg0(String value) {
		this.arg0 = value;
	}

	/**
	 * Gets the value of the arg1 property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link byte[]}
	 *         {@code >}
	 * 
	 */
	public JAXBElement<byte[]> getArg1() {
		return arg1;
	}

	/**
	 * Sets the value of the arg1 property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link byte[]}
	 *            {@code >}
	 * 
	 */
	public void setArg1(JAXBElement<byte[]> value) {
		this.arg1 = ((JAXBElement<byte[]>) value);
	}

	/**
	 * Gets the value of the arg2 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getArg2() {
		return arg2;
	}

	/**
	 * Sets the value of the arg2 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setArg2(String value) {
		this.arg2 = value;
	}

	/**
	 * Gets the value of the arg3 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getArg3() {
		return arg3;
	}

	/**
	 * Sets the value of the arg3 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setArg3(String value) {
		this.arg3 = value;
	}

	/**
	 * Gets the value of the arg4 property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getArg4() {
		return arg4;
	}

	/**
	 * Sets the value of the arg4 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setArg4(String value) {
		this.arg4 = value;
	}

	/**
	 * Gets the value of the arg5 property.
	 * 
	 */
	public boolean isArg5() {
		return arg5;
	}

	/**
	 * Sets the value of the arg5 property.
	 * 
	 */
	public void setArg5(boolean value) {
		this.arg5 = value;
	}

	/**
	 * Gets the value of the arg6 property.
	 * 
	 */
	public boolean isArg6() {
		return arg6;
	}

	/**
	 * Sets the value of the arg6 property.
	 * 
	 */
	public void setArg6(boolean value) {
		this.arg6 = value;
	}

}