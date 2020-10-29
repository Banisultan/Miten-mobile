//package id.bnn.convey;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.imgproc.Imgproc;
//
//public class Testing extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
//
//    static{
//        if(OpenCVLoader.initDebug()){
//            Log.d("RESPONSE", "OpenCV connected");
//        }else{
//            Log.d("RESPONSE", "OpenCV not connect");
//        }
//    }
//
//    CameraBridgeViewBase camerajava;
//    Mat mat1, mat2, mat3;
//
//    BaseLoaderCallback baseLoaderCallback;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_testing);
//
//        camerajava = findViewById(R.id.camerajava);
//        camerajava.setCvCameraViewListener(this);
//
//        baseLoaderCallback = new BaseLoaderCallback(this) {
//            @Override
//            public void onManagerConnected(int status) {
//                super.onManagerConnected(status);
//                switch (status){
//                    case BaseLoaderCallback.SUCCESS:
//
//                        camerajava.enableView();
//
//
//                        break;
//                    default:
//                        super.onManagerConnected(status);
//                        break;
//                }
//            }
//        };
//    }
//
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputframe){
//        mat1 = inputframe.rgba();
//
//        Core.transpose(mat1, mat2);
//        Imgproc.resize(mat2, mat3, mat3.size(), 0, 0, 0);
//        Core.flip(mat2, mat1, 1);
//        return mat1;
//    }
//
//    @Override
//    public void onCameraViewStopped(){
//        mat1.release();
////        mat2.release();
////        mat3.release();
//    }
//
//    @Override
//    public void onCameraViewStarted(int width, int height){
//        mat1 = new Mat(width, height, CvType.CV_8UC4);
//        mat2 = new Mat(width, height, CvType.CV_8UC4);
//        mat3 = new Mat(width, height, CvType.CV_8UC4);
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//
//        if(!OpenCVLoader.initDebug()){
//            Toast.makeText(getApplicationContext(), "Bermasalah pada camera", Toast.LENGTH_LONG).show();
//        }else{
//            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
//        }
//    }
//
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//    }
//
//    @Override
//    public void onPause(){
//        super.onPause();
//        if(camerajava != null){
//            camerajava.disableView();
//        }
//    }
//}
