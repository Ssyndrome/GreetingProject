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
}