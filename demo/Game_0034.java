package demo;
import java.util.*;
import common.Game;
import common.Machine;

public class Game_0034 extends Game
{
	private int t;
	private ArrayList<Machine> machines= new ArrayList<Machine>();
	@Override
	public void addMachines(ArrayList<Machine> machines, int numFaulty)
	{
		for(int i=0;i<machines.size();i++)
		{
			machines.get(i).setMachines(machines);
		}
		this.machines=machines;
		this.t=numFaulty;
	}

	@Override
	public void startPhase()
	{
		Random r=new Random();
		// we select few machines which are going to be faulty.
		// to do this we obtain random integers and put them in an integer ArrayList.
		ArrayList<Integer> ilist=new ArrayList<Integer>();
		int fid;
		while(true)
		{
			// this loop runs till we have enough integers for faulty machines.
			if(ilist.size()==t)
				break;
			fid=r.nextInt(machines.size());
			if(ilist.contains(fid)==false)
			{
				ilist.add(fid);
			}
		}
		// now we give the states to each machine.
        for(int i=0;i<machines.size();i++)
        {
            if(ilist.contains(i)==true)
            {
                machines.get(i).setState(false);
                //printing out faulty machines.
                //System.out.println("faulty machine: "+machines.get(i).name());
            }
            else
                machines.get(i).setState(true);
            machines.get(i).setMachines(machines);
        }
        int lid = r.nextInt(machines.size());
        machines.get(lid).setLeader();
        //System.out.println("leader is: " + machines.get(lid).name());
	}
}