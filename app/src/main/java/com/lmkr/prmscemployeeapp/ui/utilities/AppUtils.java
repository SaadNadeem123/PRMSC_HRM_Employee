package com.lmkr.prmscemployeeapp.ui.utilities;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.components.BuildConfig;
import com.google.gson.Gson;
import com.lmkr.prmscemployeeapp.App;
import com.lmkr.prmscemployeeapp.R;
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse;
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData;
import com.lmkr.prmscemployeeapp.ui.fragments.ToastFragmentManager;
import com.lmkr.prmscemployeeapp.ui.locationUtils.LocationAlertDialogFragment;
import com.lmkr.prmscemployeeapp.ui.utilities.networkUtils.NetworkMonitorListener;
import com.lmkr.prmscemployeeapp.ui.utilities.networkUtils.NetworkObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.HttpException;
import retrofit2.Response;

public class AppUtils {

    public static final String TEMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String LOCAL_TIME_FORMAT = "EE MMM dd hh:mm:ss ZZZZ yyyy";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String APP_NAME = "PSDF";
    public static final String FORMAT_HOUR = "HH";
    public static final String FORMAT_MIN = "mm";
    public static final String FORMAT_DAY = "dd";
    public static final String FORMAT_MONTH = "MM";
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_MONTH_YEAR = "MMMM yyyy";
    public static final String FORMAT0 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String FORMAT1 = "yyyy-MM-dd HH:mm:ss.S";
    public static final String FORMAT2 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT3 = "yyyy-MM-dd";
    public static final String FORMAT4 = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String FORMAT5 = "hh:mm a";
    public static final String FORMAT6 = "HH:mm";
    public static final String FORMAT7 = "hh";
    public static final String FORMAT8 = "EEE";
    public static final String FORMAT9 = "dd, MMMM yyyy";
    public static final String FORMAT10 = "MMM dd, yyyy"; //   Nov 12, 2016
    public static final String FORMAT11 = "MM/dd/yyyy hh:mm a"; //    08/20/2018 6:13 PM
    public static final String FORMAT12 = "dd/MM/yyyy hh:mm a"; //    20/08/2018 6:13 PM
    public static final String FORMAT13 = "dd/MM/yyyy hh:mm:ss a"; //    20/08/2018 6:13 PM
    public static final String FORMAT14 = "yyyy-MM-dd HH:mm:ss"; //    20/08/2018 6:13
    public static final String FORMAT15 = "dd-MM-yyyy"; //    20/08/2018
    public static final String FORMAT16 = "dd-MM-yyyy hh:mm a"; //    20/08/2018 6:13 PM
    public static final String FORMAT17 = "MM/dd/yyyy"; //    08/20/2018
    public static final String FORMAT18 = "HH:mm:ss"; //    08/20/2018
    public static final String FORMAT19 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //    2020-11-09T19:00:00.000Z
    public static final String FORMAT20 = "MM-yyyy"; //    11-2020
    public static final String FORMAT21 = "yyyy-MM-dd'T'HH:mm:ss.SSS";   //    2020-11-09T19:00:00.000
    public static final String FORMAT22 = "dd-MM-yyyy   hh:mm a"; //    20/08/2018 6:13 PM
    public static final String FORMAT23 = "dd MMMM yyyy (EEEE)"; // 18 MARCH 2023 Wednesday
    public static final String FORMAT24 = "MMMM dd"; // MARCH 12
    public static final int MY_IGNORE_OPTIMIZATION_REQUEST = 5;
    public static final int STARTING = 0;
    public static final int ENDING = 1;
    public static final int ENDED = 2;
    public static final int INVALID = 3;
    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    private static final String TAG = "AppUtils";
    public static String USERID;
    public static int REQUEST_TIME_OUT_10_Min = 600000; //10min
    public static int REQUEST_TIME_OUT_30_Sec = 30000; //30sec
    public static int REQUEST_TIME_OUT = REQUEST_TIME_OUT_10_Min;
    public static String ENCODE_DECODE_STRING = "PSDF_FINGERPRINT_BASE64_";
    /*public static String getDeviceId(Context context) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }*/
    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    private static Toast toast;


    public static String getFormattedDateWithSuffix(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        if (!((day > 10) && (day < 19))) switch (day % 10) {
            case 1:
                return new SimpleDateFormat("d'st' MMMM yyyy\n hh:mm a").format(date);
            case 2:
                return new SimpleDateFormat("d'nd' MMMM yyyy\n hh:mm a").format(date);
            case 3:
                return new SimpleDateFormat("d'rd' MMMM yyyy\n hh:mm a").format(date);
            default:
                return new SimpleDateFormat("d'th' MMMM yyyy\n hh:mm a").format(date);
        }
        return new SimpleDateFormat("d'th' MMMM yyyy\n hh:mm a").format(date);
    }

