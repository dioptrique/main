package seedu.address.model.wish;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SAVED_AMOUNT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SAVED_AMOUNT_BOB;

import org.junit.Test;

import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.testutil.Assert;

public class SavedAmountTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new SavedAmount(null));
    }

    @Test
    public void constructor_invalidPrice_throwsIllegalArgumentException() {
        String invalidSavedAmount = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new SavedAmount(invalidSavedAmount));
    }

    @Test
    public void isValidPrice() {
        // null phone number
        Assert.assertThrows(NullPointerException.class, () -> SavedAmount.isValidSavedAmount(null));

        // invalid prices
        assertFalse(SavedAmount.isValidSavedAmount("")); // empty string
        assertFalse(SavedAmount.isValidSavedAmount(" ")); // spaces only
        assertFalse(SavedAmount.isValidSavedAmount("91..1")); // double dot
        assertFalse(SavedAmount.isValidSavedAmount("phone")); // non-numeric
        assertFalse(SavedAmount.isValidSavedAmount("1.0e321")); // exp
        assertFalse(SavedAmount.isValidSavedAmount("9312 1534")); // spaces within digits
        assertFalse(SavedAmount.isValidSavedAmount("2.")); // no digits after decimal point

        // valid prices
        assertTrue(SavedAmount.isValidSavedAmount("+93121534")); // no decimal digit
        assertTrue(SavedAmount.isValidSavedAmount("1.0")); // one decimal digit
        assertTrue(SavedAmount.isValidSavedAmount("-1.02")); // two decimal digits
    }

    @Test
    public void equals() {
        SavedAmount savedAmount1 = new SavedAmount(VALID_SAVED_AMOUNT_AMY);
        SavedAmount savedAmount1b = new SavedAmount(VALID_SAVED_AMOUNT_AMY);
        SavedAmount savedAmount2 = new SavedAmount(VALID_SAVED_AMOUNT_BOB);

        // same object
        assertTrue(savedAmount1.equals(savedAmount1));

        // same value
        assertTrue(savedAmount1.equals(savedAmount1b));

        // different value
        assertFalse(savedAmount1.equals(savedAmount2));

        // null
        assertFalse(savedAmount1.equals(null));

        // different type
        assertFalse(savedAmount1.equals(new Name(VALID_NAME_AMY)));
    }
}
