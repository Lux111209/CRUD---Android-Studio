package RecyclerViewHelper

import Modelo.ClaseConexion
import Modelo.dataClassProductos
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import luz.gaspario.crudluzgaspario2a.R

class Adaptador(private var Datos: List<dataClassProductos>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista: List<dataClassProductos>){
        Datos = nuevaLista
        notifyDataSetChanged()

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
        holder.textView.text = producto.nombreProducto

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
                eliminarRegistro(item.nombreProducto,position)

            }
            builder.setNegativeButton("No"){ dialog, which ->

            }

            //Creamos la alerta
            val alertDialog = builder.create()

            //Mostramos la alerta
            alertDialog.show()
        }

    }
}