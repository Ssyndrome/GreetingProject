import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IoCContextImlTest {

    @Test
    void should_succeed_creating_instance_for_MyBean() throws InstantiationException, IllegalAccessException {

        MyBean expectedInstance = new MyBean();

        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);
        context.registerBean(MyDependency.class);
        MyBean myBeanInstance = context.getBean(MyBean.class);
        assertEquals(expectedInstance.getClass(), myBeanInstance.getClass());
    }

    @Test
    void should_succeed_creating_two_instance_for_MyBean() throws InstantiationException, IllegalAccessException {
        MyBean expectedBeanInstance = new MyBean();
        ValidClassForRegister expectedValidInstance = new ValidClassForRegister();

        IoCContext context = new IoCContextIml();

        context.registerBean(MyBean.class);
        context.registerBean(MyDependency.class);

        context.registerBean(ValidClassForRegister.class);

        MyBean myBeanInstance = context.getBean(MyBean.class);
        ValidClassForRegister myValidInstance = context.getBean(ValidClassForRegister.class);

        assertEquals(expectedBeanInstance.getClass(), myBeanInstance.getClass());
        assertEquals(expectedValidInstance.getClass(), myValidInstance.getClass());
    }

    @Test
    void should_throw_exception_when_input_null_for_registerBean() {
        IoCContext context = new IoCContextIml();

        Class exceptedClass = IllegalArgumentException.class;

        Throwable actualThrowable = assertThrows(exceptedClass, () -> context.registerBean(null));
        assertEquals("beanClazz is mandatory", actualThrowable.getMessage());
    }

    @Test
    void should_throw_exception_when_input_class_cannot_be_instanced() {
        IoCContext context = new IoCContextIml();

        Class exceptedClass = IllegalArgumentException.class;

        Throwable actualThrowable = assertThrows(exceptedClass, () -> context.registerBean(IoCContext.class));
        assertEquals(IoCContext.class.getName() + " is abstract", actualThrowable.getMessage());
    }

    @Test
    void should_throw_exception_when_input_class_have_no_default_constructor() {
        IoCContext context  = new IoCContextIml();
        Class exceptedClass = IllegalArgumentException.class;

        int[] array = new int[]{};

        Throwable actualThrowable = assertThrows(exceptedClass, () -> context.registerBean(NoDefaultConstructorClass.class));
        assertEquals(NoDefaultConstructorClass.class.getName() + " has no default constructor", actualThrowable.getMessage());
    }

    @Test
    void should_throw_exception_when_get_instance_without_register() {
        IoCContext context = new IoCContextIml();

        assertThrows(Exception.class, () -> context.getBean(MyBean.class));
    }

    @Test
    void should_get_two_different_instance() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);
        context.registerBean(MyDependency.class);

        assertNotSame(context.getBean(MyBean.class), context.getBean(MyBean.class));
    }

    @Test
    void should_occurs_nothing_when_a_class_repeated_register() {
        IoCContext context = new IoCContextIml();

        context.registerBean(MyBean.class);
        context.registerBean(MyBean.class);
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

        assertThrows(expectedException, () -> context.getBean(ValidClassForRegister.class));
    }

    @Test
    void should_continue_throwing_exception_or_error_when_these_occurs_during_getBean() {
        IoCContext context = new IoCContextIml();
        context.registerBean(ExceptionInResolvedClass.class);

        constructorException constructorException = assertThrows(constructorException.class, () -> {
            context.getBean(ExceptionInResolvedClass.class);
        });

        assertEquals("Error constructor", constructorException.getMessage());
    }

    @Test
    void should_throw_error_when_registerBean_after_getBean() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);
        context.registerBean(MyDependency.class);
        context.getBean(MyBean.class);

        Class expectedException = IllegalStateException.class;

        assertThrows(expectedException, () -> {
            context.registerBean(MyBean.class);
        });

        assertThrows(expectedException, () -> {
            context.registerBean(ValidClassForRegister.class);
        });
    }

}

class NoDefaultConstructorClass {
    public NoDefaultConstructorClass(int thisIsArgument) {
    }
}

class ValidClassForRegister {
    public ValidClassForRegister() {
    }
}

class ExceptionInResolvedClass {
    public ExceptionInResolvedClass() throws Exception{
        throw new constructorException("Error constructor");
    }
}

