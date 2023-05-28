/*
* name: Annan Wang
* id: 1652663
* */
import java.util.*;

public class hw4{
    static int size = 10;// number of requests
    static int diskSize = 100;// size of disk

    public static void main(String[] args) {

        int arr[] = new int[size];// array of requests, which contains 10 random numbers
        Random rand = new Random();
        for(int i=0; i<size; i++){
            arr[i] = Math.abs(rand.nextInt(diskSize));// random number between 0 and 100
        }

        int head = 50;// initial position of head, which is 50
        System.out.println("Initial position of head: " + head);

        System.out.println("Cylinder requests: ");
        for(int i=0; i<size; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();

        System.out.println("\nFCFS Disk Scheduling");
        FCFS(arr, head);
        System.out.println("\nSSTF Disk Scheduling");
        SSTF(arr, head);
        System.out.println("\nSCAN Disk Scheduling");
        SCAN(arr, head, "left");// left means the head is moving left at start of the algorithm
    }

    static void FCFS(int[] arr, int head) {
        int totalDistance = 0;
        int distance, curTrack;

        for (int i = 0; i < size; i++) {
            curTrack = arr[i];
            distance = Math.abs(curTrack - head);// distance between current track and head
            totalDistance += distance;
            head = curTrack;// head is now at current track
            System.out.println("Reading track " + curTrack + ", distance moved: " + distance);
        }

        System.out.println("FCFS total distance: " + totalDistance);
    }

    static void SSTF(int[] arr, int head) {

        int[] temp = new int[size];// copy of arr
        boolean[] visited = new boolean[size];// whether the track is visited

        int totalDistance = 0;
        System.arraycopy(arr, 0, temp, 0, size);//
        Arrays.sort(temp);// sort temp in ascending order

        int curTrack;
        for(int i=0; i<size; i++){
            curTrack = closestDistance(temp, head, visited);// find the closest track to head
            visited[curTrack] = true;
            totalDistance += Math.abs(temp[curTrack] - head);
            System.out.println("Reading track " + temp[curTrack] + ", distance moved: " + Math.abs(temp[curTrack] - head));
            head = temp[curTrack];// head is now at current track
        }

        System.out.println("SSTF total distance: " + totalDistance);
    }

    static void SCAN(int[] arr, int head, String direction) {

        int totalDistance = 0;
        int curTrack;

        ArrayList<Integer> left = new ArrayList<Integer>();// tracks on the left of head
        ArrayList<Integer> right = new ArrayList<Integer>();// tracks on the right of head

        for(int i = 0; i < size; i++){
            if (arr[i] < head)
                left.add(arr[i]);// add tracks on the left of head to left
            if (arr[i] > head)
                right.add(arr[i]);// add tracks on the right of head to right
        }
        Collections.sort(left);// sort left in ascending order
        Collections.sort(right);// sort right in ascending order

        int run = 2;// run twice, one for left, one for right
        while (run-- >0) {
            if (direction == "left") {
                for (int i = left.size() - 1; i >= 0; i--) {// start from the largest track on the left
                    curTrack = left.get(i);
                    totalDistance += Math.abs(curTrack - head);
                    System.out.println("Reading track " + curTrack + ", distance moved: " + Math.abs(curTrack - head));
                    head = curTrack;
                }
                direction = "right";// change direction
            }
            else if (direction == "right") {
                for (int i = 0; i < right.size(); i++) {// start from the smallest track on the right
                    curTrack = right.get(i);
                    totalDistance += Math.abs(curTrack - head);
                    System.out.println("Reading track " + curTrack + ", distance moved: " + Math.abs(curTrack - head));
                    head = curTrack;
                }
                direction = "left";// change direction
            }
        }

        System.out.println("SCAN total distance: " + totalDistance);
    }

    //helper function for SSTF, find the closest track to head
    static int closestDistance(int arr[], int head, boolean visited[]){
        int min = Integer.MAX_VALUE, min_index = -1;
        for(int i=0; i<size; i++){//for each track
            if(Math.abs(arr[i]-head)<min && !visited[i]){//if the track is closer to head, and not visited
                min = Math.abs(arr[i] - head);
                min_index = i;
            }
        }
        return min_index;//return the index of the closest track
    }
}
