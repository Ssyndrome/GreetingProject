import jdk.nashorn.internal.ir.annotations.Ignore;
import jdk.nashorn.internal.objects.NativeDebug;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IoCContextImlTest {

    @Test
    void should_succeed_creating_instance_for_MyBean() {

        MyBean expectedInstance = new MyBean();

        assertDoesNotThrow(() -> {
            IoCContext context = new IoCContextIml();
            context.registerBean(MyBean.class);
            MyBean myBeanInstance = context.getBean(MyBean.class);
            assertEquals(expectedInstance.getClass(), myBeanInstance.getClass());
        });
    }

    @Test
    void should_succeed_creating_two_instance_for_MyBean() {
        MyBean expectedBeanInstance = new MyBean();
        ValidClassForRegister expectedValidInstance = new ValidClassForRegister();

        assertDoesNotThrow(() -> {
            IoCContext context = new IoCContextIml();

            context.registerBean(MyBean.class);
            MyBean myBeanInstance = context.getBean(MyBean.class);

            context.registerBean(ValidClassForRegister.class);
            ValidClassForRegister myValidInstance = context.getBean(ValidClassForRegister.class);

            assertEquals(expectedBeanInstance.getClass(), myBeanInstance.getClass());
            assertEquals(expectedValidInstance.getClass(), myValidInstance.getClass());
        });
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

        assertThrows(exceptedClass, () -> context.registerBean(NoDefaultConstructorClass.class), NoDefaultConstructorClass.class.getName() + "has no default constructor");
    }

    @Ignore
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

        assertThrows(expectedException, () -> context.getBean(ValidClassForRegister.class));
    }

    @Test
    void should_continue_throwing_exception_or_error_when_these_occurs_during_getBean() {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);

        assertThrows(Exception.class, () -> context.getBean(ExceptionInResolvedClass.class));
    }

    @Test
    void should_throw_error_when_registerBean_after_getBean() {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);

        Class expectedException = IllegalStateException.class;
        assertThrows(expectedException, () -> {
            context.getBean(ExceptionInResolvedClass.class);
            context.registerBean(ExceptionInResolvedClass.class);
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
        throw new Exception();
    }
}

