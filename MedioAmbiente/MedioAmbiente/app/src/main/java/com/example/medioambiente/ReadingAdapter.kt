package com.example.medioambiente


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReadingAdapter :
    RecyclerView.Adapter<ReadingAdapter.VH>() {

    private var items = ArrayList<Reading>()
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTime: TextView = v.findViewById(R.id.tvTime)
        val tvValues: TextView = v.findViewById(R.id.tvValues)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reading, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]

        holder.tvTime.text = sdf.format(Date(r.timestamp))

        val texto = "T: ${r.temperature ?: "N/D"}°C  " +
                "H: ${r.humidity ?: "N/D"}%  " +
                "P: ${r.pressure ?: "N/D"} hPa  " +
                "L: ${r.light ?: "N/D"} lx"

        holder.tvValues.text = texto
    }

    override fun getItemCount(): Int = items.size

    fun setItems(list: ArrayList<Reading>) {
        items = list
        notifyDataSetChanged()
    }
}
