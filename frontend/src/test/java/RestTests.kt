import al.recipes.Application
import al.recipes.models.Recipes
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.PagedResources
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.util.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = [Application::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RestTests {
    lateinit var restTemplate: RestTemplate

    @BeforeEach
    fun before() {
        restTemplate = RestTemplate()
    }

    @Test
    fun `Get the last recipe details`() {
        //val restTemplate = RestTemplate()
        val mainUrl = "http://localhost/api/recipe/10413"
        val recipe = restTemplate.getForObject(mainUrl, Recipes::class.java, 200)
        assertThat(recipe).isNotNull()
        assertThat(recipe!!.name).isEqualTo("Tave me bamje")
    }

    @Test
    fun `Get last 20 recipes`() {
        val mainUrl = "http://localhost/api/recipes/1"
        val recipes = restTemplate.exchange(mainUrl,
                HttpMethod.GET, null, object : ParameterizedTypeReference<PagedResources<Recipes>>() {})
        val recentRecipesArray = Objects.requireNonNull<PagedResources<Recipes>>(recipes.body).content
        assertThat(recentRecipesArray.size).isEqualTo(20)
    }
}