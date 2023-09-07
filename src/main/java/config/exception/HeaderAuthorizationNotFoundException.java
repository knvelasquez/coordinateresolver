package config.exception;

public class HeaderAuthorizationNotFoundException extends RuntimeException {
    public HeaderAuthorizationNotFoundException() {
        super("Header Authorization was not found");
    }
}
