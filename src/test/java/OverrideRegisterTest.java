import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void should_continue_obey_original_create_rule_before_for_new_register_override_method() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextIml();

        context.registerBean(MyBeanBase.class, MyBean.class);
        Object myBeanBaseInstance = context.getBean(MyBeanBase.class);
        Object myBeanInstance = context.getBean(MyBean.class);

        Class expectedBaseInstanceClass = MyBean.class;
        Class expectedBeanInstanceClass = MyBean.class;
        assertEquals(expectedBaseInstanceClass, myBeanBaseInstance.getClass());
        assertEquals(expectedBeanInstanceClass, myBeanInstance.getClass());
    }

    @Test
    void should_obey_original_input_rule() {
        IoCContext context = new IoCContextIml();

        Class expectedExceptionClass = IllegalArgumentException.class;
        IllegalArgumentException actualThrowable = assertThrows(IllegalArgumentException.class, () -> {
            context.registerBean(null, MyBean.class);
        });
        assertEquals("resolveClazz is mandatory", actualThrowable.getMessage());
    }

}
