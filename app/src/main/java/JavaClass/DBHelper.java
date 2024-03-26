package JavaClass;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

public class DBHelper {

    public static ArrayList<StructureClass.Module> insertModule(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM moduls", null);

        ArrayList<StructureClass.Module> moduleList = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            StructureClass.AttributesModule attributesModule = new StructureClass.AttributesModule(title, description, null, null, null, null, null);
            StructureClass.Module module = new StructureClass.Module(id, attributesModule);
            moduleList.add(module);
            Log.i("DBTag", "Прочитано из БД: " + String.valueOf(id) + " " + title + " " + description);
        }
        return moduleList;
    }

    public static void clearDB(SQLiteDatabase db){
        db.execSQL("DELETE FROM moduls");
    }

    public static void dropExtraModule(SQLiteDatabase db, int maxId){
        db.execSQL("DELETE FROM moduls WHERE id > ?", new String[]{String.valueOf(maxId)});
    }
}
