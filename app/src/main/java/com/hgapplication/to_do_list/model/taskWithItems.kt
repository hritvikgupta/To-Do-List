package com.hgapplication.to_do_list.model

import androidx.room.Embedded
import androidx.room.Relation

data class taskWithItems (
    @Embedded val tasks: Tasks,
    @Relation(
        parentColumn = "taskListName",
        entityColumn = "taskListName"
    )
    val items:List<Items>
)