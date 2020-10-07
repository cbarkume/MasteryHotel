package learn.hotel.data;

import learn.hotel.models.Guest;
import learn.hotel.models.State;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository {

    private final String filePath;

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Guest findById(int id) {
        List<Guest> all = findAll();
        if (all.size() == 0) {
            return null;
        }

        for (Guest guest: all) {
            if (guest.getGuestId() == id) {
                return guest;
            }
        }
        return null;
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        if (guest == null) {
            return null;
        }

        List<Guest> all = findAll();

        int nextId = all.stream()
                .mapToInt(Guest::getGuestId)
                .max()
                .orElse(0) + 1;

        guest.setGuestId(nextId);

        all.add(guest);
        writeAll(all);
        return guest;
    }

    @Override
    public boolean edit(Guest guest) throws DataException {
        if (guest == null) {
            return false;
        }

        List<Guest> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (guest.getGuestId() == all.get(i).getGuestId()) {
                all.set(i, guest);
                writeAll(all);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteById(int guestId) throws DataException {
        if (guestId < 0) {
            return false;
        }

        List<Guest> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (guestId == all.get(i).getGuestId()) {
                all.remove(i);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    private Guest deserialize(String[] fields) {
        Guest guest = new Guest();
        guest.setGuestId(Integer.parseInt(fields[0]));
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhone(fields[4]);
        guest.setState(State.valueOf(fields[5]));
        return guest;
    }

    private String serialize(Guest guest) {
        return String.format("%s,%s,%s,%s,%s,%s",
                guest.getGuestId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getState());
    }

    protected void writeAll(List<Guest> guests) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println("guest_id,first_name,last_name,email,phone,state");

            if (guests == null) {
                return;
            }

            for (Guest guest : guests) {
                writer.println(serialize(guest));
            }

        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }
}
