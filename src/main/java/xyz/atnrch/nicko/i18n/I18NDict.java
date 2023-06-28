package xyz.atnrch.nicko.i18n;

public class I18NDict {
    public static class Event {
        public static class Admin {
            public static final String CACHE_CLEAN = "event.admin.cache_clear";
        }

        public static class Disguise {
            public static final String SUCCESS = "event.disguise.success";
            public static final String FAIL = "event.disguise.fail";
        }

        public static class Undisguise {
            public static final String SUCCESS = "event.undisguise.success";
            public static final String FAIL = "event.undisguise.fail";
            public static final String NONE = "event.undisguise.none";
        }

        public static class PreviousSkin {
            public static final String SUCCESS = "event.previous_skin_applied.success";
            public static final String FAIL = "event.previous_skin_applied.fail";
        }
    }

    public static class Error {
        public static final String UNEXPECTED_ERROR = "error.invalid_name";
        public static final String PLAYER_OFFLINE = "error.player_offline";
        public static final String SKIN_FAIL_MOJANG = "error.couldnt_get_skin_from_mojang";
        public static final String SKIN_FAIL_CACHE = "error.couldnt_get_skin_from_cache";
        public static final String NAME_FAIL_MOJANG = "error.couldnt_get_name_from_mojang";
        public static final String INVALID_USERNAME = "error.invalid_username";
        public static final String SQL_ERROR = "error.sql";
        public static final String JSON_ERROR = "error.json";
    }
}
