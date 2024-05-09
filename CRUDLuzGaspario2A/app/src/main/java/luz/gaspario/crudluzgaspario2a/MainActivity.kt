package luz.gaspario.crudluzgaspario2a

import Modelo.ClaseConexion
import Modelo.dataClassProductos
import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1. Mandar a llamar a todos los elementos de la pantalla
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        fun limpiar(){
            txtNombre.setText("")
            txtPrecio.setText("")
            txtCantidad.setText("")
        }

        //////////////////// TODO: Mostrar Datos ////////////////////

        ////////////Mostrar/////////////
        val rcvProductos = findViewById<RecyclerView>(R.id.rcvProductos)

        //Asignar un layout al RecyclerView
        rcvProductos.layoutManager = LinearLayoutManager(this)

        //Función para obtener datos
        fun obtenerDatos(): List<dataClassProductos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbProductos")!!
            val productos = mutableListOf<dataClassProductos>()
            while (resultSet.next()){
                val nombre = resultSet.getString("nombreProducto")
                val producto = dataClassProductos(nombre)
                productos.add(producto)
            }
            return productos
        }

        //Asignar un adaptador
        CoroutineScope(Dispatchers.IO).launch {
            val productoDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val miAdapter = Adaptador(productoDB)
                rcvProductos.adapter = miAdapter
            }
        }

        //////////////////// TODO: Guardar Productos ////////////////////

        //2. Programar el boton
        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){


                //Guardar datos
                //1. Creo un objeto de la clase conexcion
                val claseConexion = ClaseConexion().cadenaConexion()

                //2. Creo una variable que contenga un PrepareStatement
                val addProducto = claseConexion?.prepareStatement("insert into tbProductos(nombreProducto, precio, cantidad) values(?, ?, ?)")!!

                addProducto.setString(1, txtNombre.text.toString())
                addProducto.setInt(2, txtPrecio.text.toString().toInt())
                addProducto.setInt(3, txtCantidad.text.toString().toInt())

                addProducto.executeUpdate()

                val  nuevosProductos = obtenerDatos()
                withContext(Dispatchers.Main){
                    (rcvProductos.adapter as? Adaptador)?.actualizarLista(nuevosProductos)
                }
            }
            //limpiar()
        }


    }
}