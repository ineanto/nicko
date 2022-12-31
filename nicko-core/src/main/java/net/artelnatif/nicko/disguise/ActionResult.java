package net.artelnatif.nicko.disguise;

import net.artelnatif.nicko.i18n.I18NDict;

public class ActionResult {
    private final I18NDict errorMessage;
    private boolean error = false;

    public ActionResult(I18NDict errorMessage) {
        this.error = true;
        this.errorMessage = errorMessage;
    }

    public ActionResult() {
        this.errorMessage = null;
    }

    public boolean isError() {
        return error;
    }

    public I18NDict getErrorMessage() {
        return errorMessage;
    }
}
