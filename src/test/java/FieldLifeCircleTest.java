import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.runtime.ConstructionException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class FieldLifeCircleTest {
    @Test
    void should_automatically_close_the_field_after_get_bean() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBean.class);
        context.registerBean(MyDependency.class);

        MyBean myBeanIntermediate = null;
        try (MyBean myBeanInstance = context.getBean(MyBean.class)) {
            Field myIsClosed = myBeanInstance.getClass().getDeclaredField("isClosed");
            myIsClosed.setAccessible(true);

            myBeanIntermediate = myBeanInstance;
            assertFalse((boolean)myIsClosed.get(myBeanInstance));
        } catch (Exception e) {
            Field myIsClosed = myBeanIntermediate.getClass().getDeclaredField("isClosed");
            myIsClosed.setAccessible(true);

            assertFalse((boolean)myIsClosed.get(myBeanIntermediate));
        }
    }

    @Test
    void should_throw_exception_after_all_instance_closed() {
        IoCContext context = new IoCContextIml();
        context.registerBean(ExceptionInCloseClass.class);

        ConstructionException exception = assertThrows(ConstructionException.class, () -> context.getBean(ExceptionInCloseClass.class));
        assertEquals("Close error", exception.getMessage());
    }
}