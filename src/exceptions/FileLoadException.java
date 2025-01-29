package exceptions;

public class FileLoadException extends RuntimeException {
    public FileLoadException(String message) {
        super(message);
    }
}
