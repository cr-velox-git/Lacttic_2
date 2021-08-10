package com.silverphoenix.lacttic_2;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDao {
    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    //Delete query
    @Delete
    void delete(MainData mainData);

    //Delete all query
    @Delete
    void reset(MainData mainData);

    //Update query
    @Query("UPDATE user_data SET number = :sNumber , name = :sName,gender = :sGender, dob = :sDOB,age = :sAge, address_line_1 = :sAddressLine1, address_line_2 = :sAddressLine2, pincode = :sPinCode,district = :sDistrict, state =:sState WHERE ID = :sID")
    void update(int sID, String sNumber, String sName, String sGender, String sDOB, String sAge, String sAddressLine1, String sAddressLine2, String sDistrict, String sState, String sPinCode);

    //get all data query
    @Query("SELECT * FROM user_data")
    List<MainData> getAll();
}
