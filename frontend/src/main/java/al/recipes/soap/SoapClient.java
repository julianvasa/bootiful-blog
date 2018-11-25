package al.recipes.soap;

import categories.wsdl.GetCategoriesRequest;
import categories.wsdl.GetCategoriesResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SoapClient extends WebServiceGatewaySupport {

    public GetCategoriesResponse getCategories() {
        GetCategoriesRequest request = new GetCategoriesRequest();
        return (GetCategoriesResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }
}