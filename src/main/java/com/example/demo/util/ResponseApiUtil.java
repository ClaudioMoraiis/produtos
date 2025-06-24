package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseApiUtil {
    public static HashMap response (String mStatus, String mMensagem){
        Map<String, String> mResponse = new HashMap<>();
        mResponse.put(mStatus, mMensagem);
        return new HashMap<>(mResponse);
    }
}
