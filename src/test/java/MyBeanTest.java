import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyBeanTest {

    @Test
    void should_succeed_creating_instance_for_MyBean() {
        IoCContext context = new IoCContextIml();
        boolean isSucceed = true;
        try {
            context.registerBean(MyBean.class);
            MyBean myBeanInstance = context.getBean(MyBean.class);
        } catch (Exception error) {
            isSucceed = false;
        }

        assertTrue(isSucceed);
    }

    @Test
    void should_throw_exception_when_input_null_for_registerBean() {
        IoCContext context = new IoCContextIml();

        Class exceptedClass = IllegalArgumentException.class;

        assertThrows(exceptedClass, () -> context.registerBean(null));
    }

    @Test
    void should_throw_exception_when_input_class_cannot_be_instanced() {
        IoCContext context = new IoCContextIml();

        Class exceptedClass = IllegalArgumentException.class;

        assertThrows(exceptedClass, () -> context.registerBean(IoCContext.class));
    }

    @Test
    void should_throw_exception_when_input_class_have_no_default_constructor() {
        IoCContext context  = new IoCContextIml();
        Class exceptedClass = IllegalArgumentException.class;

        assertThrows(exceptedClass, () -> context.registerBean(noConstructorClass.class));
    }

    @Test
    void should_occurs_nothing_when_a_class_repeated_register() {
        IoCContext context = new IoCContextIml();
        assertDoesNotThrow(() -> {
            context.getBean(MyBean.class);
            context.getBean(MyBean.class);
        });
    }


}

class noConstructorClass {
    public noConstructorClass(int thisIsArgument) {
    }
}