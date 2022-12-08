package com.indev.claraa.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.indev.claraa.entities.*
import org.jetbrains.annotations.NotNull

@Dao
interface ClaraaDao {

    @NotNull
    @Insert
    suspend fun insertUserData(userRegistrationTable: UserRegistrationModel): Long

    @Insert
    suspend fun insertUserCart(cartModel: CartModel): Long

    @Query("SELECT * FROM cart ORDER BY id ASC")
    fun getCartData() : LiveData<List<CartModel>>

    @NotNull
    @Insert
    suspend fun insertAddressData(addressDetailsModel: AddressDetailsModel): Long

    @Query("SELECT * FROM address ORDER BY local_id ASC")
    fun getAddressData() : LiveData<List<AddressDetailsModel>>


    @Query("SELECT * FROM user_master  ORDER BY local_id ASC")
    fun getRegistrationData() : LiveData<UserRegistrationModel>

    @Update
    fun update(userRegistrationTable: UserRegistrationModel)

    @NotNull
    @Insert
    suspend fun insertStateMasterData(stateModel: StateModel): Long

    @Query("DELETE FROM state_master")
    fun deleteAllStates()

    @NotNull
    @Insert
    suspend fun insertDistrictMasterData(districtModel: DistrictModel)

}