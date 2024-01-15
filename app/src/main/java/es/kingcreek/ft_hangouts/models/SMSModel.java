package es.kingcreek.ft_hangouts.models;

public class SMSModel {
    private int id;
    private String time;
    private int contactId;
    private String phoneNumber;
    private String message;
    private int inOut;

    public SMSModel(int id, int contactId, String phoneNumber, String message, String time, int inOut)
    {
        this.id = id;
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.time = time;
        this.inOut = inOut;
    }
    public SMSModel(int contactId, String phoneNumber, String message, String time, int inOut)
    {
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.time = time;
        this.inOut = inOut;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInOut() {
        return inOut;
    }

    public void setInOut(int inOut) {
        this.inOut = inOut;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
