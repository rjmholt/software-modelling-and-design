/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Robert Holt
 * SID: 
 * Last Modified: 2016-03-11
 */
package com.unimelb.swen30006.mailroom.strategies.sorting;

import com.unimelb.swen30006.mailroom.MailItem;
import com.unimelb.swen30006.mailroom.MailStorage;
import com.unimelb.swen30006.mailroom.SortingStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;

/**
 * This sorting strategy seeks to sort mail by floor, so that
 * a delivery bot only needs to visit grouped floors. This strategy may
 * fail where mail is heavily apportioned to one room, and will be slow
 * when mail distribution is sparse or when the number of delivery bots is
 * less than the number of floor brackets.
 */
public class RoomGroupedSortingStrategy implements SortingStrategy
{
    // Store the building size at construction time (it shouldn't change)
    private final int minFloor;
    private final int maxFloor;
    private final int numMailBoxes;

    public RoomGroupedSortingStrategy(int numMailBoxes, int minFloor, int maxFloor)
    {
        this.numMailBoxes = numMailBoxes;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    /**
     * Try to group floors in brackets based on how many mailboxes and
     * floors there are. If floors range from -1 to 25 and there are 5
     * mailboxes, then there are 6 floors per bracket, respectively named:
     * "|-1|0|1|2|3|4|", "|5|6|7|8|9|10|", etc.
     *
     * @param item the item being sorted
     * @param storage the storage system in use
     * @return the name/key of the mail box to insert the mail item into
     * @throws MailOverflowException
     */
    @Override
    public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException
    {
        // Break the boxes into floor brackets to divide up the mail
        int floorsPerBox = ceilDivide(maxFloor - minFloor, numMailBoxes);

        // Look for a box with the string "|<num>|" in the name
        String intStr = "|" + Integer.toString(item.floor) + "|";
        for (StorageBox.Summary summary: storage.retrieveSummaries()) {
            // Pick that box if it's found
            if (summary.identifier.contains(intStr)) {
                return summary.identifier;
            }
        }
        // If no suitable mailboxes are found, make a new one
        // Give the string a default value
        String floorID = "";
        for (int i=minFloor; i<maxFloor; i += floorsPerBox) {
            // Find the bracket the floor belongs in and give it a name
            if (i <= item.floor && item.floor < i+floorsPerBox) {
                floorID = makeFloorBracketID(i, floorsPerBox);
            }
        }
        try {
            // Try to make the box with that ID
            storage.createBox(floorID);
            return floorID;
        }
        catch (DuplicateIdentifierException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    /**
     * Do the ceiling integer division of two numbers
     * @param numerator the integer to divide
     * @param divisor the integer to divide by
     * @return the ceiling integer division result of numerator/divisor
     */
    private int ceilDivide(int numerator, int divisor)
    {
        return (numerator + divisor - 1)/divisor;
    }

    /**
     * Make a box identifier of the form "|6|7|8|..." to name a floor bracket box
     * @param lowestFloor the lowest floor in the floor bracket to be named
     * @param bracketSize the number of floors in the bracket
     * @return a floor bracket identifier composed of |-separated integer strings
     */
    private String makeFloorBracketID(int lowestFloor, int bracketSize)
    {
        StringBuilder stringBuilder = new StringBuilder();
        // Start the name with a |
        stringBuilder.append("|");
        // Go up the floors in the bracket and each time add "i|"
        for (int i=lowestFloor; i<lowestFloor+bracketSize; i++) {
            stringBuilder.append(i);
            stringBuilder.append("|");
        }
        // Return "|<i>|<i+1>|..."
        return stringBuilder.toString();
    }
}
