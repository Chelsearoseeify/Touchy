package com.example.selen.touch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.adapter.StructuresAdapter;

/**
 * Created by Administrator on 02/12/2017.
 */

public class StructureChosenActivity extends AppCompatActivity {

    ContactAdapter dbContact;
    StructuresAdapter dbStructures;
    GeoAdapter dbGeo;
    TextView id, name, category, segment, tipology, site, mail, phone, latitude, longitude, address;
    ImageButton imageButtonMail, imageButtonPhone, imageButtonWeb;
    Cursor structuresCursor, contactCursor, geoCursor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.structure_layout);

        imageButtonMail = (ImageButton) findViewById(R.id.imageButtonMail);
        imageButtonPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        imageButtonWeb = (ImageButton) findViewById(R.id.imageButtonWeb);


        Integer idFromIntent = getIntent().getExtras().getInt("id_struttura");

        dbContact = new ContactAdapter(this);
        dbStructures = new StructuresAdapter(this);
        dbGeo = new GeoAdapter(this);

        dbContact.open();
        dbStructures.open();
        dbGeo.open();

        contactCursor = dbContact.getContactById(idFromIntent);
        geoCursor = dbGeo.getGeoById(idFromIntent);
        structuresCursor = dbStructures.getStructureById(idFromIntent);


        id = (TextView) findViewById(R.id.idView);
        name = (TextView) findViewById(R.id.nameView);
        category = (TextView) findViewById(R.id.categoriaView);
        segment = (TextView) findViewById(R.id.segmentoView);
        tipology = (TextView) findViewById(R.id.tipologiaView);
        site = (TextView) findViewById(R.id.sitoView);
        mail = (TextView) findViewById(R.id.emailView);
        phone = (TextView) findViewById(R.id.phoneView);
        latitude = (TextView) findViewById(R.id.latitudineView);
        longitude = (TextView) findViewById(R.id.longitudineView);
        address = (TextView) findViewById(R.id.indirizzoView);


        name.setText(structuresCursor.getString(structuresCursor.getColumnIndexOrThrow("struttura")));
        site.setText(contactCursor.getString(contactCursor.getColumnIndexOrThrow("sito")));
        mail.setText(contactCursor.getString(contactCursor.getColumnIndexOrThrow("mail")));
        phone.setText(geoCursor.getString(geoCursor.getColumnIndexOrThrow("telefono")));
        //latitude.setText(geoCursor.getString(geoCursor.getColumnIndexOrThrow("latitudine")));
        //longitude.setText(geoCursor.getString(geoCursor.getColumnIndexOrThrow("longitudine")));
        //address.setText(geoCursor.getString(geoCursor.getColumnIndexOrThrow("indirizzo")));


        imageButtonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_SEND);
                webIntent.setData(Uri.parse("email"));
                String[] s ={contactCursor.getString(contactCursor.getColumnIndexOrThrow("mail"))};
                webIntent.putExtra(Intent.EXTRA_EMAIL, s);
                webIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                webIntent.putExtra(Intent.EXTRA_TEXT, "Text");
                webIntent.setType("message/rfc822");
                Intent chooser = Intent.createChooser(webIntent, "Launch Email");
                startActivity(chooser);
            }
        });

        imageButtonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = contactCursor.getString(contactCursor.getColumnIndexOrThrow("sito"));
                int count = url.length();
                if(count>2) {
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }else{
                    Toast.makeText(getApplicationContext(), "Selected structure hasn't a web site", Toast.LENGTH_SHORT).show();

                }
            }
        });

        imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                String num = geoCursor.getString(geoCursor.getColumnIndexOrThrow("telefono"));
                Context context = getApplicationContext();
                if(num.trim() == "-"){
                    Toast.makeText(context, "Selected structure hasn't a telephone", Toast.LENGTH_SHORT).show();
                }else{
                    phoneIntent.setData(Uri.parse("tel:" + num));
                }

                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Please grant the permission to call", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(StructureChosenActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }else{
                    startActivity(phoneIntent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StructureChosenActivity.this, RecyclerViewActivity.class);
        intent.putExtra("category", structuresCursor.getString(structuresCursor.getColumnIndexOrThrow("categoria")));
        startActivity(intent);
        finish();
    }
}
