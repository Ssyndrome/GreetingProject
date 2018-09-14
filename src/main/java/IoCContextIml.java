import java.util.ArrayList;
import java.util.List;

public class IoCContextIml implements IoCContext{

    private List<Class> pointedBeanClazz = new ArrayList<>();

    @Override
    public void registerBean(Class<?> beanClazz) {
        if (pointedBeanClazz.contains(beanClazz)) {}
        if (beanClazz == null) throw new IllegalArgumentException("beanClazz is mandatory");
        try {
            beanClazz.newInstance();
        } catch (InstantiationException exception) {
            throw new IllegalArgumentException(beanClazz.getName() + " is abstract");
        } catch (IllegalAccessException exception) {
            throw new IllegalArgumentException(beanClazz.getName() + "has no default constructor");
        }

        pointedBeanClazz.add(beanClazz);

    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) throws IllegalAccessException, InstantiationException {
        if (resolveClazz == null) throw new IllegalArgumentException();
        if (!pointedBeanClazz.contains(resolveClazz)) throw new IllegalStateException();

        return resolveClazz.newInstance();
    }
}
