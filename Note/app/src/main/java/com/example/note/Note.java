package com.example.note;

public class Note {
    private long id;
    private String content;
    private String time;
    private int tag;
    private long userid;
    public boolean checked=false;
    public Note()
    {

    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public Note(String content, String time, int tag)
    {
        this.content=content;
        this.time=time;
        this.tag=tag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "content"+"\n"+time.substring(5,16)+" "+id;
    }
}
