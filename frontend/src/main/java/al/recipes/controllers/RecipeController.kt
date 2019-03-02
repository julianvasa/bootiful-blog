package al.recipes.controllers

import al.recipes.models.Recipes
import al.recipes.soap.SoapClient
import categories.wsdl.Categories
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.PagedResources
import org.springframework.http.HttpMethod
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
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

        var recipe = restTemplate.getForObject(main_url, Recipes::class.java, 200)

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

        val userInfo = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val currentUser = userInfo.username

        if (recipe!!.user == null) {
            restTemplate.postForEntity("$urlConn/api/setUserRecipe/$id/$currentUser", null, Recipes::class.java)
        }
        recipe = restTemplate.getForObject(main_url, Recipes::class.java, 200)

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

    @GetMapping("/add")
    @ApiOperation(value = "Get template to add a new recipe")
    fun index(model: Model, locale: Locale): String {
        val page_title = messageSource!!.getMessage("add", null, locale)
        model.addAttribute("page_title", page_title)
        return "create_recipe"
    }

    companion object {
        private val log = LoggerFactory.getLogger(RecipeController::class.java)
    }
}
