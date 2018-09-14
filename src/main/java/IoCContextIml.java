

public class IoCContextIml implements IoCContext{
    @Override
    public void registerBean(Class<?> beanClazz) {
        if (beanClazz == null) throw new IllegalArgumentException("beanClazz is mandatory");
        try {
            beanClazz.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(beanClazz.getName()+" is abstract");
        }
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {
        return null;
    }
}
