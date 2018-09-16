import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DependencyWithInheritance {
    @Test
    void should_succeed_extend_parent_dependency_field() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        IoCContext context = new IoCContextIml();
        context.registerBean(ChildrenClassWithDependency.class);
        context.registerBean(ParentDependency.class);
        context.registerBean(MyDependency.class);

        ChildrenClassWithDependency children = context.getBean(ChildrenClassWithDependency.class);

        Field parentFieldInChild = children.getClass().getSuperclass().getDeclaredField("parentDependency");
        parentFieldInChild.setAccessible(true);
        Object parentFieldValueInChild = parentFieldInChild.get(children);

        Class expectedClass = ParentDependency.class;
        assertEquals(expectedClass, parentFieldValueInChild.getClass());
    }

    @Test
    void should_fail_as_original_to_extend_parent_dependency_field() {
        IoCContext context = new IoCContextIml();
        context.registerBean(ChildrenClassWithDependency.class);
        context.registerBean(MyDependency.class);

        Class expectedExceptionClass = IllegalStateException.class;
        assertThrows(expectedExceptionClass, () -> context.getBean(ChildrenClassWithDependency.class));
    }
}

class ParentClassWithDependency {
    @CreateOnTheFly
    private ParentDependency parentDependency;

    public ParentClassWithDependency() {
    }
}

class ChildrenClassWithDependency extends ParentClassWithDependency{
    @CreateOnTheFly
    private MyDependency dependency;

    public ChildrenClassWithDependency() {
    }
}

