package learn.hotel.ui;

import learn.hotel.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleIO {

    private static final String INVALID_NUMBER
            = "[INVALID] Enter a valid number.";
    private static final String NUMBER_OUT_OF_RANGE
            = "[INVALID] Enter a number between %s and %s.";
    private static final String REQUIRED
            = "[INVALID] Value is required.";
    private static final String INVALID_DATE
            = "[INVALID] Enter a date in MM/dd/yyyy format.";

    private final Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    public String readString(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    public String readRequiredString(String prompt) {
        while (true) {
            String result = readString(prompt);
            if (!result.isBlank()) {
                return result;
            }
            println(REQUIRED);
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readRequiredString(prompt));
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public double readDouble(String prompt, double min, double max) {
        while (true) {
            double result = readDouble(prompt);
            if (result >= min && result <= max) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readRequiredString(prompt));
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            int result = readInt(prompt);
            if (result >= min && result <= max) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    public boolean readBoolean(String prompt) {
        while (true) {
            String input = readRequiredString(prompt).toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }
            println("[INVALID] Please enter 'y' or 'n'.");
        }
    }

    public LocalDate readLocalDate(String prompt) {
        while (true) {
            String input = readRequiredString(prompt);
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ex) {
                println(INVALID_DATE);
            }
        }
    }

    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            String input = readRequiredString(prompt);
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        while (true) {
            BigDecimal result = readBigDecimal(prompt);
            if (result.compareTo(min) >= 0 && result.compareTo(max) <= 0) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    public void enterToContinue() {
        readString("Press [Enter] to continue.");
    }


    public void displayHeader(String message) {
        println("");
        println(message);
        println("=".repeat(message.length()));
    }

    public void displayTotal(BigDecimal total) {
        println("$" + total.toString());
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            println(message);
        }
    }

    public void displayGuests(List<Guest> guests) {
        if (guests == null || guests.isEmpty()) {
            println("No guests found.");
            return;
        }
        for (Guest guest : guests) {
            printf("Guest Name: %s %s, Phone: %s, Email: %s, State: %s%n",
                    guest.getFirstName(),
                    guest.getLastName(),
                    guest.getPhone(),
                    guest.getEmail(),
                    guest.getState().fullName);
            );
        }
    }

    public void displayHosts(List<Host> hosts) {
        if (hosts == null || hosts.isEmpty()) {
            println("No hosts found.");
            return;
        }
        for (Host host : hosts) {
            printf("Host Name: %s, Phone Number: %s, Email: %s, Address: %s, %s, %s, %s, Standard Rate: $%s, Weekend Rate: $%s%n",
                    host.getLastName(),
                    host.getPhone(),
                    host.getEmail(),
                    host.getAddress(),
                    host.getCity(),
                    host.getState(),
                    host.getPostalCode(),
                    host.getStandardRate(),
                    host.getWeekendRate());

        }
    }

    public void displayReservations(List<Reservation> reservations) {

        if (reservations.size() == 0) {
            println("No items found");
        }

        for (Reservation reservation : reservations) {
            printf("Host Name: %s, Guest Name: %s %s, Duration: %s to %s, Total: $%s%n",
                    reservation.getHost().getLastName(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getLastName(),
                    reservation.getStartDate().toString(),
                    reservation.getEndDate().toString(),
                    reservation.getTotal());
        }
    }
}
