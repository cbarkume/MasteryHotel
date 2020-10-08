package learn.hotel.data;

import learn.hotel.models.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,startDate,endDate,guestId,total";
    public String directory;
    GuestFileRepository guestRepo;
    HostFileRepository hostRepo;

    public ReservationFileRepository(String directory, GuestFileRepository guestRepo, HostFileRepository hostRepo) {
        this.directory = directory;
        this.guestRepo = guestRepo;
        this.hostRepo = hostRepo;
    }

    @Override
    public Reservation findByHostIdGuestId(String hostId, int guestId) {
        List<Reservation> reservations = findByHostId(hostId);

        if (reservations.size() == 0 || guestId <= 0) {
            return null;
        }

        for (Reservation reservation : reservations) {
            if (guestId == reservation.getGuest().getGuestId()) {
                return reservation;
            }
        }

        return null;
    }

    @Override
    public List<Reservation> findByHostId(String id) {
        ArrayList<Reservation> result = new ArrayList<>();

        if (id == null) {
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(id)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, id));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }

        return result;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        if (reservation == null) {
            return null;
        }
        List<Reservation> all = findByHostId(reservation.getHost().getId());

        int nextId = all.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;

        reservation.setId(nextId);

        all.add(reservation);
        writeAll(all);
        return reservation;
    }

    @Override
    public boolean edit(Reservation reservation) throws DataException {
        if (reservation == null) {
            return false;
        }

        List<Reservation> all = findByHostId(reservation.getHost().getId());
        for (int i = 0; i < all.size(); i++) {
            if (reservation.getId() == all.get(i).getId()) {
                all.set(i, reservation);
                writeAll(all);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean cancelByHostIdGuestId(String hostId, int guestId) throws DataException {
        if (hostId ==  null || guestId <= 0) {
            return false;
        }

        Reservation reservation = findByHostIdGuestId(hostId, guestId);

        List<Reservation> all = findByHostId(hostId);
        for (int i = 0; i < all.size(); i++) {
            if (reservation.getId() == all.get(i).getId()) {
                all.remove(i);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    private void writeAll(List<Reservation> reservations) throws DataException {
        if (reservations == null || reservations.size() == 0) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(getFilePath(reservations.get(0).getHost().getId()))) {

            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getGuestId(),
                reservation.getTotal());
    }

    private Reservation deserialize(String[] fields, String id) {
        Reservation reservation = new Reservation();

        reservation.setId(Integer.parseInt(fields[0]));
        reservation.setStartDate(LocalDate.parse(fields[1]));
        reservation.setEndDate(LocalDate.parse(fields[2]));
        reservation.setHost(hostRepo.findById(id));

        Guest guest = guestRepo.findById(Integer.parseInt(fields[3]));
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal(fields[4]));

        return reservation;
    }

}
