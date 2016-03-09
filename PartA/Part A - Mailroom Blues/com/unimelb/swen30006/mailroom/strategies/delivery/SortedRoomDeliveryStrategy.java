package com.unimelb.swen30006.mailroom.strategies.delivery;

import com.unimelb.swen30006.mailroom.DeliveryStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.SourceExhaustedException;

/**
 * Reads the summary of the current delivery box and tries to optimise the
 * order of floors visited to suit the contents.
 */
public class SortedRoomDeliveryStrategy implements DeliveryStrategy
{
    @Override
    public int chooseNextFloor(int currentFloor, StorageBox box) throws SourceExhaustedException
    {
        return 0;
    }
}
