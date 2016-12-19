package com.codepath.apps.mysimpletweet.models;

import com.codepath.apps.mysimpletweet.MyTweetDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ManyToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by Chjeng-Lun SHIEH on 2016/12/4.
 */
@Table(database = MyTweetDatabase.class)
@ManyToMany(referencedTable = Tweet.class)
public class User extends BaseModel implements Serializable{

    @PrimaryKey
    @Column
    String id;

    @Column
    private String name;

    @Column
    private String screen_name;

    @Column
    private String profile_image_url_https;

    @Column
    private String description;

    @Column
    private int followers_count;

    public User(){
        super();
    }

    public User(JSONObject object) {
        try {
            this.id = object.getString("id_str");
            this.name = object.getString("name");
            this.screen_name = object.getString("screen_name");
            this.profile_image_url_https = object.getString("profile_image_url_https");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.description = object.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.followers_count = object.getInt("followers_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", profile_image_url_https='" + profile_image_url_https + '\'' +
                ", description='" + description + '\'' +
                ", followers_count='" + followers_count + '\'' +
                '}';
    }
}
