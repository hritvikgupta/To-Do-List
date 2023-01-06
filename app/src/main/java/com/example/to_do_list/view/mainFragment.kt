package com.example.to_do_list.view

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.transition.TransitionInflater
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.R
import com.example.to_do_list.adapters.ItemsListAdapters
import com.example.to_do_list.adapters.TaskListAdapters
import com.example.to_do_list.model.CurrentTask
import com.example.to_do_list.model.Items
import com.example.to_do_list.model.LoginState
import com.example.to_do_list.model.Tasks
import com.example.to_do_list.viewmodel.MainViewModel
import com.example.to_do_list.viewmodel.itemsViewModel
import com.example.to_do_list.viewmodel.saveListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class mainFragment : Fragment(), MenuProvider{
    private lateinit var ItemsViewModel:itemsViewModel
    private lateinit var viewmodel: MainViewModel
    private lateinit var listSavingViewModel:saveListViewModel
    private lateinit var signoutBtn:Button
    private lateinit var deleteUsr: Button
    private lateinit var usernameTV:TextView
    private lateinit var mToolbar:Toolbar
    private lateinit var TasksListsAdapter:TaskListAdapters
    private lateinit var addTaskListAdapter: TaskListAdapters
    private lateinit var TasksList:RecyclerView
    private lateinit var addTaskView:RecyclerView
    private lateinit var newListName:EditText
    private lateinit var alertDialogBuilder: AlertDialog.Builder
    private lateinit var DialogView:View
    val num:Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = LoginState.user?.username
        //(activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color='#ff0000'>ActionBarTitle </font>"));

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)

        val inflater2 = TransitionInflater.from(requireContext())
        enterTransition = inflater2.inflateTransition(R.transition.slide_right)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val menuHost:MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            signoutBtn = view.findViewById(R.id.logoutButton)
            deleteUsr = view.findViewById(R.id.deleteUserBtn)
            usernameTV = view.findViewById(R.id.usernameTv)
            usernameTV.text = LoginState.user?.username
            addTaskView  = view.findViewById(R.id.addTasksRecyclerView)
            //(activity as AppCompatActivity).supportActionBar?.setLogo(R.drawable.ic_launcher_foreground)

            //(activity as AppCompatActivity).supportActionBar?.title = usernameTV.text.toString()
            signoutBtn.setOnClickListener { onSignout() }
            deleteUsr.setOnClickListener { onDelete() }
            //(activity as mainFragment?)?.activity?.actionBar?.title = LoginState.user?.username

            (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#344955")))
            (activity as AppCompatActivity).window.statusBarColor = Color.parseColor("#232F34")

            val li = LayoutInflater.from(activity)
            val customView: View = li.inflate(R.layout.main_menu, null)
            (activity as AppCompatActivity).supportActionBar?.setCustomView(customView)
            customView.findViewById<TextView>(R.id.userName).text = LoginState.user?.username
            (activity as AppCompatActivity).supportActionBar?.elevation = num

        //(activity as AppCompatActivity).supportActionBar?.title = LoginState.user?.username
            TasksList = view.findViewById(R.id.TasksRecyclerView)
            TasksListsAdapter = TaskListAdapters({task->onListItemClick(task)}, arrayListOf(),{task->onLongItemClick(task)})
            addTaskListAdapter= TaskListAdapters({task->onListItemClick(task)}, arrayListOf(Tasks("Add New List",null,
                listOf())),{task->onLongItemClick(task)})
            addTaskView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = addTaskListAdapter
            }
            TasksList.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = TasksListsAdapter
            }

            viewmodel = androidx.lifecycle.ViewModelProvider(this)[MainViewModel::class.java]
            listSavingViewModel =androidx.lifecycle.ViewModelProvider(this)[saveListViewModel::class.java]
            ItemsViewModel = androidx.lifecycle.ViewModelProvider(this)[com.example.to_do_list.viewmodel.itemsViewModel::class.java]
            observeModel()
            observeSaveListModel()
            viewmodel.refresh()


    }
    private fun onLongItemClick(tasks: Tasks){
        Toast.makeText(activity, "LongClick"+ tasks.TaskListName, Toast.LENGTH_SHORT).show()
        popBottomDialog(tasks)
    }





    fun onListItemClick(task:Tasks) {
        //Toast.makeText(activity, task.TaskListName + "Clicked", Toast.LENGTH_SHORT).show()
        if (task.TaskListName == "Add New List"){
            popDialog()}
        else{
            //Toast.makeText(activity, task.TaskListName + "Clicked", Toast.LENGTH_SHORT).show()
            openListItem(task)
        }
    }

    fun openListItem(tasks:Tasks){
        CurrentTask.currentState(tasks)
        Navigation.findNavController(usernameTV).navigate(R.id.actionGoToItems)
    }

    fun popDialog()
    {
        var name:String?=null
        alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("Create New List")
        DialogView  = layoutInflater.inflate(R.layout.alert_dialog_add_list, null, false)
        alertDialogBuilder.setView(DialogView)
        alertDialogBuilder.setPositiveButton("Add New List"){ dialog, which->
            newListName = DialogView.findViewById(R.id.addListDialogED)

            name = newListName.text.toString()
            //Toast.makeText(activity, "Clicked1" +" "+name, Toast.LENGTH_SHORT).show()
            //TasksListsAdapter.addToList(Tasks(name))

            viewmodel.addTaskList(name!!)
            LoginState.user?.taskList
            onSaveList(name!!)


        }.setNegativeButton("Cancel"){dialog, which->
            dialog.cancel()
            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
        }.show()
        //TasksListsAdapter.addToList(Tasks(name))
        //Toast.makeText(activity, "Clicked" +" "+newListName.text.toString(), Toast.LENGTH_SHORT).show()

    }
    fun observeSaveListModel()
    {
        listSavingViewModel.listSaved.observe(viewLifecycleOwner, Observer { isSaved->
            Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show()

        })
    }
    fun observeItemsListModel(){
        //viewmodel
    }

    fun observeModel(){
        viewmodel.signout.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "Signed Out", Toast.LENGTH_SHORT).show()
            (activity as AppCompatActivity).supportActionBar?.title = "To Do List"
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(false)
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#344955")))
            (activity as AppCompatActivity).window.statusBarColor = Color.parseColor("#232F34")


            Navigation.findNavController(usernameTV).navigate(R.id.actionGoToSignup)

        })
        viewmodel.userDeleted.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, "User Deleted", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(usernameTV).navigate(R.id.actionGoToSignup)
        })

        viewmodel.taskLists.observe( viewLifecycleOwner, Observer { taskLists -> taskLists.let {
            TasksListsAdapter.updateTasksLists(it)

        } })
    }

    private fun onSaveList(name:String){

        if (name.isEmpty()){
            Toast.makeText(activity, "Please fill the detail", Toast.LENGTH_SHORT).show()
        }else{
            listSavingViewModel.saveTaskList(name)
        }
    }


    private fun onSignout(){
        //(activity as AppCompatActivity).supportActionBar?.title = R.string.app_name.toString()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        viewmodel.onSignout()
    }

    private fun onDelete(){
        activity.let {
            AlertDialog.Builder(it)
                .setTitle("Delete user")
                .setMessage("Are you sure you want to delete this user ?")
                .setPositiveButton("Yes"){p0, p1->viewmodel.onDeleteUser()}
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId){
            R.id.searchTasks -> Toast.makeText(activity, "Searching...", Toast.LENGTH_SHORT).show()
            R.id.LogoutMenu -> viewmodel.onSignout()
        }

        return true
    }

    private fun onDeleteItem(tasks:Tasks){
        activity.let {
            AlertDialog.Builder(it)
                .setTitle("Delete user")
                .setMessage("Are you sure you want to delete this ${tasks.TaskListName} ?")
                .setPositiveButton("Yes"){p0, p1->viewmodel.onDeleteTask(tasks)}
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }

    }

    private fun popBottomDialog(tasks:Tasks){
        val dialog = activity?.let { BottomSheetDialog(it) }
        dialog?.setContentView(R.layout.bottom_dialogue)
        val delete = dialog?.findViewById<CardView>(R.id.deleteCardView)
        val edit = dialog?.findViewById<CardView>(R.id.editCardView)
        edit?.setOnClickListener {
            activity.let {
                var name:String?=null
                alertDialogBuilder = AlertDialog.Builder(activity)
                alertDialogBuilder.setTitle("Update Task Name")
                DialogView  = layoutInflater.inflate(R.layout.alert_dialog_add_list, null, false)
                alertDialogBuilder.setView(DialogView)
                newListName = DialogView.findViewById(R.id.addListDialogED)
                newListName.setHint("Enter New Name")
                alertDialogBuilder.setPositiveButton("Update"){ dialog, which->
                    name = newListName.text.toString()
                    //Toast.makeText(activity, "Clicked1" +" "+name, Toast.LENGTH_SHORT).show()
                    //TasksListsAdapter.addToList(Tasks(name))

                    viewmodel.onUpdateTask(tasks.TaskListName!!,name!!)


                }.setNegativeButton("Cancel"){dialog, which->
                    dialog.cancel()
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                }.show()
            }
            dialog.dismiss()
        }
        delete?.setOnClickListener {
            onDeleteItem(tasks)
            dialog.dismiss()
        }
        dialog?.show()

    }


}