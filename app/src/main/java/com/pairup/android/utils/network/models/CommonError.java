package com.pairup.android.utils.network.models;

import com.pairup.android.utils.Strings;

import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class CommonError {

    private Error error;

    public String getMessage() {
        String message = error.error == null ? "" : (error.error + '\n');
        if (error.validations != null) {
            for (String key : error.validations.keySet()) {
                message += key + " " + Strings.listToString(error.validations.get(key)) + '\n';
            }
        }
        return message;
    }

    public Map<String, List<String>> getValidations() {
        return error.validations;
    }

    public static class Error {
//        String                    id;
//        int                       status;
        private String                    error;
        private Map<String, List<String>> validations;
    }

}
