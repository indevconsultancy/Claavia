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

    @Query("SELECT * FROM cart ORDER BY local_id ASC")
    fun getCartData() : LiveData<List<CartModel>>

    @Query("SELECT * FROM product_master where type_id = :selectedCategory group by product_name ORDER BY product_id ASC")
    fun getProductMasterData(selectedCategory: Int): LiveData<List<ProductMasterModel>>


    @Query("SELECT * FROM product_master where product_name = :selectedProduct")
    fun getProductPowerList(selectedProduct: String): LiveData<List<ProductMasterModel>>


    @Query("SELECT * FROM product_master where product_id = :product_id")
    fun getProductData(product_id: Int): List<ProductMasterModel>


    @Query("SELECT product_id from product_master where power_range = :power_range")
    fun getproductID(power_range: String): Int

    @NotNull
    @Insert
    suspend fun insertAddressData(addressDetailsModel: AddressDetailsModel) : Long

    @Query("SELECT * FROM address ORDER BY local_id ASC")
    fun getAddressData() : LiveData<List<AddressDetailsModel>>


   @Query("SELECT * FROM address where local_id= :id")
    fun getAddressDatabyLocalID(id : Int) : LiveData<AddressDetailsModel>


    @Query("SELECT * FROM user_master")
    fun getRegistrationData() : LiveData<UserRegistrationModel>

    @Query("SELECT * FROM state_master")
    fun getStateList() : List<StateModel>

    @Query("SELECT * FROM district_master")
    fun getDistrictList() : List<DistrictModel>

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

    @Query("DELETE FROM cart WHERE local_id = :cartId")
    fun deleteByProductId(cartId: Int)

    @Query("UPDATE cart SET quantity = :quantity , amount = :totalPrice WHERE local_id = :id")
    fun updateCartProductQuantity(quantity: Int, totalPrice: Int, id: Int): Int


    @Query("SELECT EXISTS(SELECT * from cart where product_id = :productId and power_range = :power_range and packets = :packets)")
    fun isProductRowExist(productId: Int, power_range: String, packets: String): Int

    @Query("SELECT * from cart where product_id = :productId")
    fun getCartDatabyProductId(productId: Int) : List<CartModel>

    @Query("SELECT * from address where user_id = :addressId")
    fun getAddress(addressId: Int) : LiveData<AddressDetailsModel>


    @Query("DELETE FROM address WHERE id = :id")
    fun deleteAddress(id: Int)

    @Update
    fun editAddress(addressDetailsModel: AddressDetailsModel)


    @Query("UPDATE address SET id = :last_id WHERE id = :id")
    fun updateAddressId(last_id: Int, id: String): Int
}