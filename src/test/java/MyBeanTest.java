import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyBeanTest {

    @Test
    void should_succeed_creating_instance_for_MyBean() {
        boolean isSucceed = true;
        try {
            IoCContext context = new IoCContextIml();
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

        assertThrows(exceptedClass, () -> context.registerBean(null), "beanClazz is mandatory");
    }

    @Test
    void should_throw_exception_when_input_class_cannot_be_instanced() {
        IoCContext context = new IoCContextIml();

        Class exceptedClass = IllegalArgumentException.class;

        assertThrows(exceptedClass, () -> context.registerBean(IoCContext.class), IoCContext.class.getName() + " is abstract");
    }

    @Test
    void should_throw_exception_when_input_class_have_no_default_constructor() {
        IoCContext context  = new IoCContextIml();
        Class exceptedClass = IllegalArgumentException.class;

        assertThrows(exceptedClass, () -> context.registerBean(noDefaultConstructorClass.class), noDefaultConstructorClass.class.getName() + "has no default constructor");
    }

    @Test
    void should_occurs_nothing_when_a_class_repeated_register() {
        IoCContext context = new IoCContextIml();

        assertDoesNotThrow(() -> {
            context.getBean(MyBean.class);
            context.getBean(MyBean.class);
        });
    }

    @Test
    void should_throw_argument_exception_when_getBean_return_null() {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);

        Class expectedException = IllegalArgumentException.class;

        assertThrows(expectedException, () -> context.getBean(null));
    }

    @Test
    void should_throw_state_exception_when_pointed_object_is_not_same() {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);

        Class expectedException = IllegalStateException.class;

        assertThrows(expectedException, () -> context.getBean(validClassForRegister.class));
    }

    @Test
    void should_continue_throwing_exception_or_error_when_these_occurs_during_getBean() {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);

        assertThrows(Exception.class, () -> context.getBean(exceptionInResolvedClass.class));
    }
}

class noDefaultConstructorClass {
    public noDefaultConstructorClass(int thisIsArgument) {
    }
}

class validClassForRegister {
}

class exceptionInResolvedClass {
    public exceptionInResolvedClass() throws Exception{
        throw new Exception();
    }
}