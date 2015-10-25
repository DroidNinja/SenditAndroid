package com.binarywalllabs.sendit.constants;

import com.binarywalllabs.sendit.BuildConfig;

/**
 * Created by Arun on 06-09-2015.
 */
public class AppConstants {

    public static final String BASE_URL = BuildConfig.SERVER_URL;

    public static final String WEB_SERVICE_DATE_FORMAT = "YYYY-MM-DD HH:MM:SS";
    public enum SharedPreferenceKeys {
        USER_NAME("userName"),
        USER_EMAIL("userEmail"),
        USER_IMAGE_URL("userImageUrl"),
        DEVICE_REGISTERED("deviceRegistered"),
        DEVICE_TOKEN("deviceToken");


        private String value;

        SharedPreferenceKeys(String value) {
            this.value = value;
        }

        public String getKey() {
            return value;
        }
    }

}
