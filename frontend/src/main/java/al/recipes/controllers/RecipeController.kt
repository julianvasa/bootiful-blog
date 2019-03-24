package al.recipes.controllers

import al.recipes.models.Recipes
import al.recipes.soap.SoapClient
import categories.wsdl.Categories
import io.swagger.annotations.ApiOperation
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
        val recentRecipes = ArrayList<Recipes>()
        val https = request.scheme as String
        val urlConn = https + "://" + request.serverName
        val restTemplate = RestTemplate()
        val mainUrl = "$urlConn/api/recipe/$id"
        val tagCloudUrl = "$urlConn/api/tags"
        val recentUrl = "$urlConn/api/recipes/1"

        // Get recipe details
        var recipe = restTemplate.getForObject(mainUrl, Recipes::class.java, 200)
        // Get tags related with the current recipe
        val tags = Objects.requireNonNull<Recipes>(recipe).tags
        tags.sortWith(Comparator.comparingInt { it.getStart_pos() })
        // Get list of categories (sidebar)
        val soapResponse = categorySoapClient!!.categories
        categories.addAll(soapResponse.categories)
        // Get the category of the current recipe
        val currentCat = soapResponse.categories.stream().filter { c -> c.id == recipe!!.category.id }.findFirst().get()

        // Get tag cloud (sidebar)
        val tagsCloudResponse = restTemplate.exchange(tagCloudUrl,
                HttpMethod.GET, null, object : ParameterizedTypeReference<List<String>>() {

        }
        )
        val tagCloudArray = Objects.requireNonNull<List<String>>(tagsCloudResponse.body)
        tagCloudArray.stream().limit(20).forEach { tagCloud.add(it) }

        // Get recent recipes (sidebar)
        val recentResponse = restTemplate.exchange(recentUrl,
                HttpMethod.GET, null, object : ParameterizedTypeReference<PagedResources<Recipes>>() {

        }
        )
        val recentRecipesArray = Objects.requireNonNull<PagedResources<Recipes>>(recentResponse.body).content
        recentRecipesArray.stream().limit(10).forEach { recentRecipes.add(it) }

        // Make tags inside intro and instruction link to search url
        setTags(recipe)

        // Update recipe owner if missing
        if (SecurityContextHolder.getContext().authentication.principal is UserDetails) {
            val userInfo = SecurityContextHolder.getContext().authentication.principal as UserDetails
            val currentUser = userInfo.username

            if (recipe!!.user == null) {
                restTemplate.postForEntity("$urlConn/api/setUserRecipe/$id/$currentUser", null, Recipes::class.java)
            }
            recipe = restTemplate.getForObject(mainUrl, Recipes::class.java, 200)
        }
        if (currentCat.id > 0) {
            model.addAttribute("currentCat", currentCat)
        }
        model.addAttribute("recent", recentRecipes)
        model.addAttribute("tagCloud", tagCloud)
        model.addAttribute("tags", tags)
        model.addAttribute("recipe", recipe)
        model.addAttribute("categories", categories)
        return "view_recipe"
    }

    private fun setTags(recipe: Recipes?): Recipes? {
        val tags = Objects.requireNonNull<Recipes>(recipe).tags
        tags.sortWith(Comparator.comparingInt { it.start_pos })

        var startPos = 0
        var startPos2 = 0
        for (tag in tags) {
            if (tag.intro_instruction == "intro") {
                val ins = StringBuilder(recipe!!.intro)
                if (!ins.substring(tag.start_pos + startPos, tag.end_pos + startPos).contains("<a") && !ins.substring(tag.start_pos + startPos2, tag.end_pos + startPos2).contains("<a")) {

                    recipe.intro = ins.replace(
                            tag.start_pos + startPos,
                            tag.end_pos + startPos,
                            "<a class='tags' href='/search/" + tag.value + "'>" + tag.value + "</a>"
                    ).toString()
                    startPos2 = startPos
                    startPos += ("<a class='tags' href='/search/" + tag.value + "'>" + "</a>").length
                }
            }
        }
        startPos = 0
        startPos2 = 0
        for (tag in tags) {
            if (tag.intro_instruction == "instruction") {
                val ins = StringBuilder(recipe!!.instruction)
                if (!ins.substring(tag.start_pos + startPos, tag.end_pos + startPos).contains("<a") && !ins.substring(tag.start_pos + startPos2, tag.end_pos + startPos2).contains("<a")) {

                    recipe.instruction = ins.replace(
                            tag.start_pos + startPos,
                            tag.end_pos + startPos,
                            "<a class='tags' href='/search/" + tag.value + "'>" + tag.value + "</a>"
                    ).toString()
                    startPos2 = startPos
                    startPos += ("<a class='tags' href='/search/" + tag.value + "'>" + "</a>").length
                }
            }
        }
        return recipe!!
    }

    @GetMapping("/add")
    @ApiOperation(value = "Get template to add a new recipe")
    fun index(model: Model, locale: Locale): String {
        val page_title = messageSource!!.getMessage("add", null, locale)
        model.addAttribute("page_title", page_title)
        return "create_recipe"
    }
}
