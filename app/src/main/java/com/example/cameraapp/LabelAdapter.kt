package com.example.cameraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LabelAdapter(private val dataSet: List<String>):
RecyclerView.Adapter<LabelAdapter.ViewHolder>(){


    //Give reference to type of views to be used

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.photolabel)

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view, defining the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_label, parent, false)

        return ViewHolder(view)


        }


    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {

        viewholder.textView.text = dataSet[position]
    }

     //Return the size of your dataset

      override fun getItemCount() = dataSet.size

}

