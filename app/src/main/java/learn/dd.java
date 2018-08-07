package learn;


import org.json.JSONException;
import org.json.JSONObject;

public class dd {

    public static void main(String[] args) {


        try {
            JSONObject object= new JSONObject("{\"key\":\"我是大帅哥\"}");
//            object.put("key","我是大帅哥");
            System.out.print(object.getString("key"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
