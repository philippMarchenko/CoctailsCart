import kotlin.test.Test
import kotlin.test.assertEquals

class ExampleTest {

    @Test
    fun testAddition() {
        val result = 2 + 2
        assertEquals(4, result)
    }

    @Test
    fun testStringConcatenation() {
        val cocktail = "Mojito"
        val result = "I love $cocktail"
        assertEquals("I love Mojito", result)
    }
}
