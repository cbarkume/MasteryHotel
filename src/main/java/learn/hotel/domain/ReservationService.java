package learn.hotel.domain;

import learn.hotel.data.GuestRepository;
import learn.hotel.data.HostRepository;
import learn.hotel.data.ReservationRepository;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;

import java.util.List;

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
        if (hostEmail != null && guestEmail != null) {
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
        if (hostLastName != null && guestFirstName != null && guestLastName != null) {
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

        if (host == null) {
            return null;
        }

        List <Reservation> reservation = reservationRepo.findByHostId(host.getId());
        return reservation;
    }

    public List<Reservation> findByHostLastName(String lastName) {
        Host host = hostService.findByLastName(lastName);

        if (host == null) {
            return null;
        }

        List<Reservation> reservations = reservationRepo.findByHostId(host.getId());
        return reservations;
    }


    public Result<Reservation> validate(Reservation reservation, boolean canBeDuplicate) {
        Result<Reservation> result = new Result<>();
        result = validateNulls(reservation, result);
        if (!result.isSuccess()){
            return result;
        }

        result = validateFields(reservation, result);
        if (!result.isSuccess()){
            return result;
        }

        if (!canBeDuplicate) {
            result = validateNotDuplicate(reservation, result);
        }
        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation, Result<Reservation> result) {
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

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Start Date cannot be null.");
        }

        if (reservation.getEndDate() == null) {
            result.addErrorMessage("End Date cannot be null.");
        }
        return result;
    }

    private Result<Reservation> validateFields(Reservation reservation, Result<Reservation> result) {
        if (reservation.getStartDate().isAfter(reservation.getEndDate())
                || reservation.getStartDate().isEqual(reservation.getEndDate())) {
            result.addErrorMessage("Start Date cannot be after or on the same day as End Date.");
        }
        return result;
    }

    private Result<Reservation> validateNotDuplicate(Reservation reservation, Result<Reservation> result) {
        Reservation duplicate = reservationRepo.findByHostIdGuestId(reservation.getHost().getId(),
                reservation.getGuest().getGuestId());

        if (duplicate != null) {
            result.addErrorMessage("This is a duplicate Reservation.");
        }

        return result;
    }
}
