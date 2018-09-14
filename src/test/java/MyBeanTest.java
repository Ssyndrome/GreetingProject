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
}