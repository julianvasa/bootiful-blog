package al.recipes.controllers;

import al.recipes.models.Recipes;
import al.recipes.models.Tags;
import al.recipes.rest.controllers.TagControllerRest;
import al.recipes.soap.SoapClient;
import categories.wsdl.Categories;
import categories.wsdl.GetCategoriesResponse;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {
    
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private SoapClient categorySoapClient;
    @Autowired
    private TagControllerRest tagControllerRest;
    @Autowired
    private MessageSource messageSource;
    
    @GetMapping(value = {"/", "/{page:[0-9]+}", "/{page:[0-9]+}/cat/{cat}", "/search/{keyword}"})
    @ApiOperation(value = "Get recipes by page / by page and by cat / search", notes = "Get recipes by page / by page and by cat / search")
    public String getAllRecipes(Model model, @PathVariable(value = "page") Optional<Integer> p, @PathVariable Optional<Integer> cat,
                                @PathVariable(value = "keyword") Optional<String> searchFilter, Locale locale) {
        
        final String header_txt = messageSource.getMessage("header_txt", null, locale);
        final String search_results = messageSource.getMessage("search_results", null, locale);
        
        List<Recipes> recipes = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        List<Categories> categories = new ArrayList<>();
        List<Recipes> recent_recipes = new ArrayList<>();
        ResponseEntity<PagedResources<Recipes>> response = null;
        ResponseEntity<List<Recipes>> response_list = null;
        RestTemplate restTemplate = new RestTemplate();
        GetCategoriesResponse soapResponse = categorySoapClient.getCategories();
        categories.addAll(soapResponse.getCategories());
        Categories currentCat = null;
        Integer page = 1;
        if (p.isPresent()) page = p.get();
        String main_url = "https://bootiful-blog.herokuapp.com/api/recipes/" + page;
        String next_url = "https://bootiful-blog.herokuapp.com/api/recipes/" + (page + 1);
        String link_more = "/" + (page + 1);
        String tags_url = "https://bootiful-blog.herokuapp.com/api/tags";
        String recent_url = "https://bootiful-blog.herokuapp.com/api/recipes/1";
        
        if (cat.isPresent()) {
            main_url = "https://bootiful-blog.herokuapp.com/api/recipes/" + page + "/cat/" + cat.get();
            next_url = "https://bootiful-blog.herokuapp.com/api/recipes/" + (page + 1) + "/cat/" + cat.get();
            link_more = "/" + (page + 1) + "/cat/" + cat.get();
            currentCat = soapResponse.getCategories().stream().filter(c -> c.getId() == cat.get()).findFirst().get();
        }
        
        if (searchFilter.isPresent()) {
            main_url = "https://bootiful-blog.herokuapp.com/api/search/" + searchFilter.get();
            next_url = "";
            link_more = "#";
            currentCat = null;
            
            response_list =
                    restTemplate.exchange(main_url,
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Recipes>>() {
                            }
                    );
            Collection<Recipes> recipes_arr = Objects.requireNonNull(response_list.getBody());
            recipes_arr.stream().limit(20).forEach(r -> {
                r = setTagsIntro(r);
                recipes.add(r);
            });
            
        } else {
            
            response =
                    restTemplate.exchange(main_url,
                            HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Recipes>>() {
                            }
                    );
            Collection<Recipes> recipes_arr = Objects.requireNonNull(response.getBody()).getContent();
            recipes_arr.stream().limit(20).forEach(r -> {
                r = setTagsIntro(r);
                recipes.add(r);
            });
            
        }
        
        
        ResponseEntity<PagedResources<Recipes>> recent_response =
                restTemplate.exchange(recent_url,
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Recipes>>() {
                        }
                );
        Collection<Recipes> recent_recipes_arr = Objects.requireNonNull(recent_response.getBody()).getContent();
        recent_recipes_arr.stream().limit(10).forEach(recent_recipes::add);
        
        ResponseEntity<List<String>> tags_response =
                restTemplate.exchange(tags_url,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
                        }
                );
        Collection<String> tags_arr = Objects.requireNonNull(tags_response.getBody());
        tags_arr.stream().limit(20).forEach(tags::add);
        
        if (!next_url.isEmpty()) {
            ResponseEntity<PagedResources<Recipes>> response_next =
                    restTemplate.exchange(next_url,
                            HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Recipes>>() {
                            }
                    );
            
            if (Objects.requireNonNull(response_next.getBody()).getContent().size() > 0) {
                model.addAttribute("link_more", link_more);
            } else {
                model.addAttribute("link_more", "#");
            }
        } else {
            model.addAttribute("link_more", "#");
        }
        if (currentCat != null) {
            model.addAttribute("currentCat", currentCat);
        }
        
        searchFilter.ifPresent(s -> model.addAttribute("searchFilter", s));
        
        model.addAttribute("recent", recent_recipes);
        model.addAttribute("tagCloud", tags);
        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        if (searchFilter.isPresent()) {
            model.addAttribute("page_title", search_results + " " + searchFilter.get());
        } else if (currentCat != null) {
            model.addAttribute("page_title", currentCat.getName());
        } else {
            model.addAttribute("page_title", header_txt);
        }
        return "home";
    }
    
    private Recipes setTagsIntro(Recipes p) {
        RestTemplate restTemplate = new RestTemplate();
        Recipes recipe = restTemplate.getForObject("https://bootiful-blog.herokuapp.com/api/recipe/" + p.getId(), Recipes.class, 200);
        List<Tags> tags = Objects.requireNonNull(recipe).getTags();
        tags.sort(Comparator.comparingInt(Tags::getStart_pos));
        
        int start_pos = 0;
        int start_pos2 = 0;
        for (Tags tag : tags) {
            if (tag.getIntro_instruction().equals("intro")) {
                StringBuilder ins = new StringBuilder(p.getIntro());
                if (!ins.substring(tag.getStart_pos() + start_pos, tag.getEnd_pos() + start_pos).contains("<a") &&
                        !ins.substring(tag.getStart_pos() + start_pos2, tag.getEnd_pos() + start_pos2).contains("<a")) {
                    
                    p.setIntro(ins.replace(
                            (tag.getStart_pos() + start_pos),
                            (tag.getEnd_pos() + start_pos),
                            "<a class='tags' href='/search/" + tag.getValue() + "'>" + tag.getValue() + "</a>"
                    ).toString());
                    start_pos2 = start_pos;
                    start_pos += ("<a class='tags' href='/search/" + tag.getValue() + "'>" + "</a>").length();
                }
            }
        }
        return p;
    }

    /*private String translate(String txtToBeTranslated) {
        String txt = txtToBeTranslated;
        if (LocaleContextHolder.getLocale().toString().equals("en")) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity("https://www.googleapis.com/language/translate/" +
                    "v2?key=AIzaSyCbxc7thCFh8PQ9fmNJ_171bO3-jy_euqA&source=sq&target=en&q=" + txtToBeTranslated, String.class);

            txt = response.getBody().replace("{\n" +
                    "  \"data\": {\n" +
                    "    \"translations\": [\n" +
                    "      {\n" +
                    "        \"translatedText\": \"", "").replace("\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}", "");
        }
        if (txt.length() > 100) return txt.substring(0, 100) + "...";
        return txt;
    }*/
}

