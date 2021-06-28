package com.example.todolist.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

//wollen daten in einer db speichern. dafür in room tabelle für db erstellen

//data class: weil es data enthält und equals method um 2 daten miteinander zu vergleichen für den recyclerview damit er items vergleichen kann
//alles aus dem xml hier reinpacken was wir am ende haben wollen
//parcelable damit man nicht jedes item einzeln übergeben muss
@Entity(tableName = "task_table")
@Parcelize
data class Task(
    //val statt var damit immutable und wir bei neuen sachen auch komplett neue objekte erstellen und nicht nur ändern
    val name: String,
    //mit = false: kann auch leer sein - also standardwert = false
    val important: Boolean = false,
    val completed: Boolean = false,
    //bekomme current time in millisekunden
    val created: Long = System.currentTimeMillis(),
    //jedes item unique id mit primarykey für db
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) :Parcelable
{
    //damit zeit nicht nur als millisekundne steht
    //steht im body weil es berechnet wird und nicht übergeben wird
    val createdDateFormatted: String
        //führe das hier dann aus nach aufruf von variable - formatiere datum
        get() = DateFormat.getDateTimeInstance().format(created)
}