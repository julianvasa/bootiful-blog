import al.recipes.Application
import al.recipes.soap.SoapClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = [Application::class])
class SoapTests {

    @Autowired
    lateinit var categorySoapClient: SoapClient

    @Test
    fun `Get Soap Categories`() {
        val soapResponse = categorySoapClient.categories
        assertNotNull(soapResponse)
        assertEquals(18, soapResponse.categories.size, "Expecting 18 categories found " + soapResponse.categories.size)
        assertEquals(soapResponse.categories.get(0).id, 11, "Expecting category id 11 to be the first category (order by count desc), got " + soapResponse.categories.get(0).id)
    }
}
