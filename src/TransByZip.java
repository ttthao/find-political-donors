import java.util.*;

public class TransByZip {
    PriorityQueue<Double> lowers;
    PriorityQueue<Double> highers;
    Double runningMedian;
    Integer totalContributions;
    Double totalTransAmount;

   public TransByZip() {
       lowers = new PriorityQueue<Double> ( new Comparator<Double> () {
           public int compare(Double a, Double b) {
               return -1 * a.compareTo(b);
           }
       });
       
       highers = new PriorityQueue<Double> ();

       // Watch out for rounding errors
       runningMedian = 0.0;

       totalContributions = 0;

       totalTransAmount = 0.0;
   }

   public void addTransAmt(double transAmt) {
       if (this.lowers.size() == 0 || transAmt < this.lowers.peek()) {
           this.lowers.add(transAmt);
       } else {
           this.highers.add(transAmt);
       }
   }

   public void rebalanceHeaps() {
       PriorityQueue<Double> biggerHeap = this.lowers.size() > this.highers.size() ? this.lowers : this.highers;
       PriorityQueue<Double> smallerHeap = this.lowers.size() > this.highers.size() ? this.highers : this.lowers;

       if (biggerHeap.size() - smallerHeap.size() >= 2) {
           smallerHeap.add(biggerHeap.poll());
       }
   }

   public double getMedian() {
       PriorityQueue<Double> biggerHeap = this.lowers.size() > this.highers.size() ? this.lowers : this.highers;
       PriorityQueue<Double> smallerHeap = this.lowers.size() > this.highers.size() ? this.highers : this.lowers;

       if (biggerHeap.size() == smallerHeap.size()) {
           return (biggerHeap.peek() + smallerHeap.peek())/2;
       } else {
           return biggerHeap.peek();
       }
   }

   public void setTotalContributions() {
       this.totalContributions++;
   }

   public void setTotalAmount(double transAmt) {
       this.totalTransAmount = this.totalTransAmount + transAmt;
   }

   public int getTotalContributions() {
       return this.totalContributions;
   }
   
   public double getTotalAmount() {
       return this.totalTransAmount;
   }

}