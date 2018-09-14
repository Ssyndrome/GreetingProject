

public class IoCContextIml implements IoCContext{

    private Class pointedBeanClazz;

    @Override
    public void registerBean(Class<?> beanClazz) {
        if (beanClazz == null) throw new IllegalArgumentException("beanClazz is mandatory");
        try {
            beanClazz.newInstance();
        } catch (InstantiationException exception) {
            throw new IllegalArgumentException(beanClazz.getName() + " is abstract");
        } catch (IllegalAccessException exception) {
            throw new IllegalArgumentException(beanClazz.getName() + "has no default constructor");
        }

        pointedBeanClazz = beanClazz;

    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {
        if (resolveClazz == null) throw new IllegalArgumentException();

        if (resolveClazz != pointedBeanClazz) throw new IllegalStateException();

        return null;
    }
}
