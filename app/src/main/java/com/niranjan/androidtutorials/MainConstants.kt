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

    const val DELAY_TIME_MILLIS: Long = 3000
}