import java.util.*;

public class IoCContextIml implements IoCContext{

    private List<Class> pointedBeanClazz = new ArrayList<>();
    private Map<Class, Class> relatedClazz = new HashMap<>();

    private boolean hasGot = false;

    @Override
    public void registerBean(Class<?> beanClazz) {
        if (hasGot) throw new IllegalStateException();

        if (beanClazz == null) throw new IllegalArgumentException("beanClazz is mandatory");
        if (beanClazz.getConstructors().length == 0) {
            throw new IllegalArgumentException(beanClazz.getName() + " is abstract");
        }
        if (Arrays.stream(beanClazz.getConstructors()).noneMatch(constructor -> constructor.getParameterTypes().length == 0)) {
            throw new IllegalArgumentException(beanClazz.getName() + " has no default constructor");
        }


        pointedBeanClazz.add(beanClazz);

    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<?> beanClazz) {
        registerBean(beanClazz);

        if (resolveClazz == null) throw new IllegalArgumentException("resolveClazz is mandatory");

        relatedClazz.put(resolveClazz, beanClazz);
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) throws IllegalAccessException, InstantiationException {
        if (resolveClazz == null) throw new IllegalArgumentException();
        if (!(pointedBeanClazz.contains(resolveClazz) || relatedClazz.containsKey(resolveClazz))) throw new IllegalStateException();

        hasGot = true;

        T returnedInstance;
        if (relatedClazz.containsKey(resolveClazz)) {
            returnedInstance = (T) relatedClazz.get(resolveClazz).newInstance();
        } else {
            returnedInstance = (T) resolveClazz.newInstance();
        }

        Arrays.stream(returnedInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CreateOnTheFly.class))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        field.set(returnedInstance, field.getType().newInstance());
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                });
        return returnedInstance;
    }
}
