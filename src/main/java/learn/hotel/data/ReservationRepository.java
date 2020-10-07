package learn.hotel.data;

import learn.hotel.models.*;

import java.util.List;

public interface ReservationRepository {
    Reservation findByHostIdGuestId(String hostId, int guestId);

    List<Reservation> findByHostId(String id);

    Reservation add(Reservation reservation) throws DataException;

    boolean edit(Reservation reservation) throws DataException;

    Reservation cancelByHostIdGuestId(String hostId, int guestId) throws DataException;
}
