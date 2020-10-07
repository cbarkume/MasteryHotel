package learn.hotel.data;

import learn.hotel.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll();

    Guest findById(int id);

    Guest add(Guest guest) throws DataException;

    boolean edit(Guest guest) throws DataException;

    boolean deleteById(int guestId) throws DataException;

}
