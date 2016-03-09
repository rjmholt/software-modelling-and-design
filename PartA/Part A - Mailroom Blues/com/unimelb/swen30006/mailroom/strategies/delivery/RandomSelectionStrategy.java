package com.unimelb.swen30006.mailroom.strategies.delivery;

import com.unimelb.swen30006.mailroom.SelectionStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.NoBoxReadyException;

import java.util.Random;

/**
 * Created by rob on 9/03/16.
 */
public class RandomSelectionStrategy implements SelectionStrategy
{
    @Override
    public String selectNextDelivery(StorageBox.Summary[] summaries) throws NoBoxReadyException
    {
        if (summaries.length > 0) {
            Random randomiser = new Random();
            StorageBox.Summary selection = summaries[randomiser.nextInt(summaries.length)];
            return selection.identifier;
        }
        // No boxes ready to select
        throw new NoBoxReadyException();
    }
}
