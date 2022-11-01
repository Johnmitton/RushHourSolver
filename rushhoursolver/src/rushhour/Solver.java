package rushhour;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Solver {

    public static PriorityQueue<RushHourOwn> priorityQueue;

    private static boolean bfsSearch(RushHourOwn startState) throws Exception {
        LinkedList<RushHourOwn> q = new LinkedList();
        q.addLast(startState);

        Set<Integer> seenBoards = new HashSet<>();
    //    seenBoards.add(startState.getBoard());

        while(!q.isEmpty()){
            RushHourOwn state = q.get(0);
            q.remove(q.get(0));

            for (int i = 0; i < 6; i++) {
                for (int k = 0; k < 6; k++) {
                    System.out.print(state.getBoard()[i][k]);
                }
                System.out.println();
            }
            System.out.println();

            if(state.isSolved())
                return true;

            for(RushHourOwn newState : state.getStates()){
                if(!seenBoards.contains(Arrays.deepHashCode(newState.getBoard()))){
                    seenBoards.add(state.hashBoard());
                    q.addLast(new RushHourOwn(newState.getBoard()));
                }
            }
        }
        return false;
    }

    private static LinkedList<RushHourOwn> findPath(HashMap<RushHourOwn, RushHourOwn> original, RushHourOwn goal){
        if(goal == null) {
            return null;
        }

       LinkedList<RushHourOwn> path = new LinkedList<>();
       RushHourOwn state = goal;
       path.addFirst(state.clone());
       while(original.get(state) != null){
           RushHourOwn parent = original.get(state);
           path.addFirst(parent.clone());
           state = parent;
       }
       return path;
   }

    private static LinkedList<RushHourOwn> searchAStar(RushHourOwn startState) throws Exception {
        HashMap<RushHourOwn, RushHourOwn> original = new HashMap<>();
        HashMap<Integer, Boolean> visited = new HashMap<>();

        RushHourOwn state = new RushHourOwn(startState.getBoard());
        RushHourOwn goal = null;
        state.setCost(0);

        priorityQueue.add(state);
        visited.put(state.hashBoard(), true);
        while(!priorityQueue.isEmpty()){
            RushHourOwn newState = priorityQueue.poll();
            if(newState.isSolved()){
                goal = newState;
                break;
            }
            for(RushHourOwn nextState : newState.getStates()){
                int cost = newState.getCost() + 1 + nextState.heuristic();
                if(!visited.containsKey(nextState.hashBoard())){
                    nextState.setCost(cost);
                    priorityQueue.add(nextState);
                    original.put(nextState, newState);
                    visited.put(nextState.hashBoard(), true);
                }
            }
        }
        return findPath(original, goal);
    }

    public static void solveFromFile(String input, String output) {
       priorityQueue = new PriorityQueue<>(10, Comparator.comparingInt(RushHourOwn::getCost));

        LinkedList<RushHourOwn> path = null;
        try {
            path = searchAStar(new RushHourOwn(input));

            if(path == null)
                return;

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        LinkedList<String> moves = new LinkedList<>();


        for(RushHourOwn r : path) {
            if(moves.size() > 0 && moves.getLast() != null && r.getMoveMade().substring(0, 2).equals(moves.getLast().substring(0, 2))) {
                    String num = moves.getLast().substring(2);
                    String carAndDirection = moves.getLast().substring(0, 2);
                    int numInt = Integer.parseInt(num);
                    numInt++;
                    num = (Integer.toString(numInt));
                    carAndDirection = carAndDirection + num;
                    moves.removeLast();
                    moves.addLast(carAndDirection);
            }
            else {
                moves.addLast(r.getMoveMade());
            }
        //    r.printBoard();
        //    System.out.println();
        }


        StringBuilder outputToFile = new StringBuilder();

        for(int i = 0; i < moves.size(); i++){
            if(moves.get(i) == null)
                continue;
            outputToFile.append(moves.get(i));
            if(i != moves.size()-1)
                outputToFile.append("\n");
        }

        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(output);
            myWriter.write(outputToFile.toString());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
