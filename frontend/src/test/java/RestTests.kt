import al.recipes.Application
import al.recipes.models.Recipes
import io.restassured.RestAssured
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.PagedResources
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.util.*
import io.restassured.RestAssured.*
import io.restassured.matcher.RestAssuredMatchers.*
import org.hamcrest.Matchers.*
import org.springframework.boot.web.server.LocalServerPort

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = [Application::class],webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestTests {
    @LocalServerPort
    private val port: Int = 0

    @BeforeEach
    fun before() {
        RestAssured.port = port
    }

    @Test
    fun `Get the last recipe name`() {
        val mainUrl = "http://localhost/api/recipe/10413"
        get(mainUrl).then().body("name", equalTo("Tave me bamje"))
    }

    @Test
    fun `Get last 20 recipes`() {
        val mainUrl = "http://localhost/api/recipes/1"
        get(mainUrl).then().body("content.size()", equalTo(20))
    }
}
