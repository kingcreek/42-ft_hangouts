package es.kingcreek.ft_hangouts.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ContactModel implements Parcelable {

    private long id;
    private String number;
    private String firstName;
    private String lastName;
    private String address;
    private String email;

    public ContactModel(long id, String number, String firstName, String lastName, String address, String email) {
        this.id = id;
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
    }

    public ContactModel(String number, String firstName, String lastName, String address, String email) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Parcelable implementation (need to send as Intent)
    protected ContactModel(Parcel in) {
        id = in.readLong();
        number = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        address = in.readString();
        email = in.readString();
    }

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel in) {
            return new ContactModel(in);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(number);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(address);
        dest.writeString(email);
    }
}