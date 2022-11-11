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
}