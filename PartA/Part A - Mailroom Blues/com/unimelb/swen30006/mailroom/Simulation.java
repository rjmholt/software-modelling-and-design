/* SWEN30006 Software Modelling and Design 
 * Project 1 - Mailroom Blues
 * Author: Mathew Blair <mathew.blair@unimelb.edu.au>
 */
package com.unimelb.swen30006.mailroom;

import com.unimelb.swen30006.mailroom.buildings.Building;
import com.unimelb.swen30006.mailroom.buildings.BuildingFactory;
import com.unimelb.swen30006.mailroom.samples.*;
import com.unimelb.swen30006.mailroom.strategies.delivery.RandomSelectionStrategy;
import com.unimelb.swen30006.mailroom.strategies.delivery.SortedByFloorDeliveryStrategy;
import com.unimelb.swen30006.mailroom.strategies.sorting.RandomSortingStrategy;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A basic driver program to instantiate an instance of the MailSorter with an
 * ineffective strategy and the random mail generator.
 */
public class Simulation
{
    // Default settings to use when command line areguments are not provided
    private static boolean PRINT_DETAILED_DEFAULT = false;
    private static boolean IS_PREDICTABLE_DEFAULT = true;
    private static Building.BuildingType BUILDING_TYPE_DEFAULT = Building.BuildingType.Unspecified;

    private static int NUM_RUNS = 10;

    public static void main(String args[])
    {
        SimulationCommandLineSettings cmdLineSettings =
                new SimulationCommandLineSettings(PRINT_DETAILED_DEFAULT,
                                                  IS_PREDICTABLE_DEFAULT,
                                                  BUILDING_TYPE_DEFAULT);
        cmdLineSettings.takeArguments(args);

        BuildingFactory buildingFactory = new BuildingFactory();
        Building testBuilding = buildingFactory.getNewBuilding(cmdLineSettings.getBuildingType());

        // Create the appropriate strategies
        SortingStrategy sortStrategy = new RandomSortingStrategy(testBuilding.getMaxBoxes());
        SelectionStrategy selectionStrategy = new RandomSelectionStrategy();
        DeliveryStrategy deliveryStrategy = new SortedByFloorDeliveryStrategy();

        // Run the simulation with the appropriate arguments
        runSimulation(testBuilding, cmdLineSettings.isPredictable(), selectionStrategy,
                      deliveryStrategy, sortStrategy, cmdLineSettings.printDetailed(), NUM_RUNS);
    }

    /**
     * A method to run a simulation given a set of parameters and strategies. Will handle running the multiple
     * simulation runs and averaging the results.
     * @param building the building that the simulation will operate on
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
    private static void runSimulation(Building building, boolean predictable,
                                      SelectionStrategy selectionStrategy, DeliveryStrategy deliveryStrategy,
                                      SortingStrategy sortingStrategy, boolean printDetailed, int numRuns)
    {
        // Get building parameters
        int minFloor        = building.getMinFloor();
        int maxFloor        = building.getMaxFloor();
        int numMail         = building.getMaxMailUnits();
        int maxBoxes        = building.getMaxBoxes();
        int maxMailUnits    = building.getMaxMailUnits();
        int numBots         = building.getNumBots();
        int mailLevel       = building.getMailRoomLevel();

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
