import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class FieldLifeCircleTest {
    @Test
    void should_automatically_close_the_field_after_get_bean() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);
        context.registerBean(MyDependency.class);
        Object myBeanInstance = context.getBean(MyBean.class);

        Field myIsClosed = myBeanInstance.getClass().getDeclaredField("isClosed");
        myIsClosed.setAccessible(true);

        assertTrue((boolean)myIsClosed.get(myBeanInstance));
    }
}