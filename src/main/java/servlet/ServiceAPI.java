package servlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class ServiceAPI {
	private static final Logger logger =  LoggerFactory.getLogger(ProviderAPI.class); 
	private CallAPI callAPI;
	private ProviderAPI providerAPI;
	
	public ServiceAPI(HttpServletRequest request, ProviderAPI providerAPI) {
		this.providerAPI = providerAPI;
		callAPI = new CallAPI();
	}
	
	public boolean send(String METHOD)  {
		try {
			callAPI.send(METHOD, providerAPI.getCompleteUrl(), null);
			return true;
		} catch (MalformedURLException e) {
			logger.error("Error connect with UrlAPI: {}", e.getMessage());
		} catch (ProtocolException e) {
			logger.error("Error connect with ProcotollAPI: {}", e.getMessage());
		} catch (IOException e) {
			logger.error("Error not catched in send");
		}	
		logger.info("Send METHOD :: {}", METHOD);
		return false;
	}
	
	public int statusCode() {
		try {
			int status = callAPI.statusCode();
			logger.info("Status code {}", status);
			return status;
		} catch (IOException e) {
			logger.error("Status code error {}", -1);
			return -1;
		}
	}
	
	public String getBody() {
		if(statusCode() != -1) {
			try {
				return callAPI.getBody();
			} catch (IOException e) {
				logger.info("body exception {}", e.getMessage());
				return null;
			}
		}
		return null;
	}
	
}
