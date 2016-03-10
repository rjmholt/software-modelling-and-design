package com.unimelb.swen30006.mailroom.strategies.sorting;

import com.unimelb.swen30006.mailroom.MailItem;
import com.unimelb.swen30006.mailroom.MailStorage;
import com.unimelb.swen30006.mailroom.SortingStrategy;
import com.unimelb.swen30006.mailroom.StorageBox;
import com.unimelb.swen30006.mailroom.exceptions.DuplicateIdentifierException;
import com.unimelb.swen30006.mailroom.exceptions.MailOverflowException;

/**
 * Created by rob on 10/03/16.
 */
public class EvenDistributionSortingStrategy implements SortingStrategy
{
    private final int numMailBoxes;
    private int currMailBox;

    public EvenDistributionSortingStrategy(int numMailBoxes)
    {
        this.numMailBoxes = numMailBoxes;
        this.currMailBox = 0;
    }

    @Override
    public String assignStorage(MailItem item, MailStorage storage) throws MailOverflowException
    {
        String id = Integer.toString(currMailBox);
        incrementBox();

        for (StorageBox.Summary summary: storage.retrieveSummaries()) {
            if (id.equals(summary.identifier)) {
                return id;
            }
        }

        try {
            storage.createBox(id);
            return id;
        }
        catch (DuplicateIdentifierException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void incrementBox() {
        this.currMailBox = (this.currMailBox + 1) % numMailBoxes;
    }
}
