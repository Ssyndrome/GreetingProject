import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverrideRegisterTest {
    @Test
    void should_succeed_produce_instance_for_second_argument_in_register_bean() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBeanBase.class, MyBean.class);
        Object myBeanBaseInstance = context.getBean(MyBeanBase.class);

        Class expectedClass = MyBean.class;
        assertEquals(expectedClass, myBeanBaseInstance.getClass());
    }

    @Test
    void should_cover_before_registered_class_after_second_register_by_parent() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextIml();
        context.registerBean(MyBeanBase.class, MyBean.class);

        context.registerBean(MyBeanBase.class, MyBeanCooler.class);
        Object myBeanBaseInstance = context.getBean(MyBeanBase.class);

        Class expectedClass = MyBeanCooler.class;
        assertEquals(expectedClass, myBeanBaseInstance.getClass());
    }
}
