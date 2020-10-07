package learn.hotel.models;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    public int id;
    public LocalDate startDate;
    public LocalDate endDate;
    public Host host;
    public Guest guest;
    public BigDecimal total;

    public Reservation() {
    }

    public Reservation(int id, LocalDate startDate, LocalDate endDate, Host host, Guest guest) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.host = host;
        this.guest = guest;
    }

    public Reservation(int id, LocalDate startDate, LocalDate endDate, Host host, Guest guest, BigDecimal total) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.host = host;
        this.guest = guest;
        this.total = total;
    }

    public BigDecimal calculateTotal() {
        if (startDate == null || endDate == null ||
                host.getStandardRate().max(BigDecimal.ZERO).equals(BigDecimal.ZERO) || host.getWeekendRate().max(BigDecimal.ZERO).equals(BigDecimal.ZERO)) {
            return null;
        }
        BigDecimal total = BigDecimal.ZERO;
        LocalDate counterDate = startDate;

        for (; counterDate.isBefore(endDate) || counterDate.isEqual(endDate); counterDate = counterDate.plusDays(1)) {
            if (counterDate.getDayOfWeek() == DayOfWeek.SATURDAY || counterDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                total = total.add(host.getWeekendRate());
            }
            else {
                total = total.add(host.getStandardRate());
            }
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return getId() == that.getId() &&
                getStartDate().equals(that.getStartDate()) &&
                getEndDate().equals(that.getEndDate()) &&
                getHost().equals(that.getHost()) &&
                getGuest().equals(that.getGuest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStartDate(), getEndDate(), getHost(), getGuest());
    }
}
