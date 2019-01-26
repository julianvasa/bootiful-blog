package al.recipes.controllers

import al.recipes.models.Recipes
import al.recipes.soap.SoapClient
import categories.wsdl.Categories
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.hateoas.PagedResources
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/")
class HomeController {
    @Autowired
    private val categorySoapClient: SoapClient? = null
    @Autowired
    private val messageSource: MessageSource? = null

    @GetMapping(value = arrayOf("/", "/{page:[0-9]+}", "/{page:[0-9]+}/cat/{cat}", "/search/{keyword}"))
    @ApiOperation(value = "Get recipes by page / by page and by cat / search", notes = "Get recipes by page / by page and by cat / search")
    fun getAllRecipes(model: Model, @PathVariable(value = "page") p: Optional<Int>, @PathVariable cat: Optional<Int>,
                      @PathVariable(value = "keyword") searchFilter: Optional<String>, locale: Locale, request: HttpServletRequest): String {

        val header_txt = messageSource!!.getMessage("header_txt", null, locale)
        val search_results = messageSource.getMessage("search_results", null, locale)
        val https = request.scheme as String
        val urlConn = https + "://" + request.serverName

        val recipes = ArrayList<Recipes>()
        val tags = ArrayList<String>()
        val categories = ArrayList<Categories>()
        val recent_recipes = ArrayList<Recipes>()
        var response: ResponseEntity<PagedResources<Recipes>>
        var response_list: ResponseEntity<List<Recipes>>
        val restTemplate = RestTemplate()
        val soapResponse = categorySoapClient!!.categories
        categories.addAll(soapResponse.categories)
        var currentCat: Categories? = null
        var page: Int? = 1
        if (p.isPresent) page = p.get()
        var main_url = "$urlConn/api/recipes/$page"
        var next_url = urlConn + "/api/recipes/" + (page!! + 1)
        var link_more = "/" + (page + 1)
        val tags_url = "$urlConn/api/tags"
        val recent_url = "$urlConn/api/recipes/1"

        if (cat.isPresent) {
            main_url = urlConn + "/api/recipes/" + page + "/cat/" + cat.get()
            next_url = urlConn + "/api/recipes/" + (page + 1) + "/cat/" + cat.get()
            link_more = "/" + (page + 1) + "/cat/" + cat.get()
            currentCat = soapResponse.categories.stream().filter { c -> c.id == cat.get() }.findFirst().get()
        }

        if (searchFilter.isPresent) {
            main_url = urlConn + "/api/search/" + searchFilter.get()
            next_url = ""
            link_more = "#"
            currentCat = null

            response_list = restTemplate.exchange(main_url, HttpMethod.GET)
            val recipes_arr = response_list.getBody()
            if (recipes_arr != null) {
                recipes_arr.stream().limit(20).forEach { recipes.add(setTagsIntro(it, urlConn)) }
            }
        } else {
            response = restTemplate.exchange(main_url, HttpMethod.GET)
            val recipes_arr = Objects.requireNonNull<PagedResources<Recipes>>(response.body).content
            recipes_arr.stream().limit(20).forEach { recipes.add(setTagsIntro(it, urlConn)) }
        }


        val recent_response: ResponseEntity<PagedResources<Recipes>> = restTemplate.exchange(recent_url, HttpMethod.GET)
        val recent_recipes_arr = Objects.requireNonNull<PagedResources<Recipes>>(recent_response.body).content
        recent_recipes_arr.stream().limit(10).forEach { recent_recipes.add(it) }

        val tags_response: ResponseEntity<List<String>> = restTemplate.exchange(tags_url, HttpMethod.GET)
        val tags_arr = Objects.requireNonNull<List<String>>(tags_response.body)
        tags_arr.stream().limit(20).forEach { tags.add(it) }

        if (!next_url.isEmpty()) {
            val response_next: ResponseEntity<PagedResources<Recipes>> = restTemplate.exchange(next_url, HttpMethod.GET)
            if (Objects.requireNonNull<PagedResources<Recipes>>(response_next.body).content.isNotEmpty()) {
                model.addAttribute("link_more", link_more)
            } else {
                model.addAttribute("link_more", "#")
            }
        } else {
            model.addAttribute("link_more", "#")
        }
        if (currentCat != null) {
            model.addAttribute("currentCat", currentCat)
        }

        searchFilter.ifPresent { s ->
            model.addAttribute("searchFilter", s)
        }

        model.addAttribute("recent", recent_recipes)
        model.addAttribute("tagCloud", tags)
        model.addAttribute("recipes", recipes)
        model.addAttribute("categories", categories)
        when {
            searchFilter.isPresent -> model.addAttribute("page_title", search_results + " " + searchFilter.get())
            currentCat != null -> model.addAttribute("page_title", currentCat.name)
            else -> model.addAttribute("page_title", header_txt)
        }
        return "home"
    }

    private fun setTagsIntro(p: Recipes, urlConn: String): Recipes {
        val restTemplate = RestTemplate()

        val recipe = restTemplate.getForObject(urlConn + "/api/recipe/" + p.id, Recipes::class.java, 200)
        val tags = Objects.requireNonNull<Recipes>(recipe).tags
        tags.sortWith(Comparator.comparingInt { it.start_pos })

        var start_pos = 0
        var start_pos2 = 0
        for (tag in tags) {
            if (tag.intro_instruction == "intro") {
                val ins = StringBuilder(p.intro)
                if (!ins.substring(tag.start_pos + start_pos, tag.end_pos + start_pos).contains("<a") && !ins.substring(tag.start_pos + start_pos2, tag.end_pos + start_pos2).contains("<a")) {

                    p.intro = ins.replace(
                            tag.start_pos + start_pos,
                            tag.end_pos + start_pos,
                            "<a class='tags' href='/search/" + tag.value + "'>" + tag.value + "</a>"
                    ).toString()
                    start_pos2 = start_pos
                    start_pos += ("<a class='tags' href='/search/" + tag.value + "'>" + "</a>").length
                }
            }
        }
        return p
    }

    companion object {

        private val log = LoggerFactory.getLogger(HomeController::class.java)
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

