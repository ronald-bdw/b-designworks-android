package com.b_designworks.android.utils.network;

import com.b_designworks.android.utils.Strings;

import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class CommonError {

    private _Error error;

    public static class _Error {
//        String                    id;
//        int                       status;
        String                    error;
        Map<String, List<String>> validations;
    }

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

}