    public static String getFormattedStringDateWithSuffixMultiLine(String dateString) {
        Date date = getDateFromString2(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        if (!((day > 10) && (day < 19))) switch (day % 10) {
            case 1:
                return new SimpleDateFormat("d'st' MMMM yyyy\n hh:mm a").format(date);
            case 2:
                return new SimpleDateFormat("d'nd' MMMM yyyy\n hh:mm a").format(date);
            case 3:
                return new SimpleDateFormat("d'rd' MMMM yyyy\n hh:mm a").format(date);
            default:
                return new SimpleDateFormat("d'th' MMMM yyyy\n hh:mm a").format(date);
        }
        return new SimpleDateFormat("d'th' MMMM yyyy\n hh:mm a").format(date);
    }

    public static String getFormattedStringDateWithSuffixSingleLine(String dateString) {
        Date date = getDateFromString2(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        if (!((day > 10) && (day < 19))) switch (day % 10) {
            case 1:
                return new SimpleDateFormat("d'st' MMMM yyyy hh:mm a").format(date);
            case 2:
                return new SimpleDateFormat("d'nd' MMMM yyyy hh:mm a").format(date);
            case 3:
                return new SimpleDateFormat("d'rd' MMMM yyyy hh:mm a").format(date);
            default:
                return new SimpleDateFormat("d'th' MMMM yyyy hh:mm a").format(date);
        }
        return new SimpleDateFormat("d'th' MMMM yyyy hh:mm a").format(date);
    }


    public static String getFormattedStringDateOnlyWithSuffix(String dateString) {
        Date date = getDateFromString2(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        if (!((day > 10) && (day < 19))) switch (day % 10) {
            case 1:
                return new SimpleDateFormat("d'st' MMMM yyyy").format(date);
            case 2:
                return new SimpleDateFormat("d'nd' MMMM yyyy").format(date);
            case 3:
                return new SimpleDateFormat("d'rd' MMMM yyyy").format(date);
            default:
                return new SimpleDateFormat("d'th' MMMM yyyy").format(date);
        }
        return new SimpleDateFormat("d'th' MMMM yyyy").format(date);
    }

    public static String encodeToBase64(byte[] fingerprint) {
        String base64 = null;
        try {
            base64 = Base64.encodeToString(fingerprint, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static boolean IsBase64(String base64String) {
        if (base64String.replace(" ", "").length() % 4 != 0) {
            return false;
        }

        try {
            byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static byte[] decodeFromoBase64(String fingerprintBase64) {
        byte[] bytes = null;
        try {
            bytes = Base64.decode(fingerprintBase64, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }


    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }


    public static long getDateDifference(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff;
    }

    public static String printDifference(Date startDate, Date endDate, int status) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthsInMilli = (long) (daysInMilli * 365.24 / 12);

        long elapsedMonths = different / monthsInMilli;
        different = different % monthsInMilli;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        String diff = "";

        if (elapsedMonths > 0) {
            diff += String.format("%d Mo, ", elapsedMonths);
        }
        if (elapsedDays > 0) {
            diff += String.format("%d Days, ", elapsedDays);
        }
        if (elapsedHours > 0) {
            diff += String.format("%d Hrs, ", elapsedHours);
        }
        if (elapsedMinutes > 0) {
            diff += String.format("%d Min, ", elapsedMinutes);
        }

        if ((status == STARTING || status == ENDING) && elapsedSeconds > 0) {
            diff += String.format("%d Sec, ", elapsedSeconds);
        }
        if (diff.length() > 0) {
            diff = diff.substring(0, diff.length() - 2);
        }

//        String diff = String.format("%d mo, %d days, %d hrs, %d min"/*, %d sec %n*/, elapsedMonths,
//                elapsedDays, elapsedHours, elapsedMinutes/*, elapsedSeconds*/);
        System.out.printf("%d months, %d days, %d hours, %d minutes, %d seconds%n", elapsedMonths, elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return elapsedMonths > 12 ? elapsedMonths / 12 + " Years" : diff;

    }

    public static String printDifference2(Date startDate, Date endDate, int status) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthsInMilli = (long) (daysInMilli * 365.24 / 12);

        long elapsedMonths = different / monthsInMilli;
        different = different % monthsInMilli;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String diff = "";

        if (elapsedMonths > 0) {
            if (elapsedMonths < 10) {
                diff += String.format("0%d : ", elapsedMonths);
            } else {
                diff += String.format("%d : ", elapsedMonths);
            }
        }

        if (elapsedDays > 0) {
            if (elapsedDays < 10) {
                diff += String.format("0%d : ", elapsedDays);
            } else {
                diff += String.format("%d : ", elapsedDays);
            }
        }

        if (elapsedHours > 0) {

            if (elapsedHours < 10) {
                diff += String.format("0%d : ", elapsedHours);
            } else {
                diff += String.format("%d : ", elapsedHours);
            }
        }

        if (elapsedMinutes >= 0) {
            if (elapsedMinutes < 10) {
                diff += String.format("0%d : ", elapsedMinutes);
            } else {
                diff += String.format("%d : ", elapsedMinutes);
            }
        }

        if ((status == STARTING || status == ENDING) && elapsedSeconds >= 0) {
            if (elapsedSeconds < 10) {
                diff += String.format("0%d : ", elapsedSeconds);
            } else {
                diff += String.format("%d : ", elapsedSeconds);
            }
        }

        if (diff.length() > 0) {
            diff = diff.substring(0, diff.length() - 2);
        }

        if (diff.endsWith(":")) {
            diff = diff.substring(0, diff.length() - 1);
        }

//        String diff = String.format("%d mo, %d days, %d hrs, %d min"/*, %d sec %n*/, elapsedMonths,elapsedDays, elapsedHours, elapsedMinutes/*, elapsedSeconds*/);

        System.out.printf("%d months, %d days, %d hours, %d minutes, %d seconds%n", elapsedMonths, elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return elapsedMonths > 12 ? elapsedMonths / 12 + " Years" : diff;

    }

    public static String printCountDownDifference(Context context, String start, String end) {

        Date startDate = getDateFromString2(start);
        Date endDate = getDateFromString2(end);

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : " + endDate);
//        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
//        long monthsInMilli = (long) (daysInMilli * 365.24 / 12);

       /* long elapsedMonths = different / monthsInMilli;
        different = different % monthsInMilli;*/

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


/*        String diff = "";

        if (elapsedMonths > 0) {
            if (elapsedMonths < 10) {
                diff += String.format("0%d : ", elapsedMonths);
            } else {
                diff += String.format("%d : ", elapsedMonths);
            }
        }

        if (elapsedDays > 0) {
            if (elapsedDays < 10) {
                diff += String.format("0%dd ", elapsedDays);
            } else {
                diff += String.format("%dd ", elapsedDays);
            }
        }

        if (elapsedHours > 0) {
            if (elapsedHours < 10) {
                diff += String.format("0%dh ", elapsedHours);
            } else {
                diff += String.format("%dh ", elapsedHours);
            }
        }

        if (elapsedMinutes >= 0) {
            if (elapsedMinutes < 10) {
                diff += String.format("0%dm ", elapsedMinutes);
            } else {
                diff += String.format("%dm ", elapsedMinutes);
            }
        }
        if (elapsedSeconds > 0) {
            if (elapsedSeconds < 10) {
                diff += String.format("0%ds ", elapsedSeconds);
            } else {
                diff += String.format("%ds ", elapsedSeconds);
            }
        }
        if (diff.endsWith(" ")) {
            diff = diff.substring(0, diff.length() - 1);
        }
*/


//        String diff = String.format("%dd %dh %dm %ds", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        String diff = "";

        if (elapsedDays > 0) {
            diff += elapsedDays + "d ";
        }
        if (elapsedHours > 0) {
            diff += elapsedHours + "h ";
        }
        if (elapsedMinutes > 0) {
            diff += elapsedMinutes + "m ";
        }
        if (elapsedSeconds > 0) {
            diff += elapsedSeconds + "s";
        }

        /*String[] countDown = diff.split(" ");

        String counter = "";
        int a = 0;
        for (int i = 0; i < countDown.length; i++) {
            if (a < 2) {
                counter += countDown[i] + " ";
                a++;
            }
            else {
                break;
            }
        }

        if(counter.length()<=0)
        {
            counter = String.format("%ds ", elapsedSeconds);
        }*/

//        return counter.trim();
        return diff.trim();
    }

    public static String generateRandomId() {
        return String.format("%05d-%04d-%04d-%04d%04d%04d", new Random().nextInt(100000), new Random().nextInt(10000), new Random().nextInt(10000), new Random().nextInt(10000), new Random().nextInt(10000), new Random().nextInt(10000));
    }

    public static Date addMinutesToDate(int minutes, Date beforeTime) {
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static void openFile(Context context, final String fileName, String type, String filePath) {

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory + File.separator + fileName);
        File f = (new File(filePath));
        Uri path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", f);

        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfOpenintent.setDataAndType(path, type);
        pdfOpenintent.putExtra(MediaStore.EXTRA_OUTPUT, path);
        try {
            context.startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {

        }
    }

/*
    public static void downloadFile(Context context, Document document) {
        try {
            String url = ApiCalls.BASE_DOCUMENT_URL;
            if (document.getDocumentPath().startsWith("/")) {
                url += document.getDocumentPath();
            } else {
                url += "/" + document.getDocumentPath();
            }

            String file = document.getDocumentName();
            DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                    .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS + File.separator, file)
                    .setTitle(file).setDescription(getString(R.string.save_file))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            String folder = "/Download";
            try { //V28 Below
                request.setDestinationInExternalPublicDir(folder, file);//v 28 allow to create and it deprecated method(v28+)

            } catch (Exception e) {

                //For Android  28+
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file);//(Environment.DIRECTORY_PICTURES,"picname.jpeg")
            }

            dm.enqueue(request);
            Toast.makeText(context, getString(R.string.downloading_file) + "/n" + document.getDocumentName(), Toast.LENGTH_LONG).show();
        } catch (IllegalStateException ex) {
            Toast.makeText(context, "Storage Error", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        } catch (Exception ex) {
            // just in case, it should never be called anyway
            Toast.makeText(context, "Unable to save file", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public static void downloadFile(Context context, FileModel document) {
        try {
            String url = ApiCalls.BASE_DOCUMENT_URL;
            if (document.getPath().startsWith("/")) {
                url += document.getPath();
            } else {
                url += "/" + document.getPath();
            }

            String file = document.getFileName();
            DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                    .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS + File.separator, file)
                    .setTitle(file).setDescription(getString(R.string.save_file))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            String folder = "/Download";
            try { //V28 Below
                request.setDestinationInExternalPublicDir(folder, file);//v 28 allow to create and it deprecated method(v28+)

            } catch (Exception e) {

                //For Android  28+
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file);//(Environment.DIRECTORY_PICTURES,"picname.jpeg")
            }

            dm.enqueue(request);
            Toast.makeText(context, getString(R.string.downloading_file) + "/n" + document.getFileName(), Toast.LENGTH_LONG).show();
        } catch (IllegalStateException ex) {
            Toast.makeText(context, "Storage Error", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        } catch (Exception ex) {
            // just in case, it should never be called anyway
            Toast.makeText(context, "Unable to save file", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
*/


    public static String getPreparationCountDownTime(String startDate, String endDate) {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        if (getDateFromString(endDate, FORMAT1).compareTo(getDateFromString(startDate, FORMAT1)) >= 0) {
            if (currentDate.compareTo(getDateFromString(startDate, FORMAT1)) < 0) {
                return "Cooking will be started in " + printDifference(currentDate, getDateFromString(startDate, FORMAT1), STARTING);

            } else if (currentDate.compareTo(getDateFromString(startDate, FORMAT1)) > 0 && currentDate.compareTo(getDateFromString(endDate, FORMAT1)) < 0) {
                return "Food will be ready in <br> <font color='red'> <b>" + printDifference(currentDate, getDateFromString(endDate, FORMAT1), ENDING) + "</b> </font>";
            } else {
                return "Food is ready ";//+ printDifference(getDateFromString(endDate, FORMAT1), currentDate, ENDED) + " Ago";
            }
        }

        return "Food is ready ";//"StartDate is after End Date \n" + printDifference(getDateFromString(startDate, FORMAT1), getDateFromString(endDate, FORMAT1), INVALID);


//        return getConvertedDateFromOneFormatToOther(startDate, AppUtils.FORMAT1, AppUtils.FORMAT10) + " - " + getConvertedDateFromOneFormatToOther(endDate, AppUtils.FORMAT1, AppUtils.FORMAT10);
    }

    public static String getConvertedDateFromOneFormatToOther(String date, String currentFormat, String requiredFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
            SimpleDateFormat sdf2 = new SimpleDateFormat(requiredFormat);
            Date c = null;
            c = sdf.parse(date);
            String date1 = sdf2.format(c);
//            System.out.println(date1);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getCurrentDateTimeForFilter() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT14);
        Date date = new Date();
        return formatter.format(date);
    }

    public static String getTodayDateWithStartTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT14);
        Date date = new Date();
        return (formatter.format(date)).split(" ")[0] + " 00:00:00";
    }

    public static String getTodayDateWithEndTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT14);
        Date date = new Date();
        return (formatter.format(date)).split(" ")[0] + " 23:59:59";
    }

    public static String get7DaysBackDateTimeForFilter() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT14);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();
        return (formatter.format(todate1)).split(" ")[0] + " 00:00:00";

    }

    public static String getConvertedDateFromOneFormatToOther_GMT_T0_OTHER_ZONE(String date, String currentFormat, String requiredFormat) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat sdf2 = new SimpleDateFormat(requiredFormat);
            sdf2.setTimeZone(TimeZone.getTimeZone("GMT+5"));
            Date c = null;
            c = sdf.parse(date);
            String date1 = sdf2.format(c);
//            System.out.println(date1);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Date getDateFromString(String date, String currentFormat) {
        if (date.contains("T")) {
            date = date.replace('T', ' ');
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
            Date c = null;
            c = sdf.parse(date);
            return c;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Date getDateFromString3(String date, String currentFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
            Date c = null;
            c = sdf.parse(date);
            return c;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateFromString2(String date) {
        if (date.contains("T")) {
            date = date.replace('T', ' ');
        }
        try {

            SimpleDateFormat sdf = new SimpleDateFormat();
            if (DATE_FORMAT.contains(".")) {
                sdf = new SimpleDateFormat(FORMAT0);
            } else {
                sdf = new SimpleDateFormat(FORMAT14);
            }
            Date c = null;
            c = sdf.parse(date);
            return c;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getStringFromDate(Date date, String currentFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

/*
    public static void setMontserrat(View view) {

        Typeface montserrat = null;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {//android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            montserrat = ResourcesCompat.getFont(App.getInstance(), R.font.montserrat);
        } else {
            // do something for phones running an SDK before lollipop
            montserrat = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Montserrat_Regular.ttf");
        }

        if (montserrat == null) {
            return;
        }

        try {

            if (view instanceof TextView)
                ((TextView) view).setTypeface(montserrat);
            else if (view instanceof AppCompatEditText)
                ((AppCompatEditText) view).setTypeface(montserrat);
            else if (view instanceof EditText)
                ((EditText) view).setTypeface(montserrat);
            else if (view instanceof Button)
                ((Button) view).setTypeface(montserrat);
            else if (view instanceof AutoCompleteTextView)
                ((AutoCompleteTextView) view).setTypeface(montserrat);
            else if (view instanceof RadioButton)
                ((RadioButton) view).setTypeface(montserrat);
            else if (view instanceof CheckBox)
                ((CheckBox) view).setTypeface(montserrat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMontserratBold(View view) {
        Typeface montserratBold = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {//android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            montserratBold = ResourcesCompat.getFont(App.getInstance(), R.font.montserrat_bold);
        } else {
            // do something for phones running an SDK before lollipop
            montserratBold = Typeface.createFromAsset(App.getInstance().getAssets(), "fonts/Montserrat_Bold.ttf");
        }

        if (montserratBold == null) {
            return;
        }
        try {

            if (view instanceof TextView)
                ((TextView) view).setTypeface(montserratBold);
            else if (view instanceof AppCompatEditText)
                ((AppCompatEditText) view).setTypeface(montserratBold);
            else if (view instanceof EditText)
                ((EditText) view).setTypeface(montserratBold);
            else if (view instanceof Button)
                ((Button) view).setTypeface(montserratBold);
            else if (view instanceof AutoCompleteTextView)
                ((AutoCompleteTextView) view).setTypeface(montserratBold);
            else if (view instanceof RadioButton)
                ((RadioButton) view).setTypeface(montserratBold);
            else if (view instanceof CheckBox)
                ((CheckBox) view).setTypeface(montserratBold);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

*/

    public static boolean canGetLocation(Context context) {
        boolean result = true;
        LocationManager lm = null;
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        if (lm == null)

            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        result = gpsEnabled && networkEnabled;

       /* if (!gpsEnabled && !networkEnabled) {
            // notify user
            (new AlertDialog.Builder(context)
                    .setMessage(R.string.location_disabled)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
            ).show();
        }*/

        return result;
    }

    /*  public static void showSettingsAlert(Context context) {
          androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.Dialog);

          alertDialog.setTitle(context.getResources().getString(R.string.gps_not_enabled));

          alertDialog.setMessage(context.getResources().getString(R.string.do_you_want_to_enable_gps));


          alertDialog.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                  context.startActivity(intent);
              }
          });


          alertDialog.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                  dialog.cancel();
              }
          });

          alertDialog.create().setOnShowListener(new DialogInterface.OnShowListener() {
              @Override
              public void onShow(DialogInterface dialog) {
                  Window view = ((AlertDialog) dialog).getWindow();
                  view.setBackgroundDrawableResource(R.drawable.white_rounded);
              }
          });


          alertDialog.show();
      }
  */

    public static void showLocationSettingsAlert(final AppCompatActivity context) {
        try {
            LocationAlertDialogFragment fragment = LocationAlertDialogFragment.getInstance();
            if (fragment.isAdded()) {
                return;
            }
            fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_NoTitle);
            fragment.show(context.getSupportFragmentManager(), "Location Enabler Fragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Window window = activity.getWindow();
            if (window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static int getScreenWidth(Activity activity) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int widthPixels = displaymetrics.widthPixels;
        return widthPixels;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static String getCurrentTimeStamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeStampGMT5() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT19);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5"));
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeStampGMT() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT19);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    public static String getStringFromMilliSeconds(long milliseconds, String format) {
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }

    public static long getCurrentTimeStampInMilliSeconds() {
        return System.currentTimeMillis();
    }
    
    public static long getTimeStampInMilliSecondsByThisYEAR() {
        Calendar firstDayOfCurrentYear = Calendar.getInstance();
        firstDayOfCurrentYear.set(Calendar.DATE, 1);
        firstDayOfCurrentYear.set(Calendar.MONTH, 0);
        return firstDayOfCurrentYear.getTimeInMillis();
    }

    public static long getlongFromStringDate(String timestamp, String dateFormat) {
        //Specifying the pattern of input date and time
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(timestamp);
            return date.getTime();
/*
            System.out.println("Given Time in milliseconds : "+date.getTime());

            Calendar calendar = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendar.setTime(date);
            System.out.println("Given Time in milliseconds : "+calendar.getTimeInMillis());
*/
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getCurrentTimeStampInMilliSeconds();
    }

    public static void deleteFolder(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles())
                deleteFolder(child);
        }
        fileOrDirectory.delete();
//        UpdateAndroidGallery();
    }

    public static String getDefaultTimeZoneDateTime(String datetime) {

        String scheduleTime = datetime;

        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();

        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
        Date date2 = null;
        try {
            date2 = readDate.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat writeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        writeDate.setTimeZone(new SimpleTimeZone(mGMTOffset, mTimeZone.getID()));
        scheduleTime = writeDate.format(date2);

        Log.i("Date2 = ", date2.toString());
        Log.i("scheduledTime = ", scheduleTime);

        return scheduleTime;
    }

    public static String getDateWithMonthInInteger(String date) {

        if (date == null || date.isEmpty()) {
            return "";
        }
        Log.e("date = ", date + ">>");
        date = date.replace(",", "");
        date = date.replace(".", "");
        date = date.replace(" ", "-");
        String[] datearray = date.split("-");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("MMM").parse(datearray[1]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int monthInt = cal.get(Calendar.MONTH) + 1;
        String day = Integer.parseInt(datearray[0]) < 10 ? "0" + datearray[0] : datearray[0];
        String mon = monthInt < 10 ? "0" + monthInt : String.valueOf(monthInt);
//        String newdate = day + "-" + mon + "-" + datearray[2];
        String newdate = datearray[2] + "-" + mon + "-" + day;

        return newdate;
    }

    public static String getCurrentTimeStampNoSeconds() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeOfDay() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeOfDayWithSeconds() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentHourOfDay() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_HOUR);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5"));
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentMinOfDay() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5"));
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeOnlyWithSeconds() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentDate() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentYear() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentMonth() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentMonthYearOnly() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeStamp(String outputFormat) {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat);

            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getSha1Hex(String clearString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    // Coverts a bitmap into a base64 image.
    public static String getBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String getDeviceDetails() {

        return (Build.MANUFACTURER + " -> " + Build.MODEL + " -> " + Build.VERSION.RELEASE);
    }

    public static void makeNotificationToast(String notification, Activity activity) {
        toast = new Toast(activity);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);

        View myToast = activity.getLayoutInflater().inflate(R.layout.toast_layout, activity.findViewById(R.id.toastParent_new), true);
        TextView textView = myToast.findViewById(R.id.toast_text);
        textView.setText(notification);
        toast.setView(myToast);
        toast.show();

    }

    public static boolean isDateTimeAuto(Context context) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        int timeSetting;
        int timeZoneSetting;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);

        if (!localTime.equalsIgnoreCase("+0500")) {
            makeAlertDialogDateTime("Info", "Set your timezone to GMT+0500 in your Date & Time settings", context);
            return false;
        }

        if (currentapiVersion < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            timeSetting = Settings.System.getInt(context.getContentResolver(), Settings.System.AUTO_TIME, 0);
            timeZoneSetting = Settings.System.getInt(context.getContentResolver(), Settings.System.AUTO_TIME_ZONE, 0);
        } else {
            timeSetting = Settings.System.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME, 0);
            timeZoneSetting = Settings.System.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0);
        }

        if (timeSetting == 0) {
            makeAlertDialogDateTime("Info", "Set your Date & time to Automatic/Network Provided in your Date & Time settings", context);
            return false;
        }
        if (timeZoneSetting == 0) {
            makeAlertDialogDateTime("Info", "Set your time zone to Automatic in your Date & Time settings", context);
            return false;
        }
        return true;
    }

    private static void makeAlertDialogDateTime(String info, String s, final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialogBuilder.setTitle(info);

        // Setting Dialog Message
        alertDialogBuilder.setMessage(s);
        AlertDialog alertDialog = alertDialogBuilder.create();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // do work
                        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                        context.startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        // do work
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // do work
                        break;
                    default:
                        break;
                }
            }
        };
        // On pressing Ok button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", listener);
//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", listener);
//        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel",

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                Button button = alertDialog.getButton(dialog.BUTTON_POSITIVE);
                button.setTextColor(context.getResources().getColor(R.color.black));
            }
        });


