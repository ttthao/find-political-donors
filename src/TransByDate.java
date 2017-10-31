import java.util.*;
import java.math.*;

public class TransByDate {
    PriorityQueue<Integer> lowers;
    PriorityQueue<Integer> highers;
    Integer median;
    Integer totalContributions;
    Integer totalTransAmount;

    // Priority queue/heaps to balance half of the input transaction
   public TransByDate() {
       lowers = new PriorityQueue<Integer> ( new Comparator<Integer> () {
           public int compare(Integer a, Integer b) {
               return -1 * a.compareTo(b);
           }
       });
       highers = new PriorityQueue<Integer> ();
       
       median = 0;
       totalContributions = 0;
       totalTransAmount = 0;
   }

   // Update priority queues and metdata on each input
   public void update(int transAmt) {
       addTransAmt(transAmt);

       // Update streaming total amount
       setTotalAmount(transAmt);

       // Update streaming total contributions
       setTotalContributions();

       // Rebalance
       rebalanceHeaps();
   }

   // Output string for file writer
   public String toString(String cmteId, String transDate) {
       Integer median = getMedian();
       Integer totalContributions = getTotalContributions();
       Integer totalAmount = getTotalAmount();
       return String.format("%s|%s|%s|%s|%s", cmteId, transDate, Integer.toString(median), Integer.toString(totalContributions), Integer.toString(totalAmount));
   }

   // Insert input's amount to one of the heaps
   public void addTransAmt(int transAmt) {
       if (this.lowers.size() == 0 || transAmt < this.lowers.peek()) {
           this.lowers.add(transAmt);
       } else {
           this.highers.add(transAmt);
       }
   }

   // Rebalance heap on eveery input
   public void rebalanceHeaps() {
       PriorityQueue<Integer> biggerHeap = this.lowers.size() > this.highers.size() ? this.lowers : this.highers;
       PriorityQueue<Integer> smallerHeap = this.lowers.size() > this.highers.size() ? this.highers : this.lowers;

       if (biggerHeap.size() - smallerHeap.size() >= 2) {
           smallerHeap.add(biggerHeap.poll());
       }
   }

   // Divide by float to get decimal for rounding
   public int getMedian() {
       PriorityQueue<Integer> biggerHeap = this.lowers.size() > this.highers.size() ? this.lowers : this.highers;
       PriorityQueue<Integer> smallerHeap = this.lowers.size() > this.highers.size() ? this.highers : this.lowers;

       if (biggerHeap.size() == smallerHeap.size()) {
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