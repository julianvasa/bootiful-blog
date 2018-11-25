package al.recipes.endpoint;

import al.recipes.categories.GetCategoriesRequest;
import al.recipes.categories.GetCategoriesResponse;
import al.recipes.models.Categories;
import al.recipes.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Endpoint
public class CategoriesEndpoint {
    private static final String NAMESPACE_URI = "categories.recipes.al";
    @Autowired
    private CategoriesService categoriesService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCategoriesRequest")
    @ResponsePayload
    public GetCategoriesResponse GetCategoriesResponse(@RequestPayload GetCategoriesRequest request) {
        GetCategoriesResponse response = new GetCategoriesResponse();
        List<Categories> uu = categoriesService.findAll();
        List<al.recipes.categories.Categories> uuu = new ArrayList<>();

        uu.sort(Comparator.comparingInt(Categories::getCount).reversed());
        uu.forEach(p ->
                {
                    al.recipes.categories.Categories ppp = new al.recipes.categories.Categories();
                    ppp.setId(p.getId());
                    ppp.setCount(p.getCount());
                    ppp.setImage(p.getImage());
                    ppp.setName(p.getName());
                    uuu.add(ppp);
                }
        );
        response.setCategories(uuu);

        return response;
    }
}