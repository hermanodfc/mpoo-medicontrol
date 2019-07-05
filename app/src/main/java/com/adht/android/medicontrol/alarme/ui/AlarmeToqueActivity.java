package com.adht.android.medicontrol.alarme.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.infra.ml.CameraImageGraphic;
import com.adht.android.medicontrol.infra.ml.CameraSource;
import com.adht.android.medicontrol.infra.ml.CameraSourcePreview;
import com.adht.android.medicontrol.infra.ml.FrameMetadata;
import com.adht.android.medicontrol.infra.ml.GraphicOverlay;
import com.adht.android.medicontrol.infra.ml.VisionProcessorBase;
import com.adht.android.medicontrol.infra.ml.labeldetector.LabelGraphic;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlarmeToqueActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        AdapterView.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {

    public static final String EXTRA_NOME_REMEDIO = "NOME_REMEDIO";
    private static final int PERMISSION_REQUESTS = 1;
    private static final String TAG = "AlarmeToqueActivity";
    private static final String AUTOML_IMAGE_LABELING = "AutoML Vision Edge";
    private String selectedModel = AUTOML_IMAGE_LABELING;
    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private MediaPlayer player = new MediaPlayer();
    private Button adiar;
    private String remedio;
    private int request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_alarme_toque);
        Intent intent = getIntent();


        remedio = intent.getStringExtra("ALARME_NOME2");
        request = intent.getIntExtra("REQUEST2", 0);

        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
        } else {
            getRuntimePermissions();
        }

        adiar = findViewById(R.id.adiar);
        adiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(AlarmeToqueActivity.this, Alarm.class);
                PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(),request, intent3,0);
                AlarmManager a = (AlarmManager)getSystemService(ALARM_SERVICE);

                long currentTime = System.currentTimeMillis();
                long total = currentTime + 10000;
                intent3.putExtra("ALARME_NOME2", remedio);
                intent3.putExtra("ALARME_REQUEST2", request);
                a.setExact(AlarmManager.RTC_WAKEUP, total, p1);

                finish();
            }
        });

        /*Intent intent = getIntent();
        remedio = intent.getStringExtra(EXTRA_NOME_REMEDIO);*/
        TextView nomeRemedio = findViewById(R.id.nomeRemedio);
        nomeRemedio.setText(remedio);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AssetFileDescriptor descriptor = null;

        try {
            descriptor = getAssets().openFd("alarme/alarme.mp3");
            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setLooping(true);
        player.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }

    @Override
    public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedModel = parent.getItemAtPosition(pos).toString();
        Log.d(TAG, "Selected model: " + selectedModel);
        preview.stop();
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
            startCameraSource();
        } else {
            getRuntimePermissions();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "Set facing");
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
            } else {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
            }
        }
        preview.stop();
        startCameraSource();
    }

    private void createCameraSource(String model) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {
            switch (model) {
                case AUTOML_IMAGE_LABELING:
                    cameraSource.setMachineLearningFrameProcessor(new AutoMLImageLabelerProcessor(this));
                    break;
                default:
                    Log.e(TAG, "Unknown model: " + model);
            }
        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: " + model, e);
            Toast.makeText(
                    getApplicationContext(),
                    "Can not create image processor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    class AutoMLImageLabelerProcessor
            extends VisionProcessorBase<List<FirebaseVisionImageLabel>> {

        private static final String TAG = "ODAutoMLILProcessor";

        private final FirebaseVisionTextRecognizer textDetector;
        private static final String LOCAL_MODEL_NAME = "automl_image_labeling_model";

        private FirebaseVisionImage image;
        private final FirebaseVisionImageLabeler imageDetector;

        public AutoMLImageLabelerProcessor(final Context context) throws FirebaseMLException {
            FirebaseModelManager.getInstance()
                    .registerLocalModel(
                            new FirebaseLocalModel.Builder(LOCAL_MODEL_NAME)
                                    .setAssetFilePath("automl/manifest.json")
                                    .build());

            FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder optionsBuilder =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder().setConfidenceThreshold(0.925f);

            optionsBuilder.setLocalModelName(LOCAL_MODEL_NAME);

            imageDetector =
                    FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(optionsBuilder.build());
            textDetector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        }

        @Override
        public void stop() {
            try {
                imageDetector.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception thrown while trying to close the image labeler: " + e);
            }
        }

        @Override
        protected Task<List<FirebaseVisionImageLabel>> detectInImage(FirebaseVisionImage image) {
            this.image = image;
            return imageDetector.processImage(image);
        }

        @Override
        protected void onSuccess(
                @Nullable Bitmap originalCameraImage,
                @NonNull List<FirebaseVisionImageLabel> labels,
                @NonNull FrameMetadata frameMetadata,
                @NonNull GraphicOverlay graphicOverlay) {
            graphicOverlay.clear();
            if (originalCameraImage != null) {
                CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay,
                        originalCameraImage);
                graphicOverlay.add(imageGraphic);
            }
            LabelGraphic labelGraphic = new LabelGraphic(graphicOverlay, labels);
            graphicOverlay.add(labelGraphic);
            graphicOverlay.postInvalidate();

            for (FirebaseVisionImageLabel label : labels) {
                if (label.getText().equals("medicine")) {
                    Task<FirebaseVisionText> task = textDetector.processImage(image);
                    task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                List<FirebaseVisionText.TextBlock> textBlocks = firebaseVisionText.getTextBlocks();
                                for (FirebaseVisionText.TextBlock block : textBlocks) {
                                    if (block.getText().toLowerCase().contains(remedio.toLowerCase())) {
                                        finish();
                                    }
                                }
                        }
                    });
                }
            }

        }

        @Override
        protected void onFailure(@NonNull Exception e) {
            Log.w(TAG, "Label detection failed." + e);
        }
    }

    class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

        private static final String TAG = "TextRecProc";

        private final FirebaseVisionTextRecognizer detector;

        public TextRecognitionProcessor() {
            detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        }

        @Override
        public void stop() {
            try {
                detector.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception thrown while trying to close Text Detector: " + e);
            }
        }

        @Override
        protected Task<FirebaseVisionText> detectInImage(FirebaseVisionImage image) {
            return detector.processImage(image);
        }

        @Override
        protected void onSuccess(
                @Nullable Bitmap originalCameraImage,
                @NonNull FirebaseVisionText results,
                @NonNull FrameMetadata frameMetadata,
                @NonNull GraphicOverlay graphicOverlay) {
            graphicOverlay.clear();
            if (originalCameraImage != null) {
                CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay,
                        originalCameraImage);
                graphicOverlay.add(imageGraphic);
            }
            List<FirebaseVisionText.TextBlock> blocks = results.getTextBlocks();
            for (int i = 0; i < blocks.size(); i++) {
                List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
                for (int j = 0; j < lines.size(); j++) {
                    List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                    for (int k = 0; k < elements.size(); k++) {

                    }
                }
            }
            graphicOverlay.postInvalidate();
        }

        @Override
        protected void onFailure(@NonNull Exception e) {
            Log.w(TAG, "Text detection failed." + e);
        }
    }
}
