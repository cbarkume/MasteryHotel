package learn.hotel.ui;

public enum MainMenuOption {

    EXIT(0, "Exit"),
    VIEW_RESERVATIONS(1, "View Reservations"),
    VIEW_GUESTS(2, "View Guests"),
    VIEW_HOSTS(3, "View Hosts"),
    ADD_RESERVATION(4, "Add Reservation"),
    ADD_GUEST(5, "Add Guest"),
    EDIT_RESERVATION(6, "Edit Reservation"),
    EDIT_GUEST(7, "Edit Guest"),
    CANCEL_RESERVATION(8, "Cancel Reservation"),
    DELETE_GUEST(9, "Delete Guest");

    private int value;
    private String message;

    private MainMenuOption(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
