package com.example.salonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.salonapp.views.CurvedBottomNavigationView;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

public class CurvedNavigationFooter extends AppCompatActivity  {

    CurvedBottomNavigationView bottomNavigationView;
    VectorMasterView fab, fab1, fab2;

    RelativeLayout lin_id;
    PathModel outline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curved_navigation_footer);

        /*CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedCustomBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);*/

    }
}
