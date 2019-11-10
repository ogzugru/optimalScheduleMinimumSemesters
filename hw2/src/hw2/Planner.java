package hw2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//-----------------------------------------------------
// Title: Planner class
// Author: Oguzhan Ugur Sarisakaloglu
// ID: 39274105326
// Section: 1
// Assignment: 2
// Description: This class tests the assignment and gets inputs
// and returns results by connecting topological.java.
//-----------------------------------------------------
public class Planner {
    public static void main(String[] args) throws NumberFormatException, IOException {


        // create Empty Topological Object
        Topological courseGraph = new Topological();

        // create scanner
        Scanner scanner = new Scanner(System.in);

        // get input for maximum course count in per semester
        int maxCoursePerSemester = Integer.parseInt(args[0]);

        // initialize finput file name
        String fileName = args[1];

        // load the graph according to input file
        courseGraph.loadGraph(fileName);

        // sort the graph using topological sort
        courseGraph.topologicalSort();

        // schedule the courses according to the given limit of course
        String[] courceList = courseGraph.listShedule(maxCoursePerSemester);

        for (int i = 0; i < 8; i++) {

            // print the courses for 8 semesters
            System.out.println(courceList[i]);
        }

        while (true) {

            // reconstruct the unsorted adjacency matrix
            for (int i = 0; i < courseGraph.dag.adjMatrix.length; i++) {
                for (int j = 0; j < courseGraph.dag.adjMatrix.length; j++) {
                    courseGraph.dag.adjMatrix[i][j] = courseGraph.dag.copyAdj[i][j];
                }
            }


            System.out.print("Enter choice (0: Exit, 1: List schedule, 2: Check order, 3: change load):");
            int input = scanner.nextInt(); // user input integer

            // exit from the program
            if (input == 0) {
                System.exit(-1);

            } else if (input == 1) { // same as the user input 3, but in this user can't enter new load
                System.out.println("The program will schedule " + maxCoursePerSemester + " courses per semester.");

                // return results from constructed courseGraph object.
                String[] array = courseGraph.listShedule(maxCoursePerSemester);

                // then print them
                for (int i = 0; i < 8; i++) {
                    System.out.println(array[i]);
                }

            } else if (input == 2) { // print schedule


                String subject = "";

                System.out.print("Enter course: ");

                // Get course name which checked
                subject = scanner.next();

                // We need free indexList in courseGraph object
                courseGraph.indexList.clear();

                // by using checkOrder Method use all required courses
                courseGraph.checkOrder(subject);

                //System.out.println(courseGraph.indexList);

                List<Integer> tempListSortedIndexOfCourses = new ArrayList<Integer>();

                System.out.print("You should take " + subject + " after ");

                for (int i = 0; i < courseGraph.indexList.size(); i++) {

                    // get the index of course from sorted list
                    int indexOfSortedList = courseGraph.st.indexOf(courseGraph.indexList.get(i));

                    tempListSortedIndexOfCourses.add(indexOfSortedList); // put that index into to list


                }


                Collections.sort(tempListSortedIndexOfCourses); // sort the created list
                for (int i = 0; i < tempListSortedIndexOfCourses.size(); i++) {
                    int x = courseGraph.st.get(tempListSortedIndexOfCourses.get(i)); // get the cource index related to the cource after sorting
                    System.out.print(courseGraph.courseList.get(x)); // print the cource related to the above index
                    if (i != tempListSortedIndexOfCourses.size() - 1) {
                        System.out.print(" -> ");
                    } else {
                        System.out.println(".");
                    }
                }

            } else if (input == 3) {


                System.out.print("Enter new course load: ");

                int newLoad = scanner.nextInt();

                maxCoursePerSemester = newLoad; // get the new load as a user input

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
