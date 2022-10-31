package com.niranjan.androidtutorials.quotes.network

import com.google.gson.Gson
import okhttp3.*

/**
 * A list of fake results to return.
 */
private val FAKE_RESULTS = listOf(
    "Life isn’t about getting and having, it’s about giving and being. - Kevin Kruse",
    "Whatever the mind of man can conceive and believe, it can achieve. - Napoleon Hill",
    "Strive not to be a success, but rather to be of value. - Albert Einstein",
    "Two roads diverged in a wood, and I—I took the one less traveled by, And that has made all the difference.- Robert Frost",
    "I attribute my success to this: I never gave or took any excuse.- Florence Nightingale",
    "You miss 100% of the shots you don’t take. - Wayne Gretzky",
    "I’ve missed more than 9000 shots in my career. I’ve lost almost 300 games. 26 times I’ve been trusted to take the game winning shot and missed. I’ve failed over and over and over again in my life. And that is why I succeed. -Michael Jordan",
    "Life is what happens to you while you’re busy making other plans.- John Lennon"
)

/**
 * This class will return fake [Response] objects to Retrofit, without actually using the network.
 */
class SkipNetworkInterceptor: Interceptor {
    private var lastResult: String = ""
    val gson = Gson()

    private var attempts = 0

    /**
     * Return true iff this request should error.
     */
    private fun wantRandomError() = attempts++ % 5 == 0

    /**
     * Stop the request from actually going out to the network.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        pretendToBlockForNetworkRequest()
        return if (wantRandomError()) {
            makeErrorResult(chain.request())
        } else {
            makeOkResult(chain.request())
        }
    }

    /**
     * Pretend to "block" interacting with the network.
     *
     * Really: sleep for 500ms.
     */
    private fun pretendToBlockForNetworkRequest() = Thread.sleep(500)

    /**
     * Generate an error result.
     *
     * ```
     * HTTP/1.1 500 Bad server day
     * Content-type: application/json
     *
     * {"cause": "not sure"}
     * ```
     */
    private fun makeErrorResult(request: Request): Response {
        return Response.Builder()
            .code(500)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("Bad server day")
            .body(
                ResponseBody.create(
                MediaType.get("application/json"),
                gson.toJson(mapOf("cause" to "not sure"))))
            .build()
    }

    /**
     * Generate a success response.
     *
     * ```
     * HTTP/1.1 200 OK
     * Content-type: application/json
     *
     * "$random_string"
     * ```
     */
    private fun makeOkResult(request: Request): Response {
        var nextResult = lastResult
        while (nextResult == lastResult) {
            nextResult = FAKE_RESULTS.random()
        }
        lastResult = nextResult
        return Response.Builder()
            .code(200)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(ResponseBody.create(
                MediaType.get("application/json"),
                gson.toJson(nextResult)))
            .build()
    }
}