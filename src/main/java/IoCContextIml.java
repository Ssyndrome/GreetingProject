

public class IoCContextIml implements IoCContext{
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


    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {
        return null;
    }
}
