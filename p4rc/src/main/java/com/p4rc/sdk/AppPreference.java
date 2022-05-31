package com.p4rc.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.p4rc.sdk.model.User;

public class AppPreference {

    protected Context context;
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        this.context = context;
    }

    protected SharedPreferences preferences;
    public static final String PREFERENCE = "com.p4rc.sdk.main";

    public void saveUser(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("offerCountry", user.getOfferCountry());
        editor.putString("email", user.getEmail());
        editor.putInt("totalPoints", user.getTotalPoints());
        editor.putInt("userType", user.getUserType());
        editor.putString("image", user.getImage());
        editor.putInt("totalLifeTimePoints", user.getTotalLifeTimePoints());
        editor.putInt("userSource", user.getUserSource());
        editor.putString("firstName", user.getFirstName());
        editor.putString("lastName", user.getLastName());
        editor.putBoolean("userAcceptedTerms", user.getUserAcceptedTerms());
        editor.putString("userAvatarURL", user.getUserAvatarURL());
        editor.putInt("facebookId", user.getFacebookId());
        editor.putInt("profileCompletionPercent", user.getProfileCompletionPercent());
        editor.putBoolean("fbPublishAllowed", user.getFbPublishAllowed());
        editor.putBoolean("admin", user.getAdmin());
        editor.putInt("id", user.getId());
        editor.apply();
    }

    public User getUser(){
        User user = new User();
        user.setOfferCountry(preferences.getString("offerCountry", ""));
        user.setEmail(preferences.getString("email", ""));
        user.setTotalPoints(preferences.getInt("totalPoints", 0));
        user.setUserType(preferences.getInt("userType", 0));
        user.setImage(preferences.getString("image", ""));
        user.setTotalLifeTimePoints(preferences.getInt("totalLifeTimePoints", 0));
        user.setUserSource(preferences.getInt("userSource", 0));
        user.setFirstName(preferences.getString("firstName", ""));
        user.setLastName(preferences.getString("lastName", ""));
        user.setUserAcceptedTerms(preferences.getBoolean("userAcceptedTerms", false));
        user.setUserAvatarURL(preferences.getString("userAvatarURL", ""));
        user.setFacebookId(preferences.getInt("facebookId", 0));
        user.setProfileCompletionPercent(preferences.getInt("profileCompletionPercent", 0));
        user.setFbPublishAllowed(preferences.getBoolean("fbPublishAllowed", false));
        user.setAdmin(preferences.getBoolean("admin", false));
        user.setId(preferences.getInt("id", 0));
        return user;
    }
}
