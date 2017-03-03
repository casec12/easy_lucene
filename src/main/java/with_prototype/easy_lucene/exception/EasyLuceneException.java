package with_prototype.easy_lucene.exception;

public class EasyLuceneException extends RuntimeException {

    public EasyLuceneException(String message) {
        super(message);
    }

    public EasyLuceneException(String message, Throwable cause) {
        super(message, cause);
    }

    public EasyLuceneException(Throwable cause) {
        super(cause);
    }

    protected EasyLuceneException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
