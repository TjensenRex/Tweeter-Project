package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import com.google.gson.Gson;
/*copied from the app module instead of imported because I did not want to create a dependency between them. */
public class JsonSerializer {

    public static String serialize(Object requestInfo) {
        return (new Gson()).toJson(requestInfo);
    }

    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }
}
