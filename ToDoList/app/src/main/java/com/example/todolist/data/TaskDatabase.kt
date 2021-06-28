package com.example.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

//entities = alle tabellen die wir benutzen wollen, version = wie wir unsere db updaten wollen in zukunft nach release
//sonst auch einfach app auf handy löschen und nochmal neu runnen - wenn wir was an der db ändern
//abstract weil room alles übernimmt - roomdatabase übernehmen dafür
@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    //um unser dao (mit den db operationen) zu bekommen benutzen wir dependency injection
    //dagger packt dafür alles an eine zentrale stelle und übernimmt die injection wo wir sie dann brauchen - hilt ist hier library für dagger damits einfacher ist
    abstract fun taskDao(): TaskDao

    //unsere dummy text klasse für die app / sagt dagger wie man ne instanz der klasse erstellt und benötgite dependancys
    class Callback @Inject constructor(
        //private val database: TaskDatabase -> nicht gut weil kreis dependency

        //Provider = bekomme dependency lazily => wird erst aufgerufen bei .get() aufruf dadurch gibts db dann schon
        private val database: Provider<TaskDatabase>,

        @ApplicationScope private val applicationScope: CoroutineScope

    ) : RoomDatabase.Callback() {
        //wird beim ersten erstellen der db ausgeführt - aber erst nachdem die db erstellt wurde
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //db operations / bekomme unser dao
            val dao = database.get().taskDao()

            //dao.insert() -> geht nicht weil suspend fun

            //GlobalScope = läuft solange die app läuft bis coroutine durch ist -> ist aber schlechter stil weil wenig kontrolle
            //besser: eigene coroutine scope aus appmodule

            applicationScope.launch {
                //braucht typ Task - siehe TaskDao erstellung
                dao.insert(Task("Wash the dishes"))
                dao.insert(Task("Play Dota 2", completed = true))
                dao.insert(Task("Hit Waldmann", important = true))
                dao.insert(Task("Buy Food"))
                dao.insert(Task("Go outside"))
                dao.insert(Task("Read a book"))


            }

        }
    }
}































