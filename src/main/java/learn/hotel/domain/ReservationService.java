package learn.hotel.domain;

import learn.hotel.data.DataException;
import learn.hotel.data.GuestRepository;
import learn.hotel.data.HostRepository;
import learn.hotel.data.ReservationRepository;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class ReservationService {

    private final GuestRepository guestRepo;
    private final HostRepository hostRepo;
    private final ReservationRepository reservationRepo;
    private final GuestService guestService;
    private final HostService hostService;

    public ReservationService(GuestRepository guestRepo, HostRepository hostRepo,
                              ReservationRepository reservationRepo, GuestService guestService,
                              HostService hostService) {
        this.guestRepo = guestRepo;
        this.hostRepo = hostRepo;
        this.reservationRepo = reservationRepo;
        this.guestService = guestService;
        this.hostService = hostService;
    }

    public Reservation findByHostEmailGuestEmail(String hostEmail, String guestEmail) {
        if (hostEmail == null || guestEmail == null) {
            return null;
        }
        Host host = hostService.findByEmail(hostEmail);
        Guest guest = guestService.findByEmail(guestEmail);

        if (host == null || guest == null) {
            return null;
        }

        Reservation reservation = reservationRepo.findByHostIdGuestId(host.getId(), guest.getGuestId());
        return reservation;
    }

    public Reservation findByHostLastNameGuestFullName(String hostLastName, String guestFirstName, String guestLastName) {
        if (hostLastName == null || guestFirstName == null || guestLastName == null) {
            return null;
        }
        Guest guest = guestService.findByName(guestFirstName, guestLastName);
        Host host = hostService.findByLastName(hostLastName);

        if (host == null || guest == null) {
            return null;
        }

        Reservation reservation = reservationRepo.findByHostIdGuestId(host.getId(), guest.getGuestId());
        return reservation;
    }

    public List<Reservation> findByHostEmail(String email) {
        Host host = hostService.findByEmail(email);

        List<Reservation> reservations = new ArrayList<>();

        if (host == null) {
            return reservations;
        }

        List <Reservation> reservation = reservationRepo.findByHostId(host.getId());
        return reservation;
    }

    public List<Reservation> findByHostLastName(String lastName) {
        Host host = hostService.findByLastName(lastName);

        List<Reservation> reservations = new ArrayList<>();

        if (host == null) {
            return reservations;
        }

        reservations = reservationRepo.findByHostId(host.getId());
        return reservations;
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation, false);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepo.add(reservation));

        return result;
    }

    public Result<Reservation> edit(Reservation reservation) throws DataException {
        Result<Reservation> result = new Result<>();
        result = validateNulls(reservation, result, true);
        if (!result.isSuccess()) {
            return result;
        }
        reservation.setId(findByHostEmailGuestEmail
                (reservation.getHost().getEmail(), reservation.getGuest().getEmail()).getId());

        if (reservation.getStartDate() == null) {
            reservation.setStartDate(findByHostEmailGuestEmail
                    (reservation.getHost().getEmail(), reservation.getGuest().getEmail()).getStartDate());
        }
        if (reservation.getEndDate() == null) {
            reservation.setEndDate(findByHostEmailGuestEmail
                    (reservation.getHost().getEmail(), reservation.getGuest().getEmail()).getEndDate());
        }

        result = validate(reservation, true);
        if (!result.isSuccess()) {
            return result;
        }

        if (!reservationRepo.edit(reservation)) {
            result.addErrorMessage("Unable to edit.");
        }
        result.setPayload(reservation);
        return result;
    }

    public Result<Reservation> cancelByHostLastNameGuestFullName(String hostLastName, String guestFirstName, String guestLastName) throws DataException {
        Result<Reservation> result = new Result<>();

        Reservation reservation = findByHostLastNameGuestFullName(hostLastName, guestFirstName, guestLastName);

        if (reservation == null) {
            result.addErrorMessage("Name not found. Check your spelling.");
            return result;
        }


        if (!reservationRepo.cancelByHostIdGuestId(reservation.getHost().getId(), reservation.getGuest().getGuestId())) {
            result.addErrorMessage("Error: Already entered guest has invalid ID.");
        }
        return result;
    }

    public Result<Reservation> cancelByHostEmailGuestEmail(String hostEmail, String guestEmail) throws DataException {
        Result<Reservation> result = new Result<>();

        Reservation reservation = findByHostEmailGuestEmail(hostEmail, guestEmail);

        if (reservation == null) {
            result.addErrorMessage("Email not found. A proper email format is 'example@yahoo.com'.");
            return result;
        }


        if (!reservationRepo.cancelByHostIdGuestId(reservation.getHost().getId(), reservation.getGuest().getGuestId())) {
            result.addErrorMessage("Error: Already entered guest has invalid ID.");
        }
        return result;
    }

    private Result<Reservation> validate(Reservation reservation, boolean isEdit) {
        Result<Reservation> result = new Result<>();
        result = validateNulls(reservation, result, isEdit);
        if (!result.isSuccess()){
            return result;
        }

        result = validateDates(reservation, result);
        if (!result.isSuccess()){
            return result;
        }

        result = validateNotDuplicate(reservation, result, isEdit);
        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation, Result<Reservation> result, boolean isEdit) {
        if (reservation == null) {
            result.addErrorMessage("Reservation cannot be null.");
            return result;
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest cannot be null.");
        }

        if (reservation.getHost() == null) {
            result.addErrorMessage("Host cannot be null.");
        }

        if (isEdit) {
            return result;
        }

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Start Date cannot be null.");
        }

        if (reservation.getEndDate() == null) {
            result.addErrorMessage("End Date cannot be null.");
        }
        return result;
    }

    private Result<Reservation> validateDates(Reservation reservation, Result<Reservation> result) {
        if (reservation.getStartDate().isAfter(reservation.getEndDate())
                || reservation.getStartDate().isEqual(reservation.getEndDate())) {
            result.addErrorMessage("Start Date cannot be after or on the same day as End Date.");
        }
        return result;
    }

    private Result<Reservation> validateNotDuplicate(Reservation reservation, Result<Reservation> result, boolean isEdit) {
        Reservation duplicate = reservationRepo.findByHostIdGuestId(reservation.getHost().getId(),
                reservation.getGuest().getGuestId());

        if (duplicate != null) {
            if (!isEdit) {
                result.addErrorMessage("This is a duplicate Reservation.");
            }
            return result;
        }
        if (isEdit) {
            result.addErrorMessage("Reservation not found.");
        }

        return result;
    }
}
