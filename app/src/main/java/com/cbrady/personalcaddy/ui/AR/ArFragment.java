package com.cbrady.personalcaddy.ui.AR;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cbrady.personalcaddy.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Objects;


public class ArFragment extends Fragment implements Scene.OnUpdateListener{

    private static final double MIN_OPENGL_VERSION = 3.0;
    Context mContext;
    private com.google.ar.sceneform.ux.ArFragment arFragment;
    private AnchorNode currentAnchorNode;
    private TextView distance;
    ModelRenderable shapeRenderable;
    private Anchor currentAnchor = null;


    public ArFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ar, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        if (!checkIsSupportedDeviceOrFinish(mContext)) {
            Toast.makeText(mContext, "Device not supported", Toast.LENGTH_LONG).show();
        }


        arFragment = (com.google.ar.sceneform.ux.ArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        distance = root.findViewById(R.id.tvDistance);


        initModel();

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            if (shapeRenderable == null)
                return;

            // Creating Anchor
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            clearAnchor();

            currentAnchor = anchor;
            currentAnchorNode = anchorNode;


            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
            node.setRenderable(shapeRenderable);
            node.setParent(anchorNode);
            arFragment.getArSceneView().getScene().addOnUpdateListener(this);
            arFragment.getArSceneView().getScene().addChild(anchorNode);
            node.select();

        });

        return root;
    }

    public boolean checkIsSupportedDeviceOrFinish(final Context context) {

        String openGlVersionString =
                ((ActivityManager) Objects.requireNonNull(context.getSystemService(Context.ACTIVITY_SERVICE)))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Toast.makeText(context, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    private void initModel() {
        MaterialFactory.makeTransparentWithColor(mContext, new Color(android.graphics.Color.WHITE))
                .thenAccept(
                        material -> {
                            Vector3 vector3 = new Vector3(0.0f, 0.01f, 0.0f);
                            shapeRenderable = ShapeFactory.makeCylinder(0.1f, 0.1f,vector3,  material);
                            shapeRenderable.setShadowCaster(false);
                            shapeRenderable.setShadowReceiver(false);
                        });
    }

    private void clearAnchor() {
        currentAnchor = null;
        if (currentAnchorNode != null) {
            arFragment.getArSceneView().getScene().removeChild(currentAnchorNode);
            currentAnchorNode.getAnchor().detach();
            currentAnchorNode.setParent(null);
            currentAnchorNode = null;
        }
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();

        Log.d("API123", "onUpdateframe... current anchor node " + (currentAnchorNode == null));


        if (currentAnchorNode != null) {
            Pose objectPose = currentAnchor.getPose();
            Pose cameraPose = frame.getCamera().getPose();

            float dx = objectPose.tx() - cameraPose.tx();
            float dy = objectPose.ty() - cameraPose.ty();
            float dz = objectPose.tz() - cameraPose.tz();

            ///Compute the straight-line distance.
            float distanceCalc = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
            double distanceMeters = (float)Math.round(distanceCalc * 100.0) / 100.0;

            //get whether it should be calculated for meters or yards:
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String mes_unit_adj = sharedPrefs.getString("dist_Measure", "1");
            String mes_unit;
            float adjustment = Float.valueOf(mes_unit_adj);
            if(mes_unit_adj.equals("1")){
                mes_unit = "cm";
                adjustment = adjustment * 100;

            }
            else{
                mes_unit = "feet";
                adjustment= adjustment * 3;
            }

            distance.setText(String.format("%.1f", distanceMeters * adjustment) + " " + mes_unit);

        }
    }
}