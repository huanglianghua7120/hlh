package saaf.common.fmw.message.utils;

public class NotTokenException extends Exception {
    public NotTokenException(String string, Throwable throwable, boolean b, boolean b1) {
        super(string, throwable, b, b1);
    }

    public NotTokenException(Throwable throwable) {
        super(throwable);
    }

    public NotTokenException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public NotTokenException(String string) {
        super(string);
    }

    public NotTokenException() {
        super();
    }
}
