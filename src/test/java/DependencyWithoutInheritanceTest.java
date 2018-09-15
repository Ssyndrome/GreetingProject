import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class DependencyWithoutInheritanceTest {
    @Test
    void should_succeed_create_dependency_instance() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);
        context.registerBean(MyDependency.class);
        MyBean myBeanInstance = context.getBean(MyBean.class);

        Field myDependencyField = myBeanInstance.getClass().getDeclaredField("dependency");
        myDependencyField.setAccessible(true);
        Object myDependency = myDependencyField.get(myBeanInstance);

        Class expectedToBeMyDependency = MyDependency.class;
        assertEquals(expectedToBeMyDependency, myDependency.getClass());
    }

    @Test
    void should_throw_exception_when_get_bean_as_any_dependencies_have_not_been_registered() {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);

        Class expectedExceptionClass = IllegalStateException.class;
        assertThrows(expectedExceptionClass, () -> context.getBean(MyBean.class));
    }

    @Test
    void should_succeed_get_bean_when_no_dependencies() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBeanCooler.class);

        Class expectedClass = MyBeanCooler.class;
        assertEquals(expectedClass, context.getBean(MyBeanCooler.class).getClass());
    }
}