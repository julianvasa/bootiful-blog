package al.recipes.soap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class SoapConfiguration {
    @Value("${local.server.port}")
    private int serverPort;
    
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("categories.wsdl");
        return marshaller;
    }
    
    @Bean
    public SoapClient categoriesClient(Jaxb2Marshaller marshaller) throws UnknownHostException {
        String urlConn = "http://" + InetAddress.getLocalHost().getHostName();
        System.out.println("JULIIIII " + serverPort);
        SoapClient client = new SoapClient();
        client.setDefaultUri(urlConn + "/ws/categories");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
    
}
