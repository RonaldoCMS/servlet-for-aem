package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallAPI {
	
    private static final Logger logger =  LoggerFactory.getLogger(CallAPI.class); 
    private String url;
    private String METHOD;
    private Map<String, String> header;
    private HttpURLConnection con;
    
    public void send(String METHOD, String url, Map<String, String> header) throws MalformedURLException, ProtocolException, IOException {
    	setMETHOD(METHOD);
    	setUrl(url);
    	setHeader(header);
    	send();
    }
    
	public void send() throws MalformedURLException, ProtocolException, IOException {
    	URL obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod(METHOD);
		logger.info("openConnection URL :: {}", url);
		logger.info("openConnection METHOD :: {}", METHOD);
		insertHeader();
    }
    
    private void insertHeader() {
    	if(header != null && !header.isEmpty()) {
    		Iterator<Entry<String, String>> it = header.entrySet().iterator();
    		while(it.hasNext()) {
    			Entry<String, String> entry = it.next();
    			String key = entry.getKey();
    			String value = entry.getValue();
    			con.setRequestProperty(key, value);
    			logger.info("[Header] key: {}, value: {}", key, value);
    		}
    	} else {
    		logger.info("[Header] empty");
    	}
    }
    
    public String getBody() throws IOException  {
    	 BufferedReader br = null;
    	 String body = "";
		 logger.info("[HTTP] response :: {}", statusCode());
		 if (statusCode() == 200) {
		     br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		         body = updateBody(br, body);
		 } else {
		     br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		         body = updateBody(br, body);
		 }
		 br.close();
		 return body;
    }

	private String updateBody(BufferedReader br, String body) throws IOException {
		String strCurrentLine;
		while ((strCurrentLine = br.readLine()) != null) {
		        body += strCurrentLine;
		 }
		return body;
	}
    
    public int statusCode() throws IOException {
    	return con.getResponseCode();
    }
    
    public void setCon(HttpURLConnection con) {
		this.con = con;
	}
    
    public void setHeader(Map<String, String> header) {
		this.header = header;
	}
    
    public void setMETHOD(String mETHOD) {
		METHOD = mETHOD;
	}
    
    public void setUrl(String url) {
		this.url = url;
	}
}
