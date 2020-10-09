package learn.hotel.ui;

import learn.hotel.models.Reservation;

import java.util.ArrayList;
import java.util.List;

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
}
