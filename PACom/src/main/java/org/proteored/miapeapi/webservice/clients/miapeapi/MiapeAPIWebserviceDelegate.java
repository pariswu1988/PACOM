package org.proteored.miapeapi.webservice.clients.miapeapi;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "MiapeAPIWebserviceDelegate", targetNamespace = "http://webservice.miapeapi.proteored.org/")
public interface MiapeAPIWebserviceDelegate {

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@RequestWrapper(localName = "deleteProject", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteProject")
	@ResponseWrapper(localName = "deleteProjectResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteProjectResponse")
	public void deleteProject(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns int
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getUserId", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetUserId")
	@ResponseWrapper(localName = "getUserIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetUserIdResponse")
	public int getUserId(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.util.List<org.proteored.miapeapi.webservice.clients.
	 *         miapeapi.IntegerString>
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getAllProjects", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetAllProjects")
	@ResponseWrapper(localName = "getAllProjectsResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetAllProjectsResponse")
	public List<IntegerString> getAllProjects(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns boolean
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "projectExists", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.ProjectExists")
	@ResponseWrapper(localName = "projectExistsResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.ProjectExistsResponse")
	public boolean projectExists(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2,
			@WebParam(name = "arg3", targetNamespace = "") String arg3)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns int
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "storeMiapeGE", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreMiapeGE")
	@ResponseWrapper(localName = "storeMiapeGEResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreMiapeGEResponse")
	public int storeMiapeGE(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") byte[] arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeGEById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGEById")
	@ResponseWrapper(localName = "getMiapeGEByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGEByIdResponse")
	public byte[] getMiapeGEById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.util.List<org.proteored.miapeapi.webservice.clients.
	 *         miapeapi.IntegerString>
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeGEsByName", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGEsByName")
	@ResponseWrapper(localName = "getMiapeGEsByNameResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGEsByNameResponse")
	public List<IntegerString> getMiapeGEsByName(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2,
			@WebParam(name = "arg3", targetNamespace = "") String arg3)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeGEHeaderById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGEHeaderById")
	@ResponseWrapper(localName = "getMiapeGEHeaderByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGEHeaderByIdResponse")
	public byte[] getMiapeGEHeaderById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@RequestWrapper(localName = "deleteMiapeGE", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteMiapeGE")
	@ResponseWrapper(localName = "deleteMiapeGEResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteMiapeGEResponse")
	public void deleteMiapeGE(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns int
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "storeMiapeGI", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreMiapeGI")
	@ResponseWrapper(localName = "storeMiapeGIResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreMiapeGIResponse")
	public int storeMiapeGI(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") byte[] arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeGIById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGIById")
	@ResponseWrapper(localName = "getMiapeGIByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGIByIdResponse")
	public byte[] getMiapeGIById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.util.List<org.proteored.miapeapi.webservice.clients.
	 *         miapeapi.IntegerString>
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeGIsByName", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGIsByName")
	@ResponseWrapper(localName = "getMiapeGIsByNameResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGIsByNameResponse")
	public List<IntegerString> getMiapeGIsByName(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2,
			@WebParam(name = "arg3", targetNamespace = "") String arg3)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeGIHeaderById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGIHeaderById")
	@ResponseWrapper(localName = "getMiapeGIHeaderByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeGIHeaderByIdResponse")
	public byte[] getMiapeGIHeaderById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@RequestWrapper(localName = "deleteMiapeGI", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteMiapeGI")
	@ResponseWrapper(localName = "deleteMiapeGIResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteMiapeGIResponse")
	public void deleteMiapeGI(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns int
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "storeMiapeMS", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreMiapeMS")
	@ResponseWrapper(localName = "storeMiapeMSResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreMiapeMSResponse")
	public int storeMiapeMS(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") byte[] arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeMSHeaderById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSHeaderById")
	@ResponseWrapper(localName = "getMiapeMSHeaderByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSHeaderByIdResponse")
	public byte[] getMiapeMSHeaderById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeMSIHeaderById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSIHeaderById")
	@ResponseWrapper(localName = "getMiapeMSIHeaderByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSIHeaderByIdResponse")
	public byte[] getMiapeMSIHeaderById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeMSById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSById")
	@ResponseWrapper(localName = "getMiapeMSByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSByIdResponse")
	public byte[] getMiapeMSById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.util.List<org.proteored.miapeapi.webservice.clients.
	 *         miapeapi.IntegerString>
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeMSsByName", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSsByName")
	@ResponseWrapper(localName = "getMiapeMSsByNameResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSsByNameResponse")
	public List<IntegerString> getMiapeMSsByName(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2,
			@WebParam(name = "arg3", targetNamespace = "") String arg3)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@RequestWrapper(localName = "deleteMiapeMS", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteMiapeMS")
	@ResponseWrapper(localName = "deleteMiapeMSResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteMiapeMSResponse")
	public void deleteMiapeMS(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns int
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "storeMiapeMSI", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreMiapeMSI")
	@ResponseWrapper(localName = "storeMiapeMSIResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreMiapeMSIResponse")
	public int storeMiapeMSI(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") byte[] arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeMSIById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSIById")
	@ResponseWrapper(localName = "getMiapeMSIByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSIByIdResponse")
	public byte[] getMiapeMSIById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.util.List<org.proteored.miapeapi.webservice.clients.
	 *         miapeapi.IntegerString>
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapeMSIsByName", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSIsByName")
	@ResponseWrapper(localName = "getMiapeMSIsByNameResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapeMSIsByNameResponse")
	public List<IntegerString> getMiapeMSIsByName(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2,
			@WebParam(name = "arg3", targetNamespace = "") String arg3)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@RequestWrapper(localName = "deleteMiapeMSI", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteMiapeMSI")
	@ResponseWrapper(localName = "deleteMiapeMSIResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.DeleteMiapeMSIResponse")
	public void deleteMiapeMSI(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg1
	 * @param arg0
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@RequestWrapper(localName = "checkUserCredentials", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.CheckUserCredentials")
	@ResponseWrapper(localName = "checkUserCredentialsResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.CheckUserCredentialsResponse")
	public void checkUserCredentials(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg5
	 * @param arg4
	 * @param arg3
	 * @param arg2
	 * @param arg23
	 * @param arg1
	 * @param arg22
	 * @param arg0
	 * @param arg21
	 * @param arg20
	 * @param arg19
	 * @param arg17
	 * @param arg18
	 * @param arg15
	 * @param arg16
	 * @param arg13
	 * @param arg14
	 * @param arg11
	 * @param arg12
	 * @param arg10
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @return
	 *         returns int
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "saveUser", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.SaveUser")
	@ResponseWrapper(localName = "saveUserResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.SaveUserResponse")
	public int saveUser(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2,
			@WebParam(name = "arg3", targetNamespace = "") Integer arg3,
			@WebParam(name = "arg4", targetNamespace = "") String arg4,
			@WebParam(name = "arg5", targetNamespace = "") String arg5,
			@WebParam(name = "arg6", targetNamespace = "") String arg6,
			@WebParam(name = "arg7", targetNamespace = "") Integer arg7,
			@WebParam(name = "arg8", targetNamespace = "") String arg8,
			@WebParam(name = "arg9", targetNamespace = "") String arg9,
			@WebParam(name = "arg10", targetNamespace = "") String arg10,
			@WebParam(name = "arg11", targetNamespace = "") String arg11,
			@WebParam(name = "arg12", targetNamespace = "") String arg12,
			@WebParam(name = "arg13", targetNamespace = "") String arg13,
			@WebParam(name = "arg14", targetNamespace = "") String arg14,
			@WebParam(name = "arg15", targetNamespace = "") String arg15,
			@WebParam(name = "arg16", targetNamespace = "") String arg16,
			@WebParam(name = "arg17", targetNamespace = "") String arg17,
			@WebParam(name = "arg18", targetNamespace = "") String arg18,
			@WebParam(name = "arg19", targetNamespace = "") String arg19,
			@WebParam(name = "arg20", targetNamespace = "") XMLGregorianCalendar arg20,
			@WebParam(name = "arg21", targetNamespace = "") String arg21,
			@WebParam(name = "arg22", targetNamespace = "") String arg22,
			@WebParam(name = "arg23", targetNamespace = "") String arg23)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg4
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns int
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "storeProject", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreProject")
	@ResponseWrapper(localName = "storeProjectResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.StoreProjectResponse")
	public int storeProject(@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") XMLGregorianCalendar arg2,
			@WebParam(name = "arg3", targetNamespace = "") String arg3,
			@WebParam(name = "arg4", targetNamespace = "") String arg4)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns byte[]
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getProjectById", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetProjectById")
	@ResponseWrapper(localName = "getProjectByIdResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetProjectByIdResponse")
	public byte[] getProjectById(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.util.List<org.proteored.miapeapi.webservice.clients.
	 *         miapeapi.IntegerString>
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapesByProjectID", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapesByProjectID")
	@ResponseWrapper(localName = "getMiapesByProjectIDResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapesByProjectIDResponse")
	public List<IntegerString> getMiapesByProjectID(
			@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.util.List<org.proteored.miapeapi.webservice.clients.
	 *         miapeapi.IntegerString>
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getProjectsByName", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetProjectsByName")
	@ResponseWrapper(localName = "getProjectsByNameResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetProjectsByNameResponse")
	public List<IntegerString> getProjectsByName(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2,
			@WebParam(name = "arg3", targetNamespace = "") String arg3)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.util.List<org.proteored.miapeapi.webservice.clients.
	 *         miapeapi.IntegerString>
	 * @throws MiapeDatabaseException_Exception
	 * @throws MiapeSecurityException_Exception
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getAllMiapes", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetAllMiapes")
	@ResponseWrapper(localName = "getAllMiapesResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetAllMiapesResponse")
	public List<IntegerString> getAllMiapes(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2)
			throws MiapeDatabaseException_Exception, MiapeSecurityException_Exception;

	/**
	 * 
	 * @param arg3
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.lang.String
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getMiapePermissions", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapePermissions")
	@ResponseWrapper(localName = "getMiapePermissionsResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetMiapePermissionsResponse")
	public String getMiapePermissions(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2,
			@WebParam(name = "arg3", targetNamespace = "") String arg3);

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 * @return
	 *         returns java.lang.String
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getProjectPermissions", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetProjectPermissions")
	@ResponseWrapper(localName = "getProjectPermissionsResponse", targetNamespace = "http://webservice.miapeapi.proteored.org/", className = "org.proteored.miapeapi.webservice.clients.miapeapi.GetProjectPermissionsResponse")
	public String getProjectPermissions(@WebParam(name = "arg0", targetNamespace = "") int arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2);

}