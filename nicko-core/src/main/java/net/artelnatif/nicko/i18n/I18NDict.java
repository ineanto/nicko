package net.artelnatif.nicko.i18n;

public class I18NDict {
    public enum Event {
        DISGUISE_SUCCESS("disguise.success"),
        DISGUISE_FAIL("disguise.fail"),
        PREVIOUS_SKIN_APPLIED("previous_skin_applied.ok"),
        PREVIOUS_SKIN_APPLY_FAIL("previous_skin_applied.fail");

        private final String messageKey;

        Event(String messageKey) {
            this.messageKey = "event." + messageKey;
        }

        public String getKey() {
            return messageKey;
        }
    }

    public enum Error {
        SKIN_FAIL_MOJANG("couldnt_get_skin_from_mojang"),
        SKIN_FAIL_CACHE("couldnt_get_skin_from_cache"),
        NAME_FAIL_MOJANG("couldnt_get_name_from_mojang"),
        UNEXPECTED_ERROR("generic");

        private final String messageKey;

        Error(String messageKey) {
            this.messageKey = "error." + messageKey;
        }

        public String getKey() {
            return messageKey;
        }
    }
}
