package com.example.notemeandroidapp.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM table_name WHERE status='Open'")
    List<Task> getOpenTask();

    @Query("SELECT * FROM table_name where status='In-Progress'")
    List<Task> getProgressTask();

    @Query("SELECT * FROM table_name where status='Test'")
    List<Task> getTestTask();

    @Query("SELECT * FROM table_name where status='Done'")
    List<Task> getDoneTask();

//    @Query("SELECT * FROM task WHERE status='open'")
//    List<Task> getAllDetails();

    @Query("SELECT * FROM table_name WHERE id=:sID")
    Task getDetailsTask(int sID);

//    @Query("SELECT * FROM table_name")
//    List<Task> getAllTask();

    @Query("UPDATE table_name SET task_name =:sTask WHERE id=:sID")
    void updatename(int sID,String sTask);

    @Query("UPDATE table_name SET email =:sEmail WHERE id=:sID")
    void updateemail(int sID,String sEmail);

    @Query("UPDATE table_name SET phone =:sPhone WHERE id=:sID")
    void updatephone(int sID,String sPhone);

    @Query("UPDATE table_name SET url =:sUrl WHERE id=:sID")
    void updateurl(int sID,String sUrl);

    @Query("UPDATE table_name SET task_description =:desTask WHERE id=:sID")
    void updatedescripton(int sID,String desTask);

    @Query("UPDATE table_name SET deadline =:deadline WHERE id=:sID")
    void updatedeadline(int sID,String deadline);

    @Query("UPDATE table_name SET created =:created WHERE id=:sID")
    void updatecreated(int sID,String created);

    @Query("UPDATE table_name SET status =:status WHERE id=:sID")
    void updatestatus(int sID,String status);

    @Insert(onConflict = REPLACE)
    void insertTask(Task... tasks);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

}
