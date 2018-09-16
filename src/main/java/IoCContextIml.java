import sun.jvm.hotspot.runtime.ConstructionException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class IoCContextIml implements IoCContext{

    private List<Class> registeredBeanClazz = new ArrayList<>();
    private Map<Class, Class> baseRelatedClazz = new HashMap<>();
    private List gotClazz = new ArrayList();

    private boolean hasGot = false;

    @Override
    public void registerBean(Class<?> beanClazz) {
        if (hasGot) throw new IllegalStateException();

        isValidBeanClazz(beanClazz);

        registeredBeanClazz.add(beanClazz);
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<?> beanClazz) {
        isCondition(resolveClazz == null, "resolveClazz is mandatory");
        registerBean(beanClazz);

        baseRelatedClazz.put(resolveClazz, beanClazz);
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) throws IllegalAccessException, InstantiationException {
        if (resolveClazz == null) throw new IllegalArgumentException();
        if (!(registeredBeanClazz.contains(resolveClazz) || baseRelatedClazz.containsKey(resolveClazz) || baseRelatedClazz.containsValue(resolveClazz))) throw new IllegalStateException();

        hasGot = true;

        T returnedInstance;
        if (baseRelatedClazz.containsKey(resolveClazz)) {
            returnedInstance = (T) baseRelatedClazz.get(resolveClazz).newInstance();
        } else {
            returnedInstance = (T) resolveClazz.newInstance();
        }

        gotClazz.add(returnedInstance);
        getDependencyFieldInitialized(returnedInstance);

        close();
        return returnedInstance;
    }

    @Override
    public void close() {
        List<Throwable> exceptions = new ArrayList<>();
        Collections.reverse(gotClazz);
            gotClazz.forEach(clazz -> {
                if (Arrays.stream(clazz.getClass().getInterfaces()).anyMatch(derivedInterface -> derivedInterface == AutoCloseable.class)) {
                    try {
                        clazz.getClass().getDeclaredMethod("close").invoke(clazz);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        exceptions.add(e);
                    }
                }
            });
            if (exceptions.size() != 0) exceptions.get(0).printStackTrace();
    }

    private <T> void getDependencyFieldInitialized(T returnedInstance) {
        getAllFields(new ArrayList<>(), returnedInstance.getClass()).stream()
                .filter(field -> field.isAnnotationPresent(CreateOnTheFly.class))
                .forEach(field -> {
                    if (!registeredBeanClazz.contains(field.getType())) throw new IllegalStateException();
                    try {
                        field.setAccessible(true);
                        field.set(returnedInstance, field.getType().newInstance());
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                });
    }

    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
        List<Field> sortedList = Arrays.asList(type.getDeclaredFields());
        fields.addAll(fields);

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return sortedList;
    }

    private void isValidBeanClazz(Class<?> beanClazz) {
        isCondition(beanClazz == null, "beanClazz is mandatory");
        isCondition(beanClazz.getConstructors().length == 0, beanClazz.getName() + " is abstract");
        isCondition(Arrays.stream(beanClazz.getConstructors()).noneMatch(constructor -> constructor.getParameterTypes().length == 0),
                beanClazz.getName() + " has no default constructor");
    }

    private void isCondition(boolean condition, String message) {
        if (condition) throw new IllegalArgumentException(message);
    }
}
