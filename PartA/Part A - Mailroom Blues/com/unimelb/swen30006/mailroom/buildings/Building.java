/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Robert Holt
 * SID: 
 * Last Modified: 2016-03-11
 */
package com.unimelb.swen30006.mailroom.buildings;

/**
 * A building in which mail delivery is simulated
 */
public class Building
{
    // The types/sizes of building supported
    public enum BuildingType {Small, Medium, Large, Unspecified}

    // Building's minimum floor
    private static final int MIN_FLOOR          = -1;
    // Building's maximum floor
    private static final int MAX_FLOOR          = 20;
    // Building's maximum number of mailboxes
    private static final int MAX_BOXES          = 5;
    // Maximum storage units per mailbox
    private static final int MAX_MAIL_UNITS     = 200;
    // Floor the mail room is on
    private static final int MAIL_ROOM_LEVEL    = 10;
    // Number of mail delivery bots
    private static final int NUM_BOTS           = 1;

    // The minimum floor of the building
    public int getMinFloor()
    {
        return MIN_FLOOR;
    }
    // The maximum floor of the building
    public int getMaxFloor()
    {
        return MAX_FLOOR;
    }

    // The maximum number of mail boxes allowed in the building
    public int getMaxBoxes()
    {
        return MAX_BOXES;
    }
    // The maximum number of mail units each box can fit
    public int getMaxMailUnits()
    {
        return MAX_MAIL_UNITS;
    }

    // The building level where the mail room is
    public int getMailRoomLevel()
    {
        return MAIL_ROOM_LEVEL;
    }

    // The number of mail delivery robots servicing the building
    public int getNumBots()
    {
        return NUM_BOTS;
    }
}
