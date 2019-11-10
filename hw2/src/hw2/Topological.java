package hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//-----------------------------------------------------
// Title: Topological class
// Author: Oguzhan Ugur Sarisakaloglu
// ID: 39274105326
// Section: 1
// Assignment: 2
// Description: This class we store graph and vertex class and we can provide topological sort in here.
//-----------------------------------------------------

class Vertex {
    public String key;

    public Vertex(String ch) {
        key = ch;
    }
}

class Graph {
    private Vertex vertexList[];
    private int nVertex;
    public int adjMatrix[][];
    public int copyAdj[][];

    public Graph(int size) {
        vertexList = new Vertex[size];
        adjMatrix = new int[size][size];
        copyAdj = new int[size][size];
        nVertex = 0;
    }

    public void addVertex(String labe) {
        vertexList[nVertex++] = new Vertex(labe);
    }

    public void addEdge(int start, int end) {
        adjMatrix[start][end] = 1;
    }
}

public class Topological {

    public List<String> courseList = new ArrayList<String>();
    public Stack<Integer> st = new Stack<Integer>(); // that stores he reverse
    public Graph dag;
    public List<Integer> indexList = new ArrayList<Integer>();

    //  load the graph
    public void loadGraph(String filename) throws NumberFormatException, IOException {


        String input_file_path= filename;

        System.out.println(filename);

        ClassLoader classLoader = Topological.class.getClassLoader();

        File input_file = new File(classLoader.getResource(filename).getFile());

        String fileName = classLoader.getResource(input_file_path).getFile();

        System.out.println(fileName);


        BufferedReader br = new BufferedReader(new FileReader(input_file.getAbsolutePath()));

        String line = null;
        String courseName = null;

        int maxCount = Integer.parseInt(br.readLine());

        //  first create list of vertices
        int count = 0;
        try {
            while (count < maxCount) {
                line = br.readLine();
                courseName = line.split(" ")[0];
                courseList.add(courseName);
                count++;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        br.close(); // close buffer reader, program has all of courses


        //  create a Graph object with maxCount
        dag = new Graph(maxCount);


        // create vertex for all courses in input, to the graph
        for (int i = 0; i < courseList.size(); i++) {
            dag.addVertex(courseList.get(i));
        }




        //  create new reader object to set edges between verises.
        BufferedReader bfr = new BufferedReader(new FileReader(input_file.getAbsolutePath()));
        String xx = bfr.readLine(); // ignore the that value.

        // create an adjacency matrix with connected edges.
        int addEdgeCount = 0;
        while (addEdgeCount < maxCount) {
            line = bfr.readLine();
            int endPoint = courseList.indexOf(line.split(" ")[0]);
            int i = 1; /// if it is 0, program gets name of course
            String[] split = line.split(" ");
            while (i < split.length) {
                int startPoint = courseList.indexOf(split[i]);
                dag.addEdge(startPoint, endPoint); // create an edge
                i++;
            }
            addEdgeCount++;
        }
        bfr.close(); // close the reader
    }

    //  topological sort this sorted list will be stored in stack.
    public void topologicalSort() {

        //  create a temporary adjacency matrix for future work.
        //   because when creating a sorted list adjecency matrix will be changed.
        for (int i = 0; i < dag.adjMatrix.length; i++) {
            for (int j = 0; j < dag.adjMatrix.length; j++) {
                dag.copyAdj[i][j] = dag.adjMatrix[i][j];
            }
        }


        // Process nodes in a topologically sorted order,
        // sort them with topological sort
        for (int z = 0; z < dag.adjMatrix.length; z++) {

            for (int i = 0; i < dag.adjMatrix.length; i++) {

                boolean test = false;

                for (int j = 0; j < dag.adjMatrix.length; j++) {
                    if (dag.adjMatrix[j][i] == 1) {
                        test = true;
                    }
                    // System.out.print(dag.adjMatrix[i][j] + " ");
                }
                if (test == false) {
                    int x = st.search(i);
                    if (x == -1) {
                        st.push(i);
                    }
                }
            }

            for (int m = 0; m < st.size(); m++) {
                // System.out.println(temp);
                for (int k = 0; k < dag.adjMatrix.length; k++) {
                    dag.adjMatrix[st.get(m)][k] = 0;
                }
            }

        }




    }

    //  list schedule.....//
    public String[] listShedule(int maxNumberOfCourses) {
        String[] list = new String[8];
        for (int i = 0; i < st.size(); i++) {
        }
        //  re-create adjcency matrix usinf copyAdj matrix
        for (int i = 0; i < dag.adjMatrix.length; i++) {
            for (int j = 0; j < dag.adjMatrix.length; j++) {
                dag.adjMatrix[i][j] = dag.copyAdj[i][j];
            }
        }

        int textc = 0;

        for (int i = 0; i < 8; i++) {


            String temp = "";
            int currentSemester = 0;
            List<Integer> tempCoords = new ArrayList<Integer>();
            temp = temp + "semester " + String.valueOf(i + 1) + " : ";
            
            
            
            for (int courseCount = textc; courseCount < st.size(); courseCount++) {
                boolean check = false;
                int num = st.get(courseCount);
                for (int col = 0; col < dag.adjMatrix.length; col++) {
                    if (dag.adjMatrix[col][num] == 1) {
                        check = true;

                    }
                }
                if (check == false && currentSemester < maxNumberOfCourses) {
                    if (!tempCoords.contains(st.get(courseCount))) {
                        tempCoords.add(st.get(courseCount));
                    }
                    temp = temp + courseList.get(st.get(courseCount)) + " ";
                    currentSemester++;
                    textc++;
                }

            }
            list[i] = temp;
            for (int x = 0; x < tempCoords.size(); x++) {
                int z = tempCoords.get(x);
                for (int y = 0; y < dag.adjMatrix.length; y++) {
                    dag.adjMatrix[z][y] = 0;
                }
            }

        }
        return list;
    }

    //   In this class we find required courses by recursively then put them into indexlist one by one.
    public void checkOrder(String v) {

        // find the index of course v i courseList
        // int point = courseList.indexOf(v);

        for (int i = 0; i < dag.adjMatrix.length; i++) {

            if (dag.adjMatrix[i][courseList.indexOf(v)] == 1) {

                // There is edge in i to point

                if (!indexList.contains(i)) { // if found 1, and index for that
                    // 1 is not contained on indexList, put it in to indexList
                    indexList.add(i);
                }

                // call same method for required course
                checkOrder(courseList.get(i));


            }

        }

    }

}