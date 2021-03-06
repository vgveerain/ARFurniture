package com.example.vgvee.arfurniture;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;




import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

//    private PointerDrawable pointer = new PointerDrawable();
//    private boolean isTracking;
//    private boolean isHitting;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    RecyclerView recyclerView;
    ArrayList<Files> contents;
    FilesAdapter filesAdapter;
    LinearLayoutManager llm;

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ImageButton removeBtn,captureBtn,addBtn;
    private Anchor anchor;
    private Anchor x;
    private TransformableNode y;
    private NestedScrollView nestedScrollView;

    ArrayList<Anchor> anchorList;
    ArrayList<TransformableNode> transformableNodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        anchorList = new ArrayList<>();
        transformableNodes = new ArrayList<>();

        setContentView(R.layout.activity_main);
        removeBtn = findViewById(R.id.removeBtn);
        captureBtn = findViewById(R.id.capBtn);
        addBtn = findViewById(R.id.addBtn);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        recyclerView = findViewById(R.id.rView);
        llm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        contents = new ArrayList<>();

        contents.add(new Files(R.drawable.a_pool_table,"Pool Table","Toys and Rides","25,000",Uri.parse("apooltable.sfb")));
        contents.add(new Files(R.drawable.air_hockey_table,"AirHockey Table","Toys and Rides","30,000",Uri.parse("airhockeytable.sfb")));
        contents.add(new Files(R.drawable.a_tv_set,"Tv with Table","Dua Furniture","85,000",Uri.parse("atv.sfb")));
        contents.add(new Files(R.drawable.achair_black_chair,"Office Chair (Black)","Grover Furniture","7,000",Uri.parse("achair.sfb")));
        contents.add(new Files(R.drawable.a_book_shelf,"Book Shelf","Grover Furniture","11,000",Uri.parse("abookshelf.sfb")));
        contents.add(new Files(R.drawable.papilio_red_chair,"Chair (Red)","Ikea","15,000",Uri.parse("papilio.sfb")));
        contents.add(new Files(R.drawable.meshseat_black_chair,"Mesh Seat","Ikea","8,000",Uri.parse("meshseat.sfb")));
        contents.add(new Files(R.drawable.gra_sofa,"Chair (Light Grey)","Dua Furniture","18,000",Uri.parse("gra.sfb")));
        contents.add(new Files(R.drawable.black_sofa,"Sofa (Black)","Sharma Furniture","30,000",Uri.parse("blackSofa.sfb")));
        contents.add(new Files(R.drawable.grey_bed,"Double Bed (Grey)","Sharma Furniture","47,000",Uri.parse("bed.sfb")));
        contents.add(new Files(R.drawable.sofanumber9_white_sofa,"Sofa (White)","Gulati Furniture","40,000",Uri.parse("sofa+number+9.sfb")));
        contents.add(new Files(R.drawable.lc363_grey_sofa,"Sofa (Grey)","Gulati Furniture","42,000",Uri.parse("Mare+LC363.sfb")));
        contents.add(new Files(R.drawable.lc351_grey_sofa,"Sofa (Grey)","Sharma Furniture","40,000",Uri.parse("Mare+LC351.sfb")));
        contents.add(new Files(R.drawable.lc309_grey_sofa,"Sofa (Grey)","Gulati Furniture","17,000",Uri.parse("Mare+LC309.sfb")));
        contents.add(new Files(R.drawable.lc306_grey_sofa,"Couch (Grey)","Grover Furniture","32,000",Uri.parse("Mare+LC306.sfb")));
        contents.add(new Files(R.drawable.lc302_grey_sofa,"Sofa (Grey)","Sharma Furniture","20,000",Uri.parse("Mare+LC302.sfb")));
        contents.add(new Files(R.drawable.couchwide_grey,"Couch (Grey)","Gulati Furniture","21,000",Uri.parse("CouchWide.sfb")));
        contents.add(new Files(R.drawable.natuzzi_yellow_sofa,"Sofa Set (Yellow)","Sharma Furniture","55,000",Uri.parse("natuzzi.sfb")));
        contents.add(new Files(R.drawable.blue_bed,"Bed","Google","25,000",Uri.parse("Bed_01.sfb")));
        contents.add(new Files(R.drawable.a_beanbag,"Beanbag (Orange)","Ikea","8,000",Uri.parse("beanbag.sfb")));
        contents.add(new Files(R.drawable.tablelargerectang,"A Table","Sharma Furniture","15,000",Uri.parse("Table_Large_Rectangular_01.sfb")));
        contents.add(new Files(R.drawable.foosball_table,"Foosball Table","Toys and Rides","32,000",Uri.parse("122186.sfb")));



        filesAdapter = new FilesAdapter(this,contents);
        SnapHelper snapHelper = new PagerSnapHelper();

        recyclerView.setLayoutManager(llm);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(filesAdapter);



        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                anchor.detach();
//                if((anchorList.size())<=0){
//                    Toast.makeText(MainActivity.this, "No Anchors to Delete!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                x = anchorList.get(anchorList.size()-1);
//                x.detach();
//                anchorList.remove(anchorList.size()-1);

