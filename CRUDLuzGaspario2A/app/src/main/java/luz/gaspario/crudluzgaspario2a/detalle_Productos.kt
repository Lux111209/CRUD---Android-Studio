package luz.gaspario.crudluzgaspario2a

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detalle_Productos : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_productos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1. Mando a llamar a todos los elementos de la pantalla
        val txtUUID_Detalle = findViewById<TextView>(R.id.txtUUID_Detalle)
        val txtNombreDetalle = findViewById<TextView>(R.id.txtNombreDetalle)
        val txtPrecioDetalle = findViewById<TextView>(R.id.txtPrecioDetalle)
        val txtCantidadDetalle = findViewById<TextView>(R.id.txtCantidadDetalle)
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)

        //2. Recibir los valores
        val UUIDProducto = intent.getStringExtra("uuid")
        val nombreProducto = intent.getStringExtra("nombre")
        val precioProducto = intent.getIntExtra("precio", 0)
        val cantidadProducto = intent.getIntExtra("cantidad", 0)

        //3. Poner los valores recibidos en el textView
        txtUUID_Detalle.text = UUIDProducto
        txtNombreDetalle.text = nombreProducto
        txtPrecioDetalle.text = precioProducto.toString()
        txtCantidadDetalle.text = cantidadProducto.toString()


        //Ir una pantalla hacia atrás
        imgAtras.setOnClickListener {
            val pantallaAtras = Intent(this, MainActivity::class.java)
            startActivity(pantallaAtras)
        }
    }
}