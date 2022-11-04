package net.artelnatif.nicko.i18n;

public class I18NDict {
    private String key;

    public I18NDict(String key) {
        this.key = key;
    }

    public static class Event {
        public static final I18NDict UNDISGUISE_SUCCESS = new I18NDict("undisguise.success");
        public static final I18NDict UNDISGUISE_FAIL = new I18NDict("undisguise.fail");
        public static final I18NDict UNDISGUISE_NOTACTIVE = new I18NDict("undisguise.notactive");
        public static final I18NDict DISGUISE_SUCCESS = new I18NDict("disguise.success");
        public static final I18NDict DISGUISE_FAIL = new I18NDict("disguise.fail");
        public static final I18NDict PREVIOUS_SKIN_APPLIED = new I18NDict("previous_skin_applied.success");
        public static final I18NDict PREVIOUS_SKIN_APPLY_FAIL = new I18NDict("previous_skin_applied.fail");
    }

    public static class Error {
        public static final I18NDict SKIN_FAIL_MOJANG = new I18NDict("couldnt_get_skin_from_mojang");
        public static final I18NDict SKIN_FAIL_CACHE = new I18NDict("couldnt_get_skin_from_cache");
        public static final I18NDict NAME_FAIL_MOJANG = new I18NDict("couldnt_get_name_from_mojang");
        public static final I18NDict INVALID_USERNAME = new I18NDict("invalid_username");
        public static final I18NDict UNEXPECTED_ERROR = new I18NDict("generic");
    }

    public String getKey() {
        return key;
    }
}
