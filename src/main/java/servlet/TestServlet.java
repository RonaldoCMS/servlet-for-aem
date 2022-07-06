package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
	/*
	 * microTask
	 * [X] Creare una classe ProviderAPI che inizializza i parametri procurarti dalla servlet.
	 * [X] Creare una classe xxx che permette di fare una chiamata API
	 * [X] creare un servizio che associ una chiamata API con il provider
	da capire come 
	
	*/
	private static final long serialVersionUID = 1L;
    private static final Logger logger =  LoggerFactory.getLogger(TestServlet.class); 
	private ProviderAPI provider = ProviderAPI.getInstance();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		try {
			provider.setProvider(req, TypeProvider.POST_BODY);
			ServiceAPI service = new ServiceAPI(req, provider);
			if(service.send("GET")) {
				writer.print(service.getBody());
			} else {
				logger.error("send METHOD :: POST not work");
				resp.setStatus(500);
				writer.print("{\"error\": \"error server\"}");
			}
		} catch (Exception e) {
			resp.setStatus(405);
			logger.error("servlet stopped - statusCode :: {}", resp.getStatus());
			writer.print("{\"error\": \"1 or more parameters not valid\"}");
			writer.flush();
		}

		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		try {
			provider.setProvider(req, TypeProvider.PARAMETERS);
			ServiceAPI service = new ServiceAPI(req, provider);
			if(service.send("GET")) {
				writer.print(service.getBody());
			} else {
				logger.error("send METHOD :: POST not work");
				resp.setStatus(500);
				writer.print("{\"error\": \"error server\"}");
			}
		} catch (Exception e) {
			resp.setStatus(405);
			logger.error("servlet stopped - statusCode :: {}", resp.getStatus());
			writer.print("{\"error\": \"1 or more parameters not valid\"}");
			writer.flush();
		}
	}
}
