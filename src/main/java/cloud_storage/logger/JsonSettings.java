package cloud_storage.logger;

import cloud_storage.constant.Constant;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.util.Map;

public class JsonSettings implements ISettings {
    private JSONObject jsonObject;

    public void getSettings() {
        try {
            String fileName = Constant.LOG_SETT_FILE;
            JSONParser parser = new JSONParser(new FileReader(fileName));
            Map obj = (Map) parser.parse();
            jsonObject = new JSONObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonSettings() {
        getSettings();
    }

    public String getStrValue(String name) {
        return (String) jsonObject.get(name);
    }
}
