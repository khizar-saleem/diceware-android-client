package edu.cnm.deepdive.diceware.model;

import com.google.gson.annotations.Expose;
import java.util.List;

public class Passphrase {

  @Expose
  private long id;

  @Expose
  private String key;

  @Expose
  private List<String> words;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public List<String> getWords() {
    return words;
  }

  public void setWords(List<String> words) {
    this.words = words;
  }
}
