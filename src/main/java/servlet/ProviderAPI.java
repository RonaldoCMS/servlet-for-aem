package servlet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import servlet.exp.ProviderParameterInvalid;

enum TypeProvider {
	PARAMETERS, POST_BODY, POST_HEADER
}

public class ProviderAPI {
	private static final Logger logger =  LoggerFactory.getLogger(ProviderAPI.class); 
	private String urlProvider;
	private String path;
	
	private static ProviderAPI INSTANCE;
	
	public static ProviderAPI getInstance() {
		if(INSTANCE == null) return new ProviderAPI();
		return INSTANCE;
	}
	
	public ProviderAPI(String urlProvider, String path) {
		this.urlProvider = urlProvider;
		this.path = path;
	}
	
	private void setProviderAPI(ProviderAPI newProvider) throws NullPointerException, ProviderParameterInvalid {
		if(newProvider == null) throw new NullPointerException("new provider is null");
 		this.urlProvider = newProvider.getUrlProvider();
		this.path = newProvider.getPath();
		parametersNotValid();
	}
	
	private ProviderAPI() {}
	
	public void setProvider(HttpServletRequest request, TypeProvider type) throws Exception {
		logger.info("Type provider: {}", type);
		switch(type) {
			case PARAMETERS:
				initialFromParameter(request);
				break;
			case POST_BODY:
				initialFromBody(request);
				break;
			case POST_HEADER:
				initialFromHeader(request);
				break;
			default: 
				throw new Exception("exception not catched");
		}
	}
	
	private void initialFromParameter(HttpServletRequest request) throws ProviderParameterInvalid {
		this.urlProvider = request.getParameter("urlProvider");
		this.path = request.getParameter("path");
		parametersNotValid();
	}
	
	private void initialFromHeader(HttpServletRequest request) throws ProviderParameterInvalid {
		this.urlProvider = request.getHeader("urlProvider");
		this.path = request.getHeader("path");
		parametersNotValid();
	}
	
	private void initialFromBody(HttpServletRequest request) throws ProviderParameterInvalid, JsonMappingException, JsonProcessingException, NullPointerException, IOException {
	    ObjectMapper objectMapper = new ObjectMapper();
	    setProviderAPI(objectMapper.readValue(ServletUtil.getBodyFromRequest(request), ProviderAPI.class));
	}
	
	private void parametersNotValid() throws ProviderParameterInvalid {
		if(urlProvider == null || urlProvider.isEmpty() || urlProvider.isBlank()) {
			throw new ProviderParameterInvalid("urlProvider empty or null");
		} else if(path == null || path.isEmpty() || path.isBlank()) {
			throw new ProviderParameterInvalid("path empty or null");

		}
	}
	
	public String getPath() {
		return path;
	}
	
	public String getUrlProvider() {
		return urlProvider;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setUrlProvider(String urlProvider) {
		this.urlProvider = urlProvider;
	}
	
	public String getCompleteUrl() {
		return "http://"+urlProvider+path;
	}

	@Override
	public String toString() {
		return "ProviderAPI [urlProvider=" + urlProvider + ", path=" + path + "]";
	}
	
}
