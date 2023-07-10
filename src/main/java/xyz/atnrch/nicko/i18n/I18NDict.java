package xyz.atnrch.nicko.i18n;

public class I18NDict {
    public static class Event {
        public static final String EVENT_KEY = "event.";

        public static class Admin {
            public static final String ADMIN_KEY = EVENT_KEY + "admin.";

            public static class Cache {
                public static final String CACHE_KEY = ADMIN_KEY + "cache.";

                public static final String INVALIDATE_ALL = CACHE_KEY + "invalidate_all";
                public static final String INVALIDATE_ENTRY = CACHE_KEY + "invalidate_entry";

            }
        }

        public static class Appearance {
            public static final String APPEARANCE_KEY = EVENT_KEY + "appearance.";

            public static class Set {
                public static final String SET_KEY = APPEARANCE_KEY + "set.";

                public static final String OK = SET_KEY + "ok";
                public static final String ERROR = SET_KEY + "error";
            }

            public static class Remove {
                public static final String REMOVE_KEY = APPEARANCE_KEY + "remove.";

                public static final String OK = REMOVE_KEY + "ok";
                public static final String MISSING = REMOVE_KEY + "missing";
                public static final String ERROR = REMOVE_KEY + "error";
            }

            public static class Restore {
                public static final String RESTORE_KEY = APPEARANCE_KEY + "restore.";

                public static final String OK = RESTORE_KEY + "ok";
                public static final String ERROR = RESTORE_KEY + "error";
            }
        }
    }

    public static class Error {
        public static final String GENERIC = "error.generic";
        public static final String PERMISSION = "error.permission";
        public static final String PLAYER_OFFLINE = "error.offline";
        public static final String CACHE = "error.cache";
        public static final String MOJANG_NAME = "error.mojang_name";
        public static final String MOJANG_SKIN = "error.mojang_skin";
        public static final String INVALID_USERNAME = "error.invalid_username";
        public static final String SQL_ERROR = "error.sql";
        public static final String JSON_ERROR = "error.json";
    }
}
