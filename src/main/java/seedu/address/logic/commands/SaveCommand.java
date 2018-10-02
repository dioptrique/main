package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.wish.SavedAmount;
import seedu.address.model.wish.Wish;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SAVING;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WISHES;

public class SaveCommand extends Command {
    public static final String COMMAND_WORD = "save";
    public static final String COMMAND_ALIAS = "sv";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Channels saving amount to wish."
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_SAVING + "SAVING_AMOUNT]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SAVING + "108.50";

    public static final String MESSAGE_SAVE_SUCCESS = "Saved %1$s for wish %2$s";

    private final Index index;
    private final SavedAmount savedAmount;

    public SaveCommand(Index index, SavedAmount savedAmount) {
        requireNonNull(index);
        requireNonNull(savedAmount);

        this.index = index;
        this.savedAmount = savedAmount;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        List<Wish> lastShownList = model.getFilteredWishList();

        if(index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);
        }

        Wish wishToEdit = lastShownList.get(index.getZeroBased());

        Wish editedWish = new Wish(wishToEdit.getName(), wishToEdit.getPrice(), wishToEdit.getEmail(),
                wishToEdit.getUrl(), wishToEdit.getSavedAmount().incrementSavedAmount(savedAmount),
                wishToEdit.getRemark(), wishToEdit.getTags());

        model.updateWish(wishToEdit, editedWish);
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        model.commitWishBook();

        return new CommandResult(String.format(MESSAGE_SAVE_SUCCESS, savedAmount.toString(),
                index.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) {
            return true;
        }

        if(!(other instanceof SaveCommand)) {
            return false;
        }

        SaveCommand saveCommand = (SaveCommand) other;
        return this.index.equals(saveCommand.index)
                && this.savedAmount.equals(saveCommand.savedAmount);
    }
}
