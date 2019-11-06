package hw2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Planner {
    public static void main(String[] args) throws NumberFormatException, IOException {
        Topological courseGraph = new Topological();
        Scanner scanner = new Scanner(System.in);
        int maxCourcePerSemester = Integer.parseInt(args[0]);
        String fileName = args[1];

        courseGraph.loadGraph(fileName); // load the graph
        courseGraph.topologicalSort(); // sort the graph using topological sort
        String[] courceList = courseGraph.listShedule(maxCourcePerSemester); // shedule the cource acoding to the given maximun number

        for (int i = 0; i < 8; i++) {
            System.out.println(courceList[i]); // print the cource for each semester
        }
        while (true) {
            // reconstruct the unsorted adjacency matrix
            for (int i = 0; i < courseGraph.grapg.adjMatrix.length; i++) {
                for (int j = 0; j < courseGraph.grapg.adjMatrix.length; j++) {
                    courseGraph.grapg.adjMatrix[i][j] = courseGraph.grapg.copyAdj[i][j];
                }
            }
            System.out.print("Enter choice (0: Exit, 1: List schedule, 2: Check order, 3: change load):");
            int input = scanner.nextInt(); // user input integer


            if (input == 0) { // exit from the program..
                System.exit(-1);

            } else if (input == 1) { // same as the user input 3, but in this user can't enter new load
                System.out.println("The program will schedule " + maxCourcePerSemester + " courses per semester.");
                String[] array = courseGraph.listShedule(maxCourcePerSemester);
                for (int i = 0; i < 8; i++) {
                    System.out.println(array[i]);
                }
            } else if (input == 2) {
                String subject = "";
                System.out.print("Enter course: ");
                String test = scanner.nextLine();
                subject = scanner.nextLine();
                courseGraph.indexList.clear();
                courseGraph.checkOrder(subject);
                // System.out.println(courseGraph.indexList);
                List<Integer> list = new ArrayList<Integer>();
                System.out.print("You should take " + subject + " after ");
                for (int i = 0; i < courseGraph.indexList.size(); i++) {
                    int indexForCource = courseGraph.indexList.get(i); // get the index of cource
                    int indexOfSortedList = courseGraph.st.indexOf(indexForCource); // get the index of cource from sorted list
                    list.add(indexOfSortedList); // put that index in to list
                }
                Collections.sort(list); // sort the created list
                for (int i = 0; i < list.size(); i++) {
                    int x = courseGraph.st.get(list.get(i)); // get the cource index related to the cource after sorting
                    System.out.print(courseGraph.courseList.get(x)); // print the cource related to the above index
                    if (i != list.size() - 1) {
                        System.out.print(" -> ");
                    } else {
                        System.out.println(".");
                    }
                }

            } else if (input == 3) {
                System.out.print("Enter new course load: ");
                int newLoad = scanner.nextInt();
                maxCourcePerSemester = newLoad; // get the new load as a user input
                System.out.println("The program will schedule " + newLoad + " courses per semester.");
                String[] newLoadArray = courseGraph.listShedule(newLoad); // re-shedule the list for given load value
                for (int i = 0; i < 8; i++) {
                    System.out.println(newLoadArray[i]);
                }
                // when user enter a number that>=4, that is invalide user input.
            } else {
                System.out.println("Error: invalide input.");
            }
            // scanner.close();


        }

    }

}