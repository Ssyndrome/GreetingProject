import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GreetingTest {

    @Test
    void should_print_greet() {
        Greeting greetingFunction = new Greeting();
        assertEquals("Hello world!", greetingFunction.greet());
    }
}