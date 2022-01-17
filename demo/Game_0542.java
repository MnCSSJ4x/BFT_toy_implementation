package demo;
import java.util.Random;
import java.util.Collections;

//import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

import java.util.ArrayList;

import common.Game;
import common.Machine;

public class Game_0542 extends Game {
	private ArrayList<Machine> macList;
	private int fau;

	@Override
	public void addMachines(ArrayList<Machine> machines, int numFaulty) {
		// TODO Auto-generated method stub
		this.macList=machines;   //setting variables
		this.fau = numFaulty;
		
}

	@Override
	public void startPhase() {
		// TODO Auto-generated method stub
		int led_int; //leader index
		Random random = new Random(); //random object
		ArrayList <Integer> ar= new ArrayList<>(); //arrraylist for set of indexs (o to size)
		ArrayList <Integer> new_ar= new ArrayList<>(); // new array list
		for(int j=0;j<macList.size();j=j+1)
		{   //adding the indexs to the first arraylist
			ar.add(j);
		}
		//first we will take the arrray list of index and we will shuffle and latter we w
		//take the indexs from the shuffeled array list
		//we will take the indexs according to the number of faulty machines
		int fau_1=random.nextInt(fau);//no of faulty machines can be from 0 to fau
		Collections.shuffle(ar); // shuffling
		for(int j=0;j<fau_1;j=j+1){
			new_ar.add(ar.get(j));//adding the required no of indexs to the new array
		}
		int k;
		//now we iterate the machines list and when the index is equal to the any index in 
		//new array then we are assigning it as faulty remaining all are correct
		for(int j=0;j<macList.size();j=j+1)
		{
			macList.get(j).setMachines(macList);
			for( k=0;k<new_ar.size();k=k+1)
			{
				if(new_ar.get(k)==j) //if index equal faulty
				{
					macList.get(j).setState(false);
				}
			}
			if(k==new_ar.size())// if no match then we will assign this machine as correct
			{
				macList.get(j).setState(true);
			}
		}
		led_int= random.nextInt(macList.size());//leader index random
		macList.get(led_int).setLeader(); //setting it as leader

		
	}

}
