package JavaClass;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonHelper {

    public static ArrayList<StructureClass.Module> readModule(String responce) throws JSONException {
        JSONObject jsonObject = new JSONObject(responce);
        JSONArray data = jsonObject.getJSONArray("data");

        ArrayList<StructureClass.Module> moduleList = new ArrayList<>();
        for(int i = 0; i < data.length(); i++) {
            JSONObject object = data.getJSONObject(i);
            int id = object.getInt("id");
            JSONObject attributes = object.getJSONObject("attributes");
            String title = attributes.getString("title");
            String description = attributes.getString("description");
            StructureClass.AttributesModule attributesModule = new StructureClass.AttributesModule(title, description, null, null, null, null, null);
            StructureClass.Module module = new StructureClass.Module(id, attributesModule);
            moduleList.add(module);
            Log.e("JSONTag", String.valueOf(id) + " " + title + " " + description);
        }
        return moduleList;
//        Log.e("JSONTag", String.valueOf(id));
    }

    public static ArrayList<StructureClass.Lesson> readLectures(String responce) throws JSONException{
        JSONObject jsonObject = new JSONObject(responce);
        ArrayList<StructureClass.Lesson> lessons = new ArrayList<>();

        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject attributeModule = data.getJSONObject("attributes");
        Log.e("LectureRead", attributeModule.toString());
        JSONObject lessonsFromJSON = attributeModule.getJSONObject("lectures");
        JSONArray dataLectures = lessonsFromJSON.getJSONArray("data");
        for(int i = 0; i < dataLectures.length(); i++){
            JSONObject object = dataLectures.getJSONObject(i);
            int id = object.getInt("id");

            JSONObject attribute = object.getJSONObject("attributes");
            String title = attribute.getString("title");
            String description = attribute.getString("description");

            StructureClass.AttributesLecture attributesLesson = new StructureClass.AttributesLecture(title, description, null, null, null, null, null);
            StructureClass.Lesson lesson = new StructureClass.Lesson(id, attributesLesson);

            Log.e("LectureRead", id + " " + title + " " + description);
            lessons.add(lesson);
        }
        return lessons;
    }


    public static ArrayList<Integer> readStepFromLectures(String responce) throws JSONException {
        ArrayList<Integer> steps = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(responce);
        JSONObject lectureData = jsonObject.getJSONObject("data");
        JSONObject lectureAttribute = lectureData.getJSONObject("attributes");
        JSONObject stepInfo = lectureAttribute.getJSONObject("steps");
        JSONArray stepsData = stepInfo.getJSONArray("data");
        for(int i = 0; i < stepsData.length(); i++){
            JSONObject obj = stepsData.getJSONObject(i);
            int id = obj.getInt("id");
            steps.add(id);
        }
        return steps;
    }

    public static StructureClass.Step readStep(String responce) throws JSONException {
        JSONObject json = new JSONObject(responce);
        JSONObject jsonObject = json.getJSONObject("data");
        int id = jsonObject.getInt("id");
        JSONObject stepAttribute = jsonObject.getJSONObject("attributes");
        String title = stepAttribute.getString("title");

        JSONArray contentList = stepAttribute.getJSONArray("content");
        ArrayList<StructureClass.Content> contents = new ArrayList<>();
        for(int i = 0; i < contentList.length(); i++){
            JSONObject obj = contentList.getJSONObject(i);
            String text = obj.getString("text");
            StructureClass.Content content = new StructureClass.Content(i, null, text);
            contents.add(content);
        }
        StructureClass.AttributesStep attributesStep = new StructureClass.AttributesStep(title, null, null, null, null, contents, null);

        StructureClass.Step step = new StructureClass.Step(id, attributesStep);
        return step;
    }

}
