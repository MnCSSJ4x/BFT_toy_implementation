package demo;

import java.util.* ;

import common.Game;
import common.Machine ; 


public class Game_0502 extends Game {
    
    @Override
    public void addMachines(ArrayList<Machine> machines, int numFaulty){
        this.machines = machines;
        this.numFaulty = numFaulty ; 
        /*initialiser for game as the game needs to know the machines as well 
        as the number of faulty machines. No condition check takes place here as per
        the assumption that number of faulty machines is put valid. */
    }

    @Override
    public  void startPhase(){
        Random rand = new Random(); 
        //1. select leader
        int rand_leader_id =rand.nextInt(machines.size());
        //2. identify number of faulty elements
        
        int rand_num_faulty = rand.nextInt(numFaulty+1);
        System.out.println("number of faulties taken "+rand_num_faulty);

        //3. Now to allocate which ones are faulty we need to create an array of random nums and just 
        //pick the first or last numFaulty elements (for ease we take the from beginning)

        ArrayList<Integer> rand_array = new ArrayList<>();
        for(int i = 0 ; i<machines.size();i++){
            rand_array.add(i);
        }
        Collections.shuffle(rand_array);

        
        System.out.println("rand_array in game =  " + rand_array);
         

        //setMachines for all individual machines declare them as good or bad
        int count = 0; 
        for(int i : rand_array){
            machines.get(i).setMachines(machines) ;
            if(count < rand_num_faulty){
                count++;
                machines.get(i).setState(false);
            }

            else{
                machines.get(i).setState(true); 
            }

        }
        machines.get(rand_leader_id).setLeader();


        


    }





    private ArrayList<Machine> machines = new ArrayList<>() ; 
    private int numFaulty; 


}
