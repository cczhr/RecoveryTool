package com.cczhr.recoverytool.database

import androidx.room.*
/**
 * @author cczhr
 * @since  2020/9/6
 * @description https://github.com/cczhr
 */
@Dao
interface CommandDao {
    @Query("SELECT * FROM Command")
    suspend fun getAll(): List<Command>

    @Insert
    suspend fun insertAll(commands: List<Command>)

    @Insert
    suspend fun insert(command: Command)

    @Update
    suspend fun update(command: Command)

    @Delete
    suspend fun delete(command: List<Command>)
}
