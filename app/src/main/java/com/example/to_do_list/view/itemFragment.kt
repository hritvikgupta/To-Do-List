package com.example.to_do_list.view

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.R
import com.example.to_do_list.adapters.ItemsListAdapters
import com.example.to_do_list.databinding.ActivityMainBinding
import com.example.to_do_list.model.CurrentTask
import com.example.to_do_list.model.Items
import com.example.to_do_list.viewmodel.itemsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*


class itemFragment : Fragment(), MenuProvider{

    private lateinit var taskListName:TextView
    private lateinit var dateItemCreated:TextView
    private lateinit var ItemsViewModel:itemsViewModel
    private lateinit var ItemsListsAdapter:ItemsListAdapters
    private lateinit var fixItemListAdapter:ItemsListAdapters
    private lateinit var ItemsList:RecyclerView
    private lateinit var alertDialogBuilder:AlertDialog.Builder
    private lateinit var DialogView:View
    private lateinit var newListName: EditText
    private lateinit var fixRecyclerView: RecyclerView
    private lateinit var appBarConfiguration: AppBarConfiguration
    var dummy = arrayListOf<Items>()
    val num:Float = 0f
    var time:String?=null
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent:PendingIntent
    private lateinit var receiver: Receiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding = ActivityMainBinding.inflate(layoutInflater)

