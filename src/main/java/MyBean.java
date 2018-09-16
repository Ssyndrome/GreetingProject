public class MyBean implements AutoCloseable {
    private boolean isClosed = false;

    @Override
    public void close() {
        isClosed = true;
    }

    @CreateOnTheFly
    private MyDependency dependency;
}
