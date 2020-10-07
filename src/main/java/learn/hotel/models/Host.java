package learn.hotel.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Host {
    public String id;
    public String lastName;
    public String email;
    public String phone;
    public String address;
    public String city;
    public State state;
    public String postalCode;
    public BigDecimal standardRate;
    public BigDecimal weekendRate;

    public Host(){

    }

    public Host(String id, String lastName, String email, String phone, String address, String city, State state, String postalCode, BigDecimal standardRate, BigDecimal weekendRate) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.standardRate = standardRate;
        this.weekendRate = weekendRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    public BigDecimal getWeekendRate() {
        return weekendRate;
    }

    public void setWeekendRate(BigDecimal weekendRate) {
        this.weekendRate = weekendRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return getId().equals(host.getId()) &&
                getLastName().equals(host.getLastName()) &&
                getEmail().equals(host.getEmail()) &&
                getPhone().equals(host.getPhone()) &&
                getAddress().equals(host.getAddress()) &&
                getCity().equals(host.getCity()) &&
                getState() == host.getState() &&
                getPostalCode().equals(host.getPostalCode()) &&
                getStandardRate().equals(host.getStandardRate()) &&
                getWeekendRate().equals(host.getWeekendRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLastName(), getEmail(), getPhone(), getAddress(), getCity(), getState(), getPostalCode(), getStandardRate(), getWeekendRate());
    }
}
