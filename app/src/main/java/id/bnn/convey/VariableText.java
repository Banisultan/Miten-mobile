package id.bnn.convey;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VariableText {
    public final String URL = "https://egn-1.miten.tech/mitn/";
//    public final String URL = "http://180.131.147.103:8002";
//    public final String URL = "https://ipehapp.intek.id/conveydev/";
    public final String URL_v2 = "https://egn-2.miten.tech/";
//    public final String URL_v2 = "http://180.131.147.103:8001/";
//    public final String URL = "http://202.125.83.4/conveydev/";
    public final String DEF_GROUP = "CONVEY";
    public final int DELAY_MESSAGE = 200;
    public final int DELAY_MESSAGEV2 = 400;
    public final int DELAY_SPLASH_SCREEN = 2000;

    public final String MESSAGE_ALERT = "Maaf, saat ini sistem sedang sibuk";
    public final String MESSAGE_CONTENT = "Work safety! Bekerja dengan giat, pulang dengan selamat. Pakailah APD mu!";

    public final int CODE_CAPTURE_IMAGE_CONTAINER = 1000;
    public final int CODE_CAPTURE_IMAGE_CSC = 1010;
    public final int CODE_CAPTURE_IMAGE_REPAIR = 1020;
    public final int CODE_CAPTURE_IMAGE_RUSAK = 1030;
    public final int CODE_CAPTURE_IMAGE_POSISI = 1040;

    public final int CODE_PERMISSION_CAMERA = 10;

    public final String TIPE_DB_WAITING = "WAITING";
    public final String TIPE_DB_APPROVE = "APPROVE";
    public final String TIPE_DB_REPAIR = "REPAIR";
    public final String TIPE_DB_APPROVE_REPAIR = "APPROVE_REPAIR";
    public final String TIPE_DB_DONE = "DONE";


    public final String SECRET_SHA1 = "TotalitasTanpaBatas<>";
    public final String SECRET_AUTH = "KerjaBakti007BersamaMiten";

    public String createMobID(){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date timenow = Calendar.getInstance().getTime();
        return dateformat.format(timenow)+""+getRandomNumberInRange(100,999);
    }

    public String datetimenow(){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timenow = Calendar.getInstance().getTime();
        return dateformat.format(timenow);
    }

    public String createSHA1(String data, String key) {
        try{
            String algo = "HmacSHA1";
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(keyspec);

            Formatter formatter = new Formatter();
            for(byte b: mac.doFinal(data.getBytes())){
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }catch (Exception e){
            return "";
        }
    }

    public Bitmap BitmapCompressFace(Bitmap bitmap, float persen) {
        float newWidth = (float) (bitmap.getWidth() * persen);
        float newHeight = (float) (bitmap.getHeight() * persen);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        if(width > height){
            matrix.postRotate(-90);
            Log.d("RESPONSE", "w > h");
        }
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }

    public Bitmap BitmapCompress(Bitmap bitmap, float persen) {
        float newWidth = (float) (bitmap.getWidth() * persen);     // 10%
        float newHeight = (float) (bitmap.getHeight() * persen);   // 10%
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeight / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public File createTempFile(Context context, Bitmap bitmap) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public File createTempFile_v2(Context context, Bitmap bitmap, File file) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public String getmonth(int i){
        switch (i){
            case 1:
                return "Januari";
            case 2:
                return "Februari";
            case 3:
                return "Maret";
            case 4:
                return "April";
            case 5:
                return "Mei";
            case 6:
                return "Juni";
            case 7:
                return "Juli";
            case 8:
                return "Agustus";
            case 9:
                return "September";
            case 10:
                return "Oktober";
            case 11:
                return "November";
            case 12:
                return "Desember";
            default:
                return "";
        }
    }

    public static long byteSizeOfv2(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;
        return lengthbmp;
    }

    public int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public int getIndexSpinner(ArrayList<String> list, String value){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).equals(value)){
                return i;
            }
        }
        return 0;
    }

    public String setFormatNominal(int hargaawal){
        BigInteger harga = BigInteger.valueOf(hargaawal);
        String formatharga = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(formatharga);
        return decimalFormat.format(harga).replace(".",",");
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path){
        try{
            ExifInterface ei = new ExifInterface(image_absolute_path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotate(bitmap, 90);

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotate(bitmap, 180);

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotate(bitmap, 270);

                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    return flip(bitmap, true, false);

                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    return flip(bitmap, false, true);

                default:
                    return bitmap;
            }
        }catch (Exception e){
            return null;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void unsubscribe(String idakun){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("MOB_"+idakun)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.d("TAGGAR", "GAGAL UNSUBCREB");
                        }else{
                            Log.d("TAGGAR", "SUKSESS UNSUBCREB");
                        }
                    }
                });
    }

    public void unsubscribeall(){
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );
        return image;
    }

    public boolean removePhoto(String filepath){
        File file = new File(filepath);
        return file.delete();
    }
}
