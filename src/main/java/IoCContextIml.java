import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IoCContextIml implements IoCContext{

    private List<Class> pointedBeanClazz = new ArrayList<>();
    private boolean hasGot = false;

    @Override
    public void registerBean(Class<?> beanClazz) {
        if (hasGot) throw new IllegalStateException();
        if (beanClazz == null) throw new IllegalArgumentException("beanClazz is mandatory");
        try {
            beanClazz.newInstance();
        } catch (InstantiationException exception) {
            throw new IllegalArgumentException(beanClazz.getName() + " is abstract");
        } catch (IllegalAccessException exception) {
            throw new IllegalArgumentException(beanClazz.getName() + "has no default constructor");
        } catch (Exception otherException) {
        }

            pointedBeanClazz.add(beanClazz);

    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) throws IllegalAccessException, InstantiationException {
        if (resolveClazz == null) throw new IllegalArgumentException();
        if (!pointedBeanClazz.contains(resolveClazz)) throw new IllegalStateException();

        pointedBeanClazz.add(resolveClazz);
        hasGot = true;

        return resolveClazz.newInstance();
    }
}
