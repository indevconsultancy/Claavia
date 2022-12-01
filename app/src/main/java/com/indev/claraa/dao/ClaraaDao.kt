package com.indev.claraa.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.indev.claraa.entities.Cart
import com.indev.claraa.entities.UserRegistrationModel
import org.jetbrains.annotations.NotNull

@Dao
interface ClaraaDao {

    @NotNull
    @Insert
    suspend fun insertUserData(userRegistrationTable: UserRegistrationModel): Long

    @Insert
    suspend fun insertUserCart(cart: Cart): Long

    @Query("SELECT * FROM cart ORDER BY id ASC")
    fun getCartData() : LiveData<List<Cart>>

    @Query("SELECT * FROM user_master  ORDER BY local_id ASC")
    fun getRegistrationData() : LiveData<UserRegistrationModel>

    @Update
    fun update(userRegistrationTable: UserRegistrationModel)


}