package RecyclerViewHelper

import Modelo.ClaseConexion
import Modelo.dataClassProductos
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import luz.gaspario.crudluzgaspario2a.R
import luz.gaspario.crudluzgaspario2a.detalle_Productos

class Adaptador(private var Datos: List<dataClassProductos>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista: List<dataClassProductos>){
        Datos = nuevaLista
        notifyDataSetChanged()

    }

    //Función para actualizar el reciclerView cuando actualizo los datos

    fun actualizarListaDespuesDeActualizar(uuid: String, nuevoNombre: String){
        val index = Datos.indexOfFirst { it.uuid ==uuid }
        Datos[index].NombreProducto = nuevoNombre
        notifyItemChanged(index)
    }
    fun actualizarProducto(nombreProducto: String, uuid: String){

        //Creo una corrutina
        //Una corrutina es como un hilo multitarea par no sobrecargar
        GlobalScope.launch(Dispatchers.IO){
            //1. Creo un objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            //2. Creo una variable que contenga un prepareStatement
            val updateProducto = objConexion?.prepareStatement("update tbProductos set nombreProducto = ? where uuid = ?")!!
            updateProducto.setString(1, nombreProducto)
            updateProducto.setString(2, uuid)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main) {
                actualizarListaDespuesDeActualizar(uuid, nombreProducto)
            }
        }

    }

    fun eliminarRegistro(nombreProducto: String, posicion: Int){

        //Quitar elemento de la lista
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        //Quitar de la base de datos
        GlobalScope.launch(Dispatchers.IO){
            //1. Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()
            val deleteProducto = objConexion?.prepareStatement("delete tbProductos where nombreProducto = ?")!!
            deleteProducto.setString(1, nombreProducto)
            deleteProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()


        }

        //Le decimos al Adaptador que se eliminaron los datos
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)    }
    override fun getItemCount() = Datos.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.NombreProducto

        val item = Datos[position]

        holder.imgBorrar.setOnClickListener {
            //Creamos una alerta
            //1. Invocamos o importamos el contexto
            val context = holder.itemView.context

            //Creo la alerta
            val builder = AlertDialog.Builder(context)

            //Le pongo un título a mi alerta
            builder.setTitle("Eliminar")

            //Ponerle un mensaje a la alerta
            builder.setMessage("¿Estás seguro de eliminar el registro?")

            //Paso final, agregar los botones
            builder.setPositiveButton("Si"){ dialog, which ->
                eliminarRegistro(item.NombreProducto,position)

            }
            builder.setNegativeButton("No"){ dialog, which ->

            }

            //Creamos la alerta
            val alertDialog = builder.create()

            //Mostramos la alerta
            alertDialog.show()
        }

        holder.imgEditar.setOnClickListener {

            val context = holder.itemView.context

            //Creo la alerta
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Modificar nombre")

            //Agregamos un cuadro de texto para que el usuario pueda escribir el nuevo nombre

            val cuadritoNuevoNombre = EditText(context)
            cuadritoNuevoNombre.setHint(item.NombreProducto)
            builder.setView(cuadritoNuevoNombre)

            builder.setPositiveButton("Actualizar"){ dialog, wich ->
                actualizarProducto(cuadritoNuevoNombre.text.toString(), item.uuid)
            }

            builder.setNegativeButton("Cancelar"){ dialog, wich ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        //Darle clic a la card

        holder.itemView.setOnClickListener {
            //Invoco el contexto
            val context = holder.itemView.context

            //Cambiamos de pantalla
            //Abro la pantalla de DetalleProducto
            val pantallaDetalles = Intent(context, detalle_Productos::class.java)

            //Aquí, ANTES de abrir la pantalla, le mando los parámetros
            pantallaDetalles.putExtra("uuid", item.uuid)
            pantallaDetalles.putExtra("nombre", item.NombreProducto)
            pantallaDetalles.putExtra("precio", item.precio)
            pantallaDetalles.putExtra("cantidad", item.cantidad)

            context.startActivity(pantallaDetalles)
        }
    }
}