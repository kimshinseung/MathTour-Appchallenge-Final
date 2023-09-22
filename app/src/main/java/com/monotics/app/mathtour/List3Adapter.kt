package com.monotics.app.mathtour

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.set
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listitem3.view.*


class List3Adapter(
    val itemlist: Array<Int>,
    val nameList: Array<String>,
    val cameralist: Array<String?>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class List3Adapter(val layout: View): RecyclerView.ViewHolder(layout)
    init {
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem3,parent,false)
        return ViewHolder(view)

    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val converter = BitmapConverter()
        lateinit var imageBitmap: Bitmap
        val imgList = arrayOf(R.mipmap.gogung2, R.mipmap.gyungbookgung2 , R.mipmap.gongyae2, R.mipmap.sound2)
        var viewHolder = (holder as ViewHolder).itemView

        if(nameList[0]==""){ //미완성된 코스
            viewHolder.imgitem.setImageResource(itemlist[position])
        }
        else if(cameralist[position]!=""){ //찍은 사진이 있을 때
            val img = converter.stringToBitmap(cameralist[position])
            viewHolder.imgitem.setImageBitmap(img)

        }else{ //찍은 사진 없을 때
            viewHolder.imgitem.setImageResource(imgList[position])
        }

        viewHolder.infocourse.text = nameList[position]

    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}