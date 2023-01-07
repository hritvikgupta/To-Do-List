package com.hgapplication.to_do_list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hgapplication.to_do_list.R
import com.hgapplication.to_do_list.model.Tasks

class TaskListAdapters(private val onItemClicked:(task:Tasks) ->Unit,var TaskLists:ArrayList<Tasks>,private val onItemLongClick: (task:Tasks) -> Unit) : RecyclerView.Adapter<TaskListAdapters.TaskListViewHolder>(){
    private lateinit var imageViewMainFragment:ImageView
    private lateinit var textViewMainFragment:TextView


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListAdapters.TaskListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tasks_lists, parent, false)
        return TaskListViewHolder(view, onItemClicked,onItemLongClick)
    }

    override fun onBindViewHolder(holder: TaskListAdapters.TaskListViewHolder, position: Int) {
            holder.bind(TaskLists[position])
            val curr_item = TaskLists[position].TaskListName
            if(curr_item == "Add New List"){
                holder.imageViewMainFragment.setImageResource(R.drawable.addition)
                holder.itemView.setOnClickListener {
                    holder.onClick(TaskLists[position])

                }
            }
            else{
                holder.itemView.setOnClickListener {
                    holder.onClick(TaskLists[position])
                }
                holder.itemView.setOnLongClickListener {
                    holder.onLongClick(TaskLists[position])
                    return@setOnLongClickListener true
                }
            }

    }


    override fun getItemCount(): Int {
        return TaskLists.size
    }

    class TaskListViewHolder(itemView: View,  private val onItemClicked:(task:Tasks) ->Unit,private var onItemLongClick: (task: Tasks) -> Unit):RecyclerView.ViewHolder(itemView){
        val imageViewMainFragment:ImageView = itemView.findViewById(R.id.imageViewMainFragment)
        val textViewMainFragment:TextView = itemView.findViewById(R.id.textViewMainFramgment)

        fun bind(tasklistsitems: Tasks){
            textViewMainFragment.text = tasklistsitems.TaskListName
        }

        init {
            itemView.setOnClickListener {this
            }
            itemView.setOnLongClickListener {
                return@setOnLongClickListener true}
        }

        fun onClick(task: Tasks) {
            onItemClicked(task)
        }
        fun onLongClick(task: Tasks){
            onItemLongClick(task)
        }


    }
    fun updateTasksLists(newList:List<Tasks>)
    {
        this.TaskLists.clear()
        TaskLists.addAll(newList)
        notifyDataSetChanged()
    }
    fun addToList(newTask: Tasks)
    {
        TaskLists.add(newTask)
        notifyDataSetChanged()
    }
}