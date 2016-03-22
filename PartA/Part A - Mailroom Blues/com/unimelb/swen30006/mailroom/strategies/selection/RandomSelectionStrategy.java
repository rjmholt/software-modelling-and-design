/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Robert Holt
 * SID: 
 * Last Modified: 2016-03-22
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
    private Random randomiser;

    public RandomSelectionStrategy()
    {
        this.randomiser = new Random();
    }

    public RandomSelectionStrategy(long seed)
    {
        this.randomiser = new Random(seed);
    }

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
            // Pick a box at random by index
            StorageBox.Summary selection = summaries[this.randomiser.nextInt(summaries.length)];
            return selection.identifier;
        }
        // No boxes ready to select
        throw new NoBoxReadyException();
    }
}
