package shubham.latest.master20;

public class Data {
    String id,sender,requested,operator,ground,message,time;

    public Data(String id, String sender, String requested, String operator, String ground, String message, String time) {
        this.id = id;
        this.sender = "FROM: "+sender;
        this.requested ="REQ FOR: "+ requested;
        this.operator = "OPERATOR: "+ operator;
        this.ground = "GROUND: "+ground;
        this.message = "MESSAGE: " + message;
        this.time = "TIME: "+ time;
    }

    public Data() {
        this.id = "1";
        this.sender="FROM: ";
        this.requested="REQ FOR: ";
        this.operator="OPERATOR: ";
        this.ground="-----------------";
        this.message="MESSAGE: ";
        this.time="TIME: ";
    }

    public String getid() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getRequested() {
        return requested;
    }

    public String getOperator() {
        return operator;
    }

    public String getGround() {
        return ground;
    }

    public String getMessage() {
        return message;
    }

    public String gettime() {
        return time;
    }
}
