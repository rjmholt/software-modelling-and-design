package com.unimelb.swen30006.mailroom.buildings;

/**
 * Produces a Building object given input parameters
 */
public class BuildingFactory
{
    public Building getDefaultBuilding()
    {
        return new Building();
    }

    public Building getNewBuilding(Building.BuildingType type)
    {
        switch (type) {
            case Large:
                return new SmallBuilding();
            case Medium:
                return new MediumBuilding();
            case Small:
                return new SmallBuilding();
            default:
                return getDefaultBuilding();
        }
    }
}
