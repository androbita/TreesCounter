package com.ngopidev.project.treescounter.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ngopidev.project.treescounter.Model.Trees
import com.ngopidev.project.treescounter.Model.Users
import com.ngopidev.project.treescounter.R
import com.ngopidev.project.treescounter.activities.DetailTrees


/**
 *   created by Irfan Assidiq on 2019-08-28
 *   email : assidiq.irfan@gmail.com
 **/
class CvTreeAdapters : RecyclerView.Adapter<CvTreeAdapters.TreesViewHolder>{

    lateinit var listofTrees : List<Trees>
    lateinit var ctx : Context

    constructor(){}
    constructor(ctx : Context, listofTrees : List<Trees>){
        this.ctx = ctx
        this.listofTrees = listofTrees
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TreesViewHolder {
        val v = LayoutInflater.from(ctx).inflate(R.layout.list_data, p0, false)
        return TreesViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listofTrees.size
    }

    override fun onBindViewHolder(p0: TreesViewHolder, p1: Int) {
        val trees = listofTrees[p1]
        p0.treesNumber.text = "Tree\'s number :\n"+trees.treeNumber
        p0.treesName.text = "Tree\'s Name :\n"+trees.treeType

        val height = Math.round(trees.treeHeight!! * 100) / 100.0
        val diameter = Math.round(trees.treeDiameter!! * 100) / 100.0

        p0.treesHeight.text = "Tree\'s Height :\n"+height.toString()
        p0.treesDiameter.text = "Tree\'s Diameter :\n"+diameter.toString()
        Glide.with(ctx)
            .load(trees.treeImage)
            .error(R.drawable.ic_leaf)
            .into(p0.img_data)
        p0.cv_trees.setOnClickListener {
            val moveToDetail = Intent(ctx, DetailTrees::class.java)
            moveToDetail.putExtra("forDetail", trees.id)
            moveToDetail.putExtra("urlQr", trees.url)
            ctx.startActivity(moveToDetail)
            Toast.makeText(ctx, "hi this is ${trees.treeType}", Toast.LENGTH_SHORT).show()
        }

    }

    class TreesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var cv_trees : CardView
        var img_data : ImageView
        var treesName : TextView
        var treesHeight : TextView
        var treesNumber : TextView
        var treesDiameter : TextView
        init {
            cv_trees = itemView.findViewById(R.id.cv_trees)
            img_data = itemView.findViewById(R.id.img_data)
            treesNumber = itemView.findViewById(R.id.tvTreesNumbers)
            treesName = itemView.findViewById(R.id.tvTreesName)
            treesHeight = itemView.findViewById(R.id.tvTreesHigh)
            treesDiameter = itemView.findViewById(R.id.tvTreesDiameter)
        }

    }
}