package learn.hotel.ui;

import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import learn.hotel.models.State;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        io.displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public int getSearchChoice(MainMenuOption option) {
        int max = 2;
        String message = "Enter how you'd like to display ";
        if (option.getMessage().contains("Reservation")) {
            message += "Reservations.";
            io.println(message);
            io.println("1: Display by Host last name.");
            io.println("2: Display by Host email.");
        }
        else if (option.getMessage().contains("Guest")) {
            message += "Guests.";
            io.println(message);
            io.println("1: Display by Guest full name.");
            io.println("2: Display by Guest last name.");
            io.println("3: Display by Guest email.");
            max = 3;
        }
        else if (option.getMessage().contains("Host")) {
            message += "Hosts.";
            io.println(message);
            io.println("1: Display by Host last name.");
            io.println("2: Display by Host email.");
        }
        return io.readInt("",1,max);
    }



    public Reservation makeReservation(Host host, Guest guest) {
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(io.readLocalDate("Reservation Start Date [MM/dd/yyyy]: "));
        reservation.setEndDate(io.readLocalDate("Reservation End Date [MM/dd/yyyy]: "));
        reservation.setTotal(reservation.calculateTotal());
        return reservation;
    }

    public Guest makeGuest() {
        Guest guest = new Guest();
        guest.setFirstName(io.readRequiredString("Guest First Name: "));
        guest.setLastName(io.readRequiredString("Guest Last Name: "));
        guest.setEmail(io.readRequiredString("Guest Email: "));
        guest.setPhone(io.readRequiredString("Guest Phone Number [(###) #######]: "));
        guest.setState(getState());
        return guest;
    }

    public State getState() {
        io.displayHeader("State choice");
        int index = 1;
        for (State s : State.values()) {
            io.printf("%s: %s%n", index++, s);
        }
        index--;
        String message = String.format("Select a State [1-%s]: ", index);
        return State.values()[io.readInt(message,1,index) - 1];
    }

    public String getGuestLastNamePrefix() {
        return io.readRequiredString("Guest last name starts with: ");
    }

    public Guest chooseGuest(List<Guest> guests) {
        if (guests == null || guests.size() == 0) {
            io.println("No guests found.");
            return null;
        }

        int index = 1;
        for (Guest guest : guests.stream().collect(Collectors.toList())) {
            io.printf("%s: %s %s%n", index++, guest.getFirstName(), guest.getLastName());
        }
        index--;
        io.println("0: Exit");
        String message = String.format("Select a Guest by their index [0-%s] ", index);

        index = io.readInt(message,0,index);
        if (index <= 0) {
            return null;
        }
        return guests.get(index - 1);
    }

    public String getHostLastNamePrefix() {
        return io.readRequiredString("Host last name starts with: ");
    }

    public Host chooseHost(List<Host> hosts) {
        if (hosts == null || hosts.size() == 0) {
            io.println("No hosts found.");
            return null;
        }

        int index = 1;
        for (Host host : hosts.stream().collect(Collectors.toList())) {
            io.printf("%s: %s %s%n", index++, host.getLastName(), host.getEmail());
        }
        index--;
        io.println("0: Exit");
        String message = String.format("Select a Host by their index [0-%s] ", index);

        index = io.readInt(message,0,index);
        if (index <= 0) {
            return null;
        }
        return hosts.get(index - 1);
    }
}
