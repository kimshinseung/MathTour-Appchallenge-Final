package com.monotics.app.mathtour

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listitem2.view.*


class List2Adapter(val itemlist: Array<ModelWeather>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class List2Adapter(val layout: View): RecyclerView.ViewHolder(layout)
    init {
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem2,parent,false)
        return ViewHolder(view)

    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        var weatherIcon = viewHolder.weather_icon


        if(itemlist[position].temp != null) viewHolder.temp.text = "기온: " + itemlist[position].temp + "°C"
        else viewHolder.temp.text = "기온: " + "??" + "°C"

        if(itemlist[position].humidity != null) viewHolder.humid.text = "습도: " + itemlist[position].humidity + "%"
        else viewHolder.humid.text = "습도: " + "??" + "%"

        if(itemlist[position].fcstTime != ""){
            val str = itemlist[position].fcstTime
            val time = str.substring(0,2)
            viewHolder.time.text = time+"시"
        }
        else{
            viewHolder.time.text = "??시"
        }

        viewHolder.temp.text = "기온: " + itemlist[position].temp + "°C"

        //Log.v("asdfasdf", time.toString()+""+time2.toString())

        when (itemlist[position].rainType) {
            "0" -> {
                when (itemlist[position].sky) {
                    "1" -> weatherIcon.setImageResource(R.drawable.sunny)//"맑음"
                    "3" -> weatherIcon.setImageResource(R.drawable.cloudy)//"구름 많음"
                    "4" -> weatherIcon.setImageResource(R.drawable.very_cloudy)//"흐림"
                }
            }
            //viewHolder.weather.setImageResource(R.drawable.sunny)//"없음"
            "1" -> weatherIcon.setImageResource(R.drawable.rain)//"비"
            "2" -> weatherIcon.setImageResource(R.drawable.rainsnow)//"비/눈"
            "3" -> weatherIcon.setImageResource(R.drawable.snow)//"눈"
            "5" -> weatherIcon.setImageResource(R.drawable.rain)//"빗방울"
            "6" -> weatherIcon.setImageResource(R.drawable.rainsnow)//"빗방울눈날림"
            "7" -> weatherIcon.setImageResource(R.drawable.snow)//"눈날림"
            else -> weatherIcon.setImageResource(R.drawable.question)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}