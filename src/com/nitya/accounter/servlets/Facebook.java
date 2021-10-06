package com.nitya.accounter.servlets;

import com.nitya.accounter.utils.StringUtils;

public class Facebook {
    // get these from your FB Dev App
    private static final String api_key = "194866317254559";
    private static final String secret = "0a98a0db485e6847d3cd093e89a4fdfd";
    private static final String client_id = "194866317254559";  

    // set this to your servlet URL for the authentication servlet/filter
    private static final String redirect_uri = "http://app.annaccounting.com/main/fbauth";
    /// set this to the list of extended permissions you want
    private static final String[] perms = new String[] {"publish_stream", "email"};

    public static String getAPIKey() {
        return api_key;
    }

    public static String getSecret() {
        return secret;
    }
    public interface StringConverter<T> {

        String toString(T object);
    }
    public static String getLoginRedirectURL() {
        return "https://graph.facebook.com/oauth/authorize?client_id=" +
            client_id + "&display=page&redirect_uri=" +
            redirect_uri+"&scope="+StringUtils.delimitObjectsToString(",", perms);
    }

    public static String getAuthURL(String authCode) {
        return "https://graph.facebook.com/oauth/access_token?client_id=" +
            client_id+"&redirect_uri=" +
            redirect_uri+"&client_secret="+secret+"&code="+authCode;
    }
  

}
