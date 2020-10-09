package learn.hotel.ui;

import learn.hotel.data.DataException;
import learn.hotel.domain.GuestService;
import learn.hotel.domain.HostService;
import learn.hotel.domain.ReservationService;
import learn.hotel.domain.Result;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
                    //viewGuests();
                    break;
                case VIEW_HOSTS:
                    //viewHosts();
                    break;
                case ADD_RESERVATION:
                    //addReservation();
                    break;
                case ADD_GUEST:
                    //addGuest();
                    break;
                case EDIT_RESERVATION:
                    //editReservation();
                    break;
                case EDIT_GUEST:
                    //editGuest();
                    break;
                case CANCEL_RESERVATION:
                    //cancelReservation();
                    break;
                case DELETE_GUEST:
                    //deleteGuest();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservations(MainMenuOption option) {
        io.displayHeader(MainMenuOption.VIEW_RESERVATIONS.getMessage());
        int choice = view.getSearchChoice(option);
        List<Reservation> reservations = getReservationList(choice);
    }

    public List<Reservation> getReservationList(int choice) {
        if (choice == 1) {
            return reservationService.findByHostLastName(io.readRequiredString("Enter a last name to search for."));
        }
        else {
            return reservationService.findByHostEmail(io.readRequiredString("Enter an email to search for."));
        }
    }

    public List<Guest> getGuest(int choice) {
        List<Guest> guests = new ArrayList<>();
        if (choice == 1) {
            guests.add(guestService.findByName(io.readRequiredString("Enter a first name to search for."),
                    io.readRequiredString("Enter a last name to search for.")));
        }
        else if (choice == 2) {
            guests = guestService.findByLastName(io.readRequiredString("Enter a last name to search for."));
        }
        else {
            guests.add(guestService.findByEmail(io.readRequiredString("Enter an email to search for.")));
        }
        return guests;
    }

    public Host getHost(int choice) {
        if (choice == 1) {
            return hostService.findByLastName(io.readRequiredString("Enter a last name to search for."));
        }
        else {
            return hostService.findByEmail(io.readRequiredString("Enter an email to search for."));
        }
    }


}
