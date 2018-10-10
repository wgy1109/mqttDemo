package com.sound.util.mqtt;

public class Config {
	private String ac;
    private String pw;
    private String topic;
    private String clientId;
    private String host;

    public Config() {
    }

    public void initpub(){
        this.ac="testone";
        this.pw="testone";
        this.topic="pubtopic";
        this.clientId="clientId";
        this.host="tcp://192.168.169.128:1883";
    }
    
    public void initsub(){
        this.ac="testone";
        this.pw="testone";
        this.topic="subtopic";
        this.clientId="clientId";
        this.host="tcp://192.168.169.128:1883";
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
