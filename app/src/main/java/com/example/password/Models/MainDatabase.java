package com.example.password.Models;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.password.Daos.Converters;
import com.example.password.Daos.FolderDao;
import com.example.password.Daos.PasswordDao;
import com.example.password.Daos.SignDao;
import com.example.password.Entities.FolderData;
import com.example.password.Entities.PasswordData;
import com.example.password.Entities.SignData;
import com.example.password.Repositories.FolderRepository;
import com.example.password.Repositories.PasswordRepository;
import com.example.password.Repositories.SignRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {SignData.class, PasswordData.class, FolderData.class}, version = 20, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MainDatabase extends RoomDatabase {

    public abstract SignDao signDao();
    public SignRepository signRepo = new SignRepository(signDao());
    public abstract PasswordDao passwordDao();
    public PasswordRepository passwordRepo ;

    public abstract FolderDao folderDao();
    public FolderRepository folderRepo ;


    private static volatile MainDatabase instance;
    private static final int threadCount = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(threadCount);
    static MainDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (MainDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    MainDatabase.class, "main_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .allowMainThreadQueries() //if not using external threads (not on ui threads)
                            .build();
                }
            }
        }
        return instance;
    }

    private static MainDatabase.Callback createCallback = new MainDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("comp3018", "onCreate");
            /*
            databaseWriteExecutor.execute(() -> {

                FolderDao folderDao = instance.folderDao();
                folderDao.deleteAll();

                SignDao signDao = instance.signDao();
                signDao.deleteAll();
                PasswordDao passwordDao = instance.passwordDao();
                passwordDao.deleteAll();




            });

             */
        }
    };

    public void initRepo(Long currentUID){
        passwordRepo = new PasswordRepository(passwordDao(),currentUID);
        folderRepo = new FolderRepository(folderDao(),currentUID);
    }


}