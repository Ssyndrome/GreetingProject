import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverrideRegisterTest {
    @Test
    void should_succeed_produce_instance_for_second_argument_in_register_bean() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBeanBase.class, MyBean.class);
        MyBeanBase myBeanBase = context.getBean(MyBeanBase.class);

        MyBeanBase expectedInstance = new MyBeanBase();
        assertEquals(expectedInstance.getClass(), myBeanBase.getClass());
    }
}
