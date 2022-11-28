package com.niranjan.androidtutorials.foto.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.niranjan.androidtutorials.MainConstants.KEY_IMAGE_URI

private const val TAG = "BlurWorker"
/***
 * [BlurWorker] extends the [Worker] class and perform the blurring work in
 * background.
 * [makeStatusNotification] is the helper class to show the notification about
 * Work Status
 *
 * We return Result
 *
 * [Result.success] -> if work success
 *
 * [Result.failure] -> if work fails
 */
class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val appContext = applicationContext
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification("Blurring image", appContext)
        // This is an utility function added to emulate slower work.
        sleep()
        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))
            val output = blurBitmap(picture, appContext)
            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            makeStatusNotification("Success Blurring $outputData", applicationContext)
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            throwable.printStackTrace()
            makeStatusNotification("Failure Blurring", applicationContext)
            Result.failure()
        }
    }
}