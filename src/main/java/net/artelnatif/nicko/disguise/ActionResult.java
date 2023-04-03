package net.artelnatif.nicko.disguise;

import net.artelnatif.nicko.i18n.I18NDict;

public class ActionResult<R> {
    private final I18NDict errorMessage;
    private boolean error = false;
    private R result;

    public ActionResult() {
        this.errorMessage = null;
    }

    public ActionResult(I18NDict errorMessage) {
        this.errorMessage = errorMessage;
        this.error = true;
        this.result = null;
    }

    public ActionResult(R result) {
        this.errorMessage = null;
        this.result = result;
    }

    public R getResult() {
        return result;
    }

    public boolean isError() {
        return error;
    }

    public I18NDict getErrorMessage() {
        return errorMessage;
    }
}
