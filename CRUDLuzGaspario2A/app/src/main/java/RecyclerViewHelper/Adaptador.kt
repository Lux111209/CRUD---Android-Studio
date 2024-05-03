package RecyclerViewHelper

import Modelo.dataClassProductos
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import luz.gaspario.crudluzgaspario2a.R

class Adaptador(private val Datos: List<dataClassProductos>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)    }
    override fun getItemCount() = Datos.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreProducto
    }
}