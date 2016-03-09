/* SWEN30006 Software Modelling and Design 
 * Project 1 - Mailroom Blues
 * Author: Mathew Blair <mathew.blair@unimelb.edu.au>
 */
package com.unimelb.swen30006.mailroom;

import com.unimelb.swen30006.mailroom.samples.*;
import com.unimelb.swen30006.mailroom.strategies.delivery.RandomSelectionStrategy;
import com.unimelb.swen30006.mailroom.strategies.delivery.SortedByFloorDeliveryStrategy;
import com.unimelb.swen30006.mailroom.strategies.selection.FullnessSelectionStrategy;
import com.unimelb.swen30006.mailroom.strategies.sorting.RoomGroupedSortingStrategy;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A basic driver program to instantiate an instance of the MailSorter with an
 * ineffective strategy and the random mail generator.
 */
public class Simulation
{
    // Command-line arguments
    private static final String DETAIL      = "detailed";
    private static final String RAND        = "random";
    private static final String LGE_BUILD   = "large_building";
    private static final String MED_BUILD   = "medium_building";
    private static final String SML_BUILD   = "small_building";

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

    // Constants for our simulation mail generator
    private static final int MIN_FLOOR = -1;
    private static final int MAX_FLOOR = 20;
    private static final int NUM_MAIL = 200;

    // Constants for our mail storage unit
    private static final int MAX_BOXES = 5;
    private static final int MAX_MAIL_UNITS = 200;

    // The floor on which the mailroom resides
    private static final int MAIL_ROOM_LEVEL = 10;

    // The number of delivery bots
    private static final int NUM_BOTS = 1;

    // The default number of simulations
    private static final int NUM_RUNS = 10;

    // The size of building being simulated for mail delivery
    private enum BuildingSize {Small, Medium, Large, Unspecified}

    // Define building size attributes
    private static class LargeBuildingAttributes
    {
        // Constants for simulation mail generator
        public static final int MIN_FLOOR = 1;
        public static final int MAX_FLOOR = 200;

        // Constants for mail storage unit
        private static final int MAX_BOXES = 50;
        private static final int MAX_MAIL_UNITS = 20;

        // The floor on which the mailroom is found
        private static final int MAIL_ROOM_LEVEL = 2;

        // The number of delivery bots
        private static final int NUM_BOTS = 20;
    }

    private static class MediumBuildingAttributes
    {
        // Constants for simulation mail generator
        public static final int MIN_FLOOR = 1;
        public static final int MAX_FLOOR = 50;

        // Constants for mail storage unit
        private static final int MAX_BOXES = 10;
        private static final int MAX_MAIL_UNITS = 30;

        // The floor on which the mailroom is found
        private static final int MAIL_ROOM_LEVEL = 20;

        // The number of delivery bots
        private static final int NUM_BOTS = 10;
    }

    private static class SmallBuildingAttributes
    {
        // Constants for simulation mail generator
        public static final int MIN_FLOOR = 1;
        public static final int MAX_FLOOR = 10;

        // Constants for mail storage unit
        private static final int MAX_BOXES = 30;
        private static final int MAX_MAIL_UNITS = 40;

        // The floor on which the mailroom is found
        private static final int MAIL_ROOM_LEVEL = 10;

        // The number of delivery bots
        private static final int NUM_BOTS = 1;
    }

    public static void main(String args[])
    {
        // --- Command-line argument configuration ---

        // Declare variables to hold argument-configured settings
        int minFloor = MIN_FLOOR,
            maxFloor = MAX_FLOOR,
            numMail = NUM_MAIL,
            maxBoxes = MAX_BOXES,
            maxMailUnits = MAX_MAIL_UNITS,
            numBots = NUM_BOTS,
            mailRoomLevel = MAIL_ROOM_LEVEL,
            numRuns = NUM_RUNS;
        boolean isPredictable = true,
                printDetailed = false;
        BuildingSize buildingSize = BuildingSize.Unspecified;

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
                        isPredictable = false;
                        break;
                    case DETAIL:
                        printDetailed = true;
                        break;
                    case LGE_BUILD:
                        buildingSize = BuildingSize.Large;
                        break;
                    case MED_BUILD:
                        buildingSize = BuildingSize.Medium;
                        break;
                    case SML_BUILD:
                        buildingSize = BuildingSize.Small;
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
                        buildingSize = BuildingSize.Large;
                        break;
                    case MED_BUILD:
                        buildingSize = BuildingSize.Medium;
                        break;
                    case SML_BUILD:
                        buildingSize = BuildingSize.Small;
                        break;
                    case RAND:
                        isPredictable = false;
                        break;
                    default:
                        throw new IllegalArgumentException(BADARG_MSG);
                }
                switch (args[1]) {
                    case RAND:
                        isPredictable = false;
                        break;
                    case DETAIL:
                        printDetailed = true;
                        break;
                    default:
                        throw new IllegalArgumentException(BADARG_MSG);
                }
                break;

