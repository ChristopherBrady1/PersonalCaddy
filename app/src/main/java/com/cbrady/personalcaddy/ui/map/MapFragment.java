package com.cbrady.personalcaddy.ui.map;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.Holes;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.models.Shots;
import com.cbrady.personalcaddy.ui.AR.ArFragment;
import com.cbrady.personalcaddy.ui.ShotDetails.ShotDetailsFragment;
import com.cbrady.personalcaddy.ui.clubChoice.clubChoice;
import com.cbrady.personalcaddy.ui.holedetails.HoleDetailsFragment;
import com.cbrady.personalcaddy.ui.home.HomeFragment;
import com.cbrady.personalcaddy.ui.scorecard.ScorecardFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.cbrady.personalcaddy.models.User;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.SENSOR_SERVICE;


public class MapFragment extends Fragment implements SensorEventListener, LocationListener {
    Context mContext;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    boolean isMarkerRotating = false;

    double azimuth = 0;
    GoogleMap map;

    SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;

    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;

    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;
    Marker marker;
    Marker destMarker;
    Marker temp;
    Polyline polyline1;
    Bitmap imageBitmap;

    float[] desired_shot_distance = new float[1];
    float[] actual_distance = new float[1];

    LatLng start_point;
    LatLng desired_end_point;
    LatLng start_point2;
    LatLng desired_end_point2;

    String lie_ball;
    String[] club;
    String spinner_item;
    SpinnerAdapter adapter;

    int current_hole = 1;
    int current_shot = 1;
    String present_hole;
    String present_shot;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    //final String userId = getUid();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    //TODO FIX HERE
    //Globals obj = new Globals();
    String currentRoundID;
    String holeKey;
    String shotKey;
    int shotNum = 1;
    int pushHolecounter = 1;
    int scorecardIndex =0;
    int numPutts = 0;
    int totalPutts = 0;
    int totalScore = 0;

    //variables to get the Greens in Regulation
    int totalGIR=0;
    int maxShotsGIR =0;
    BottomNavigationView bnv;


