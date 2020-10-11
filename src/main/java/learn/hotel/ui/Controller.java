package learn.hotel.ui;

import learn.hotel.data.DataException;
import learn.hotel.domain.GuestService;
import learn.hotel.domain.HostService;
import learn.hotel.domain.ReservationService;
import learn.hotel.domain.Result;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;
    private final ConsoleIO io;

    public Controller(GuestService guestService, HostService hostService, ReservationService reservationService, View view, ConsoleIO io) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
        this.io = io;
    }

    public void run() {
        io.displayHeader("Welcome to the Reservationtron 3000");
        try {
            runAppLoop();
        } catch (DataException ex) {
            io.displayException(ex);
        }
        io.displayHeader("Thanks for using Reservationtron 3000.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS:
                    viewReservations(option);
                    break;
                case VIEW_GUESTS:
                    viewGuests(option);
                    break;
                case VIEW_HOSTS:
                    viewHosts(option);
                    break;
                case ADD_RESERVATION:
                    addReservation();
                    break;
                case ADD_GUEST:
                    addGuest();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case EDIT_GUEST:
                    editGuest();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
                case DELETE_GUEST:
                    deleteGuest();
                    break;
                case DELETE_CHECKED_OUT:
                    deleteReservationsWithCheckedOutGuests();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservations(MainMenuOption option) {
        io.displayHeader(option.getMessage());
        int choice = view.getSearchChoice(option);
        List<Reservation> reservations = getReservationList(choice);
        io.displayReservations(reservations);
        io.enterToContinue();
    }

    private void viewGuests(MainMenuOption option) {
        io.displayHeader(option.getMessage());
        int choice = view.getSearchChoice(option);
        List<Guest> guests = getGuestList(choice);
        io.displayGuests(guests);
        io.enterToContinue();
    }

    private void viewHosts(MainMenuOption option) {
        io.displayHeader(option.getMessage());
        int choice = view.getSearchChoice(option);
        List<Host> hosts = getHostList(choice);
        io.displayHosts(hosts);
        io.enterToContinue();
    }

    private void addReservation() throws DataException {
        io.displayHeader(MainMenuOption.ADD_RESERVATION.getMessage());
        Guest guest = getGuest();
        if (guest == null) {
            return;
        }

        Host host = getHost();
        if (host == null) {
            return;
        }

        Reservation reservation = view.makeReservation(host, guest);
        reservation.setTotal(reservation.calculateTotal());

        boolean ok = view.getReservationSummary(reservation);
        if (!ok) {
            io.println("Reservation not added.");
            return;
        }

        Result<Reservation> result = reservationService.add(reservation);
        if (!result.isSuccess()) {
            io.displayStatus(false, result.getErrorMessages());
        }
        else {
            String successMessage = String.format("Reservation with ID %s created.", result.getPayload().getId());
            io.displayStatus(true, successMessage);
        }

    }

    private void addGuest() throws DataException {
        io.displayHeader(MainMenuOption.ADD_GUEST.getMessage());
        Guest guest = view.makeGuest();
        if (guest == null) {
            return;
        }
        boolean ok = view.getGuestSummary(guest);
        if (!ok) {
            io.println("Guest not added.");
            return;
        }

        Result<Guest> result = guestService.add(guest);
        if (!result.isSuccess()) {
            io.displayStatus(false, result.getErrorMessages());
        }
        else {
            String successMessage = String.format("Guest with ID %s created.", result.getPayload().getGuestId());
            io.displayStatus(true, successMessage);
        }
    }

    private void editReservation() throws DataException {
        io.displayHeader(MainMenuOption.EDIT_RESERVATION.getMessage());
        Guest guest = getGuest();
        if (guest == null) {
            return;
        }

        Host host = getHost();
        if (host == null) {
            return;
        }
        Reservation reservation = view.updateReservation(host,guest);

        if (reservation.getStartDate() == null) {
            reservation.setStartDate(reservationService.findByHostEmailGuestEmail
                    (reservation.getHost().getEmail(), reservation.getGuest().getEmail()).getStartDate());
        }
        if (reservation.getEndDate() == null) {
            reservation.setEndDate(reservationService.findByHostEmailGuestEmail
                    (reservation.getHost().getEmail(), reservation.getGuest().getEmail()).getEndDate());
        }

        reservation.setTotal(reservation.calculateTotal());

        boolean ok = view.getReservationSummary(reservation);
        if (!ok) {
            io.println("Reservation not added.");
            return;
        }

        Result<Reservation> result = reservationService.edit(reservation);

        if (!result.isSuccess()) {
            io.displayStatus(false, result.getErrorMessages());
        }
        else {
            String successMessage = String.format("Reservation with ID %s edited.", result.getPayload().getId());
            io.displayStatus(true, successMessage);
        }
    }

    private void editGuest() throws DataException {
        io.displayHeader(MainMenuOption.EDIT_RESERVATION.getMessage());
        Guest guest = getGuest();
        if (guest == null) {
            return;
        }

        guest = view.updateGuest(guest);

        boolean ok = view.getGuestSummary(guest);
        if (!ok) {
            io.println("Guest not added.");
            return;
        }

        Result<Guest> result = guestService.edit(guest);

        if (!result.isSuccess()) {
            io.displayStatus(false, result.getErrorMessages());
        }
        else {
            String successMessage = String.format("Guest with ID %s edited.", result.getPayload().getGuestId());
            io.displayStatus(true, successMessage);
        }
    }

    private void cancelReservation() throws DataException {
        MainMenuOption option = MainMenuOption.CANCEL_RESERVATION;
        io.displayHeader(option.getMessage());

        int choice = view.getSearchChoice(option);
        List<Reservation> reservations = getReservationList(choice);

        if (reservations.size() == 0) { return; }

        Reservation reservation = io.displayAndChooseReservations(reservations);

        if (reservation == null) { return; }

        Result<Reservation> result = reservationService.cancelByHostEmailGuestEmail(
                reservation.getHost().getEmail(), reservation.getGuest().getEmail());

        if (!result.isSuccess()) {
            io.displayStatus(false, result.getErrorMessages());
        }
        else {
            String successMessage = String.format("Reservation with ID %s cancelled.", result.getPayload().getId());
            io.displayStatus(true, successMessage);
        }
    }

    private void deleteGuest() throws DataException {
        MainMenuOption option = MainMenuOption.DELETE_GUEST;
        io.displayHeader(option.getMessage());

        int choice = view.getSearchChoice(option);
        List<Guest> guests = getGuestList(choice);

        if (guests.size() == 0) { return; }

        Guest guest = io.displayAndChooseGuests(guests);

        if (guest == null) { return; }

        Result<Guest> result = guestService.deleteByEmail(guest.getEmail());
        if (!result.isSuccess()) {
            io.displayStatus(false, result.getErrorMessages());
        }
        else {
            String successMessage = String.format("Guest with ID %s deleted.", result.getPayload().getGuestId());
            io.displayStatus(true, successMessage);
        }
    }

    private void deleteReservationsWithCheckedOutGuests() throws DataException {
        List<Host> allHosts = hostService.findAll();

        for (Host h : allHosts) {
            List<Reservation> allReservations = reservationService.findByHostLastName(h.getLastName());
            for (Reservation r : allReservations) {
                if (r.getEndDate().isBefore(LocalDate.now())) {
                    Result<Reservation> result = reservationService.cancelByHostEmailGuestEmail(
                            r.getHost().getEmail(), r.getGuest().getEmail());
                    io.printf("Reservation for Guest %s %s deleted as they checked out on %s.%n",
                            r.getGuest().getFirstName(),
                            r.getGuest().getLastName(),
                            r.getEndDate());
                }
            }
        }
    }

    //Helper methods
    private Guest getGuest() {
        String lastNamePrefix = view.getGuestLastNamePrefix();
        List<Guest> guests = guestService.findByLastNamePrefix(lastNamePrefix);
        return view.chooseGuest(guests);
    }

    private Host getHost() {
        String lastNamePrefix = view.getHostLastNamePrefix();
        List<Host> hosts = hostService.findByLastNamePrefix(lastNamePrefix);
        return view.chooseHost(hosts);
    }

    private List<Reservation> getReservationList(int choice) {
        if (choice == 1) {
            return reservationService.findByHostLastName(io.readRequiredString("Enter a last name to search for."));
        }
        else {
            return reservationService.findByHostEmail(io.readRequiredString("Enter an email to search for."));
        }
    }

    private List<Guest> getGuestList(int choice) {
        List<Guest> guests = new ArrayList<>();
        if (choice == 1) {
            Guest guest = guestService.findByName(io.readRequiredString("Enter a first name to search for."),
                    io.readRequiredString("Enter a last name to search for."));
            if (guest != null) {
                guests.add(guest);
            }
        }
        else if (choice == 2) {
            guests = guestService.findByLastNamePrefix(io.readRequiredString("Enter a last name to search for."));
        }
        else {

            Guest guest = guestService.findByEmail(io.readRequiredString("Enter an email to search for."));
            if (guest != null) {
                guests.add(guest);
            }
        }
        return guests;
    }

    private List<Host> getHostList(int choice) {
        List<Host> hosts = new ArrayList<>();
        if (choice == 1) {
            hosts = hostService.findByLastNamePrefix(io.readRequiredString("Enter a last name to search for."));
        }
        else {
            Host host = hostService.findByEmail(io.readRequiredString("Enter an email to search for."));
            if (host != null) {
                hosts.add(host);
            }
        }
        return hosts;
    }

}
