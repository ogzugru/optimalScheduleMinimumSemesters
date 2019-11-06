package hw2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Vertex {
 public String lable;

 public Vertex(String ch) {
  lable = ch;
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

 public List < String > courseList = new ArrayList < String > ();
 public Stack < Integer > st = new Stack < Integer > ();
 public Graph grapg;
 public List < Integer > indexList = new ArrayList < Integer > ();
 //  load the graph 
 public void loadGraph(String filename) throws NumberFormatException, IOException {
  BufferedReader br = new BufferedReader(new FileReader(filename));
  String line = null;
  String courceName = null;
  int maxCount = Integer.parseInt(br.readLine());
  grapg = new Graph(maxCount);

  //  first create verises list
  int count = 0;
  try {
   while (count < maxCount) {
    line = br.readLine();
    courceName = line.split(" ")[0];
    courseList.add(courceName);
    count++;
   }

  } catch (Exception e) {
   System.out.println("Error: " + e);
  }
  br.close();
  //  create a Grapg object...


  // add vertises for all cources to the graph
  for (int i = 0; i < courseList.size(); i++) {
   grapg.addVertex(courseList.get(i));
  }
  //  create new reader object to set edges between verises.
  BufferedReader bfr = new BufferedReader(new FileReader(filename));
  String xx = bfr.readLine(); // ignore the that value.

  // creat an adjecency matrix with connectionf edges.
  int addEdgeCount = 0;
  while (addEdgeCount < maxCount) {
   line = bfr.readLine();
   int endPoint = courseList.indexOf(line.split(" ")[0]);
   int i = 1;
   String[] split = line.split(" ");
   while (i < split.length) {
    int startPoint = courseList.indexOf(split[i]);
    grapg.addEdge(startPoint, endPoint); // creat an adge
    i++;
   }
   addEdgeCount++;
  }
  bfr.close(); // close the reader
 }
 //  topological sort this sorted list will store in stack.
 public void topologicalSort() {

  //  create a tempory adjecency matrix for future work.
  //   because when creating a sorted list adjecency matrix will be changed.
  for (int i = 0; i < grapg.adjMatrix.length; i++) {
   for (int j = 0; j < grapg.adjMatrix.length; j++) {
    grapg.copyAdj[i][j] = grapg.adjMatrix[i][j];
   }
  }
  for (int z = 0; z < grapg.adjMatrix.length; z++) {
   for (int i = 0; i < grapg.adjMatrix.length; i++) {
    boolean test = false;
    for (int j = 0; j < grapg.adjMatrix.length; j++) {
     if (grapg.adjMatrix[j][i] == 1) {
      test = true;
     }
     // System.out.print(grapg.adjMatrix[i][j] + " ");
    }
    if (test == false) {
     int x = st.search(i);
     if (x == -1) {
      st.push(i);
     }
    }
   }
   for (int m = 0; m < st.size(); m++) {
    int temp = st.get(m);
    // System.out.println(temp);
    for (int k = 0; k < grapg.adjMatrix.length; k++) {
     grapg.adjMatrix[temp][k] = 0;
    }
   }

  }
 }
 //  list shedule.....//
 public String[] listShedule(int maxNumberOfCourses) {
  String[] list = new String[8];
  for (int i = 0; i < st.size(); i++) {}
  //  re-create adjcency matrix usinf copyAdj matrix
  for (int i = 0; i < grapg.adjMatrix.length; i++) {
   for (int j = 0; j < grapg.adjMatrix.length; j++) {
    grapg.adjMatrix[i][j] = grapg.copyAdj[i][j];
   }
  }

  int textc = 0;
  for (int i = 0; i < 8; i++) {
   String temp = "";
   int cCount = 0;
   List < Integer > myCoords = new ArrayList < Integer > ();
   temp = temp + "semester " + String.valueOf(i + 1) + " : ";
   for (int courceCount = textc; courceCount < st.size(); courceCount++) {
    boolean check = false;
    int num = st.get(courceCount);
    for (int col = 0; col < grapg.adjMatrix.length; col++) {
     if (grapg.adjMatrix[col][num] == 1) {
      check = true;

     }
    }
    if (check == false && cCount < maxNumberOfCourses) {
     if (!myCoords.contains(st.get(courceCount))) {
      myCoords.add(st.get(courceCount));
     }
     temp = temp + courseList.get(st.get(courceCount)) + " ";
     cCount++;
     textc++;
    }

   }
   list[i] = temp;
   for (int x = 0; x < myCoords.size(); x++) {
    int z = myCoords.get(x);
    for (int y = 0; y < grapg.adjMatrix.length; y++) {
     grapg.adjMatrix[z][y] = 0;
    }
   }

  }
  return list;
 }
 //   check the order that given subject how to do with fre-requ..
 public void checkOrder(String v) {

  int point = courseList.indexOf(v); // find the index for input subject

  for (int i = 0; i < grapg.adjMatrix.length; i++) { // go through top to bottom and find 1's
   if (grapg.adjMatrix[i][point] == 1) {
    if (!indexList.contains(i)) { // if found 1, and index for that 1 is not exist on indexList,
     indexList.add(i); // put it in to indexList
    }
    String newObj = courseList.get(i);
    checkOrder(newObj);
   }

  }

 }

}