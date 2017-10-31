import java.util.*;
import java.math.*;

public class TransByZip {
    PriorityQueue<Integer> lowers;
    PriorityQueue<Integer> highers;
    // Double runningMedian;
    Integer runningMedian;
    Integer totalContributions;
    Integer totalTransAmount;
    // Double totalTransAmount;

   public TransByZip() {
       lowers = new PriorityQueue<Integer> ( new Comparator<Integer> () {
           public int compare(Integer a, Integer b) {
               return -1 * a.compareTo(b);
           }
       });
       
       highers = new PriorityQueue<Integer> ();

       // Watch out for rounding errors
    //    runningMedian = 0.0;
       runningMedian = 0;

       totalContributions = 0;

    //    totalTransAmount = 0.0;
       totalTransAmount = 0;
   }

   public void addTransAmt(int transAmt) {
       if (this.lowers.size() == 0 || transAmt < this.lowers.peek()) {
           this.lowers.add(transAmt);
       } else {
           this.highers.add(transAmt);
       }
   }

   public void rebalanceHeaps() {
       PriorityQueue<Integer> biggerHeap = this.lowers.size() > this.highers.size() ? this.lowers : this.highers;
       PriorityQueue<Integer> smallerHeap = this.lowers.size() > this.highers.size() ? this.highers : this.lowers;

       if (biggerHeap.size() - smallerHeap.size() >= 2) {
           smallerHeap.add(biggerHeap.poll());
       }
   }

   public int getMedian() {
       PriorityQueue<Integer> biggerHeap = this.lowers.size() > this.highers.size() ? this.lowers : this.highers;
       PriorityQueue<Integer> smallerHeap = this.lowers.size() > this.highers.size() ? this.highers : this.lowers;

       if (biggerHeap.size() == smallerHeap.size()) {
        //    return (biggerHeap.peek() + smallerHeap.peek())/2;
        //     System.out.println((int) Math.ceil(a / 2));
            // System.out.println((biggerHeap.peek() + smallerHeap.peek())/2.0);
           return (int) Math.ceil((biggerHeap.peek() + smallerHeap.peek())/2.0);
       } else {
           return biggerHeap.peek();
       }
   }

   public void setTotalContributions() {
       this.totalContributions++;
   }

   public void setTotalAmount(int transAmt) {
       this.totalTransAmount = this.totalTransAmount + transAmt;
   }

   public int getTotalContributions() {
       return this.totalContributions;
   }
   
   public int getTotalAmount() {
       return (int) this.totalTransAmount;
   }

}