        alertDialog.show();
    }

    public static void hideNotificationToast() {
        if (toast != null) toast.cancel();
    }

    public static void makeNotification(String notification, Activity activity) {
        ToastFragmentManager manager = ToastFragmentManager.getInstance(activity);
        manager.showToast(notification);

//        AlertDialogUtils.showNotificationAlertDialog(activity,null,notification);
    }

    public static void makeNotificationWithCustomTime(String notification, long length, Activity activity) {
        ToastFragmentManager manager = ToastFragmentManager.getInstance(activity);
        manager.showToast(notification, length);
    }

    public static void hideNotification(Activity activity) {
        ToastFragmentManager manager = ToastFragmentManager.getInstance(activity);
        manager.hideToast();
    }

    public static void makeNotification(String notification, Activity activity, int icon) {
        try {
            ToastFragmentManager manager = ToastFragmentManager.getInstance(activity);
            manager.showToast(notification);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void apiSpecificTasks(Activity activity) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {

        }
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkPassword(String password) {
        if (password.length() >= 8) {
            for (int x = 0; x < password.length(); x++) {
                if (Character.isDigit(password.charAt(x))) {
                    return true;
                }

            }
            return false;
        } else {
            return false;
        }
    }

    public static void backButtonPress(AppCompatActivity activity) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(startMain);
        activity.overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    public static void setRippleAnimation(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.lift_on_touch);
            view.setStateListAnimator(stateListAnimator);
        }
    }

    public static void showKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isShowing = imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            if (!isShowing)
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }


    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams p) {
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static void setText(TextView textView, String text) {
        if (TextUtils.isEmpty(text)) {
            textView.setText("--");
        } else {
            textView.setText(text);
        }
    }

    public static String readStream(InputStream stream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(stream));

            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static void writeFile(byte[] data, File file) throws IOException {

        BufferedOutputStream bos = null;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static byte[] getFileContents(File file) {

        byte[] data = null;
        try {
            RandomAccessFile f = new RandomAccessFile(file.getPath(), "r");
            data = new byte[(int) f.length()];
            f.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getFilePathFromUri(Activity activity, Uri uri) {
        String path = "";
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return path;
    }

    public static void viewImage(Context context, String imagePath) {
        try {
            File file = new File(imagePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "image/*");
            context.startActivity(intent);
        } catch (Throwable t) {

        }
    }

    public static boolean checkExternalStorageState() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File getImageStoringFile() {
        String timestamp = System.currentTimeMillis() / 1000 + ".jpeg";

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), timestamp);
    }

    public static int getColor(int resId) {
        int color = 0;
        try {
            color = Resources.getSystem().getColor(resId);
        } catch (Exception e) {
        }
        return color;
    }

   /* public static int getAppBuildVersionCode() {
        return BuildConfig.VERSION_CODE;
    }*/

    public static String getAppBuildVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getString(int resId) {
        String s = "";
        try {
            s = App.getInstance().getString(resId);
        } catch (Exception e) {

        }

        return s;
    }

    public static List<String> getCameraImages(Context context) {

        String sort = MediaStore.Images.ImageColumns.DATE_MODIFIED/*_TAKEN*//*ADDED*/ + " DESC";
        final String[] projection = {MediaStore.Images.Media.DATA};
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sort);
        ArrayList<String> result = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                result.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public static void UpdateAndroidGallery(Context context) {
        File file = new File(getAppDirectory(context).getPath());
        file.delete();
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
                context.getApplicationContext().deleteFile(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getCurrentUtcTime() {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    public static Date getCurrentDateTime() {

        String currentUtcTime = getCurrentUtcTime();
        DateFormat inputFormatter1 = new SimpleDateFormat(DATE_FORMAT);
        Date date1 = null;
        try {
            date1 = inputFormatter1.parse(currentUtcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static String getCurrentDateTimeGMT5String() {

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT14, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5"));
        return sdf.format(new Date());
    }

    public static String getCurrentDateGMT5String() {

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT3, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5"));
        return sdf.format(new Date());
    }

    public static String getCurrentDateGMT5Format2String() {

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT15, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5"));
        return sdf.format(new Date());
    }

    public static Date getCurrentDateTimeGMT5() {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5"));

        String currentUtcTime = sdf.format(new Date());
        DateFormat inputFormatter1 = new SimpleDateFormat(FORMAT14);
        Date date1 = null;
        try {
            date1 = inputFormatter1.parse(currentUtcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static Date getCurrentCheckinDateTimeGMT5() {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5"));

        String currentUtcTime = sdf.format(new Date());
        DateFormat inputFormatter1 = new SimpleDateFormat(DATE_FORMAT);
        Date date1 = null;
        try {
            date1 = inputFormatter1.parse(currentUtcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static Date getLoginAttemptDateTime(int milliseconds) {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date afterAddingMins = new Date(t + (milliseconds /** ONE_MINUTE_IN_MILLIS*/));
        return afterAddingMins;

    }

    public static int getRemainingTimerSeconds(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();
//        long different = startDate.getTime() - endDate.getTime();

        long secondsInMilli = 1000;
        int time = (int) (different / secondsInMilli);
        return time;
    }

    public static File getAppDirectory(Context context) {

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, /*"Pictures/"+*/APP_NAME);
        if (!folder.exists()) {
            Log.d("Directory", "mkdirs():" + folder.mkdirs());
        }
        return folder;
    }

    public static byte[] getImageBytes(String path) {
        File fileName = new File(path);
        byte[] bytes;
        byte[] buffer = new byte[512];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            InputStream inputStream = new FileInputStream(fileName);
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        return bytes;
    }

    public static byte[] getJpegConvertedImageBytes(String path, String type) {
        File imagefile = new File(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes;
        byte[] buffer = new byte[512];
        int bytesRead;
        if (type.equalsIgnoreCase("img")) {
            Log.i("image", "image");
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(imagefile);
                Bitmap bm = BitmapFactory.decodeStream(fis);
                bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (type.equalsIgnoreCase("vid")) {
            Log.i("video", "video");
            try {
                InputStream inputStream = new FileInputStream(imagefile);
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return bytes;
    }

    public static String getFormattedLocalTimeFromUtc(String utcTimeStamp, String outputFormat) {

        String formattedTime = null;
        if (!TextUtils.isEmpty(utcTimeStamp)) {

            String localTime = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date localDateTime = null;
            try {
                localDateTime = sdf.parse(utcTimeStamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat outputDateFormat1 = new SimpleDateFormat(outputFormat);
            localTime = outputDateFormat1.format(localDateTime);

            DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = inputDateFormat.parse(localTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
            formattedTime = outputDateFormat.format(date);
            Log.d("TIME", formattedTime);
        }
        return formattedTime;
    }

    @Nullable
    public static String formatDateTime(String dateTime) {
        try {
            if (!TextUtils.isEmpty(dateTime) && dateTime.contains(" ")) {
                String date = dateTime.split(" ")[0];
                String time = dateTime.split(" ")[1];

                if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(time)) {
                    if (time.contains(":")) {
                        try {
                            int hr = Integer.parseInt(time.split(":")[0].trim());
                            int min = Integer.parseInt(time.split(":")[1].trim());
                            DecimalFormat df = new DecimalFormat("00");
                            time = String.format("%s:%s", df.format(hr), df.format(min));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                dateTime = String.format("%s %s", date, time);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return dateTime;
    }

    public static float dp2px(Resources resources, float dp) {

        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float dp2px(Resources resources, int dimenId) {

        final float scale = resources.getDisplayMetrics().density;
        return resources.getDimension(dimenId) * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {

        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static float pixelsToSp(Context context, float px) {

        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static void log(Class a, String msg) {
        Log.e(" >>> " + a.getName(), msg);
    }

    public static void log(String tag, String msg) {
        Log.e(" >>> " + tag, msg);
    }

    public static void loadWithGlide(Context c, String imgPath, ImageView iv) {
        if (TextUtils.isEmpty(imgPath)) {
            log(TAG, "loadWithGlide: imgPath is null!");
//            iv.setImageResource(R.drawable.ic_shipper_icon);
            return;
        }
        try {
            RequestOptions ro = new RequestOptions();
            ro.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(c).applyDefaultRequestOptions(ro).load(imgPath).into(iv);
        } catch (Throwable t) {
//            iv.setImageResource(R.drawable.ic_shipper_icon);
            t.printStackTrace();
        }
    }

    public static boolean compareDates(String currentDate, String compareDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT19);
        Date Date1 = null;
        try {
            Date1 = sdf.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date Date2 = null;
        try {
            Date2 = sdf.parse(compareDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Date2.after(Date1);
    }

    public static int sortDates(String oldDate, String compareDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date Date1 = null;
        try {
            Date1 = sdf.parse(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date Date2 = null;
        try {
            Date2 = sdf.parse(compareDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Date2.after(Date1)) {
            return 1;
        } else if (Date1.after(Date2)) {
            return -1;
        }
        return 0;
    }

    public static String getUTF8EncodedString(String text) {
        try {
            String str = Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
            Log.i("text = ", text);
            Log.i("encoded text = ", str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void switchActivity(Context context, Class<?> activityClass, Bundle bundle) {

        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        hideNotification((Activity) context);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

    }

    public static void switchActivityWithoutAnimation(Context context, Class<?> activityClass, Bundle bundle) {

        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        hideNotification((Activity) context);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

    }

    public static void switchActivityforResult(AppCompatActivity context, Class<?> activityClass, Bundle bundle, int requestCode) {

        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        hideNotification(context);
        context.startActivityForResult(intent, requestCode);
        context.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

    }


    public static void switchActivityforResultWithoutAnimation(AppCompatActivity context, Class<?> activityClass, Bundle bundle, int requestCode) {

        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        hideNotification(context);
        context.startActivityForResult(intent, requestCode);
//        ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

    }

    public static float getDaysBetweenDates(String fromDate, String fromDateFormat, String fromTime, String fromTimeFormat, String toDate, String toDateFormat, String toTime, String toTimeFormat, Context context) {
        if (fromTime == null || TextUtils.isEmpty(fromTime)) {
            fromTime = "00:00:00";
            fromTimeFormat = FORMAT18;
        }
        if (toTime == null || TextUtils.isEmpty(toTime)) {
            toTime = "24:00:00";
            toTimeFormat = FORMAT18;
        }

/*
        if (fromTime.equals("00:00:00") && toTime.equals("00:00:00")) {
            String ds = AppUtils.getConvertedDateFromOneFormatToOther(fromDate, fromDateFormat, FORMAT15);
            String de = AppUtils.getConvertedDateFromOneFormatToOther(toDate, toDateFormat, FORMAT15);
            if (ds.equals(de)) {
                return 1;
            }
        }
*/
        String fromDateTime = getConvertedDateFromOneFormatToOther(fromDate + " " + fromTime, fromDateFormat + " " + fromTimeFormat, FORMAT22);
        String toDateTime = getConvertedDateFromOneFormatToOther(toDate + " " + toTime, toDateFormat + " " + toTimeFormat, FORMAT22);


        Date fd = getDateFromString3(fromDateTime, FORMAT22);
        Date td = getDateFromString3(toDateTime, FORMAT22);
        if (fd != null && td != null) {
            long diff = td.getTime() - fd.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            if (days < 1) {
                long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
                if (hours < AppWideWariables.HALF_LEAVE_HOUR_LIMIT) {
                    return 0.5f;
                } else {
                    return 1f;
                }
            } else {
                return days;
            }
        } else {
            return -1f;
        }
    }

    public static String getUTF8DecodedString(String text) {
        try {
            String str = new String(Base64.decode(text.getBytes(), Base64.DEFAULT));
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void setUnderligned(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static void setUnderligned(EditText editText) {
        editText.setPaintFlags(editText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static void setUnderligned(Button button) {
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static void showView(TextView view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void hideView(TextView view) {
        view.setVisibility(View.GONE);
    }

    public static String getDate(String dateTimePlacement) {
        if (dateTimePlacement.contains("T")) {
            dateTimePlacement = dateTimePlacement.replace('T', ' ');
        }
        try {
            SimpleDateFormat dateFormat = null;
            if (dateTimePlacement.contains(".")) {
                dateFormat = new SimpleDateFormat(FORMAT1);
            } else {
                dateFormat = new SimpleDateFormat(FORMAT14);
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat(FORMAT9);
            Date c = null;
            c = dateFormat.parse(dateTimePlacement);
            String date1 = sdf2.format(c);
//            System.out.println(date1);
            return date1;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getTime(String dateTimePlacement) {
        if (dateTimePlacement.contains("T")) {
            dateTimePlacement = dateTimePlacement.replace('T', ' ');
        }
        try {
            SimpleDateFormat dateFormat = null;
            if (dateTimePlacement.contains(".")) {
                dateFormat = new SimpleDateFormat(FORMAT1);
            } else {
                dateFormat = new SimpleDateFormat(FORMAT14);
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat(FORMAT5);
            Date c = null;
            c = dateFormat.parse(dateTimePlacement);
            String date1 = sdf2.format(c);
//            System.out.println(date1);
            return date1;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getFormattedPhoneNumber(String msisdn) {
        String phone = msisdn;

        if (!TextUtils.isEmpty(msisdn)) {
            if (msisdn.length() == 13 && msisdn.startsWith("+92")) {
                phone = msisdn;
            } else if (msisdn.length() == 14 && msisdn.startsWith("0092")) {
                phone = msisdn.replaceFirst("0092", "+92");
            } else if (msisdn.length() == 11 && msisdn.startsWith("03")) {
                phone = msisdn.replaceFirst("0", "+92");
            }
        }
        return phone;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
     /*   if (drawable instanceof BitmapDrawable) {
            ((BitmapDrawable)drawable).setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            return ((BitmapDrawable) drawable).getBitmap();
        }
*/
        int width = drawable.getIntrinsicWidth() * 2;
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight() * 2;
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth() / 2, canvas.getHeight());
        drawable.draw(canvas);

        drawable.setBounds(canvas.getWidth() / 2, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void makePhoneCall(AppCompatActivity context, String phone) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public static void makeFullScreen(View mContentView) {

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public static void logResponse(String tag, String message) {
        Log.i(tag + " = ", message);
    }

    public static void loadUrlInBrowser(Context context, String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(openUrlIntent);
    }

    public static String getGenderName(String genderID) {
        switch (genderID) {
            case "3":
                return "Male";
            case "5":
                return "Female";
            case "6":
                return "Transgender";
            case "7":
                return "Both";
            case "8":
                return "N/A";
            default:
                return "N/A";
        }
    }

    public static String getStandardHeaders(UserData userData) {
        return "Bearer " + userData.getToken();
    }

    public static String roundTo2DecimalPlaces(float totalFreightST) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(totalFreightST);
/*
        if (totalFreightST % 1 != 0) // eg. 23.5 % 1 = 0.5
        {
            return df.format(totalFreightST);
        } else {
            String value = String.valueOf(totalFreightST);
            if(value.contains("."))
            {
                value = value.split(".")[0];
            }
            return value;
        }
*/
    }

    public static File getDirectoryPath(String filePath) {

        String[] split = filePath.split("/");

        if (split.length >= 2) {

            String dirName = split[1];

//            return dirName.substring(0, 1).toUpperCase() + dirName.substring(1);

            if (dirName.contains("music")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            }
            if (dirName.contains("podcasts") || dirName.contains("podcast")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
            }
            if (dirName.contains("ringtones") || dirName.contains("ringtone")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
            }
            if (dirName.contains("alarms") || dirName.contains("alarm")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
            }
            if (dirName.contains("notifications") || dirName.contains("notification")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS);
            }
            if (dirName.contains("pictures") || dirName.contains("picture")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            }
            if (dirName.contains("movies") || dirName.contains("movie")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            }
            if (dirName.contains("download") || dirName.contains("downloads")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            }
            if (dirName.contains("DCIM")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            }
            if (dirName.contains("documents") || dirName.contains("document")) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            }
            if (dirName.contains("screenshots") || dirName.contains("screenshot")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_SCREENSHOTS);
                } else {
                    return null;
                }
            }
            if (dirName.contains("audiobooks") || dirName.contains("audiobook")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_AUDIOBOOKS);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static String getFormattedString(Context context, String string) {
        if (string == null || TextUtils.isEmpty(string) || string.equalsIgnoreCase("null") || string.toLowerCase().equals(null)) {
            return context.getResources().getString(R.string.n_a);
        } else {
            return string;
        }
    }

    public static String getFormattedString(Context context, String string, String string2) {
        String str = "";
        if (string != null && !TextUtils.isEmpty(string) && !string.equalsIgnoreCase("null") && !string.toLowerCase().equals(null)) {
            str = string;
        }

        if (string2 != null && !TextUtils.isEmpty(string2) && !string2.equalsIgnoreCase("null") && !string2.toLowerCase().equals(null)) {
            if (!TextUtils.isEmpty(str)) {
                str += " ( " + string2 + " )";
            } else {
                str = string2;
            }
        }

        if (TextUtils.isEmpty(str)) {
            str = context.getResources().getString(R.string.n_a);
        }

        return str;
    }

    public static void loadImageFromBase64(Context context, String fileContentBase64, ImageView image) {

        byte[] imageByteArray = Base64.decode(fileContentBase64, Base64.DEFAULT);

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

        Glide.with(context).asBitmap().load(bitmap).into(image);


    }

    public static String getFirstDateOfWeekFilter() {

        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT14);
        Date date = new Date();
        formatter.format(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.getInstance(Locale.FRANCE).getFirstDayOfWeek());
        int s = cal.get(Calendar.DATE);

        Date c = cal.getTime();
        return (formatter.format(c)).split(" ")[0] + " 00:00:00";
    }

    public static String getLastDateOfWeekFilter() {

        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT14);
        Date date = new Date();
        formatter.format(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.getInstance(Locale.FRANCE).getFirstDayOfWeek() + 6);
        int s = cal.get(Calendar.DATE);

        Date c = cal.getTime();
        return (formatter.format(c)).split(" ")[0] + " 23:59:59";
    }

    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23) ei = new ExifInterface(input);
        else ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static float getFloatValue(String string) {
        return Float.valueOf(string.replaceAll(",", ""));
    }

    public static BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector
        // drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our
        // bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static String getFloatOrInteger(float value) {
        return value == (int) value ? (int) value + "" : value + "";
    }

    public static boolean checkNetworkState(Activity activity) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean response = networkInfo != null && networkInfo.isConnected();
            if (!response) {
                makeNotification(activity.getResources().getString(R.string.check_internet_connection), activity);
            }
            return response;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static String formatDate(String inputDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(inputDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void copyTextToClipboard(String textToCopy, Context context) {

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendSMS(String phoneNumber, Context context) {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + phoneNumber));
        context.startActivity(sendIntent);
    }

    public static void callPhoneNumber(String phoneNumber, Context context) {
        if (!phoneNumber.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(callIntent);
        }
    }


    public static boolean isErrorResponse(int methodType, Response<?> response, Activity activity) {
        if (response == null) {
            return true;
        }

        if (response.code() == 200 || response.code() == 201) {
            try {
                if (methodType == AppWideWariables.API_METHOD_POST && response != null && response.body() != null && ((ApiBaseResponse) response.body()).getMessage() != null) {
                    try {
                        String str = ((ApiBaseResponse) response.body()).getMessage();
                        if (activity != null) {
                            AppUtils.makeNotification(str, activity);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {

            if (response.code() == 403) {
                SharedPreferenceHelper.saveBoolean(SharedPreferenceHelper.SHOULD_REFRESH_TOKEN, true, activity);
                return true;
            } else if (response.code() == 401 || response.code() == 400 || response.code() == 500) {
                try {
                    if (methodType == AppWideWariables.API_METHOD_POST && response != null && response.errorBody() != null) {
                        try {
                            ApiBaseResponse message = new Gson().fromJson(response.errorBody().charStream(), ApiBaseResponse.class);
                            if (activity != null) {
                                AppUtils.makeNotification(message.getMessage(), activity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (methodType == AppWideWariables.API_METHOD_POST && response.body() != null && ((ApiBaseResponse) response.body()).getMessage() != null) {
                        try {
                            AppUtils.makeNotification(((ApiBaseResponse) response.body()).getMessage(), activity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (methodType == AppWideWariables.API_METHOD_POST && response != null && response.message() != null && !TextUtils.isEmpty(response.message())) {
                        try {
                            AppUtils.makeNotification(response.message(), activity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                if (methodType == AppWideWariables.API_METHOD_POST) {
                    String errMessage = activity.getResources().getString(R.string.api_generic_err_response);
                    AppUtils.makeNotification(errMessage, activity);
                }
            }
            return true;
        }
    }

    public static void ApiError(Throwable t, Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            if (t instanceof HttpException) {
                String message = ((HttpException) t).message();
                switch (((HttpException) t).code()) {
                    case HttpsURLConnection.HTTP_UNAUTHORIZED:
//                    message = "Unauthorised User ";
                        break;
                    case HttpsURLConnection.HTTP_FORBIDDEN:
//                    message = "Forbidden";
                        message = "";
                        break;
                    case HttpsURLConnection.HTTP_INTERNAL_ERROR:
//                    message = "Internal Server Error";
                        message = activity.getResources().getString(R.string.api_generic_err_response);
                        break;
                    case HttpsURLConnection.HTTP_BAD_REQUEST:
//                    message = "Bad Request";
                        break;
                    default:
                        message = activity.getResources().getString(R.string.api_generic_err_response);
                }
                AppUtils.makeNotification(message, activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isFileGreaterThan2MB(File file) {
        int maxFileSize = 2 * 1024 * 1024;
        Long l = file.length();
        String fileSize = l.toString();
        int finalFileSize = Integer.parseInt(fileSize);
        return finalFileSize >= maxFileSize;
    }
    public static String getFileSizeInMB(File file) {
        Long l = file.length();
        String fileSize = l.toString();
        int finalFileSize = Integer.parseInt(fileSize);
        return finalFileSize+"";
//        float sizeInKB = finalFileSize/1024f;
//        float sizeInMB = sizeInKB/1024f;
//        return sizeInMB>=1?sizeInMB+" MB":sizeInKB+" KB";
//        return String.format("%.2f", sizeInMB>=1?sizeInMB+" MB":sizeInKB+" KB");
    }

    public static String getFileSize(File file) {
        FileInputStream fis = null;
        try {/*w  w w. ja v  a 2s  . c o  m*/
            fis = new FileInputStream(file);
            int length = fis.available();
            double GB = 1000*1000*1000;
            double MB = 1000*1000;
            double KB = 1000;
/*
            double GB = 1024*1024*1024;
            double MB = 1024*1024;
            double KB = 1024;
*/
            if (length >= GB) {
                return String.format("%.2f GB", length * 1.0 / GB);
            } else if (length >= MB) {
                return String.format("%.2f MB", length * 1.0 / MB);
            } else {
                return String.format("%.2f KB", length * 1.0 / KB);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "unknown";
    }

    public static boolean sizeLessThan2MB(File file) {
        FileInputStream fis = null;
        try {
            /*w  w w. ja v  a 2s  . c o  m*/
            fis = new FileInputStream(file);
            int length = fis.available();
            double GB = 1000*1000*1000;
            double MB = 1000*1000;
            double KB = 1000;
            if (length <= 2.0*MB) {
                return true;
            } else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean wifiLockValidate() {
        boolean a = NetworkMonitorListener.getInstance().getNetworkObject()!=null;
        boolean b = NetworkMonitorListener.getInstance().getNetworkObject().getValue()!=null;
        boolean c = false;
        boolean d = false;
       if(a&&b) {
           c = NetworkMonitorListener.getInstance().getNetworkObject().getValue().getNetworkType().equals(NetworkObject.NETWORK_TYPE_WIFI);
           d = NetworkMonitorListener.getInstance().getNetworkObject().getValue().getBssid().equals(SharedPreferenceHelper.getLoggedinUser(App.getInstance()).getBasicData().get(0).getList());
       }
       boolean e = a && b && c && d;
        return  e ;
    }
	
	public static boolean isValidPassword(String password) {
//          pattern     ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$
//          ^                 # start-of-string
//          (?=.*[0-9])       # a digit must occur at least once
//          (?=.*[a-z])       # a lower case letter must occur at least once
//          (?=.*[A-Z])       # an upper case letter must occur at least once
//          (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
//          (?=\\S+$)         # no whitespace allowed in the entire string
//          .{4,}             # anything, at least six places though
//          $                 # end-of-string
        
//          final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
//            final String PASSWORD_PATTERN = "^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[#?!@$%^&*-])(?=\\S+$).{8,}$";
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#?!@$%^&*-])(?=\\S+$).{8,}$";
        
        Pattern pattern;
        Matcher matcher;
        
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        
        return matcher.matches();
	}
	
}


