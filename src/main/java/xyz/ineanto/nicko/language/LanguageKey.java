package xyz.ineanto.nicko.language;

public class LanguageKey {
    public static final String PREFIX = "prefix";
    public static final String WHOOSH = "whoosh";
    public static final String OOPS = "oops";

    public static class Error {
        public static final String ERROR_KEY = "error.";

        public static final String PERMISSION = ERROR_KEY + "permission";
        public static final String CACHE = ERROR_KEY + "cache";
        public static final String MOJANG = ERROR_KEY + "mojang";
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

            public static class Check {
                private static final String CHECK_KEY = ADMIN_KEY + "check.";

                public static final String REMOVE_SKIN = CHECK_KEY + "remove_skin";
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

                public static final String CHAT_PROMPT_NAME = SET_KEY + "chat_prompt_name";
                public static final String CHAT_PROMPT_SKIN = SET_KEY + "chat_prompt_skin";
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
        public static final String UNAVAILABLE = GUI_KEY + "unavailable";
        public static final String LOADING = GUI_KEY + "loading";
        public static final String SCROLL_UP = GUI_KEY + "scroll_up";
        public static final String SCROLL_DOWN = GUI_KEY + "scroll_down";
        public static final String NEW_SKIN = GUI_KEY + "new_skin";
        public static final String NEW_NAME = GUI_KEY + "new_name";

        public static class Titles {
            public static final String TITLE_KEY = GUI_KEY + "title.";

            public static final String HOME = TITLE_KEY + "home";
            public static final String SETTINGS = TITLE_KEY + "settings";
            public static final String ADMIN = TITLE_KEY + "admin";
            public static final String CHECK = TITLE_KEY + "check";
            public static final String CONFIRM = TITLE_KEY + "confirm";
            public static final String CACHE = TITLE_KEY + "cache";
            public static final String INVALIDATE_SKIN = TITLE_KEY + "invalidate_skin";
            public static final String FAVORITES = TITLE_KEY + "favorites";
        }

        public static class Choice {
            private static final String CHOICE_KEY = GUI_KEY + "choice.";

            public static final String CONFIRM = CHOICE_KEY + "confirm";
            public static final String CHOOSE = CHOICE_KEY + "choose";
            public static final String CANCEL = CHOICE_KEY + "cancel";
        }

        public static class Home {
            private static final String HOME_KEY = GUI_KEY + "home.";

            public static final String ADMIN = HOME_KEY + "admin";
            public static final String CHANGE_NAME = HOME_KEY + "change_name";
            public static final String CHANGE_SKIN = HOME_KEY + "change_skin";
            public static final String CHANGE_BOTH = HOME_KEY + "change_both";
            public static final String RESET = HOME_KEY + "reset";
            public static final String RANDOM_SKIN = HOME_KEY + "random_skin";
            public static final String SETTINGS = HOME_KEY + "settings";
            public static final String FAVORITES = HOME_KEY + "favorites";
        }

        public static class Settings {
            private static final String SETTINGS_KEY = GUI_KEY + "settings.";

            public static final String CYCLING_CHOICES = SETTINGS_KEY + "cycling_choices";
            public static final String TOGGLEABLE_BUTTON = SETTINGS_KEY + "toggleable_button";

            public static final String LANGUAGE = SETTINGS_KEY + "language";
            public static final String RANDOM_SKIN = SETTINGS_KEY + "random_skin";
        }

        public static class Admin {
            private static final String ADMIN_KEY = GUI_KEY + "admin.";

            public static final String MANAGE_CACHE = ADMIN_KEY + "manage_cache";
            public static final String MANAGE_PLAYER = ADMIN_KEY + "manage_player";
            public static final String CHECK = ADMIN_KEY + "check";

            public static class Cache {
                private static final String CACHE_KEY = ADMIN_KEY + "cache.";

                public static final String STATISTICS = CACHE_KEY + "statistics";
                public static final String INVALIDATE_CACHE = CACHE_KEY + "invalidate_cache";
                public static final String INVALIDATE_SKIN = CACHE_KEY + "invalidate_skin";
                public static final String ENTRY = CACHE_KEY + "entry";
            }
        }

        public static class Favorites {
            private static final String FAVORITES_KEY = GUI_KEY + "favorites.";

            public static final String ADD = FAVORITES_KEY + "add";
            public static final String REMOVE = FAVORITES_KEY + "remove";
            public static final String ENTRY = FAVORITES_KEY + "entry";
        }
    }
}
