package kafkaleaderboard.app.serialization.json;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class JsonDeserializer<T> implements Deserializer<T> {

    private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();


    private Class<T> designationClass;
    private Type reflectionTypeToken;

    public JsonDeserializer(Class<T> designationClass){
        this.designationClass = designationClass;
    }

    public JsonDeserializer(Type reflectionTypeToken) {
        this.reflectionTypeToken = reflectionTypeToken;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null ){
            return null;
        }
        Type type = designationClass == null ? reflectionTypeToken : designationClass;
        return gson.fromJson(new String(data, StandardCharsets.UTF_8), type);
    }
    
}
