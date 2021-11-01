package com.example.ratingexample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class AppUtils {
    public static boolean openReviewDialog(Activity context) {
        SharedPreferences sharedPreferences=context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        int review=sharedPreferences.getInt("review",0);
        editor.putInt("review",++review).commit();
        if (review<=10||review%10!=0) return false;

        ReviewManager manager = ReviewManagerFactory.create(context);
//        ReviewManager manager = new FakeReviewManager(context);
        Task<ReviewInfo> request = manager.requestReviewFlow().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                Task<Void> flow = manager.launchReviewFlow(context, reviewInfo).addOnCompleteListener(task2 -> {
//                    if (task2.getException()!=null)
//                    Toast.makeText(context, task2.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, log or handle the error code.
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                @ReviewErrorCode int reviewErrorCode = ((TaskException) task.getException()).getErrorCode();
            }
        });

        return true;
    }
}
