package arevalosalazar.cesar.appbdlibros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {
    CrearBD crearbd;
    EditText etCodigo;
    EditText etTitulo;
    EditText etAutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crearbd = new CrearBD(this);
        etCodigo = findViewById(R.id.txtCod);
        etTitulo = findViewById(R.id.txtTitulo);
        etAutor = findViewById(R.id.txtAutor);
        /*SQLiteDatabase db = crearbd.getWritableDatabase();

        db.execSQL("INSERT INTO libros VALUES(1,'Guerra','Mario')");
        db.execSQL("INSERT INTO libros VALUES(2,'Paz','Luigi')");
        db.close();*/
    }

    public void ConsultarLibro(View v) {

        String cod = etCodigo.getText().toString();
        SQLiteDatabase db = crearbd.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM libros WHERE codigo='" + cod + "'", null);
        if (c.moveToNext()) {
            etCodigo.setText("");
            etTitulo.setText(c.getString(1));
            etAutor.setText(c.getString(2));
        } else {
            verMensajeToast("El libro no existe");
            if (!TextUtils.isEmpty(etTitulo.getText())) {
                etTitulo.setText("");
                etAutor.setText("");
            }

        }
        c.close();
        db.close();
    }

    public void insertarLibro(View v) {
        SQLiteDatabase db = crearbd.getWritableDatabase();
        if (etCodigo.getText().toString().equals("") || etTitulo.getText().toString().equals("") || etAutor.getText().toString().equals("")) {
            verMensajeToast("Inserta todos los datos");
        } else {
            String cod = etCodigo.getText().toString();
            String titulo = etTitulo.getText().toString();
            String autor = etAutor.getText().toString();
            try {
                db.execSQL("INSERT INTO libros VALUES(" + cod + ",'" + titulo + "', '" + autor + "');");
                verMensajeToast("Libro insertado correctamente");
            } catch (Exception sqlexc) {
                verMensajeToast(sqlexc.getMessage());
            }
        }
        LimpiarCajas();
        db.close();
    }

    public void BorrarLibro(View v) {
        SQLiteDatabase db = crearbd.getWritableDatabase();
        if (TextUtils.isEmpty(etCodigo.getText()) && TextUtils.isEmpty(etTitulo.getText())) {
            verMensajeToast("Inserta el código o el título del libro a borrar");
        } else {
            String titulo = etTitulo.getText().toString();
            String cod = etCodigo.getText().toString();
            SQLiteDatabase dbr = crearbd.getReadableDatabase();
            Cursor c = db.rawQuery("select * from libros where codigo= '" + cod + "' or titulo = '" + titulo + "'", null);
            if (c.moveToNext()) {
                if (TextUtils.isEmpty(etCodigo.getText())) {
                    try {
                        db.execSQL("DELETE FROM libros WHERE titulo= '" + titulo + "'");
                        verMensajeToast("Libro borrado correctamente");
                    } catch (Exception sqlex) {
                        verMensajeToast(sqlex.getMessage());
                    }
                } else if (TextUtils.isEmpty(etTitulo.getText())) {
                    try {
                        db.execSQL("DELETE FROM libros WHERE codigo= '" + cod + "'");
                        verMensajeToast("Libro borrado correctamente");
                    } catch (Exception sqlex) {
                        verMensajeToast(sqlex.getMessage());
                    }
                } else {
                    try {
                        db.execSQL("DELETE FROM libros WHERE codigo= '" + cod + "' and titulo= '" + titulo + "'");
                        verMensajeToast("Libro borrado correctamente");
                    } catch (Exception sqlex) {
                        verMensajeToast(sqlex.getMessage());
                    }
                }
            } else {
                verMensajeToast("Ese libro no existe");
            }
        }

        LimpiarCajas();
        db.close();
    }

    public void Lista(View v){
        Intent i = new Intent(this, VerLibros.class);
        startActivity(i);
    }

    public void verMensajeToast(String mensaje) {
        Context contexto = getApplicationContext();
        Toast t = Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG);
        t.show();
    }

    public void LimpiarCajas() {
        etAutor.setText("");
        etCodigo.setText("");
        etTitulo.setText("");
    }
}