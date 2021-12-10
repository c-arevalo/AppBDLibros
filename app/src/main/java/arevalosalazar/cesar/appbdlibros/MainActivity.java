package arevalosalazar.cesar.appbdlibros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {
    CrearBD crearbd;
    SQLiteDatabase db;
    EditText etCodigo;
    EditText etTitulo;
    EditText etAutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crearbd = new CrearBD(this);
        /*SQLiteDatabase db = crearbd.getWritableDatabase();

        db.execSQL("INSERT INTO libros VALUES(1,'Guerra','Mario')");
        db.execSQL("INSERT INTO libros VALUES(2,'Paz','Luigi')");
        db.close();*/
    }

    public void ConsultarLibro(View v){
        etCodigo = findViewById(R.id.txtCod);
        etTitulo = findViewById(R.id.txtTitulo);
        etAutor = findViewById(R.id.txtAutor);
        String cod = etCodigo.getText().toString();
        db = crearbd.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM libros WHERE codigo='"+ cod+"'",null);
        if (c.moveToNext()){
            etTitulo.setText(c.getString(1));
            etAutor.setText(c.getString(2));
        }else{
            verMensajeToast(String.valueOf(R.string.no));
            if (!TextUtils.isEmpty(etTitulo.getText())){
                etTitulo.setText("");
                etAutor.setText("");
            }

        }
        c.close();
        db.close();
    }

    public void insertarLibro(View v){
        db = crearbd.getWritableDatabase();
        if(TextUtils.isEmpty(etCodigo.getText()) || TextUtils.isEmpty(etTitulo.getText()) || TextUtils.isEmpty(etAutor.getText())){
            verMensajeToast(String.valueOf(R.string.datos));
        }else{
            String cod = etCodigo.getText().toString();
            String titulo = etTitulo.getText().toString();
            String autor = etAutor.getText().toString();
            try{
                db.execSQL("INSERT INTO libros VALUES(" + cod + ",'" + titulo +"', '" + autor + "');");
                verMensajeToast(String.valueOf(R.string.insertado));
            }catch (Exception sqlex){
                verMensajeToast(sqlex.getMessage());
            }
        }
        LimpiarCajas();
        //db.close();
    }

    public void BorrarLibro(View v){
        db = crearbd.getWritableDatabase();
        if (TextUtils.isEmpty(etCodigo.getText()) && TextUtils.isEmpty(etTitulo.getText())){
            verMensajeToast(String.valueOf(R.string.borrar));
        }else{
            String cod = etCodigo.getText().toString();
            String titulo = etTitulo.getText().toString();
            try{
                db.execSQL("DELETE FROM libros WHERE codigo=" + cod + " OR titulo= '" + titulo + "'");
                verMensajeToast(String.valueOf(R.string.borrado));
            }catch (Exception sqlex){
                verMensajeToast(sqlex.getMessage());}
        }
        LimpiarCajas();
        //db.close();
    }

    public void verMensajeToast(String mensaje){
        Context contexto = getApplicationContext();
        Toast t = Toast.makeText(contexto,mensaje,Toast.LENGTH_LONG);
        t.show();
    }

    public void LimpiarCajas(){
        etAutor.setText("");
        etCodigo.setText("");
        etTitulo.setText("");
    }
}