/* Author: Robert Holt
 * Last Modified: 2016-03-11
 */

package com.unimelb.swen30006.mailroom.strategies.selection;

import com.unimelb.swen30006.mailroom.SelectionStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.NoBoxReadyException;

import java.util.Random;

/**
 * Picks a box to deliver from by randomly selecting one from the list
 */
public class RandomSelectionStrategy implements SelectionStrategy
{
    /**
     * Takes the list of summaries and picks one of them at random using a randomised
     * index of the array.
     * @param summaries the summaries of the storage box summary
     * @return the identifier of the box to deliver
     * @throws NoBoxReadyException
     */
    @Override
    public String selectNextDelivery(StorageBox.Summary[] summaries) throws NoBoxReadyException
    {
        // Make sure there are boxes to deliver
        if (summaries.length > 0) {
            Random randomiser = new Random();
            // Pick a box at random by index
            StorageBox.Summary selection = summaries[randomiser.nextInt(summaries.length)];
            return selection.identifier;
        }
        // No boxes ready to select
        throw new NoBoxReadyException();
    }
}
