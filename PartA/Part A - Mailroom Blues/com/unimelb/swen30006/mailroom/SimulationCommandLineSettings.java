/* SWEN30006 Software Modelling and Design
 * Project 1 - Mailroom Blues
 * Author: Robert Holt
 * SID: 
 * Last Modified: 2016-03-11
 */
package com.unimelb.swen30006.mailroom;

import com.unimelb.swen30006.mailroom.buildings.Building;

/**
 * Takes in and parses commandline arguments to be settings for the Simulation class
 */
public class SimulationCommandLineSettings implements CommandLineSettings
{
    // --- COMMAND LINE ARGUMENT STRINGS ---
    // Use new randomness seed each time; not predictable
    private final String RAND = "random";
    //  Print detailed simulation log data
    private final String DETAIL = "detailed";

    // Select which building type to simulate
    private final String LGE_BUILD = "large_building";
    private final String MED_BUILD = "medium_building";
    private final String SML_BUILD = "small_building";

    // --------------------------------------

    // Error message to print on bad arguments
    private static final String BADARG_MSG = "\n*** Unsupported Commandline Argument ***\n\n"
            + "Accepted arguments are:\n"
            + "\t(large|medium|small)_building - Specifies the building size to simulate\n"
            + "\trandom - Uses a different seed for the randomness generation instead of the hardcoded one\n"
            + "\tdetailed - Print verbose reporting for the simulation\n"
            + "\nThe required ordering is:\n"
            + "\tbuilding random detailed\n"
            + "Optional arguments are:\n"
            + "\trandom detailed\n";

    // Set true by "detail" command line argument
    private boolean printDetailed;
    // Set false by "random" command line argument
    private boolean isPredictable;
    // Set by "(small|medium|large)_building" to corresponding enum
    private Building.BuildingType buildingType;

    // Constructor takes default values
    public SimulationCommandLineSettings(boolean printDetailed, boolean isPredictable,
                                         Building.BuildingType buildingType)
    {
        this.printDetailed  = printDetailed;
        this.isPredictable  = isPredictable;
        this.buildingType   = buildingType;
    }

    @Override
    public void takeArguments(String[] args)
    {
        // Possible arguments are:
        //      <building>  = "(large|medium|small)_building"
        //      <rand>      = "random"
        //      <detail>    = "detailed"
        // <building> must be first. For compatibility with the original simulation, it is optional
        // <rand> may optionally be second
        // <detail> always comes last and is optional
        // The corresponding regex is then:
        //      <runSim> (<detail> | <rand> <detail>? | <building> (<rand <detail>?)?)?
        switch (args.length) {
            case 0:
                break;

            // Expect "<runSim> <building>" | "<runSim> <rand>" | "<runSim> <detail>"
            case 1:
                switch (args[0]) {
                    case RAND:
                        this.isPredictable = false;
                        break;
                    case DETAIL:
                        this.printDetailed = true;
                        break;
                    case LGE_BUILD:
                        this.buildingType = Building.BuildingType.Large;
                        break;
                    case MED_BUILD:
                        this.buildingType = Building.BuildingType.Medium;
                        break;
                    case SML_BUILD:
                        this.buildingType = Building.BuildingType.Small;
                        break;
                    default:
                        throw new IllegalArgumentException(BADARG_MSG);
                }
                break;

            // Expect "<runSim> <building>  <rand>"
            //      | "<runSim> <building>  <detail>"
            //      | "<runSim> <random>    <detail>"
            case 2:
                switch (args[0]) {
                    case LGE_BUILD:
                        this.buildingType = Building.BuildingType.Large;
                        break;
                    case MED_BUILD:
                        this.buildingType = Building.BuildingType.Medium;
                        break;
                    case SML_BUILD:
                        this.buildingType = Building.BuildingType.Small;
                        break;
                    case RAND:
                        this.isPredictable = false;
                        break;
                    default:
                        throw new IllegalArgumentException(BADARG_MSG);
                }
                switch (args[1]) {
                    case RAND:
                        this.isPredictable = false;
                        break;
                    case DETAIL:
                        this.printDetailed = true;
                        break;
                    default:
                        throw new IllegalArgumentException(BADARG_MSG);
                }
                break;

            // Expect "<runSim> <building> <rand> <detail>"
            case 3:
                switch (args[0]) {
                    case LGE_BUILD:
                        this.buildingType = Building.BuildingType.Large;
                        break;
                    case MED_BUILD:
                        this.buildingType = Building.BuildingType.Medium;
                        break;
                    case SML_BUILD:
                        this.buildingType = Building.BuildingType.Small;
                        break;
                    default:
                        throw new IllegalArgumentException(BADARG_MSG);
                }
                if (RAND.equals(args[1]) && DETAIL.equals(args[2])) {
                    this.isPredictable = false;
                    this.printDetailed = true;
                    break;
                }
                // If we get here, 2nd and 3rd arguments are wrong
                throw new IllegalArgumentException(BADARG_MSG);
        }
    }

    public Building.BuildingType getBuildingType() {
        return this.buildingType;
    }

    public boolean isPredictable() {
        return this.isPredictable;
    }

    public boolean printDetailed() {
        return this.printDetailed;
    }
}
