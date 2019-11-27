package com.example.drugcontrol.entity;

import java.io.Serializable;

public class UpdateInfo implements Serializable {

    /**
     * id : 1
     * client_type : 1
     * version : v1.0.0
     * describe : 优化界面
     * download_url : xxx
     * force_update : 0
     * create_time : 2016-01-19 22:41:52
     * is_update : 0
     * msg : 已是最新版本
     */
    private String id;
    private String client_type;
    private String version; //versionName
    private String version_code;    //versionCode
    private String describe;
    private String download_url;
    private String force_update;
    private String create_time;
    private int is_update;
    private String msg;
    private String size;    //更新包大小
    private long content_length; //文件的总大小 因服务器的原因，特地新增字段解决下载时候total为-1的问题

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getForce_update() {
        return force_update;
    }

    public void setForce_update(String force_update) {
        this.force_update = force_update;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getIs_update() {
        return is_update;
    }

    public void setIs_update(int is_update) {
        this.is_update = is_update;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getContent_length() {
        return content_length;
    }

    public void setContent_length(int content_length) {
        this.content_length = content_length;
    }
}
