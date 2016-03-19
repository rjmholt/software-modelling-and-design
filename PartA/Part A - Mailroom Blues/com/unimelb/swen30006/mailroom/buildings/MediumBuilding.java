/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Robert Holt
 * SID: 
 * Last Modified: 2016-03-11
 */
package com.unimelb.swen30006.mailroom.buildings;

/**
 * A medium building, satisfying the "medium_building" command line option
 */
public class MediumBuilding extends Building
{
    // Building's minimum floor
    private static final int MIN_FLOOR          = 1;
    // Building's maximum floor
    private static final int MAX_FLOOR          = 50;

    // Building's maximum number of mailboxes
    private static final int MAX_BOXES          = 10;
    // Maximum storage units per mailbox
    private static final int MAX_MAIL_UNITS     = 30;

    // Floor the mail room is on
    private static final int MAIL_ROOM_LEVEL    = 20;
    // Number of mail delivery bots
    private static final int NUM_BOTS           = 10;

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
