package xyz.atnrch.nicko.i18n;

public class I18NDict {
    private final String key;

    public I18NDict(String key) { this.key = key; }

    public static class Event {
        public static class Admin {
            public static final I18NDict CACHE_CLEAN = new I18NDict("event.admin.cache_clear");
        }

        public static class Disguise {
            public static final I18NDict SUCCESS = new I18NDict("event.disguise.success");
            public static final I18NDict FAIL = new I18NDict("event.disguise.fail");
        }

        public static class Undisguise {
            public static final I18NDict SUCCESS = new I18NDict("event.undisguise.success");
            public static final I18NDict FAIL = new I18NDict("event.undisguise.fail");
            public static final I18NDict NONE = new I18NDict("event.undisguise.none");
        }

        public static class PreviousSkin {
            public static final I18NDict SUCCESS = new I18NDict("event.previous_skin_applied.success");
            public static final I18NDict FAIL = new I18NDict("event.previous_skin_applied.fail");
        }
    }

    public static class Error {
        public static final I18NDict PLAYER_OFFLINE = new I18NDict("error.player_offline");
        public static final I18NDict SKIN_FAIL_MOJANG = new I18NDict("error.couldnt_get_skin_from_mojang");
        public static final I18NDict SKIN_FAIL_CACHE = new I18NDict("error.couldnt_get_skin_from_cache");
        public static final I18NDict NAME_FAIL_MOJANG = new I18NDict("error.couldnt_get_name_from_mojang");
        public static final I18NDict INVALID_USERNAME = new I18NDict("error.invalid_username");
        public static final I18NDict UNEXPECTED_ERROR = new I18NDict("error.generic");
        public static final I18NDict SQL_ERROR = new I18NDict("error.sql");
        public static final I18NDict JSON_ERROR = new I18NDict("error.json");
    }

    public String key() {
        return key;
    }
}
