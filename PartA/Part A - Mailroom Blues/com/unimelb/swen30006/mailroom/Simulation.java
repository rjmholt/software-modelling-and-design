/* SWEN30006 Software Modelling and Design 
 * Project 1 - Mailroom Blues
 * Author: Mathew Blair <mathew.blair@unimelb.edu.au>
 */
package com.unimelb.swen30006.mailroom;

import com.unimelb.swen30006.mailroom.samples.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A basic driver program to instantiate an instance of the MailSorter with an
 * ineffective strategy and the random mail generator.
 */
public class Simulation {

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

    public static void main(String args[]) {

        // Create the appropriate strategies
        SortingStrategy sortStrategy = new SimpleSortingStrategy();
        SelectionStrategy selectionStrategy = new SimpleSelectionStrategy();
        DeliveryStrategy deliveryStrategy = new SimpleDeliveryStrategy();

        // Extract whether to print detailed runs or not
        boolean printDetailed = (args.length>0 && args[0].equals("detailed"));

        // Run the simulation with the appropriate arguments
        runSimulation(MIN_FLOOR, MAX_FLOOR, NUM_MAIL, MAX_BOXES, MAX_MAIL_UNITS, NUM_BOTS,
                MAIL_ROOM_LEVEL, true, selectionStrategy, deliveryStrategy, sortStrategy, printDetailed, NUM_RUNS);
    }

    /**
     * A method to run a simulation given a set of parameters and strategies. Will handle running the multiple
     * simulation runs and averaging the results.
     * @param minFloor the minimum floor on the building
     * @param maxFloor the maxium floor on the building
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
            ArrayList<DeliveryBot.DeliveryStatistic> stats = new ArrayList<DeliveryBot.DeliveryStatistic>();
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
        System.out.println("Average Num Floors: " + totalFloors/(double)numDeliveries);
        System.out.println("Average Num Packages: " + numMail/(double)numDeliveries);
        System.out.println("");

    }
}