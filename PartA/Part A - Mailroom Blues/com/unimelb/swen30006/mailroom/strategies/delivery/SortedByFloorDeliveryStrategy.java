/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Robert Holt
 * SID: 
 * Last Modified: 2016-03-11
 */
package com.unimelb.swen30006.mailroom.strategies.delivery;

import com.unimelb.swen30006.mailroom.DeliveryStrategy;
import com.unimelb.swen30006.mailroom.MailItem;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.BoxEmptyException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;
import com.unimelb.swen30006.mailroom.exceptions.SourceExhaustedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Tries to optimise the delivery of packages in the current box,
 * by sorting them in ascending order of floor and going to the nearest floor.
 * Note that it is highly computationally inefficient, sorting the box every step.
 */
public class SortedByFloorDeliveryStrategy implements DeliveryStrategy
{
    @Override
    public int chooseNextFloor(int currentFloor, StorageBox box) throws SourceExhaustedException
    {
        // Get the number of items in the box
        int numMailItems = box.numPackages();
        // Allocate ArrayLists for both
        ArrayList<MailItem> mailItems = new ArrayList<>(numMailItems);
        ArrayList<Integer> deliveryFloors = new ArrayList<>(numMailItems);

        // While the box isn't empty, pull items out of it and record their floors
        while (!box.isEmpty()) {
            MailItem item = null;
            try {
                item = box.popItem();
            }
            catch (BoxEmptyException e) {
                // If the box doesn't report empty but we get an EmptyBoxException, error
                e.printStackTrace();
                System.exit(0);
            }
            deliveryFloors.add(item.floor);
                mailItems.add(item);
        }
        // Now sort the items in order of their floor
        Collections.sort(mailItems, new MailFloorComparator());
        // And put them back in the box
        for (MailItem mailItem: mailItems) {
            try {
                box.addItem(mailItem);
            }
            catch (MailOverflowException e) {
                // If the box doesn't fit the objects we removed from it, error
                e.printStackTrace();
                System.exit(0);
            }
        }
        int destination = Collections.min(deliveryFloors);
        return destination;
    }

    /**
     * Compares two mail items by floor, so that sorting with this Comparator
     * returns boxes in ascending order of floor.
     */
    private class MailFloorComparator implements Comparator<MailItem>
    {
        @Override
        public int compare(MailItem i1, MailItem i2)
        {
            return Integer.compare(i1.floor, i2.floor);
        }
    }
}
