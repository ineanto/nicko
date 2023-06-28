package xyz.atnrch.nicko.disguise;

public class ActionResult<R> {
    private final String errorKey;
    private boolean error = false;
    private R result;

    public ActionResult() {
        this.errorKey = null;
    }

    public ActionResult(String errorMessage) {
        this.errorKey = errorMessage;
        this.error = true;
        this.result = null;
    }

    public ActionResult(R result) {
        this.errorKey = null;
        this.result = result;
    }

    public R getResult() {
        return result;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
