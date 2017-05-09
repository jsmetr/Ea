/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

/**
 *
 * @author Jarno
 */
public class WorkLoad implements Comparable<WorkLoad>{

    /*
    A simple Employee+integer shell to help sort technicians by workload.
     */
    private int jobcount=0;
    private String techname;
    
    public WorkLoad(String name){
        this.techname=name;
    }
    
    public void countUp(){
        this.jobcount++;
    }
    
    public int getCount(){
        return this.jobcount;
    }
    
    public String getName(){
        return this.techname;
    }

    @Override
    public int compareTo(WorkLoad other) {
        return this.jobcount-other.jobcount;
    }
    
}
