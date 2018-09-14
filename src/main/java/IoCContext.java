public interface IoCContext {
    void registerBean(Class<?> beanClazz);

    <T> void registerBean(Class<? super T> resolveClazz, Class<?> beanClazz);


    <T> T getBean(Class<T> resolveClazz) throws IllegalAccessException, InstantiationException;
}
