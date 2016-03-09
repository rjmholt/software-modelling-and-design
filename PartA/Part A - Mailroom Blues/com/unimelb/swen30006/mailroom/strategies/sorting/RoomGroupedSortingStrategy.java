package com.unimelb.swen30006.mailroom.strategies.sorting;

import com.unimelb.swen30006.mailroom.MailItem;
import com.unimelb.swen30006.mailroom.MailStorage;
import com.unimelb.swen30006.mailroom.SortingStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;

/**
 * Created by rob on 4/03/16.
 *
 * This sorting strategy seeks to sort mail by floor, so that
 * a delivery bot only needs to visit one floor. In large buildings,
 * this may cause a storage overflow. In cases of sparsely distributed mail,
 * this may prove inefficient.
 */
public class RoomGroupedSortingStrategy implements SortingStrategy
{
    @Override
    public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException
    {
        // Use the floor the item is going to as an identifier
        String floorString = Integer.toString(item.floor);

        // Identify the mailbox for the floor this item is going to.
        // Mailboxes are named "1.0", "1.1", "2.0", etc.
        // For a given floor, start with 0 and work up
        int boxNum = 0;
        for (StorageBox.Summary summary: storage.retrieveSummaries()) {
            // Find a box for the right floor
            if (summary.identifier.startsWith(floorString)) {
                // Check it has space
                if (summary.remainingUnits >= item.size) {
                    return summary.identifier;
                }
                // If it doesn't, increment the box count
                // in case we need to make a new box
                else {
                    boxNum++;
                }
            }
        }
        // If no suitable mailboxes are found, make a new one
        String id = floorString + "." + Integer.toString(boxNum);
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
}
