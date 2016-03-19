/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Robert Holt
 * SID: 
 * Last Modified: 2016-03-11
 */

package com.unimelb.swen30006.mailroom.strategies.selection;

import com.unimelb.swen30006.mailroom.SelectionStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.NoBoxReadyException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Selects the fullest box in the list of summaries provided.
 */
public class FullnessSelectionStrategy implements SelectionStrategy
{
    @Override
    public String selectNextDelivery(StorageBox.Summary[] summaries) throws NoBoxReadyException
    {
        // If there are boxes available
        if (summaries.length > 0) {
            // Get the box with the most items in it and return it
            StorageBox.Summary maxBox = Collections.max(Arrays.asList(summaries), new BoxFullnessComparator());
            return maxBox.identifier;
        }
        // If there are no summaries, box is not ready
        throw new NoBoxReadyException();
    }

    /**
     * Compares two storage box summaries based on the number of items
     */
    private class BoxFullnessComparator implements Comparator<StorageBox.Summary>
    {
        /**
         * Compare two storage box summaries based on the number of items contained
         * @param s1 the first storage box summary to compare
         * @param s2 the second storage box summary to compare
         * @return 1 if s1 contains more items than s2, -1 if it contains less and 0 if the number is equal
         */
        public int compare(StorageBox.Summary s1, StorageBox.Summary s2)
        {
            return Integer.compare(s2.numItems, s1.numItems);
        }
    }
}
