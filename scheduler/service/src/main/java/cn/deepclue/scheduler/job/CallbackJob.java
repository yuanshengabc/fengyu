package cn.deepclue.scheduler.job;

import cn.deepclue.scheduler.domain.Callback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class CallbackJob extends Job {
    private static final String CALLBACK_KEY = "CALLBACK";

    private Callback callback;

    public CallbackJob() {
    }

    public CallbackJob(int jId, int appId, ScheduleMode scheduleMode, Callback callback) {
        super(jId, appId, scheduleMode);
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public boolean run() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> formEntity = new HttpEntity<>(callback.getRequestBody(), headers);

        restTemplate.postForObject(callback.getUrl(), formEntity, Boolean.class);
        return true;
    }

    public String serialize() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put(JID_KEY, String.valueOf(jId));
        dataMap.put(APPID_KEY, String.valueOf(appId));
        dataMap.put(CALLBACK_KEY, new Gson().toJson(callback));
        dataMap.put(SCHEDULE_MODE, new Gson().toJson(scheduleMode));
        dataMap.put(SCHEDULE_MODE_TYPE, scheduleMode instanceof SimpleScheduleMode ? "0" : "1");
        return new Gson().toJson(dataMap);
    }

    public void deserialize(String jsonMap) {
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> dataMap = new Gson().fromJson(jsonMap, mapType);

        String jidKey = dataMap.get(JID_KEY);
        this.jId = Integer.valueOf(jidKey);

        String appidKey = dataMap.get(APPID_KEY);
        this.appId = Integer.valueOf(appidKey);

        String callbackKey = dataMap.get(CALLBACK_KEY);
        this.callback = new Gson().fromJson(callbackKey, Callback.class);

        String scheduleTypeKey = dataMap.get(SCHEDULE_MODE_TYPE);
        String scheduleKey = dataMap.get(SCHEDULE_MODE);
        if ("0".equals(scheduleTypeKey)) {
            this.scheduleMode = new Gson().fromJson(scheduleKey, SimpleScheduleMode.class);
        } else if ("1".equals(scheduleTypeKey)) {
            this.scheduleMode = new Gson().fromJson(scheduleKey, CronScheduleMode.class);
        } else {
            throw new RuntimeException("invalid schedule type");
        }
    }
}
