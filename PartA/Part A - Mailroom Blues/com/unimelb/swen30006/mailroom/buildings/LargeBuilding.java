/**
 * Author: Robert Holt
 * Last Modified: 2016-03-11
 */
package com.unimelb.swen30006.mailroom.buildings;

/**
 * A large building, satisfying the "large_building" command line option
 */
public class LargeBuilding extends Building
{
    // Building's minimum floor
    private static final int MIN_FLOOR          = 1;
    // Building's maximum floor
    private static final int MAX_FLOOR          = 200;

    // Building's maximum number of mailboxes
    private static final int MAX_BOXES          = 50;
    // Maximum storage units per mailbox
    private static final int MAX_MAIL_UNITS     = 40;

    // Floor the mail room is on
    private static final int MAIL_ROOM_LEVEL    = 2;
    // Number of mail delivery bots
    private static final int NUM_BOTS           = 20;

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
