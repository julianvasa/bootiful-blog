import al.recipes.Application
import al.recipes.services.CategoriesService
import al.recipes.services.RecipesService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = [Application::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ServiceTests {

    @Autowired
    lateinit var categoriesService: CategoriesService

    @Autowired
    lateinit var recipesService: RecipesService

    @Test
    fun categoriesService() {
        val totCat = categoriesService.findAll()
        assertThat(totCat.size).isEqualTo(18).withFailMessage("Expecting 18 categories in categoriesService")
    }

    @Test
    fun recipesService() {
        val totRecipes = recipesService.maxId()
        assertThat(totRecipes.get(0).id).isEqualTo(10413).withFailMessage("Expecting recipe id = 10413 to be the last one, got = " + totRecipes.get(0).id)
    }
}