    int totalShots = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        //hide actionbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = fm.getBackStackEntryCount()-1; i > 0 ; i--) {
            if(fm.getBackStackEntryAt(i) != this.getParentFragment()){
                fm.popBackStack();
            }
        }*/

        getCurrentLocation(0);
        supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.google_map);
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        }

        LocationServices.getFusedLocationProviderClient(mContext).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //When success
                if (location != null){
                    //sync map
                    supportMapFragment.getMapAsync((new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //Initialize lat lng
                            map = googleMap;
                            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                            //Bitmap marker_icon = BitmapFactory.decodeResource(getResources(),R.drawable.gps_arrow);


                            LatLng latLng = new LatLng(location.getLatitude()
                                    ,location.getLongitude());
                            //create marker options
                            //Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>

                            if (marker == null)
                                marker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("arrow", 100, 100))));

                            onLocationChanged(location);
                            //changed icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                            //MarkerOptions options = new MarkerOptions().position(latLng)
                            //        .title("I am here");
                            //Zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                            //location.bearingTo()

                            LatLng newLocaation;
                            LatLng oldLocation;

                            //add marker on map
                            // googleMap.addMarker(options);
                        }
                    }));

                }
            }
        });
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        onLocationChanged(location);
                    }
                }
            }
        };
        mLocationRequest.setInterval(20);
        LocationServices.getFusedLocationProviderClient(mContext).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        client = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation(0);
        } else {
            ActivityCompat.requestPermissions((MainActivity)mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        sensorManager = (SensorManager)mContext.getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];

        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];

        //button listeners
        ImageButton addMarker = getView().findViewById(R.id.addMarker);
        Button confirmShot = getView().findViewById(R.id.confirmShot);
        TextView distance = getView().findViewById(R.id.distance);
        Button nextHoleButton = getView().findViewById(R.id.nextHoleButton);
        TextView holeNumText = getView().findViewById(R.id.holeNum);
        TextView shotNumText = getView().findViewById(R.id.shotNum);
        Button shotMode = getView().findViewById(R.id.shotMode);
        ImageButton puttMode = getView().findViewById(R.id.puttMode);
        Button arMode = getView().findViewById(R.id.ArButton);
        Button addShotPutter = getView().findViewById(R.id.addShotPutter);
        ImageButton scorecardButton = getView().findViewById(R.id.scorecardButton);
        ImageButton finishRoundButton = getView().findViewById(R.id.finishRoundButton);

        //getting rid of bottom nav view
        bnv = getActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.GONE);



        shotNumText.setText(String.valueOf(((MainActivity)getActivity()).getShotCounter()));


        present_shot = shotNumText.getText().toString();

        //setting current hole
        present_hole = holeNumText.getText().toString();
        String check_hole = ((MainActivity)getActivity()).getCurrentHoleNum();
        totalShots = ((MainActivity)getActivity()).getShotTotal();

        holeNumText.setText(check_hole);
        shotNumText.setText(String.valueOf(totalShots));

        currentRoundID = ((MainActivity)getActivity()).getCurrentRoundKey();

        //prevent the 1st hole from being pushed more than once
       /* if(pushHolecounter == 1){
            pushHole();
            pushHolecounter++;
        }*/

        holeNumText.setText(String.valueOf(current_hole));

        /*TODO *************************************************
        Button pushHole = getView().findViewById(R.id.submitHole);

        pushHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewHole(currentRoundID, "580",  "5", "4", present_hole);
                present_hole = present_hole + 1;
            }
        });

        Button pushShot = getView().findViewById(R.id.submitShot);
        pushShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewShot( holeKey,  "250", "Driver", present_shot);
                present_shot = present_shot + 1;
            }
        });

        *///TODO ***************************************************

        //updating current hole in DB
        //if current shot is 1 and then reset this for every hole



        addMarker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {

                        //ensuring only one marker can  be added at a time
                        if (destMarker == null) {
                            destMarker = map.addMarker(new MarkerOptions().position(new LatLng(point.latitude, point.longitude)));
                            destMarker.setDraggable(true);
                            map.setOnMapClickListener(null);


                            float[] results = new float[1];
                            LatLng destPos = destMarker.getPosition();
                            LatLng myPos = marker.getPosition();
                            Location.distanceBetween(destPos.latitude, destPos.longitude,
                                    myPos.latitude, myPos.longitude,
                                    results);

                            //get whether it should be calculated for meters or yards:
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                            String mes_unit_adj = sharedPrefs.getString("dist_Measure", "1");
                            String mes_unit;
                            if(mes_unit_adj.equals("1")){
                                mes_unit = "meters";
                            }
                            else{
                                mes_unit = "yards";
                            }

                            float adjustment = Float.valueOf(mes_unit_adj);

                            distance.setText( String.format("%.1f", results[0] * adjustment) + mes_unit);
                            distance.setVisibility(View.VISIBLE);


                            List<PatternItem> pattern = Arrays.asList(new Dash(30), new Gap(25));
                            polyline1 = map.addPolyline(new PolylineOptions().add(new LatLng(destPos.latitude, destPos.longitude),
                                    new LatLng(myPos.latitude, myPos.longitude)).color(Color.RED));
                            polyline1.setPattern(pattern);

                            confirmShot.setVisibility(View.VISIBLE);

                            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker destMarkerTemp) {

                                }

                                @Override
                                public void onMarkerDrag(Marker destMarkerTemp) {

                                }

                                @Override
                                public void onMarkerDragEnd(Marker destMarkerTemp) {
                                    results[0] = 0;
                                    polyline1.remove();
                                    LatLng destPos1 = destMarkerTemp.getPosition();
                                    LatLng myPos1 = marker.getPosition();
                                    Location.distanceBetween(destPos1.latitude, destPos1.longitude,
                                            myPos1.latitude, myPos1.longitude,
                                            results);

                                    //get whether it should be calculated for meters or yards:
                                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                                    String mes_unit_adj = sharedPrefs.getString("dist_Measure", "1");
                                    String mes_unit;
                                    if(mes_unit_adj.equals("1")){
                                        mes_unit = "meters";
                                    }
                                    else{
                                        mes_unit = "yards";
                                    }

                                    float adjustment = Float.valueOf(mes_unit_adj);


                                    distance.setText(String.format("%.1f", results[0] * adjustment ) + " " + mes_unit);
                                    polyline1 = map.addPolyline(new PolylineOptions().add(new LatLng(destPos1.latitude, destPos1.longitude), new LatLng(myPos1.latitude, myPos1.longitude)).color(Color.RED));
                                    polyline1.setPattern(pattern);

                                }
                            });
                        }

                    }
                });

            }
        });

        //showing confirmShot button
        if(destMarker != null) {
            confirmShot.setVisibility(View.VISIBLE);
        }

        //setting shot variables once shot is confirmed
        confirmShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                confirmShot.setVisibility(View.INVISIBLE);
                ((MainActivity)getActivity()).setCurrentShot(shotNum);

                int temp = ((MainActivity)getActivity()).getShotCounter();
                temp++;
                ((MainActivity)getActivity()).setShotCounter(temp);


                //Calling Shot details fragment
                //********************* THIS WILL SHOW FIRST AS ITS ON THE TOP OF THE STACK
                Fragment shotDetailsFragment = new ShotDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, shotDetailsFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();


                //if the first points have been set, then use second points
                if(start_point != null){
                    start_point2 = marker.getPosition();
                    desired_end_point2 = destMarker.getPosition();
                    actual_distance[0] = 0;
                    desired_shot_distance[0] = 0;

                    //calculate desired distance
                    Location.distanceBetween(start_point2.latitude, start_point2.longitude,
                            desired_end_point2.latitude, desired_end_point2.longitude,
                            desired_shot_distance);

                    //setting desired distance globally
                    ((MainActivity)getActivity()).setDesired_distance(desired_shot_distance[0]);


                    //calculate actual distance
                    Location.distanceBetween(start_point.latitude, start_point.longitude,
                            start_point2.latitude, start_point2.longitude,
                            actual_distance);

                    //TODO push desired and actual distance to the database
                    //TODO PUSH DISTANCE, CLUB and LIE to the DB
                    String club = ((MainActivity)getActivity()).getCurrentClub1();
                    String lie = ((MainActivity)getActivity()).getCurrentLie();
                    holeKey = ((MainActivity)getActivity()).getHoleKey();
                    writeNewShot(holeKey,uid,currentRoundID,String.format("%.2f", desired_shot_distance[0]),
                            String.format("%.2f", actual_distance[0]),club,String.valueOf(shotNum),lie);


                    //increment shot num
                    shotNum++;

                    Log.d("SHOT_DETAILS", "Lie: " + ((MainActivity)getActivity()).getCurrentLie());
                    Log.d("SHOT_DETAILS", "Actual Distance: " + String.format("%.2f", actual_distance[0]));
                    Log.d("SHOT_DETAILS", "Club: " + ((MainActivity)getActivity()).getCurrentClub1());


                    //resetting points for next shot
                    start_point = start_point2;
                    desired_end_point = desired_end_point2;

                }
                else{
                    start_point = marker.getPosition();
                    desired_end_point = destMarker.getPosition();

                    //for setting global
                    //calculate desired distance
                    Location.distanceBetween(start_point.latitude, start_point.longitude,
                            desired_end_point.latitude, desired_end_point.longitude,
                            desired_shot_distance);

                    //setting desired distance globally
                    ((MainActivity)getActivity()).setDesired_distance(desired_shot_distance[0]);
                }

                totalShots++;
                shotNumText.setText(String.valueOf(totalShots));
                ((MainActivity)getActivity()).setShotTotal(totalShots);

                //removing marker
                //TODO MAKE a function called remove marker and add marker that these can call
                destMarker.remove();
                distance.setText("");
                distance.setVisibility(View.INVISIBLE);
                polyline1.remove();
                destMarker = null;

            }
        });




        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Are you sure you want to move on to the next hole");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        present_hole = holeNumText.getText().toString();
                        String currentShotNum = shotNumText.getText().toString();

                        Log.d("CHECK_SCORE", "currentShotNum: "+ currentShotNum + "shotNumVar: " + String.valueOf(shotNum));

                        //writing last shot as a putt if in putt mode as this takes into account the possibility of chipping in or a hole in 1
                        if(addShotPutter.getVisibility() == View.VISIBLE) {
                            writeNewShot(holeKey, uid, currentRoundID, "0", "0", "putter", String.valueOf(shotNum), "green");
                        }
                        else{
                            start_point2 = marker.getPosition();

                            Location.distanceBetween(start_point.latitude, start_point.longitude,
                                    desired_end_point.latitude, desired_end_point.longitude,
                                    desired_shot_distance);

                            //calculate actual distance
                            Location.distanceBetween(start_point.latitude, start_point.longitude,
                                    start_point2.latitude, start_point2.longitude,
                                    actual_distance);

                            String club = ((MainActivity)getActivity()).getCurrentClub1();
                            String lie = ((MainActivity)getActivity()).getCurrentLie();
                            holeKey = ((MainActivity)getActivity()).getHoleKey();
                            writeNewShot(holeKey,uid,currentRoundID,String.format("%.2f", desired_shot_distance[0]),String.format("%.2f", actual_distance[0]),club,String.valueOf(shotNum),lie);
                        }

                        //add score to scorecard
                        String parHole = ((MainActivity)getActivity()).getHolePar();
                        ((MainActivity)getActivity()).scorecardScores.set(scorecardIndex,currentShotNum);
                        ((MainActivity)getActivity()).scorecardPars.set(scorecardIndex,parHole);
                        scorecardIndex ++;

                        totalScore = totalScore + Integer.valueOf(currentShotNum);
                        totalPutts = totalPutts + numPutts;
                        //Code to update score of each hole in DB
                        holeKey = ((MainActivity)getActivity()).getHoleKey();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference Holeref = database.getReference("holes/" + holeKey);
                        final DatabaseReference scoreRef = Holeref.child("score");
                        final DatabaseReference puttsRef = Holeref.child("putts");
                        scoreRef.setValue(currentShotNum);
                        puttsRef.setValue(numPutts);

                        current_shot = 1;
                        shotNum=1;
                        numPutts=0;
                        shotNumText.setText(String.valueOf(current_shot));
                        current_hole++;
                        holeNumText.setText(String.valueOf(current_hole));

                        ((MainActivity)getActivity()).setCurrentShot(1);
                        ((MainActivity)getActivity()).setShotTotal(0);
                        //setting counter
                        ((MainActivity)getActivity()).setCounter(2);

                        //set variables for push
                        ((MainActivity)getActivity()).setCurrentHoleNum(present_hole);

                        Fragment holeDetailsFragment = new HoleDetailsFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, holeDetailsFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog confirmNextHole = builder1.create();




        nextHoleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                confirmNextHole.show();
                //pushHole();

            }
        });

        //TODO scorecard button
        scorecardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment scorecardFragment = new ScorecardFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, scorecardFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        //If the user enters green mode:
        puttMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide other buttons
                addMarker.setVisibility(View.INVISIBLE);
                TextView aMTxt = (TextView) getView().findViewById(R.id.addMarkerTxt);
                aMTxt.setVisibility(View.INVISIBLE);

                //show shot button
                addShotPutter.setVisibility(View.VISIBLE);
                shotMode.setVisibility(View.VISIBLE);

                //set the green in regulation

                //get the par of the current hole and work out the max number of shots to get a green in reg
                String parHole = ((MainActivity)getActivity()).getHolePar();
                //compare maxShotsGIR to shotNum and increment totalGIR if suited
                switch(parHole){
                    case "3":
                        if(shotNum <= 2){
                            totalGIR++;
                        }
                        break;
                    case "4":
                        if(shotNum <= 3){
                            totalGIR++;
                        }
                        break;
                    case "5":
                        if(shotNum <= 4){
                            totalGIR++;
                        }
                        break;
                    default:
                        break;
                }

            }
        });

        addShotPutter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String present_shot = shotNumText.getText().toString();
                int present_shot_value_int = Integer.parseInt(present_shot);
                present_shot_value_int++;
                numPutts++;


                shotNumText.setText(String.valueOf(present_shot_value_int));

                //TODO have to push shot from before green
                //Checking if a previous shot needs to be pushed
                if (start_point != null) {
                    start_point2 = marker.getPosition();

                    Location.distanceBetween(start_point.latitude, start_point.longitude,
                            desired_end_point.latitude, desired_end_point.longitude,
                            desired_shot_distance);

                    //calculate actual distance
                    Location.distanceBetween(start_point.latitude, start_point.longitude,
                            start_point2.latitude, start_point2.longitude,
                            actual_distance);

                    String club = ((MainActivity)getActivity()).getCurrentClub1();
                    String lie = ((MainActivity)getActivity()).getCurrentLie();
                    holeKey = ((MainActivity)getActivity()).getHoleKey();
                    writeNewShot(holeKey,uid,currentRoundID,String.format("%.2f", desired_shot_distance[0]),String.format("%.2f", actual_distance[0]),club,String.valueOf(shotNum),lie);


                    //increment shot num
                    shotNum++;


                    Log.d("SHOT_DETAILS", "Lie: " + ((MainActivity)getActivity()).getCurrentLie());
                    Log.d("SHOT_DETAILS", "Actual Distance: " + String.format("%.2f", actual_distance[0]));
                    Log.d("SHOT_DETAILS", "Club: " + ((MainActivity)getActivity()).getCurrentClub1());


                    //resetting points for next shot
                    start_point = null;
                    desired_end_point = null;
                    start_point2 = null;
                    desired_end_point2 = null;

                }


                //call write new shot
                holeKey = ((MainActivity)getActivity()).getHoleKey();
                writeNewShot(holeKey, uid, currentRoundID, "0","0","putter", String.valueOf(shotNum), "green");
                shotNum++;

            }
        });

        shotMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show other buttons
                addMarker.setVisibility(View.VISIBLE);
                TextView aMTxt = (TextView) getView().findViewById(R.id.addMarkerTxt);
                aMTxt.setVisibility(View.INVISIBLE);


                //hide shot button
                addShotPutter.setVisibility(View.INVISIBLE);
                shotMode.setVisibility(View.INVISIBLE);

            }
        });

        arMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.removeLocationUpdates(mLocationCallback);

                Fragment arFragment = new ArFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, arFragment, "arFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        //if the user wants to end the round early
        finishRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endRound();
            }
        });



        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensorAccelerometer);
        sensorManager.unregisterListener(this, sensorMagneticField);
    }



    public Bitmap resizeMapIcons(String iconName,int width, int height){
        imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", mContext.getPackageName()));
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        Matrix matrix = new Matrix();
        matrix.postRotate((float)(azimuth),width/2,height/2);
        return Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
    }

    private void getCurrentLocation(float azimuth) {
        //initialize task location
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44 ){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //when granted
                //call method
                getCurrentLocation(0);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                for (int i = 0; i < 3; i++) {
                    valuesAccelerometer[i] = event.values[i];
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                for (int i = 0; i < 3; i++) {
                    valuesMagneticField[i] = event.values[i];
                }
                break;
        }

        boolean success = SensorManager.getRotationMatrix(
                matrixR,
                matrixI,
                valuesAccelerometer,
                valuesMagneticField);

        if (success) {
            double tempAz = matrixValues[0];
            SensorManager.getOrientation(matrixR, matrixValues);
            azimuth = Math.toDegrees(matrixValues[0]);
            double pitch = Math.toDegrees(matrixValues[1]);
            double roll = Math.toDegrees(matrixValues[2]);
            if(!( Math.toDegrees(tempAz) == azimuth) ){
                if(! (map == null)) {
                    //centering the marker bitmap
                    marker.setAnchor(0.5f,0.5f);
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("arrow", 100, 100)));


                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng location_update =  new LatLng(location.getLatitude()
                ,location.getLongitude());
        if(marker != null) {
            marker.setPosition(location_update);
        }
        if(map!= null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location_update, 17));
        }

        if (destMarker != null){
            float[] results = new float[1];
            polyline1.remove();

            TextView distance = getView().findViewById(R.id.distance);
            LatLng destPos = destMarker.getPosition();
            LatLng myPos = marker.getPosition();
            Location.distanceBetween(destPos.latitude, destPos.longitude,
                    myPos.latitude, myPos.longitude,
                    results);

            //get whether it should be calculated for meters or yards:
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String mes_unit_adj = sharedPrefs.getString("dist_Measure", "1");
            String mes_unit;
            if(mes_unit_adj.equals("1")){
                mes_unit = "meters";
            }
            else{
                mes_unit = "yards";
            }

            float adjustment = Float.valueOf(mes_unit_adj);

            distance.setText(String.format("%.1f", results[0] * adjustment) + mes_unit);
            distance.setVisibility(View.VISIBLE);


            List<PatternItem> pattern = Arrays.asList(new Dash(30), new Gap(25));
            polyline1 = map.addPolyline(new PolylineOptions().add(new LatLng(destPos.latitude, destPos.longitude), new LatLng(myPos.latitude, myPos.longitude)).color(Color.RED));
            polyline1.setPattern(pattern);
        }
    }

    public class SpinnerAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;

        public SpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return club.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ListContent holder;
            View v = convertView;
            if (v == null) {
                mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.row_textview, null);
                holder = new ListContent();
                holder.text = (TextView) v.findViewById(R.id.textView1);

                v.setTag(holder);
            } else {
                holder = (ListContent) v.getTag();
            }

            holder.text.setText(club[position]);

            return v;
        }
    }

    static class ListContent {
        TextView text;
    }


    private void writeNewShot(String holeid, String userID, String roundID, String desiredDistance, String actualDistance, String club, String shotNum, String lie_ball) {

        //checking vals
        Log.d("SHOT_ERROR", "holeid: " + holeid + " , shotNum: " + shotNum + ", club: " + club);

        shotKey = mDatabase.child("shots").push().getKey();
        Shots shot = new Shots(holeid,userID,roundID, desiredDistance, actualDistance, club, shotNum, lie_ball);
        Map<String, Object> shotValues = shot.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/shots/" + shotKey, shotValues);

        mDatabase.updateChildren(childUpdates);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference ref = database.getReference("shots/" + shotKey);
        ref.orderByChild("shotNum");
        //System.out.println(ref.);

        // Attach a listener to read the data at our rounds reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shots shot = dataSnapshot.getValue(Shots.class);
                System.out.println(shot.shotNum);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }


    @Override
    public void onDetach() {
        super.onDetach();
        View bnv = getActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.VISIBLE);
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }

    public void endRound(){
        //updating score and total putts values
        currentRoundID = ((MainActivity)getActivity()).getCurrentRoundKey();
        int fir = ((MainActivity)getActivity()).getFIR();
        int numPar3s = ((MainActivity)getActivity()).getNumPar3s();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference roundRef = database.getReference("rounds/" + currentRoundID);
        final DatabaseReference scoreRef = roundRef.child("score");
        final DatabaseReference totalPuttsRef = roundRef.child("totalPutts");
        final DatabaseReference totalGIRRef = roundRef.child("totalGIR");
        final DatabaseReference totalFIRRef = roundRef.child("totalFIR");
        final DatabaseReference numPar3Ref = roundRef.child("numPar3s");
        scoreRef.setValue(totalScore);
        totalPuttsRef.setValue(totalPutts);
        totalGIRRef.setValue(totalGIR);
        totalFIRRef.setValue(fir);
        numPar3Ref.setValue(numPar3s);

        bnv = getActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.VISIBLE);

        ((MainActivity)getActivity()).setShotTotal(0);
        ((MainActivity)getActivity()).setHoleNum(1);

        //resetting counter for next round
        ((MainActivity)getActivity()).setCounter(1);

        //((AppCompatActivity)getActivity()).getSupportActionBar().show();


        Fragment homeFrag = new HomeFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, homeFrag, "homeFrag")
                .addToBackStack(null)
                .commit();

    }



}

