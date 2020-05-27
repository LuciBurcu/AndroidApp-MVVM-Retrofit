package com.luciburcugithub.mobilebackend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luciburcugithub.mobilebackend.R
import com.luciburcugithub.mobilebackend.model.CityEntity

class AdapterRecycler internal constructor(
    context: Context,
    private val clickListener: (CityEntity) -> Boolean
) : RecyclerView.Adapter<AdapterRecycler.CityHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cities: List<CityEntity> = arrayListOf()

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.tv_name)
        val cityPopulation: TextView = itemView.findViewById(R.id.tv_population)

        fun bindLongTouch(
            view: View,
            cityEntity: CityEntity,
            clickListener: (CityEntity) -> Boolean
        ) {
            view.setOnLongClickListener { clickListener(cityEntity) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = inflater.inflate(R.layout.adapter_layout, parent, false)
        return CityHolder(view)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.cityName.text = cities[position].name
        holder.cityPopulation.text = cities[position].population.toString()
        holder.bindLongTouch(holder.itemView, cities[position], clickListener)
    }

    override fun getItemCount() = cities.size

    internal fun setItems(cities: List<CityEntity>) {
        this.cities = cities
        notifyDataSetChanged()
    }

}