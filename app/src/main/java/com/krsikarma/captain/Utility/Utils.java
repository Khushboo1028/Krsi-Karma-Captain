package com.krsikarma.captain.Utility;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.krsikarma.captain.R;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {

    public static final String TAG = "UtilsClass";
    AlertDialog.Builder builder;
    AlertDialog alert;
    Boolean isTranslateModelDownloaded = false;
    Translator englishHindiTranslator;

    public static String capitalizeString(String str) {
        String retStr = str;
        try { // We can face index out of bound exception if the string is null
            retStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        } catch (Exception e) {
        }
        return retStr;
    }

    public static String getFormattedNumber(String number) {
        DecimalFormat formatter = new DecimalFormat("##,##,##,###");
        return formatter.format(Integer.parseInt(number));
    }

    public void alertDialogOK(Activity mActivity, String title, String message) {
        builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(mActivity.getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    public boolean isStringNull(String item) {
        if (item == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInternetAvailable(Activity mActivity) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {

            builder = new AlertDialog.Builder(mActivity);
            builder.setMessage(mActivity.getString(R.string.internet_message))
                    .setCancelable(true)
                    .setPositiveButton(mActivity.getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alert.dismiss();
                        }
                    });
            alert = builder.create();
            alert.setTitle(mActivity.getString(R.string.error_text));
            alert.show();
        }
        return false;
    }

    public void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public void translateEnglishToHindi(String text, TextView textView){
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.HINDI)
                        .build();
        englishHindiTranslator =
                Translation.getClient(options);

        //check if translation model exists in device

        DownloadConditions conditions = new DownloadConditions.Builder()
                .build();
        englishHindiTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        isTranslateModelDownloaded = true;

                        englishHindiTranslator.translate(text)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        try{
                                            textView.setText(s);
                                        }catch (Exception e){
                                            Log.i(TAG, "error: " + e.getMessage());
                                        }

                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Log.i(TAG, "could not translate " + e.getLocalizedMessage());
                                            }
                                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn???t be downloaded or other internal error.
                        Log.i(TAG, "Translator could not be downloaded " +e.getLocalizedMessage() );
                    }
                });


    }


    public void closeTranslator(){
        if(englishHindiTranslator!=null){
            englishHindiTranslator.close();
            englishHindiTranslator=null;
        }
    }
}
