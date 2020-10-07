package learn.hotel.domain;

import learn.hotel.data.GuestRepository;
import learn.hotel.data.HostRepository;
import learn.hotel.data.ReservationRepository;
import learn.hotel.models.Reservation;

import java.util.List;

public class ReservationService {

    private final GuestRepository guestRepo;
    private final HostRepository hostRepo;
    private final ReservationRepository reservationRepo;

    public ReservationService(GuestRepository guestRepo, HostRepository hostRepo, ReservationRepository reservationRepo) {
        this.guestRepo = guestRepo;
        this.hostRepo = hostRepo;
        this.reservationRepo = reservationRepo;
    }


    public Result<Reservation> validate(Reservation reservation) {
        Result<Reservation> result = new Result<>();
        result = validateNulls(reservation, result);
        if (!result.isSuccess()){
            return result;
        }

        result = validateFields(reservation, result);
        if (!result.isSuccess()){
            return result;
        }

        result = validateNotDuplicate(reservation, result);
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
