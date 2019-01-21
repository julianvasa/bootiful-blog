package al.recipes.soap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class SoapConfiguration implements ApplicationListener<ApplicationReadyEvent> {
    private int webServerPort = 0;
    private String host = "http://localhost";
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("categories.wsdl");
        return marshaller;
    }
    
    @Bean
    public SoapClient categoriesClient(Jaxb2Marshaller marshaller) throws UnknownHostException {
        String urlConn = "http://" + InetAddress.getLocalHost().getHostName();
        SoapClient client = new SoapClient();
        client.setDefaultUri(urlConn + ":" + webServerPort + "/ws/categories");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            String hostName = InetAddress.getLocalHost().getHostAddress();
            int port = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
            webServerPort = port;
            host = hostName;
            System.out.println(webServerPort + " " + host);
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