//                ((AnchorNode)andy.getParent()).getAnchor().detach();
                if(transformableNodes.size()<=0){
                    Toast.makeText(MainActivity.this, "No Anchors to Delete!", Toast.LENGTH_SHORT).show();
                    return;
                }
                y = transformableNodes.get(transformableNodes.size()-1);
                ((AnchorNode)y.getParent()).getAnchor().detach();
                transformableNodes.remove(transformableNodes.size()-1);
            }
        });
    }


    private String generateFilename() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "Sceneform/" + date + "_screenshot.jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {
        final String filename = generateFilename();
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(MainActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("Open in Photos", v -> {
                    File photoFile = new File(filename);

                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                            MainActivity.this.getPackageName() + ".ar.codelab.name.provider",
                            photoFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                });
                snackbar.show();
            } else {
                Toast toast = Toast.makeText(MainActivity.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < 24) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

   /* private void initializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

//        ImageView2 armchair = new ImageView2(this);
//        armchair.setImageResource(R.drawable.armchair);
//        armchair.setContentDescription("armchair");
//        armchair.setOnClickListener(view ->{addObject(Uri.parse("Armchair_01.sfb"));});
//        gallery.addView(armchair);
//


        ImageView2 bed = new ImageView2(this);
        bed.setImageResource(R.drawable.bed);
        bed.setContentDescription("bed");
        bed.setOnClickListener(view ->{addObject(Uri.parse(""));});
        gallery.addView(bed);
//
//        ImageView2 bedroom = new ImageView2(this);
//        bedroom.setImageResource(R.drawable.bedroom);
//        bedroom.setContentDescription("bedroom");
//        bedroom.setOnClickListener(view ->{addObject(Uri.parse("Bedroom.sfb"));});
//        gallery.addView(bedroom);
//
//        ImageView2 bookcase = new ImageView2(this);
//        bookcase.setImageResource(R.drawable.bookcase);
//        bookcase.setContentDescription("bookcase");
//        bookcase.setOnClickListener(view ->{addObject(Uri.parse("bookcase.sfb"));});
//        gallery.addView(bookcase);
//
//        ImageView2 breakfast = new ImageView2(this);
//        breakfast.setImageResource(R.drawable.breakfastbar);
//        breakfast.setContentDescription("breakfastbar");
//        breakfast.setOnClickListener(view ->{addObject(Uri.parse("BreakFastBar.sfb"));});
//        gallery.addView(breakfast);
//
//        ImageView2 cornertable = new ImageView2(this);
//        cornertable.setImageResource(R.drawable.cornertable);
//        cornertable.setContentDescription("andy");
//        cornertable.setOnClickListener(view ->{addObject(Uri.parse("CornerTable.sfb"));});
//        gallery.addView(cornertable);
//
//        ImageView2 couchred = new ImageView2(this);
//        couchred.setImageResource(R.drawable.couchred);
//        couchred.setContentDescription("andy");
//        couchred.setOnClickListener(view ->{addObject(Uri.parse("CouchRed.sfb"));});
//        gallery.addView(couchred);
//

//
//        ImageView2 credenza = new ImageView2(this);
//        credenza.setImageResource(R.drawable.credenza);
//        credenza.setContentDescription("credenza");
//        credenza.setOnClickListener(view ->{addObject(Uri.parse("Credenza.sfb"));});
//        gallery.addView(credenza);
//
//        ImageView2 tabletennis = new ImageView2(this);
//        tabletennis.setImageResource(R.drawable.tabletennis);
//        tabletennis.setContentDescription("TableTennis Table");
//        tabletennis.setOnClickListener(view ->{addObject(Uri.parse("TTtable.sfb"));});
//        gallery.addView(tabletennis);









    }

    */

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth()/2, vw.getHeight()/2);
    }

    public void addObject(Uri model){
        ModelRenderable.builder()
                .setSource(this, model)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

//        arFragment.setOnTapArPlaneListener(
//                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
//                    if (andyRenderable == null) {
//                        Toast.makeText(this, "Select a model to load", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    // Create the Anchor.
//                    anchor = hitResult.createAnchor();
//                    anchorList.add(anchor);
//                    AnchorNode anchorNode = new AnchorNode(anchor);
//                    anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//                    // Create the transformable andy and add it to the anchor.
//                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
//                    andy.setRenderable(andyRenderable);
//    //                    andy.getScaleController().setMinScale(5.0f);
//                    andy.getScaleController().setMaxScale(15.0f);
//                    andy.setParent(anchorNode);
//                    andy.select();
//                    transformableNodes.add(andy);
//    //                    ((AnchorNode)andy.getParent()).getAnchor().detach();
//                });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Frame frame = arFragment.getArSceneView().getArFrame();
                android.graphics.Point pt = getScreenCenter();
                if (andyRenderable == null) {
                    Toast.makeText(MainActivity.this, "Select a model to load", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(frame != null){
                    List<HitResult> hits = frame.hitTest(pt.x, pt.y);
                    for (HitResult hit : hits) {
                        Trackable trackable = hit.getTrackable();
                        if (trackable instanceof Plane &&
                                ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
//                    modelLoader.loadModel(hit.createAnchor(), model);
                            anchor = hit.createAnchor();
                            anchorList.add(anchor);
                            AnchorNode anchorNode = new AnchorNode();
                            anchorNode.setParent(arFragment.getArSceneView().getScene());

                            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                            andy.setRenderable(andyRenderable);
                            andy.getScaleController().setMaxScale(15.0f);
                            andy.setParent(anchorNode);
                            andy.select();
                            transformableNodes.add(andy);
                            break;

                        }
                    }
                }
            }
        });
    }
}
