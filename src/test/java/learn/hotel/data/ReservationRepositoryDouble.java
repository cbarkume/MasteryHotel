package learn.hotel.data;

import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import learn.hotel.models.State;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {

    private Host testHost = new Host("TestID", "TestNAME", "Test@EMAIL.com", "(555) 5555555", "TestADDRESS",
            "TestCITY", State.DE, "TestPOSTAL", new BigDecimal(10.00), new BigDecimal(15.00));
    private Guest testGuest = new Guest(1, "Cecilia", "Barkume", "ceciliabarkume@gmail.com", "(734) 5079310", State.MI);
    private Reservation reservation = new Reservation(1, LocalDate.of(2020,10,6), LocalDate.of(2020,10,14), testHost, testGuest);
    private final List<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        reservations.add(reservation);
    }

    @Override
    public Reservation findByHostIdGuestId(String hostId, int guestId) {
        if (reservation.getGuest().getGuestId() == guestId
            && reservation.getHost().getId().equals(hostId)) {
            return reservation;
        }
        return null;
    }

    @Override
    public List<Reservation> findByHostId(String hostId) {
        if (reservation.getHost().getId().equals(hostId)) {
            return reservations;
        }
        return null;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        return reservation;
    }

    @Override
    public boolean edit(Reservation reservation) throws DataException {
        return true;
    }

    @Override
    public boolean cancelByHostIdGuestId(String hostId, int guestId) throws DataException {
        if (reservation.getGuest().getGuestId() == guestId
                && reservation.getHost().getId().equals(hostId)) {
            return true;
        }
        return false;
    }
}
