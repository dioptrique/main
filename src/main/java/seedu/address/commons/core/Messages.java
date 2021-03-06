package seedu.address.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";

    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_DOUBLE_DATE_FORMAT = "Both Period and Dates are stated. "
            + "Only one can be used!\n";
    public static final String MESSAGE_INVALID_TIME_FORMAT = "Invalid time FORMAT! \n%1$s"
            + "\nShould be [years]y[months]m[days]d! Take note of the order!";
    public static final String MESSAGE_INVALID_WISH_DISPLAYED_INDEX = "The wish index provided is invalid";
    public static final String MESSAGE_WISHES_LISTED_OVERVIEW = "%1$d wishes listed!";
    public static final String MESSAGE_INVALID_AMOUNT = "Invalid amount! \n%1$s";
    public static final String MESSAGE_WISH_FULFILLED = "Wish has already been fulfilled!";
}
