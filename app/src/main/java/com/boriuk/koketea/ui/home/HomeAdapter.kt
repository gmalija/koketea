package com.boriuk.koketea.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boriuk.koketea.R
import com.boriuk.koketea.domain.PrendaItem
import com.bumptech.glide.Glide

class HomeAdapter(private val dataSet: ArrayList<PrendaItem>,
                  private val context: Context,
                  private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val imPrenda: ImageView = view.findViewById(R.id.imPrenda)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.home_list_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvName.text = dataSet[position].descripcion
        viewHolder.tvPrice.text = dataSet[position].precio.toString() + " eur"

        Glide.with(context)
            .load(dataSet[position].imagen)
            .into(viewHolder.imPrenda)

        // On click
        viewHolder.itemView.setOnClickListener {
            onClick(dataSet[position].id)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}