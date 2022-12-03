package com.niranjan.androidtutorials

object MainConstants {
    enum class Feature(val value: String) {
        SLOT("Slot Machine"),
        PRICE("Price Calculator"),
        QUIZ("Quiz"),
        STOPWATCH("STOPWATCH")
    }

    const val SLOT_EXTRA_KEY = "SLOT_EXTRA_KEY"

    /**
     * Define the ViewType constants
     */
    enum class MainViewType {
        VIEW_TYPE_APP_TITLE,
        VIEW_TYPE_TOPICS
    }

    /**
     * WorkManager Constants
     */

    // Notification Channel constants
    // Name of Notification Channel for verbose notifications of background work
    @JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
        "Verbose WorkManager Notifications"
    const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
        "Shows notifications whenever work starts"
    @JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
    const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
    const val NOTIFICATION_ID = 1

    // The name of the foto editing work
    const val FOTO_EDITING_WORK_NAME = "foto_editing_work"

    // Other keys
    const val OUTPUT_PATH = "foto_editing_outputs"
    const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
    const val TAG_OUTPUT = "OUTPUT"
    const val TAG_IMAGE_BLURRED = "IMAGE_BLURRED"

    const val DELAY_TIME_MILLIS: Long = 3000

    // Work Manager
    const val WORK_REQUEST_INPUT_DATA_KEY = "WORK_REQUEST_INPUT_DATA_KEY"
    const val WORK_REQUEST_OUTPUT_DATA = "WORK_REQUEST_OUTPUT_DATA"
    const val WORK_REQUEST_INPUT_DATA_VALUE = "WORK_REQUEST_INPUT_DATA_VALUE"
    const val MY_UNIQUE_WORK = "MY_UNIQUE_WORK"
    const val MY_WORK_A_ID = "MY_WORK_A_ID"
    const val MY_WORK_TAG_A = "MY_WORK_TAG_A"
    const val MY_WORK_TAG_B = "MY_WORK_TAG_B"
    const val MY_WORK_TAG_C = "MY_WORK_TAG_C"
    const val MY_WORK_TAG_D = "MY_WORK_TAG_E"
}