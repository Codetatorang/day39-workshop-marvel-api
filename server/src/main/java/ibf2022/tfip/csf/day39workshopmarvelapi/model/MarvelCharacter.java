package ibf2022.tfip.csf.day39workshopmarvelapi.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class MarvelCharacter implements Serializable{
    private Integer id;
    private String name;
    private String description;
    private String photo;
    private List<Comment> comments;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public static MarvelCharacter createJson(JsonObject jsonObject) {
        MarvelCharacter m = new MarvelCharacter();
        JsonObject t = jsonObject.getJsonObject("thumbnail");
        String path = t.getString("path");
        String ext = t.getString("extension");
        m.setId(jsonObject.getJsonNumber("id").intValue());
        m.setName(jsonObject.getString("name"));
        m.setPhoto(path + "." + ext);
        m.setDescription(jsonObject.getString("description"));

        return m;
    }

    public static List<MarvelCharacter> create(String json)throws IOException{
        List<MarvelCharacter> chars = new LinkedList<>();
        try(InputStream is  = new ByteArrayInputStream(json.getBytes())){
            JsonReader jrd = Json.createReader(is);
            JsonObject o = jrd.readObject();
            JsonObject oo = o.getJsonObject("data");
            if(oo.getJsonArray("results") != null){
                chars = oo.getJsonArray("results").stream()
                    .map(v-> (JsonObject)v)
                    .map(v-> MarvelCharacter.createJson(v))
                    .toList();
            }
        }
        return chars;
    }

    public static MarvelCharacter createFromCache(String json)throws IOException{
        MarvelCharacter m = new MarvelCharacter();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader jrd = Json.createReader(is);
            JsonObject o = jrd.readObject();
            m.setId((o.getJsonNumber("id").intValue()));
            m.setName((o.getString("name")));
            m.setDescription((o.getString("description")));
            m.setPhoto((o.getString("photo")));
        }
        return m;
    }

    public JsonObject toJSON(){
        return Json.createObjectBuilder()
            .add("id", getId())
            .add("name", getId())
            .add("description", getId())
            .add("photo", getId())
            .build();
    }
}
