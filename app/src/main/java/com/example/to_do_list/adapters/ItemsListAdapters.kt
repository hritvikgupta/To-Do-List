package com.example.to_do_list.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.R
import com.example.to_do_list.model.Items
import com.example.to_do_list.model.Tasks

class ItemsListAdapters(private val onlistItemClicked:(item:Items) ->Unit,var itemsLists:ArrayList<Items>, var key:Int,private val onItemLongClick: (item:Items) -> Unit) : RecyclerView.Adapter<ItemsListAdapters.ItemsListViewHolder>(){
    private lateinit var imageViewMainFragment:ImageView
    private lateinit var textViewMainFragment:TextView


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsListAdapters.ItemsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_list,parent,false)
        return ItemsListViewHolder(view, onlistItemClicked, onItemLongClick)
    }

    override fun onBindViewHolder(holder: ItemsListAdapters.ItemsListViewHolder, position: Int) {
        holder.bind(itemsLists[position])
        if(key ==1){
            holder.imageViewMainFragment.setImageResource(R.drawable.addition)
        }
        val curr_item = itemsLists[position].itemName
        if (curr_item!=""){}
        if(curr_item == "Create A Task"){
                holder.itemView.setOnClickListener {
                    holder.onClick(itemsLists[position])

                }
            }
        holder.itemView.setOnLongClickListener {
            holder.onLongClick(itemsLists[position])
            return@setOnLongClickListener true
        }




    }


    override fun getItemCount(): Int {
        return itemsLists.size
    }

    class ItemsListViewHolder(itemView: View, private val onlistItemClicked:(item:Items) ->Unit,
                              private var onItemLongClick: (item:Items) -> Unit):RecyclerView.ViewHolder(itemView){
        val imageViewMainFragment:ImageView = itemView.findViewById(R.id.imageViewItemFragment)
        val textViewItemFragment:TextView = itemView.findViewById(R.id.textViewItemTask)

        fun bind(tasklistsitems: Items){
            textViewItemFragment.text = tasklistsitems.itemName
        }

        init {
            itemView.setOnClickListener {this
            }
            itemView.setOnLongClickListener {
                return@setOnLongClickListener true}
        }

        fun onClick(item: Items) {
            onlistItemClicked(item)
        }

        fun onLongClick(item:Items){
            onItemLongClick(item)
        }



    }
    fun updateItemsLists(newList:List<Items>)
    {
        this.itemsLists.clear()
        itemsLists.addAll(newList)
        notifyDataSetChanged()
    }
    fun addToList(newItems: Items)
    {
        itemsLists.add(newItems)
        notifyDataSetChanged()
    }


}