package org.proteored.miapeapi.webservice.clients.miapeapi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * <p>
 * An example of how this class may be used:
 * 
 * <pre>
 * MiapeAPIWebserviceService service = new MiapeAPIWebserviceService();
 * MiapeAPIWebserviceDelegate portType = service.getMiapeAPIWebservicePort();
 * portType.deleteProject(...);
 * </pre>
 * 
 * </p>
 * 
 */
@WebServiceClient(name = "MiapeAPIWebserviceService", targetNamespace = "http://webservice.miapeapi.proteored.org/", wsdlLocation = "file:/C:/Users/Salva/Workspaces/EclipseClassic/miape-api-webservice/WebRoot/WEB-INF/wsdl/MiapeAPIWebserviceService.wsdl")
public class MiapeAPIWebserviceService extends Service {

	private final static URL MIAPEAPIWEBSERVICESERVICE_WSDL_LOCATION;
	private final static Logger logger = Logger
			.getLogger(org.proteored.miapeapi.webservice.clients.miapeapi.MiapeAPIWebserviceService.class
					.getName());

	static {
		URL url = null;
		try {
			URL baseUrl;
			baseUrl = org.proteored.miapeapi.webservice.clients.miapeapi.MiapeAPIWebserviceService.class
					.getResource(".");
			url = new URL(
					baseUrl,
					"file:/C:/Users/Salva/Workspaces/EclipseClassic/miape-api-webservice/WebRoot/WEB-INF/wsdl/MiapeAPIWebserviceService.wsdl");
		} catch (MalformedURLException e) {
			logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/Users/Salva/Workspaces/EclipseClassic/miape-api-webservice/WebRoot/WEB-INF/wsdl/MiapeAPIWebserviceService.wsdl', retrying as a local file");
			logger.warning(e.getMessage());
		}
		MIAPEAPIWEBSERVICESERVICE_WSDL_LOCATION = url;
	}

	public MiapeAPIWebserviceService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public MiapeAPIWebserviceService() {
		super(MIAPEAPIWEBSERVICESERVICE_WSDL_LOCATION, new QName(
				"http://webservice.miapeapi.proteored.org/", "MiapeAPIWebserviceService"));
	}

	/**
	 * 
	 * @return
	 *         returns MiapeAPIWebserviceDelegate
	 */
	@WebEndpoint(name = "MiapeAPIWebservicePort")
	public MiapeAPIWebserviceDelegate getMiapeAPIWebservicePort() {
		return super.getPort(new QName("http://webservice.miapeapi.proteored.org/",
				"MiapeAPIWebservicePort"), MiapeAPIWebserviceDelegate.class);
	}

}