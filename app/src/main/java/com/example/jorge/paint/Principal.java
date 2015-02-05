package com.example.jorge.paint;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



public class Principal extends Activity{
    private Vista vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vista = new Vista(this);
        setContentView(vista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_colores) {
            vista.colorSelector();
            return true;
        }else if (id == R.id.action_mano_alzada) {
            vista.accion(0);
            return true;
        }else if (id == R.id.action_cuadrado) {
            vista.accion(1);
            return true;
        }else if (id == R.id.action_linea) {
            vista.accion(2);
            return true;
        }else if (id == R.id.action_circulo) {
            vista.accion(3);
            return true;
        }else if (id == R.id.action_goma) {
            vista.accion(4);
            return true;
        }else if (id == R.id.action_grosor) {
            vista.grosor(this);
            return true;
        }else if (id == R.id.action_guardar) {
            vista.guardar(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
