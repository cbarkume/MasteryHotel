package learn.hotel.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Guest {

    public int guestId;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public State state;

    public Guest() {
    }

    public Guest(int guestId, String firstName, String lastName, String email, String phone, State state) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.state = state;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return getGuestId() == guest.getGuestId() &&
                getFirstName().equals(guest.getFirstName()) &&
                getLastName().equals(guest.getLastName()) &&
                getEmail().equals(guest.getEmail()) &&
                getPhone().equals(guest.getPhone()) &&
                getState() == guest.getState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGuestId(), getFirstName(), getLastName(), getEmail(), getPhone(), getState());
    }

}