        createNotificationChannel()

        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dummy.add(Items("Testing"))
        taskListName = view.findViewById(R.id.taskListName)
        ItemsList = view.findViewById(R.id.itemRecyclerView)
        fixRecyclerView = view.findViewById(R.id.fixItemRecyclerView)
        ItemsListsAdapter = ItemsListAdapters({items->onListItemClick(items)}, arrayListOf(Items("User List Adapter")),0,{items->onLongItemClick(items)})
        //ItemsListsAdapter = ItemsListAdapters({items->onListItemClick(items)}, arrayListOf(Items("Create A Task")),0)
        fixItemListAdapter = ItemsListAdapters({items->onListItemClick(items)}, arrayListOf(Items("Create A Task")),1,{items->onLongItemClick(items)})
        fixRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = fixItemListAdapter
        }
        ItemsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ItemsListsAdapter
        }


        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(20), rnd.nextInt(20))

        val colors = arrayOf(
            Color.parseColor("#ff8a65"),
            Color.parseColor("#ffbb93"),
            Color.parseColor("#a69b97"),
            Color.parseColor("#9ea7aa"),
            Color.parseColor("#ff9e80")


            //...more
        )
        val randomColor = colors.random()
        view.setBackgroundColor(randomColor)


        taskListName.text = CurrentTask.taskList?.TaskListName!!
        (activity as AppCompatActivity).supportActionBar?.title = "Lists"

        ItemsViewModel = androidx.lifecycle.ViewModelProvider(this)[itemsViewModel::class.java]
        ItemsViewModel.refresh()
        //ItemsViewModel.addTaskList("Default")
        observeItemModel()

        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(randomColor))
        (activity as AppCompatActivity).supportActionBar?.elevation = num

        //(activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        val li = LayoutInflater.from(activity)
        val customView: View = li.inflate(R.layout.items_menu, null)
        (activity as AppCompatActivity).supportActionBar?.setCustomView(customView)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(true)
        //customView.setBackgroundColor(randomColor)
        (activity as AppCompatActivity).window.statusBarColor = randomColor

        val backButton:ImageView  = customView.findViewById(R.id.backPress_itemMenu)
        backButton.setOnClickListener {
            Navigation.findNavController(taskListName).navigate(R.id.actionGoToMain)
        }
        val textView:TextView = customView.findViewById(R.id.textViewItemMenu)
        textView.text = "Lists"


    }

    private fun onLongItemClick(items:Items){
        Toast.makeText(activity, "LongClick"+ items.itemName, Toast.LENGTH_SHORT).show()
        popBottomDialog(items)
    }


    private fun onListItemClick(items: Items) {
        Toast.makeText(activity,"Clicked" + items.itemName, Toast.LENGTH_SHORT).show()
        popDialog()
    }

    fun observeItemModel(){
        ItemsViewModel.listItems.observe(viewLifecycleOwner, Observer { listItems->listItems.let {
            ItemsListsAdapter.updateItemsLists(it)
        } })
    }
    fun popDialog()
    {
        var name:String?=null
        alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("Create New To-Do")
        DialogView  = layoutInflater.inflate(R.layout.alert_dialog_add_list, null, false)
        alertDialogBuilder.setView(DialogView)
        newListName = DialogView.findViewById(R.id.addListDialogED)
        newListName.setHint("Enter Item")
        alertDialogBuilder.setPositiveButton("Add"){ dialog, which->
            name = newListName.text.toString()
            //Toast.makeText(activity, "Clicked1" +" "+name, Toast.LENGTH_SHORT).show()
            //TasksListsAdapter.addToList(Tasks(name))

            ItemsViewModel.addTaskList(name!!)


        }.setNegativeButton("Cancel"){dialog, which->
            dialog.cancel()
            Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
        }.show()
        //TasksListsAdapter.addToList(Tasks(name))
        //Toast.makeText(activity, "Clicked" +" "+newListName.text.toString(), Toast.LENGTH_SHORT).show()

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
       // menuInflater.inflate(R.menu.menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId){
            //R.id.searchTasks -> Toast.makeText(activity, "Searching...", Toast.LENGTH_SHORT).show()
            //R.id.LogoutMenu -> viewmodel.onSignout()
        }

        return true
    }

    private fun onDelete(item:Items){
        activity.let {
            AlertDialog.Builder(it)
                .setTitle("Delete user")
                .setMessage("Are you sure you want to delete this ${item.itemName} ?")
                .setPositiveButton("Yes"){p0, p1->ItemsViewModel.onDeleteItem(item)}
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }

    }
    /*
       // Event start and end time with date
        val startTime = "2022-02-1T09:00:00"
        val endTime = "2022-02-1T12:00:00"

        // Parsing the date and time
        val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val mStartTime = mSimpleDateFormat.parse(startTime)
        val mEndTime = mSimpleDateFormat.parse(endTime)
        val mIntent = Intent(Intent.ACTION_EDIT)
            mIntent.type = "vnd.android.cursor.item/event"
            mIntent.putExtra("beginTime", mStartTime.time)
            mIntent.putExtra("time", true)
            mIntent.putExtra("rule", "FREQ=YEARLY")
            mIntent.putExtra("endTime", mEndTime.time)
            mIntent.putExtra("title", "Geeksforgeeks Event")
            startActivity(mIntent)*/

    @SuppressLint("SimpleDateFormat")
    private fun popBottomDialog(items:Items){
        val dialog = activity?.let { BottomSheetDialog(it) }
        dialog?.setContentView(R.layout.bottom_dialogue)
        val delete = dialog?.findViewById<CardView>(R.id.deleteCardView)
        val edit = dialog?.findViewById<CardView>(R.id.editCardView)
        val reminder = dialog?.findViewById<CardView>(R.id.reminderCardView)

        reminder?.setOnClickListener {
          popDateTimeDialog(items)
        }
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

                    ItemsViewModel.onUpdateItemName(name)


                }.setNegativeButton("Cancel"){dialog, which->
                    dialog.cancel()
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show()
                }.show()
            }
            dialog.dismiss()
        }
        delete?.setOnClickListener {
            onDelete(items)
            dialog.dismiss()
        }
        dialog?.show()

    }

    private fun popDateTimeDialog(items:Items){
        val dialogView = View.inflate(activity, R.layout.date_time_picker, null)
        val alertDialog = AlertDialog.Builder(activity).create()
        val setDetails = dialogView.findViewById<Button>(R.id.date_time_set)
        setDetails.setOnClickListener {
            val datePicker:DatePicker = dialogView.findViewById(R.id.date_picker)
            val timePicker:TimePicker = dialogView.findViewById(R.id.time_picker)
            val calendar:Calendar= GregorianCalendar(datePicker.year,
                                                    datePicker.month,
                                                    datePicker.dayOfMonth,
                                                    timePicker.hour,
                                                    timePicker.minute)
            setAlaram(calendar, items)
            time = calendar.timeInMillis.toString()
            Toast.makeText(activity, time,Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
        alertDialog.setView(dialogView)
        alertDialog.show()

    }

    private fun setAlaram(time:Calendar, items: Items){
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Receiver::class.java)
        intent.putExtra("TaskName", items.itemName)
// Used for filtering inside Broadcast receiver
        intent.action = "MyBroadcastReceiverAction"
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

// In this particular example we are going to set it to trigger after 30 seconds.
// You can work with time later when you know this works for sure.
        val msUntilTriggerHour: Long = 1000
        //val alarmTimeAtUTC: Long = System.currentTimeMillis() + msUntilTriggerHour
        val alarmTimeAtUTC: Long = time.timeInMillis
// Depending on the version of Android use different function for setting an
// Alarm.
// setAlarmClock() - used for everything lower than Android M
// setExactAndAllowWhileIdle() - used for everything on Android M and higher
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(alarmTimeAtUTC, pendingIntent),
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeAtUTC,
                pendingIntent
            )
        }


    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name:CharSequence = "Do yout task"
            val description = "Channel For Alaram Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("foxandroid", name, importance)
            channel.description = description
            val manager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


}

