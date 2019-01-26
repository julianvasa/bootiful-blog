package al.recipes.controllers

import al.recipes.models.Recipes
import al.recipes.rest.controllers.TagControllerRest
import al.recipes.soap.SoapClient
import categories.wsdl.Categories
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.PagedResources
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.RestTemplate
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/")
class RecipeController {
    @Autowired
    private val categorySoapClient: SoapClient? = null
    @Autowired
    private val tagControllerRest: TagControllerRest? = null
    @Autowired
    private val messageSource: MessageSource? = null

    @GetMapping("/recipe/{id}")
    @ApiOperation(value = "Get recipe by id and display the view_recipe template")
    fun index(model: Model, @PathVariable id: Long, request: HttpServletRequest): String {
        val categories = ArrayList<Categories>()
        val tagCloud = ArrayList<String>()
        val recent_recipes = ArrayList<Recipes>()
        val https = request.scheme as String
        val urlConn = https + "://" + request.serverName

        val restTemplate = RestTemplate()
        val main_url = "$urlConn/api/recipe/$id"
        val tag_cloud_url = "$urlConn/api/tags"
        val recent_url = "$urlConn/api/recipes/1"

        val recipe = restTemplate.getForObject(main_url, Recipes::class.java, 200)
        val tags = Objects.requireNonNull<Recipes>(recipe).tags
        tags.sortWith(Comparator.comparingInt { it.getStart_pos() })
        val soapResponse = categorySoapClient!!.categories
        categories.addAll(soapResponse.categories)

        val currentCat = soapResponse.categories.stream().filter { c -> c.id == recipe!!.category.id }.findFirst().get()

        val tags_response = restTemplate.exchange(tag_cloud_url,
                HttpMethod.GET, null, object : ParameterizedTypeReference<List<String>>() {

        }
        )
        val tags_arr = Objects.requireNonNull<List<String>>(tags_response.body)
        tags_arr.stream().limit(20).forEach({ tagCloud.add(it) })

        val recent_response = restTemplate.exchange(recent_url,
                HttpMethod.GET, null, object : ParameterizedTypeReference<PagedResources<Recipes>>() {

        }
        )
        val recent_recipes_arr = Objects.requireNonNull<PagedResources<Recipes>>(recent_response.body).content
        recent_recipes_arr.stream().limit(10).forEach({ recent_recipes.add(it) })

        var start_pos = 0
        var start_pos2 = 0
        for (tag in tags) {
            if (tag.intro_instruction == "instruction") {
                val ins = StringBuilder(recipe!!.instruction)
                if (!ins.substring(tag.start_pos + start_pos, tag.end_pos + start_pos).contains("<a") && !ins.substring(tag.start_pos + start_pos2, tag.end_pos + start_pos2).contains("<a")) {

                    recipe.instruction = ins.replace(
                            tag.start_pos + start_pos,
                            tag.end_pos + start_pos,
                            "<a class='tags' href='/search/" + tag.value + "'>" + tag.value + "</a>"
                    ).toString()
                    start_pos2 = start_pos
                    start_pos += ("<a class='tags' href='/search/" + tag.value + "'>" + "</a>").length
                }
            }
        }

        start_pos = 0
        start_pos2 = 0
        for (tag in tags) {
            if (tag.intro_instruction == "intro") {
                val ins = StringBuilder(recipe!!.intro)
                if (!ins.substring(tag.start_pos + start_pos, tag.end_pos + start_pos).contains("<a") && !ins.substring(tag.start_pos + start_pos2, tag.end_pos + start_pos2).contains("<a")) {

                    recipe.intro = ins.replace(
                            tag.start_pos + start_pos,
                            tag.end_pos + start_pos,
                            "<a class='tags' href='/search/" + tag.value + "'>" + tag.value + "</a>"
                    ).toString()
                    start_pos2 = start_pos
                    start_pos += ("<a class='tags' href='/search/" + tag.value + "'>" + "</a>").length
                }
            }
        }
        if (currentCat.id > 0) {
            model.addAttribute("currentCat", currentCat)
        }
        model.addAttribute("recent", recent_recipes)
        model.addAttribute("tagCloud", tagCloud)
        model.addAttribute("tags", tags)
        model.addAttribute("recipe", recipe)
        model.addAttribute("categories", categories)

        return "view_recipe"
    }

    /*@PostMapping("/addrecipe")
    public String newRecipe(Model model, @RequestParam(value = "id", required = true) long id,
                            @RequestParam(value = "category_id", required = true) int category_id,
                            @RequestParam(value = "name", required = true) String name,
                            @RequestParam(value = "intro", required = false) String intro,
                            @RequestParam(value = "instruction", required = true) String instruction,
                            @RequestParam(value = "image", required = false) String image,
                            @RequestParam(value = "link", required = false) String link,
                            @RequestParam(value = "time", required = false) String time,
                            @RequestParam(value = "servings", required = false) String servings,
                            @RequestParam(value = "calories", required = false) String calories,
                            @RequestParam(value = "favorite", required = false) int favorite,
                            @RequestParam(value = "rating", required = false) int rating,
                            @RequestParam(value = "posted", required = false) int posted,
                            @RequestParam(value = "video", required = false) String video,
                            @RequestParam(value = "username", required = true) String username) {
        System.out.println(SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal());

        if (!SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal().equals("anonymousUser")) {
            String url = urlConn+"/api/newrecipe?" + id;
            ResponseEntity<Recipes> response = new RestTemplate().postForEntity(url, null, Recipes.class);
            System.out.println(response.getBody().getId());
            return "redirect:/recipe/" + response.getBody().getId();
        } else {
            return "login";
        }
    }*/

    @GetMapping("/add")
    @ApiOperation(value = "Get template to add a new recipe")
    fun index(model: Model, locale: Locale): String {
        val page_title = messageSource!!.getMessage("add", null, locale)
        model.addAttribute("page_title", page_title)
        return "edit_recipe"
    }

    companion object {
        private val log = LoggerFactory.getLogger(HomeController::class.java)
    }
}
