package seedu.address.logic.commands;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.logic.commands.SaveCommand.MESSAGE_SAVE_SUCCESS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WISHES;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WISH;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WISH;
import static seedu.address.testutil.TypicalWishes.getTypicalWishBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.WishBook;
import seedu.address.model.wish.SavedAmount;
import seedu.address.model.wish.Wish;
import seedu.address.testutil.WishBuilder;

public class SaveCommandTest {
    private Model model = new ModelManager(getTypicalWishBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_saveUnfilteredList_success() {
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        Wish firstWish = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        // new wish with already incremented saved amount
        Wish editedWish = new WishBuilder(firstWish)
                .withSavedAmountIncrement(VALID_SAVED_AMOUNT_AMY).build();

        SavedAmount amountToSave = new SavedAmount(VALID_SAVED_AMOUNT_AMY);
        Index editedWishIndex = INDEX_FIRST_WISH;

        SaveCommand saveCommandToTest = new SaveCommand(INDEX_FIRST_WISH, amountToSave);
        String expectedMessage = String.format(MESSAGE_SAVE_SUCCESS, amountToSave,
                editedWishIndex.getOneBased());

        Model expectedModel = new ModelManager(new WishBook(model.getWishBook()), new UserPrefs());
        expectedModel.updateWish(firstWish, editedWish);
        expectedModel.commitWishBook();

        assertCommandSuccess(saveCommandToTest, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_saveFilteredList_success() {
        showWishAtIndex(model, INDEX_FIRST_WISH);

        Wish firstWish = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        // new wish with already incremented saved amount
        Wish editedWish = new WishBuilder(firstWish)
                .withSavedAmountIncrement(VALID_SAVED_AMOUNT_AMY).build();

        SavedAmount amountToSave = new SavedAmount(VALID_SAVED_AMOUNT_AMY);
        Index editedWishIndex = INDEX_FIRST_WISH;

        SaveCommand saveCommandToTest = new SaveCommand(INDEX_FIRST_WISH, amountToSave);
        String expectedMessage = String.format(MESSAGE_SAVE_SUCCESS, amountToSave,
                editedWishIndex.getOneBased());

        Model expectedModel = new ModelManager(new WishBook(model.getWishBook()), new UserPrefs());
        expectedModel.updateWish(firstWish, editedWish);
        expectedModel.commitWishBook();

        assertCommandSuccess(saveCommandToTest, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidWishIndexUnfilteredList_failure() {
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        Index indexOutOfBounds = Index.fromOneBased(model.getFilteredWishList().size() + 1);
        SaveCommand saveCommand = new SaveCommand(indexOutOfBounds, new SavedAmount(VALID_SAVED_AMOUNT_AMY));

        String expectedMessage = MESSAGE_INVALID_WISH_DISPLAYED_INDEX;

        assertCommandFailure(saveCommand, model, commandHistory, expectedMessage);
    }

    /*
     * Edit a Wish at an one based index larger than size of filtered list but
     * smaller than or equal to internal list size.
     */
    @Test
    public void execute_invalidWishIndexFilteredList_failure() {
        showWishAtIndex(model, INDEX_FIRST_WISH);

        Index indexOutOfBoundsInFilteredWishList = INDEX_SECOND_WISH;
        Index largestIndexInCurrWishBook = Index.fromOneBased(model.getWishBook().getWishList().size());

        // TypicalWishes must have at least 2 wishes
        assertTrue(indexOutOfBoundsInFilteredWishList.getOneBased() <=
                largestIndexInCurrWishBook.getOneBased());

        SaveCommand saveCommand = new SaveCommand(indexOutOfBoundsInFilteredWishList,
                new SavedAmount(VALID_SAVED_AMOUNT_AMY));

        String expectedMessage = MESSAGE_INVALID_WISH_DISPLAYED_INDEX;

        assertCommandFailure(saveCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void executeUndoRedo_saveUnfilteredList_success() {
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        Wish firstWish = model.getFilteredWishList().get(INDEX_FIRST_WISH.getZeroBased());
        // new wish with already incremented saved amount
        Wish editedWish = new WishBuilder(firstWish)
                .withSavedAmountIncrement(VALID_SAVED_AMOUNT_AMY).build();

        SavedAmount amountToSave = new SavedAmount(VALID_SAVED_AMOUNT_AMY);
        Index editedWishIndex = INDEX_FIRST_WISH;

        SaveCommand saveCommandToTest = new SaveCommand(INDEX_FIRST_WISH, amountToSave);
        String expectedMessage = String.format(MESSAGE_SAVE_SUCCESS, amountToSave,
                editedWishIndex.getOneBased());

        Model expectedModel = new ModelManager(new WishBook(model.getWishBook()), new UserPrefs());
        expectedModel.updateWish(firstWish, editedWish);
        expectedModel.commitWishBook();

        // Ensure that save command works first
        assertCommandSuccess(saveCommandToTest, model, commandHistory, expectedMessage, expectedModel);

        // Test undo
        expectedModel.undoWishBook();
        expectedMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(new UndoCommand(), model, commandHistory, expectedMessage, expectedModel);

        // Test redo
        expectedModel.redoWishBook();
        expectedMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(new RedoCommand(), model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_saveInvalidIndexUnfilteredList_failure() {
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        Index indexOutOfBounds = Index.fromOneBased(model.getFilteredWishList().size() + 1);
        SaveCommand saveCommandToFail = new SaveCommand(indexOutOfBounds, new SavedAmount(VALID_SAVED_AMOUNT_AMY));

        String expectedMessage = MESSAGE_INVALID_WISH_DISPLAYED_INDEX;

        // ensure that the command fails
        assertCommandFailure(saveCommandToFail, model, commandHistory, expectedMessage);

        // test undo -> undo should fail as there is nothing in history
        String expectedFailureMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(new UndoCommand(), model, commandHistory, expectedFailureMessage);

        // test redo -> nothing should be undone
        expectedFailureMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(new RedoCommand(), model, commandHistory, expectedFailureMessage);
    }

    @Test
    public void equals() {
        final SavedAmount savedAmountAmy = new SavedAmount(VALID_SAVED_AMOUNT_AMY);
        final SavedAmount savedAmountBob = new SavedAmount(VALID_SAVED_AMOUNT_BOB);
        final SaveCommand saveCommand1a = new SaveCommand(INDEX_FIRST_WISH, savedAmountAmy);
        final SaveCommand saveCommand1b = new SaveCommand(INDEX_FIRST_WISH, savedAmountAmy);
        final SaveCommand saveCommand2 = new SaveCommand(INDEX_FIRST_WISH, savedAmountBob);

        // same object
        assertTrue(saveCommand1a.equals(saveCommand1a));

        // same values
        assertTrue(saveCommand1b.equals(saveCommand1a));

        // different values
        assertFalse(saveCommand1a.equals(saveCommand2));

        // null
        assertFalse(saveCommand1a.equals(null));

        // different command
        assertFalse(saveCommand1a.equals(new ClearCommand()));
    }
}
