package com.indev.claraa.dao

import android.content.Context
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

    @Query("SELECT * FROM product_master where type_id = :selectedCategory group by product_name ORDER BY product_id ASC")
    fun getProductMasterData(selectedCategory: Int): LiveData<List<ProductMasterModel>>


    @Query("SELECT * FROM product_master where product_name = :selectedProduct")
    fun getProductPowerList(selectedProduct: String): LiveData<List<ProductMasterModel>>


    @Query("SELECT * FROM product_master where product_name = :selectedProduct")
    fun getProductData(selectedProduct: String): List<ProductMasterModel>

    @NotNull
    @Insert
    suspend fun insertAddressData(addressDetailsModel: AddressDetailsModel): Long

    @Query("SELECT * FROM address ORDER BY local_id ASC")
    fun getAddressData() : LiveData<List<AddressDetailsModel>>


    @Query("SELECT * FROM user_master ORDER BY local_id ASC")
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
    suspend fun insertDistrictMasterData(districtModel: DistrictModel): Long


    @Query("DELETE FROM district_master")
    fun deleteAllDistricts()

    @Query("DELETE FROM user_master")
    fun deleteUserMasterTable()

    @NotNull
    @Insert
    suspend fun insertProductPacketMasterData(productPacketModel: ProductPacketModel): Long

    @NotNull
    @Insert
    suspend fun insertProductMasterData(productMasterModel: ProductMasterModel): Long

    @Query("DELETE FROM product_master")
    fun deleteAllProductMaster()

    @NotNull
    @Insert
    suspend fun insertProductTypeData(productTypeModel: ProductTypeModel): Long

    @Query("DELETE FROM product_type")
    fun deleteAllProductType()

    @Query("DELETE FROM product_packet")
    fun deleteAllProductPackets()

    @Query("DELETE FROM cart WHERE id = :cartId")
    fun deleteByProductId(cartId: Int)

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :id")
    fun updateCartProductQuantity(quantity: Int, id: Int): Int
}