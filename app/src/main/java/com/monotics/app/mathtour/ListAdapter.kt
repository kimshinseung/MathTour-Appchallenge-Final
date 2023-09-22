package com.monotics.app.mathtour

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listitem.view.*


class ListAdapter(val itemlist: ArrayList<ListData>, val mainFragment: MainFragment): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ListAdapter(val layout: View): RecyclerView.ViewHolder(layout)
    init {
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem,parent,false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewHolder = (holder as ViewHolder).itemView
        viewHolder.location.text = itemlist[position].address
        viewHolder.info.text = itemlist[position].inf + "코스"
        Log.v("asdf", position.toString())

        WeatherApiData.weatherListLiveData.observe(mainFragment) {
            // it로 넘어오는 param은 LiveData의 value
            WeatherApiData.weatherListLiveData.value?.get(position % 17)?.get(0)?.rainType?.let { it1 ->
                Log.v("position: " + position.toString(),
                    it1
                )
            }
            when (WeatherApiData.weatherListLiveData.value?.get(position % 17)?.get(0)?.rainType) {
                "0" -> {
                    when (WeatherApiData.weatherListLiveData.value?.get(position % 17)
                        ?.get(0)?.sky) {
                        "1" -> viewHolder.weather.setImageResource(R.drawable.sunny)//"맑음"
                        "3" -> viewHolder.weather.setImageResource(R.drawable.cloudy)//"구름 많음"
                        "4" -> viewHolder.weather.setImageResource(R.drawable.very_cloudy)//"흐림"
                    }
                }
                //viewHolder.weather.setImageResource(R.drawable.sunny)//"없음"
                "1" -> viewHolder.weather.setImageResource(R.drawable.rain)//"비"
                "2" -> viewHolder.weather.setImageResource(R.drawable.rainsnow)//"비/눈"
                "3" -> viewHolder.weather.setImageResource(R.drawable.snow)//"눈"
                "5" -> viewHolder.weather.setImageResource(R.drawable.rain)//"빗방울"
                "6" -> viewHolder.weather.setImageResource(R.drawable.rainsnow)//"빗방울눈날림"
                "7" -> viewHolder.weather.setImageResource(R.drawable.snow)//"눈날림"
                else -> viewHolder.weather.setImageResource(R.drawable.question)
            }
        }


//        when(WeatherApiData.weaterList[position%17][0].rainType) {
//            "0" -> {
//                when(WeatherApiData.weaterList[position%17][0].sky) {
//                    "1" -> viewHolder.weather.setImageResource(R.drawable.sunny)//"맑음"
//                    "3" -> viewHolder.weather.setImageResource(R.drawable.cloudy)//"구름 많음"
//                    "4" -> viewHolder.weather.setImageResource(R.drawable.very_cloudy)//"흐림"
//                }
//            }
//            //viewHolder.weather.setImageResource(R.drawable.sunny)//"없음"
//            "1" -> viewHolder.weather.setImageResource(R.drawable.rain)//"비"
//            "2" -> viewHolder.weather.setImageResource(R.drawable.rainsnow)//"비/눈"
//            "3" -> viewHolder.weather.setImageResource(R.drawable.snow)//"눈"
//            else -> viewHolder.weather.setImageResource(R.drawable.pin2)
//        }

        viewHolder.setOnClickListener {
            val context = viewHolder.context
            val intent = Intent(context, DetailcourseActivity::class.java)
            val address = viewHolder.location.text
            intent.putExtra("loc",address)
            intent.putExtra("id", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}