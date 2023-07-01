package xyz.atnrch.nicko.disguise;

public class ActionResult {
    private final String errorKey;
    private boolean error = false;

    public static ActionResult ok() {
        return new ActionResult();
    }

    public static ActionResult error(String errorMessage) {
        return new ActionResult(errorMessage);
    }

    private ActionResult() {
        this.errorKey = null;
    }

    private ActionResult(String errorMessage) {
        this.errorKey = errorMessage;
        this.error = true;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
