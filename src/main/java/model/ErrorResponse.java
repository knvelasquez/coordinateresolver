package model;

public class ErrorResponse {
    private final String cause;
    private final String detail;
    private final String datetime;
    private final int status;

    public ErrorResponse(String cause, String detail, String datetime, int status) {
        this.cause = cause;
        this.detail = detail;
        this.datetime = datetime;
        this.status = status;
    }

    public String getCause() {
        return cause;
    }

    public String getDetail() {
        return detail;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getStatus() {
        return status;
    }
}
