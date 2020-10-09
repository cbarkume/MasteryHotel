package learn.hotel.ui;

import learn.hotel.data.DataException;
import learn.hotel.domain.GuestService;
import learn.hotel.domain.HostService;
import learn.hotel.domain.ReservationService;

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
                    //viewReservations();
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

}
