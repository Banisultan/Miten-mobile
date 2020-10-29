package id.bnn.convey.Test;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {
    private File mFile;
    private String mPath;
    private UploadCallbacks mListener;
    private String content_type;

    int jumlah;
    int posisi;
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    public interface UploadCallbacks {
        void onProgressUpdate(float percentage, int posisi);
        void onError();
        void onFinish();
    }

    public ProgressRequestBody(final File file, String content_type, int jumlah, int posisi) {
        this.content_type = content_type;
        mFile = file;

        this.jumlah = jumlah;
        this.posisi = posisi;
    }


    @Override
    public MediaType contentType() {
        return MediaType.parse(content_type+"/*");
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            android.os.Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
//            float percent = ((float)(100 * mUploaded / mTotal));
//            float hitung = ((100/jumlah)*posisi) + (percent / jumlah);
//            mListener.onProgressUpdate(hitung, jumlah);
        }
    }
}
