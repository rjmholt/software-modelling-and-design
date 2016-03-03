/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Mathew Blair <mathew.blair@unimelb.edu.au>
 */
package com.unimelb.swen30006.mailroom.samples;

import com.unimelb.swen30006.mailroom.DeliveryStrategy;
import com.unimelb.swen30006.mailroom.MailItem;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.SourceExhaustedException;

/**
 * A very simple implementation of a delivery strategy, doesn't attempt to do
 * anything clever, will simply pop the top item off the stack and use that floor
 * as the delivery zone.
 */
public class SimpleDeliveryStrategy implements DeliveryStrategy {

    @Override
    public int chooseNextFloor(int currentFloor, StorageBox box) throws SourceExhaustedException {
        int destination = 0;
        // Try and peek at the item on top of the box
        try {
            MailItem item = box.popItem();
            destination = item.floor;
            box.addItem(item);
        } catch (Exception e){
            System.out.println(e);
            System.exit(0);
        }

        return destination;
    }

}
