package com.example.jorge.paint;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Vista extends View implements Picker.OnColorChangedListener{

    private int alto, ancho;
    private Paint pincel;
    private Bitmap mapaDeBits;
    private Canvas lienzoFondo;
    private float radio=0;
    private Path rectaPoligonal = new Path();
    private int color = Color.BLACK;
    private int accionPulsada = 0;
    private int grosor = 5;

    private float x0=0, y0=0, xi=0, yi=0;

    public Vista(Context context) {
        super(context);
        pincel = new Paint();
        pincel.setColor(color);
        pincel.setStrokeWidth(grosor);//ancho de la linea
        pincel.setAntiAlias(true);
        pincel.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
        lienzoFondo.drawColor(Color.WHITE);
        alto = h;
        ancho = w;
    }

    @Override
    protected void onDraw(Canvas lienzo) {
        super.onDraw(lienzo);
        lienzo.drawBitmap(mapaDeBits, 0, 0, null);

        switch (accionPulsada){
            case 0://Pincel
                pincel.setColor(color);
                pincel.setStrokeWidth(grosor);
                lienzo.drawPath(rectaPoligonal, pincel);
                break;
            case 1://Dibujar cuadrado
                pincel.setColor(color);
                pincel.setStrokeWidth(grosor);
                //Para dibujar en todos los sentidos.
                float xorigen = Math.min(x0,xi);
                float xdestino = Math.max(x0,xi);
                float yorigen = Math.min(y0,yi);
                float ydestino = Math.max(y0,yi);
                lienzo.drawRect(xorigen,yorigen,xdestino,ydestino,pincel);
                break;
            case 2://Dibujar lineas rectas
                pincel.setColor(color);
                pincel.setStrokeWidth(grosor);
                lienzo.drawLine(x0, y0, xi, yi, pincel);
                break;
            case 3://Dibujar un circulo
                pincel.setColor(color);
                pincel.setStrokeWidth(grosor);
                lienzo.drawCircle(x0, y0, radio, pincel);
                break;
            case 4://Goma
                pincel.setColor(Color.WHITE);
                pincel.setStrokeWidth(grosor);
                lienzo.drawPath(rectaPoligonal, pincel);
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (accionPulsada){
                    case 0://Pincel
                        x0 = xi = x;
                        y0 = yi = y;
                        rectaPoligonal.reset();
                        rectaPoligonal.moveTo(x0, y0);
                        break;
                    case 1://Para dibujar cuadrados, rectangulos...
                        x0 = xi = x;
                        y0 = yi = y;
                        break;
                    case 2://Lineas rectas
                        x0 = x;
                        y0 = y;
                        break;
                    case 3://Dibujar un circulo
                        x0 = x;
                        y0 = y;
                        break;
                    case 4://Goma
                        x0 = xi = x;
                        y0 = yi = y;
                        rectaPoligonal.reset();
                        rectaPoligonal.moveTo(x0, y0);
                        break;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                switch (accionPulsada){
                    case 0://Pincel
                        rectaPoligonal.quadTo(xi, yi,(x+xi)/2, (y+yi)/2);
                        x0 = xi = x;
                        y0 = yi = y;
                        invalidate();
                        break;
                    case 1://Para dibujar cuadrados, rectangulos...
                        xi = x;
                        yi = y;
                        invalidate();
                        break;
                    case 2://Lineas rectas
                        xi = x;
                        yi = y;
                        invalidate();
                        break;
                    case 3://Dibujar un circulo
                        xi = x;
                        yi = y;
                        radio = (float)Math.sqrt(Math.pow( x0 - xi, 2) + Math.pow(y0 - yi, 2));
                        invalidate();
                        break;
                    case 4://Goma
                        rectaPoligonal.quadTo(xi, yi,(x+xi)/2, (y+yi)/2);
                        x0 = xi = x;
                        y0 = yi = y;
                        invalidate();
                        break;
                }
                break;

            case MotionEvent.ACTION_UP:
                switch (accionPulsada){
                    case 0://Pincel
                        lienzoFondo.drawPath(rectaPoligonal, pincel);
                        x0 = y0 = xi = yi = -1;
                        break;
                    case 1://Para dibujar cuadrados, rectangulos...
                        xi = x;
                        yi = y;
                        lienzoFondo.drawRect(x0,y0,xi,yi,pincel);
                        break;
                    case 2://Lineas rectas
                        lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                        break;
                    case 3://Dibujar un circulo
                        xi = x;
                        yi = y;
                        radio = (float)Math.sqrt(Math.pow( x0 - xi, 2) + Math.pow(y0 - yi, 2));
                        lienzoFondo.drawCircle(x0, y0, radio, pincel);
                        break;
                    case 4://Goma
                        lienzoFondo.drawPath(rectaPoligonal, pincel);
                        x0 = y0 = xi = yi = -1;
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public void colorChanged(int color) {
        this.color = color;
    }

    public void colorSelector(){
        new Picker(this.getContext(), Vista.this, Color.WHITE).show();
    }

    public void accion(int accion){
        this.accionPulsada = accion;
    }

    public boolean grosor(Context principal){
        AlertDialog.Builder alert = new AlertDialog.Builder(principal);
        alert.setTitle(R.string.numero_grosor);
        LayoutInflater inflater = LayoutInflater.from(principal);
        final View vista = inflater.inflate(R.layout.grosor, null);
        alert.setView(vista);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText et1;
                        et1 = (EditText) vista.findViewById(R.id.etGrosor);
                        String c = et1.getText().toString();
                        grosor = Integer.parseInt(c);
                    }
                });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }

    public boolean guardar(final Context principal) {

        AlertDialog.Builder alert = new AlertDialog.Builder(principal);
        alert.setTitle(R.string.nombre_guardar);
        LayoutInflater inflater = LayoutInflater.from(principal);
        final View vista = inflater.inflate(R.layout.guardar, null);
        alert.setView(vista);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText et1;
                        et1 = (EditText) vista.findViewById(R.id.etGuardar);
                        String nombre = et1.getText().toString();
                        File carpeta = new File(Environment.getExternalStorageDirectory().getPath());
                        File archivo = new File (carpeta, nombre+".png");
                        try {
                            FileOutputStream fos = new FileOutputStream(archivo);
                            mapaDeBits.compress(Bitmap.CompressFormat.PNG, 90, fos);
                            Toast.makeText(principal, R.string.toast_guardar, Toast.LENGTH_SHORT).show();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }
}
