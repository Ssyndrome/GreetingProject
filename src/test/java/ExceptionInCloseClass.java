public class ExceptionInCloseClass implements AutoCloseable{
    private boolean isClosed = false;

    @Override
    public void close() throws Exception {
        throw new constructorException("Close error");
    }


}
