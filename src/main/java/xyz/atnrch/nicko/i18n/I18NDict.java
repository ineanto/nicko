package xyz.atnrch.nicko.i18n;

public class I18NDict {
    public static class Error {
        public static final String ERROR_KEY = "error.";

        public static final String GENERIC = ERROR_KEY + "generic";
        public static final String PERMISSION = ERROR_KEY + "permission";
        public static final String CACHE = ERROR_KEY + "cache";
        public static final String MOJANG_NAME = ERROR_KEY + "mojang_name";
        public static final String MOJANG_SKIN = ERROR_KEY + "mojang_skin";
        @Deprecated
        public static final String INVALID_USERNAME = ERROR_KEY + "invalid_username";
        public static final String SQL_ERROR = ERROR_KEY + "sql";
        public static final String JSON_ERROR = ERROR_KEY + "json";
    }

    public static class Event {
        private static final String EVENT_KEY = "event.";

        public static class Admin {
            private static final String ADMIN_KEY = EVENT_KEY + "admin.";

            public static class Cache {
                private static final String CACHE_KEY = ADMIN_KEY + "cache.";

                public static final String INVALIDATE_CACHE = CACHE_KEY + "invalidate_cache";
                public static final String INVALIDATE_ENTRY = CACHE_KEY + "invalidate_entry";

            }
        }

        public static class Settings {
            private static final String SETTINGS_KEY = EVENT_KEY + "settings.";

            public static final String ERROR = SETTINGS_KEY + "error";
        }

        public static class Appearance {
            private static final String APPEARANCE_KEY = EVENT_KEY + "appearance.";

            public static class Set {
                public static final String SET_KEY = APPEARANCE_KEY + "set.";

                public static final String OK = SET_KEY + "ok";
                public static final String ERROR = SET_KEY + "error";
            }

            public static class Remove {
                private static final String REMOVE_KEY = APPEARANCE_KEY + "remove.";

                public static final String OK = REMOVE_KEY + "ok";
                public static final String MISSING = REMOVE_KEY + "missing";
                public static final String ERROR = REMOVE_KEY + "error";
            }

            public static class Restore {
                private static final String RESTORE_KEY = APPEARANCE_KEY + "restore.";

                public static final String OK = RESTORE_KEY + "ok";
                public static final String ERROR = RESTORE_KEY + "error";
            }
        }
    }

    public static class GUI {
        private static final String GUI_KEY = "gui.";

        public static final String EXIT = GUI_KEY + "exit";
        public static final String GO_BACK = GUI_KEY + "go_back";

        public static class Home {
            private static final String HOME_KEY = GUI_KEY + "home.";

            public static final String TITLE = HOME_KEY + "title";
            public static final String ADMIN = HOME_KEY + "admin";
            public static final String CHANGE_NAME = HOME_KEY + "change_name";
            public static final String CHANGE_SKIN = HOME_KEY + "change_skin";
            public static final String CHANGE_BOTH = HOME_KEY + "change_both";
            public static final String RESET = HOME_KEY + "reset";
            public static final String SETTINGS = HOME_KEY + "settings";
        }

        public static class Settings {
            private static final String SETTINGS_KEY = GUI_KEY + "settings.";

            public static final String TITLE = SETTINGS_KEY + "title";
            public static final String LANGUAGE = SETTINGS_KEY + "language";
            public static final String BUNGEECORD = SETTINGS_KEY + "bungeecord";
        }

        public static class Admin {
            private static final String ADMIN_KEY = GUI_KEY + "admin.";

            public static final String TITLE = ADMIN_KEY + "title";
            public static final String MANAGE_CACHE = ADMIN_KEY + "manage_cache";
            public static final String MANAGE_PLAYER = ADMIN_KEY + "manage_player";
        }
    }
}
