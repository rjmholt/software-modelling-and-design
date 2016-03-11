/**
 * Author: Robert Holt
 * Last Modified: 2016-03-11
 */
package com.unimelb.swen30006.mailroom.buildings;

/**
 * A small building, satisfying the "small_building" command line option
 */
public class SmallBuilding extends Building
{
    // Building's minimum floor
    private static final int MIN_FLOOR          = 1;
    // Building's maximum floor
    private static final int MAX_FLOOR          = 10;

    // Building's maximum number of mailboxes
    private static final int MAX_BOXES          = 30;
    // Maximum storage units per mailbox
    private static final int MAX_MAIL_UNITS     = 40;

    // Floor the mail room is on
    private static final int MAIL_ROOM_LEVEL    = 10;
    // Number of mail delivery bots
    private static final int NUM_BOTS           = 1;

    @Override
    public int getMinFloor() {
        return MIN_FLOOR;
    }

    @Override
    public int getMaxFloor() {
        return MAX_FLOOR;
    }

    @Override
    public int getMaxBoxes() {
        return MAX_BOXES;
    }

    @Override
    public int getMaxMailUnits() {
        return MAX_MAIL_UNITS;
    }

    @Override
    public int getMailRoomLevel() {
        return MAIL_ROOM_LEVEL;
    }

    @Override
    public int getNumBots() {
        return NUM_BOTS;
    }
}

