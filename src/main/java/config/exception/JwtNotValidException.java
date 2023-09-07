package config.exception;

public class JwtNotValidException extends RuntimeException {
    public JwtNotValidException() {
        super("Jwt is not valid");
    }
}
