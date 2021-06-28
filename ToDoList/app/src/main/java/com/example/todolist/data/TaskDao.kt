package com.example.todolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Operationen für unsere tabellen
//Dao für room library wichtig
@Dao
interface TaskDao {


    //query schreiben in sql
    //flow von kotlin. = stream von data -> wenn sich tabelle ändert werden neue werte übernommen
    @Query("SELECT * FROM task_table")
    fun getTasks(): Flow<List<Task>>


    //damit es room operation wird: @insert
    //suspend weil coroutine um zu nem anderen thread zu gehen und ui nicht einzufrieren
    //room übernimmt insert logik
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)


}