package arevalosalazar.cesar.appbdlibros;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class VerLibros extends AppCompatActivity {

    CrearBD crearbd;
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_libros);
        crearbd = new CrearBD(this);
        lista = (ListView) findViewById(R.id.listaLibros);
        consultarLibros();
    }

    public void consultarLibros()
    {
        List<String> items = new ArrayList<String>();
        SQLiteDatabase db = crearbd.getReadableDatabase();
        Cursor contenido = db.rawQuery("select * from libros order by 1",null);
        int i=0;
        String cad ="";

        while(contenido.moveToNext() && i< contenido.getCount())
        {
            cad = "\nLibro "+ contenido.getString(0) + ":\n\t-Título: "+contenido.getString(1)+"\n\t-Autor: "+ contenido.getString(2)+"\n";
            items.add(cad);
            i++;
        }
        ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        lista.setAdapter(a);
        contenido.close();
        db.close();

    }
}