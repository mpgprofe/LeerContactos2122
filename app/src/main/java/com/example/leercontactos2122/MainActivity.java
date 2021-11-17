package com.example.leercontactos2122;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISO_CONTACTOS = 666;
    ListView listaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaContactos = findViewById(R.id.listaContactos);

        int permisoCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (permisoCheck == PackageManager.PERMISSION_GRANTED) {
            mostrarContactos();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISO_CONTACTOS);
        }

    }

    private void mostrarContactos() {
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                null,null,ContactsContract.Contacts.DISPLAY_NAME+ " ASC ");

        ArrayList contactos = new ArrayList<String>();

        while(c.moveToNext()){
            @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String telefono = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactos.add("Nombre: "+nombre+"\nTeléfono:"+telefono);
        }
        c.close();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,contactos);
        listaContactos.setAdapter(arrayAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISO_CONTACTOS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mostrarContactos();
            }else{
                Toast.makeText(this, "Sin permisos no te puedo mostrar los contactos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}