            // Expect "<runSim> <building> <rand> <detail>"
            case 3:
                switch (args[0]) {
                    case LGE_BUILD:
                        buildingSize = BuildingSize.Large;
                        break;
                    case MED_BUILD:
                        buildingSize = BuildingSize.Medium;
                        break;
                    case SML_BUILD:
                        buildingSize = BuildingSize.Small;
                        break;
                    default:
                        throw new IllegalArgumentException(BADARG_MSG);
                }
                if (RAND.equals(args[1]) && DETAIL.equals(args[2])) {
                    isPredictable = false;
                    printDetailed = true;
                    break;
                }
                // If we get here, 2nd and 3rd arguments are wrong
                throw new IllegalArgumentException(BADARG_MSG);
        }

        // --- End argument configuration ---

        // Configure the building attributes for simulation based on user selection
        switch (buildingSize) {
            case Large:
                minFloor = LargeBuildingAttributes.MIN_FLOOR;
                maxFloor = LargeBuildingAttributes.MAX_FLOOR;
                maxBoxes = LargeBuildingAttributes.MAX_BOXES;
                maxMailUnits = LargeBuildingAttributes.MAX_MAIL_UNITS;
                mailRoomLevel = LargeBuildingAttributes.MAIL_ROOM_LEVEL;
                numBots = LargeBuildingAttributes.NUM_BOTS;
                break;
            case Medium:
                minFloor = MediumBuildingAttributes.MIN_FLOOR;
                maxFloor = MediumBuildingAttributes.MAX_FLOOR;
                maxBoxes = MediumBuildingAttributes.MAX_BOXES;
                maxMailUnits = MediumBuildingAttributes.MAX_MAIL_UNITS;
                mailRoomLevel = MediumBuildingAttributes.MAIL_ROOM_LEVEL;
                numBots = MediumBuildingAttributes.NUM_BOTS;
                break;
            case Small:
                minFloor = SmallBuildingAttributes.MIN_FLOOR;
                maxFloor = SmallBuildingAttributes.MAX_FLOOR;
                maxBoxes = SmallBuildingAttributes.MAX_BOXES;
                maxMailUnits = SmallBuildingAttributes.MAX_MAIL_UNITS;
                mailRoomLevel = SmallBuildingAttributes.MAIL_ROOM_LEVEL;
                numBots = SmallBuildingAttributes.NUM_BOTS;
                break;
            default:
                // Use the default values already assigned
                break;
        }

        // Create the appropriate strategies
        SortingStrategy sortStrategy = new SimpleSortingStrategy();
        SelectionStrategy selectionStrategy = new RandomSelectionStrategy();
        DeliveryStrategy deliveryStrategy = new SortedByFloorDeliveryStrategy();

        // Run the simulation with the appropriate arguments
        runSimulation(minFloor, maxFloor, numMail, maxBoxes, maxMailUnits, numBots,
                mailRoomLevel, isPredictable, selectionStrategy, deliveryStrategy,
                sortStrategy, printDetailed, numRuns);
    }

    /**
     * A method to run a simulation given a set of parameters and strategies. Will handle running the multiple
     * simulation runs and averaging the results.
     * @param minFloor the minimum floor on the building
     * @param maxFloor the maximum floor on the building
     * @param numMail the number of mail items to simulation
     * @param maxBoxes the number of boxes allowed in the storage unit
     * @param maxMailUnits the size of each of the boxes in the storage unit (in mail units)
     * @param numBots the number of delivery bots servicing the building
     * @param mailLevel the level of the building that the mail room operates on
     * @param predictable whether to use predictable (fixed seed) mail generation or not. Setting this value to false
     *                    will use random seeds for each run. Setting it to true will result in the same values for each
     *                    run.
     * @param selectionStrategy the selection strategy to use for this simulation
     * @param deliveryStrategy the delivery strategy to use for this simulation
     * @param sortingStrategy the sorting strategy to use for this simulation
     * @param printDetailed whether or not you want the detailed output for each run. If true the console output will be
     *                      very verbose.
     * @param numRuns The number of simulation runs for this experiment. Will average the results over this many runs.
     */
    private static void runSimulation(int minFloor, int maxFloor, int numMail, int maxBoxes, int maxMailUnits,
                                      int numBots, int mailLevel, boolean predictable,
                                      SelectionStrategy selectionStrategy, DeliveryStrategy deliveryStrategy,
                                      SortingStrategy sortingStrategy, boolean printDetailed, int numRuns){

        // Setup variables for the simulation
        double totalTime = 0;
        double totalFloors = 0;
        double numDeliveries = 0;

        // Print detailed header if required
        if(printDetailed) {
            System.out.println("==========    DETAILED RUNS    ==========");
        }

        // Run the required number of simulations
        for(int i=0; i<numRuns; i++){
            // Setup Mail Generator
            MailItem.MailPriority[] priorities = MailItem.MailPriority.values();
            MailItem.MailType[] types = MailItem.MailType.values();
            MailSource generator = new SimpleMailGenerator(minFloor, maxFloor, priorities, types, numMail, predictable);

            // Setup storage
            MailStorage storage = new SimpleMailStorage(maxBoxes, maxMailUnits);

            // Setup MailSorter
            MailSorter sorter = new MailSorter(generator, storage,sortingStrategy);

            // Create the deliver bots
            DeliveryBot bots[] = new DeliveryBot[numBots];
            for(int k=0;  k<numBots;  k++){
                bots[k] = new DeliveryBot(selectionStrategy, deliveryStrategy, storage, mailLevel);
            }

            // Run the simulation
            boolean finished = false;
            while(!finished){
                // Update the sorter
                sorter.step();

                // Update all the delivery bots
                boolean anyBotBlocking = false;
                for(int b=0; b<numBots; b++){
                    bots[b].step();
                    anyBotBlocking = !bots[b].canFinish() || anyBotBlocking;
                }

                // Check if we are finished
                finished = sorter.canFinish() && !anyBotBlocking;
            }

            // Retrieve statistics
            ArrayList<DeliveryBot.DeliveryStatistic> stats = new ArrayList<>();
            for(int j=0; j<numBots; j++){
                DeliveryBot.DeliveryStatistic[] botStats = bots[j].retrieveStatistics();
                stats.addAll(Arrays.asList(botStats));
            }

            // Calculate averages and totals
            for(DeliveryBot.DeliveryStatistic stat : stats){
                totalTime += stat.timeTaken;
                totalFloors += stat.numFloors;
            }

            // Calculate statistics
            numDeliveries += stats.size();
            if(printDetailed) {
                System.out.println("======   Completed Run Number " + i + "    ======");

                for (DeliveryBot.DeliveryStatistic stat : stats) {
                    System.out.println(stat);
                }
                System.out.println("=========================================");
            }
        }

        // Average the results
        totalFloors = totalFloors /(double)numRuns;
        totalTime = totalTime /(double)numRuns;
        numDeliveries = numDeliveries /(double) numRuns;

        // Print the results
        System.out.println("========== SIMULATION COMPLETE ==========");
        System.out.println("");
        System.out.println("Delivered: " + numMail + " packages");
        System.out.println("Total Delivery Runs: " + numDeliveries);
        System.out.println("Total Time Taken: " + totalTime);
        System.out.println("Total Delivery Bots: " + numBots);
        System.out.println("Average Time Per Bots: " + totalTime/(double)numBots);
        System.out.println("Average Num Floors: " + totalFloors/numDeliveries);
        System.out.println("Average Num Packages: " + numMail/numDeliveries);
        System.out.println("");

    }
}
