package com.phearom.api.keys;

public interface IntentKeys {
    String PERMISSION_ADMIN = "com.phearom.superreminder.permission.ADMINISTRATOR";
    String EVENT_NOTIFICATION = "com.phearom.superreminder.EVENT_NOTIFICATION";
    String BROADCAST_ACTION= "com.phearom.superreminder.BROADCAST_ACTION";

    String EXTRA_EMIT_DATA = "com.phearom.extra.EMIT_DATA";
    String EXTRA_EMIT_KEY_RESULT = "com.phearom.extra.EMIT_KEY";
    String EXTRA_DATA = "com.phearom.extra.EXTRA_DATA";

    int CONNECTED = 1;
    int NO_SOCKET_CONNECTION = 0;
    int NO_INTERNET_CONNECTION = -1;
    int CONNECTING = 2;
}
