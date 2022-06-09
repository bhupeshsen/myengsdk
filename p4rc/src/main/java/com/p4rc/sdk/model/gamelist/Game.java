
package com.p4rc.sdk.model.gamelist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    private String description;
    private Boolean userPlayed;
    private Object videoLink;
    private String name;
    private Object language;
    private Double size;
    private Object version;
    private Integer gameId;
    private String gameRefId;
    private Object verificationStatus;
    private Integer campaignType;
    private Object unit;
    private String updatedDate;
    private List<String> imageList = null;
    private String fileId;
    private String androidDownloadUrl;
    private String fblikeURL;
    private String developerName;
    private Integer gameImageType;
    private Object csvFileURL;
    private Object developerUrl;
    private String category;
    private String gameUrl;
    private String ipadDownloadURL;
    private String encodedFBLikeURL;
    private String iPhoneDownloadUrl;
    private String imageUrl;
    private Integer minVideoWatchDuration;
    private Integer likes;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getUserPlayed() {
        return userPlayed;
    }

    public void setUserPlayed(Boolean userPlayed) {
        this.userPlayed = userPlayed;
    }

    public Object getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(Object videoLink) {
        this.videoLink = videoLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLanguage() {
        return language;
    }

    public void setLanguage(Object language) {
        this.language = language;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Object getVersion() {
        return version;
    }

    public void setVersion(Object version) {
        this.version = version;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getGameRefId() {
        return gameRefId;
    }

    public void setGameRefId(String gameRefId) {
        this.gameRefId = gameRefId;
    }

    public Object getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(Object verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Integer getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(Integer campaignType) {
        this.campaignType = campaignType;
    }

    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
        this.unit = unit;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getAndroidDownloadUrl() {
        return androidDownloadUrl;
    }

    public void setAndroidDownloadUrl(String androidDownloadUrl) {
        this.androidDownloadUrl = androidDownloadUrl;
    }

    public String getFblikeURL() {
        return fblikeURL;
    }

    public void setFblikeURL(String fblikeURL) {
        this.fblikeURL = fblikeURL;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public Integer getGameImageType() {
        return gameImageType;
    }

    public void setGameImageType(Integer gameImageType) {
        this.gameImageType = gameImageType;
    }

    public Object getCsvFileURL() {
        return csvFileURL;
    }

    public void setCsvFileURL(Object csvFileURL) {
        this.csvFileURL = csvFileURL;
    }

    public Object getDeveloperUrl() {
        return developerUrl;
    }

    public void setDeveloperUrl(Object developerUrl) {
        this.developerUrl = developerUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getIpadDownloadURL() {
        return ipadDownloadURL;
    }

    public void setIpadDownloadURL(String ipadDownloadURL) {
        this.ipadDownloadURL = ipadDownloadURL;
    }

    public String getEncodedFBLikeURL() {
        return encodedFBLikeURL;
    }

    public void setEncodedFBLikeURL(String encodedFBLikeURL) {
        this.encodedFBLikeURL = encodedFBLikeURL;
    }

    public String getiPhoneDownloadUrl() {
        return iPhoneDownloadUrl;
    }

    public void setiPhoneDownloadUrl(String iPhoneDownloadUrl) {
        this.iPhoneDownloadUrl = iPhoneDownloadUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getMinVideoWatchDuration() {
        return minVideoWatchDuration;
    }

    public void setMinVideoWatchDuration(Integer minVideoWatchDuration) {
        this.minVideoWatchDuration = minVideoWatchDuration;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }


    public static Game fromJSON(JSONObject jsonObject) {
        Game game = new Game();
        game.setDescription(jsonObject.optString("description"));
        game.setUserPlayed(jsonObject.optBoolean("userPlayed"));
        game.setVideoLink(jsonObject.optString("videoLink"));
        game.setName(jsonObject.optString("name"));
        game.setLanguage(jsonObject.optString("language"));
        game.setSize(jsonObject.optDouble("size"));
        game.setVersion(jsonObject.optString("version"));
        game.setGameId(jsonObject.optInt("gameId"));
        game.setGameRefId(jsonObject.optString("gameRefId"));
        game.setVerificationStatus(jsonObject.optString("verificationStatus"));
        game.setCampaignType(jsonObject.optInt("campaignType"));
        game.setUnit(jsonObject.optString("unit"));
        game.setUpdatedDate(jsonObject.optString("updatedDate"));
//            game.setImageList(jsonObject.optString("imageList"));
        JSONArray imageList = jsonObject.optJSONArray("imageList");
        if (imageList != null) {
            List<String> imageList1 = new ArrayList<>();
            for (int i = 0; i < imageList.length(); i++) {
                imageList1.add(imageList.optString(i));
            }
            game.setImageList(imageList1);
        }
        game.setFileId(jsonObject.optString("fileId"));
        game.setAndroidDownloadUrl(jsonObject.optString("androidDownloadUrl"));
        game.setFblikeURL(jsonObject.optString("fblikeURL"));
        game.setDeveloperName(jsonObject.optString("developerName"));
        game.setGameImageType(jsonObject.optInt("gameImageType"));
        game.setCsvFileURL(jsonObject.optString("csvFileURL"));
        game.setDeveloperUrl(jsonObject.optString("developerUrl"));
        game.setCategory(jsonObject.optString("category"));
        game.setGameUrl(jsonObject.optString("gameUrl"));
        game.setIpadDownloadURL(jsonObject.optString("ipadDownloadURL"));
        game.setEncodedFBLikeURL(jsonObject.optString("encodedFBLikeURL"));
        game.setiPhoneDownloadUrl(jsonObject.optString("iPhoneDownloadUrl"));
        game.setImageUrl(jsonObject.optString("imageUrl"));
        game.setMinVideoWatchDuration(jsonObject.optInt("minVideoWatchDuration"));
        game.setLikes(jsonObject.optInt("likes"));

        return game;
    }

    @Override
    public String toString() {
        return "Game{" +
                "description='" + description + '\'' +
                ", userPlayed=" + userPlayed +
                ", videoLink=" + videoLink +
                ", name='" + name + '\'' +
                ", language=" + language +
                ", size=" + size +
                ", version=" + version +
                ", gameId=" + gameId +
                ", gameRefId='" + gameRefId + '\'' +
                ", verificationStatus=" + verificationStatus +
                ", campaignType=" + campaignType +
                ", unit=" + unit +
                ", updatedDate='" + updatedDate + '\'' +
                ", imageList=" + imageList +
                ", fileId='" + fileId + '\'' +
                ", androidDownloadUrl='" + androidDownloadUrl + '\'' +
                ", fblikeURL='" + fblikeURL + '\'' +
                ", developerName='" + developerName + '\'' +
                ", gameImageType=" + gameImageType +
                ", csvFileURL=" + csvFileURL +
                ", developerUrl=" + developerUrl +
                ", category='" + category + '\'' +
                ", gameUrl='" + gameUrl + '\'' +
                ", ipadDownloadURL='" + ipadDownloadURL + '\'' +
                ", encodedFBLikeURL='" + encodedFBLikeURL + '\'' +
                ", iPhoneDownloadUrl='" + iPhoneDownloadUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", minVideoWatchDuration=" + minVideoWatchDuration +
                ", likes=" + likes +
                '}';
    }
}
