package al.recipes.controllers

import al.recipes.models.Recipes
import al.recipes.soap.SoapClient
import categories.wsdl.Categories
import io.swagger.annotations.ApiOperation
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

    @GetMapping(value = ["/", "/{page:[0-9]+}", "/{page:[0-9]+}/cat/{cat}", "/search/{keyword}"])
    @ApiOperation(value = "Get recipes by page / by page and by cat / search", notes = "Get recipes by page / by page and by cat / search")
    fun getAllRecipes(model: Model, @PathVariable(value = "page") pageRequest: Optional<Int>, @PathVariable cat: Optional<Int>,
                      @PathVariable(value = "keyword") searchFilter: Optional<String>, locale: Locale, request: HttpServletRequest): String {
        val headerTxt = messageSource!!.getMessage("header_txt", null, locale)
        val searchResults = messageSource.getMessage("search_results", null, locale)
        val https = request.scheme as String
        val urlConn = https + "://" + request.serverName
        val recipes = ArrayList<Recipes>()
        val tags = ArrayList<String>()
        val recentRecipes = ArrayList<Recipes>()
        val response: ResponseEntity<PagedResources<Recipes>>
        val responseList: ResponseEntity<List<Recipes>>
        val restTemplate = RestTemplate()
        var page = 1
        if (pageRequest.isPresent) {
            page = pageRequest.get()
        }
        var currentCat: Categories? = null
        var mainUrl = "$urlConn/api/recipes/$page"
        var nextUrl = urlConn + "/api/recipes/" + (page + 1)
        var moreLink = "/" + (page + 1)
        val tagsUrl = "$urlConn/api/tags"
        val recentUrl = "$urlConn/api/recipes/1"

        // Get list of categories (sidebar)
        val categories = ArrayList<Categories>()
        val soapResponse = categorySoapClient!!.categories
        categories.addAll(soapResponse.categories)

        // If showing recipes of a specific category, get category details
        if (cat.isPresent) {
            mainUrl = urlConn + "/api/recipes/" + page + "/cat/" + cat.get()
            nextUrl = urlConn + "/api/recipes/" + (page + 1) + "/cat/" + cat.get()
            moreLink = "/" + (page + 1) + "/cat/" + cat.get()
            currentCat = soapResponse.categories.stream().filter { c -> c.id == cat.get() }.findFirst().get()
        }

        // If search filter is present, filter recipes by searchFilter
        if (searchFilter.isPresent) {
            mainUrl = urlConn + "/api/search/" + searchFilter.get()
            nextUrl = ""
            moreLink = "#"
            currentCat = null

            responseList = restTemplate.exchange(mainUrl, HttpMethod.GET)
            val recipes_arr = responseList.getBody()
            recipes_arr?.stream()?.limit(20)?.forEach { recipes.add(setTagsIntro(it)) }
        } else {
            response = restTemplate.exchange(mainUrl, HttpMethod.GET)
            val recipes_arr = Objects.requireNonNull<PagedResources<Recipes>>(response.body).content
            recipes_arr.stream().limit(20).forEach { recipes.add(setTagsIntro(it)) }
        }

        // Get recent recipes (sidebar)
        val recentRecipesResponse: ResponseEntity<PagedResources<Recipes>> = restTemplate.exchange(recentUrl, HttpMethod.GET)
        val recentRecipesArray = Objects.requireNonNull<PagedResources<Recipes>>(recentRecipesResponse.body).content
        recentRecipesArray.stream().limit(10).forEach { recentRecipes.add(it) }

        // Get tag cloud (sidebar)
        val tagCloudResponse: ResponseEntity<List<String>> = restTemplate.exchange(tagsUrl, HttpMethod.GET)
        val tagCloudArray = Objects.requireNonNull<List<String>>(tagCloudResponse.body)
        tagCloudArray.stream().limit(20).forEach { tags.add(it) }

        if (!nextUrl.isEmpty()) {
            val response_next: ResponseEntity<PagedResources<Recipes>> = restTemplate.exchange(nextUrl, HttpMethod.GET)
            if (Objects.requireNonNull<PagedResources<Recipes>>(response_next.body).content.isNotEmpty()) {
                model.addAttribute("link_more", moreLink)
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

        model.addAttribute("recent", recentRecipes)
        model.addAttribute("tagCloud", tags)
        model.addAttribute("recipes", recipes)
        model.addAttribute("categories", categories)
        when {
            searchFilter.isPresent -> model.addAttribute("page_title", searchResults + " " + searchFilter.get())
            currentCat != null -> model.addAttribute("page_title", currentCat.name)
            else -> model.addAttribute("page_title", headerTxt)
        }
        return "home"
    }


    private fun setTagsIntro(p: Recipes): Recipes {
        val tags = Objects.requireNonNull<Recipes>(p).tags
        tags.sortWith(Comparator.comparingInt { it.start_pos })

        var startPos = 0
        var startPos2 = 0
        for (tag in tags) {
            if (tag.intro_instruction == "intro") {
                val ins = StringBuilder(p.intro)
                if (!ins.substring(tag.start_pos + startPos, tag.end_pos + startPos).contains("<a") && !ins.substring(tag.start_pos + startPos2, tag.end_pos + startPos2).contains("<a")) {

                    p.intro = ins.replace(
                            tag.start_pos + startPos,
                            tag.end_pos + startPos,
                            "<a class='tags' href='/search/" + tag.value + "'>" + tag.value + "</a>"
                    ).toString()
                    startPos2 = startPos
                    startPos += ("<a class='tags' href='/search/" + tag.value + "'>" + "</a>").length
                }
            }
        }
        return p
    }
}

