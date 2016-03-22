/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Robert Holt
 * SID: 
 * Last Modified: 2016-03-22
 */

package com.unimelb.swen30006.mailroom.strategies.sorting;

import com.unimelb.swen30006.mailroom.MailItem;
import com.unimelb.swen30006.mailroom.MailStorage;
import com.unimelb.swen30006.mailroom.SortingStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;

import java.util.HashMap;
import java.util.Random;

/**
 * Randomly picks a box between "0" and numMailBoxes - 1
 */
public class RandomSortingStrategy implements SortingStrategy
{
    private final int numMailBoxes;
    private final Random randomiser;

    public RandomSortingStrategy(long seed, int numMailBoxes) {
        this.numMailBoxes = numMailBoxes;
        this.randomiser = new Random(seed);
    }

    /**
     *
     * @param item the item being sorted
     * @param storage the storage system in use
     * @return a random box between "0" and numMailBoxes - 1
     * @throws MailOverflowException
     */
    @Override
    public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException
    {
        // Make a hashmap of box summaries in case we need to operate over them multiple times
        HashMap<String, StorageBox.Summary> boxSummaries = new HashMap<>();
        for (StorageBox.Summary summary: storage.retrieveSummaries()) {
            boxSummaries.put(summary.identifier, summary);
        }

        // Pick a random box
        String id = getRandomBoxID(numMailBoxes);

        // Try to find the box and insert the item
        while (true) {
            if (boxSummaries.containsKey(id)) {
                if (boxSummaries.get(id).remainingUnits >= item.size) {
                    return id;
                }
                // If the box is too full, generate a new random ID
                else {
                    id = getRandomBoxID(numMailBoxes);
                }
            }
            // If there is no such box, stop looking and make a new one
            else {
                break;
            }
        }

        // If the box doesn't already exist, try to create it
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
     * Pick a mailbox at random, given the maximum number of mailboxes that can exist
     * @param numMailBoxes the maximum number of mailboxes the mail storage can support
     * @return the identifier of the randomly selected mailbox
     */
    private String getRandomBoxID(int numMailBoxes)
    {
        // Pick a numbered mailbox, between 0 and numMailBoxes-1
        int idNum = this.randomiser.nextInt(numMailBoxes);
        String id = Integer.toString(idNum);
        return id;
    }
}
