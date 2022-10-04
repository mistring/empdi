package net.mistring.empdi.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    //CRUD

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(entity: EmployeeEntry)

    @Query("SELECT COUNT(*) FROM EmployeeEntry")
    suspend fun userCount(): Long

    @Query("SELECT * FROM EmployeeEntry")
    suspend fun getUsers(): List<EmployeeEntry>


}