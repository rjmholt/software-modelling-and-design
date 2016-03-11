/**
 * Author: Robert Holt
 * Last Modified: 2016-03-11
 */
package com.unimelb.swen30006.mailroom.strategies.sorting;

import com.unimelb.swen30006.mailroom.MailItem;
import com.unimelb.swen30006.mailroom.MailStorage;
import com.unimelb.swen30006.mailroom.SortingStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;

import java.util.HashMap;

/**
 * Tries to distribute mail evenly across mailboxes, so that no mailbox ever contains
 * more than one item more than any other. This is likely to be a very poor strategy in
 * terms of delivery time/steps, even with a large number of robots. This class forms a kind of
 * closure, maintaining an internal state based on the last box visited.
 */
public class EvenDistributionSortingStrategy implements SortingStrategy
{
    // The number of mailboxes to put mail into
    private final int numMailBoxes;
    // The mailbox to look at on this iteration
    private int currMailBox;

    /**
     * Make an even distribution sorting strategy to distribute mail evenly across
     * the number of mail boxes passed through.
     * @param numMailBoxes the maximum number of mailboxes available to sort into
     */
    public EvenDistributionSortingStrategy(int numMailBoxes)
    {
        this.numMailBoxes = numMailBoxes;
        // Start at the 0th box
        this.currMailBox = 0;
    }

    /**
     * Go to the next numbered mailbox and place an item inside. If we are at
     * the last mailbox, we return to the 0th again.
     * @param item the item being sorted
     * @param storage the storage system in use
     * @return the identifier (a simple integer string -- e.g. "3") of the box where the mail is to be stored
     * @throws MailOverflowException
     */
    @Override
    public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException
    {
        // Put summaries in a hashmap so if we need to look at them more than once
        // we don't have to go through the list again each time a box is full
        // (highly unlikely given the strategy)
        HashMap<String, StorageBox.Summary> summaryHashMap =
                summaryListToIDHashMap(storage.retrieveSummaries());

        // Get the next box id
        String id = Integer.toString(this.currMailBox);
        // Record the box we started with so we can know later if we've tried all boxes
        String startID = id;
        // Increment the box the strategy looks for
        incrementBox();
        // If the box exists, try to insert the item
        while (summaryHashMap.containsKey(id)) {
            StorageBox.Summary summary = summaryHashMap.get(id);
            if (item.size <= summary.remainingUnits) {
                return id;
            }
            // If the item is too big, try the next box
            id = Integer.toString(this.currMailBox);
            // If we return to a box we've already looked at, we've looped
            // and there is no room for the box
            if (id.equals(startID)) {
                throw new MailOverflowException();
            }
            // Set up the next box to look for
            incrementBox();
        }

        // If the ID is not in the box summaries, we try to make a new box
        try {
            storage.createBox(id);
            return id;
        }
        catch (DuplicateIdentifierException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    /**
     * Change the number of the box we're looking for. This wraps the number
     * automatically, based on the maximum number of boxes available.
     */
    private void incrementBox() {
        this.currMailBox = (this.currMailBox + 1) % numMailBoxes;
    }

    /**
     * Puts the summary list into a hashmap keyed by ID, for faster lookup.
     * @param summaries an array of mailbox summaries to insert into the hashmap
     * @return a hashmap of the mail summaries given, keyed by identifier
     */
    private HashMap<String, StorageBox.Summary> summaryListToIDHashMap(StorageBox.Summary[] summaries)
    {
        HashMap<String, StorageBox.Summary> summaryHashMap = new HashMap<>(summaries.length);
        for (StorageBox.Summary summary: summaries) {
            summaryHashMap.put(summary.identifier, summary);
        }

        return summaryHashMap;
    }
}
