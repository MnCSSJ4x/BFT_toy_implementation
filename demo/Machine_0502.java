package demo;

import java.util.*;
import common.Game;
import common.Location;
import common.Machine;

public class Machine_0502 extends Machine {

    public Machine_0502() {
        id = ID++;
    }

    @Override
    public void setMachines(ArrayList<Machine> machines) {
        this.machines = machines;

    }

    @Override
    public void setState(boolean isCorrect) {
        this.isCorrect = isCorrect;
        right_vote = 0;
        left_vote = 0;
        stage_2_l = 0;
        stage_2_r = 0;

    }

    @Override
    public void setLeader() {
        // This method basically starts the protocol , we set a leader start round 0
        // here by calling sendMessage()
        Random rand = new Random();
        System.out.println("Leader says Am i Good ? " + isCorrect);
        isLeader = true;
        if (isCorrect) {
            // for a correct leader , it needs to pick a decision (made random here) and
            // send it to all and start protocol
            int LeadDecision = rand.nextInt(2);
            for (Machine m : machines) {
                m.sendMessage(id, phaseNum, 0, LeadDecision);
            }

        } else {
            // given n>3t => t<n/3 =? 2t+1 < 2n/3 +1 so set threshold to this

            // for the first part we first form a random array of ids where we would send
            // the same to first (2t)/3 +1 elements
            ArrayList<Integer> rand_array = new ArrayList<>();
            for (int i = 0; i < machines.size(); i++) {
                rand_array.add(i);
            }
            Collections.shuffle(rand_array);
            System.out.println(rand_array + " My rand array in machine");

            // As we need to be greater than the threshold we will add a number. let
            // threshold be increased to anywhere between
            // 0 to n-1/3
            int t = (2 * machines.size() / 3) + 1;
            t += rand.nextInt((machines.size() - 1) / 3);
            // Randomly take a decision but be consistent to all before threshold
            int LeadDecision = rand.nextInt(2);
            // propagate the fixed decision till threshold and then randomly send anything
            // to the remaining
            int count = 0;
            for (int i : rand_array) {
                if (count < t) {
                    count++;
                    machines.get(i).sendMessage(id, phaseNum, 0, LeadDecision);
                    // sends a single consistent a decision

                } else {
                    // randomly sends decision neednt be consistent at all
                    machines.get(i).sendMessage(id, phaseNum, 0, rand.nextInt(2));
                }

            }

        }

    }

    @Override
    public void sendMessage(int sourceId, int phaseNum, int roundNum, int decision) {
        // There are 3 types of messages to be sent
        // Round 0 :- Leader Only
        // Round 1,2 :- Everyone
        // In round 1 we have 2 perespective : Once as a receiver of info and the other
        // as sender
        // In round 2 also we have 2 jobs : for self : take tally of left and right
        // votes
        // and move according to Byzantine condtion
        Random rand = new Random();
        if (roundNum == 0) {
            self_decision = decision;
            num_of_mssg_sent++; // As given in question we also send a message to ourself
            System.out
                    .println("ID for round 0 " + id + " Decision taken " + self_decision + " Am I good ? " + isCorrect);

            if (isCorrect) {
                for (Machine ma : machines) {
                    ma.sendMessage(id, phaseNum, 1, self_decision);
                }
            }

            else {

                // As it is given in question we can do anything we want if we are
                // malicious so we will randomise this too for unpredictibility
                int malicious_decision = rand.nextInt(2);
                if (rand.nextInt(2) == 1) {
                    for (Machine ma : machines) {
                        ma.sendMessage(id, phaseNum, 1, malicious_decision);
                    }
                }
            }

        } else {

            if (roundNum == 1) {
                // Sender's perespective
                if (decision == 1)
                    right_vote++;
                else {
                    left_vote++;
                }
                System.out
                        .println("ID for round 1 : " + id + " left votes " + left_vote + " right votes " + right_vote);
                // Recivers Perespective :- where it gets message from all (right and left
                // votes)
                // and broadcast its decision to all (only if its uncorrupt)

                if (right_vote + left_vote == (2 * machines.size()) / 3 && !vel_set) {
                    if (stage2_start == -2) {
                        if (isCorrect) {

                            // send the message of direction from round 1

                            if (right_vote > left_vote) {
                                r1_decision = 1;

                            } else {
                                r1_decision = 0;

                            }
                            System.out.println("Round 1 ID " + id + " Am i good ? " + isCorrect + " Round_1_decision "
                                    + r1_decision);
                            // Start stage 2
                            stage2_start = 1;
                            for (Machine m : machines) {
                                m.sendMessage(id, phaseNum, 2, r1_decision);
                            }
                        } else {
                            stage2_start = 1;
                            // can practically decide anything if malicious but has to be consistent message
                            // to all
                            if (right_vote > left_vote) {
                                r1_decision = 0;
                            } else {
                                r1_decision = 1;
                            }
                            System.out.println("Round 1 ID " + id + " Am i good ? " + isCorrect + " Round_1_decision "
                                    + r1_decision);
                            for (Machine m : machines) {
                                m.sendMessage(id, phaseNum, 2, r1_decision);
                            }

                        }
                    }
                }

            } else if (roundNum == 2) {
                // taking tally of all the votes . Confirmation phase as given in question
                if (decision == 1) {
                    stage_2_r++;

                } else {
                    stage_2_l++;
                }
                System.out.println(
                        "id stage 2 " + id + " left movemnet stage 2 votes " + stage_2_l + " right mov stage2 votes  " + stage_2_r);
                // final poll count and action phase . if protocol fails default movemnet

                if (stage_2_r > 2 * machines.size() / 3 && !vel_set) {
                  System.out.println("going right");

                    direction.setLoc(direction.getY(), -direction.getX());
                    vel_set = true;

                } else if (stage_2_l > 2 * machines.size() / 3 && !vel_set) {
                    System.out.println("going left ");
                   
                    direction.setLoc(-direction.getY(), direction.getX());
                    vel_set = true; 
                } else {
                    
                    // default assumed as stationary
                }
            }

        }

    }

    @Override
    public void setStepSize(int stepSize) {
        stepsize = stepSize;

    }

    @Override
    public void move() {
        // 1.Move objects to specific location
        position.setLoc(position.getX() + direction.getX() * stepsize, position.getY() + direction.getY() * stepsize);

        // 2. Reset all the state variables
        stage2_start = -2;
        right_vote = 0;
        phaseNum = 0;
        stage_2_l = 0;
        stage_2_r = 0;
        self_decision = 1; // randomly given as it will be anyways overwritten by other methods
        r1_decision = 1;
        vel_set = false ;

    }

    @Override
    public String name() {
        return "DEMO_502" + id;

    }

    @Override
    public Location getPosition() {
        Location temp = new Location(position.getX(), position.getY());
        return temp;
    }

    private static int ID = 0;
    private int id;
    private int stepsize;
    private boolean isCorrect;
    private int right_vote, left_vote;
    private int stage_2_r, stage_2_l;
    private Location position = new Location(0, 0);
    private Location direction = new Location(0, 1);
    private ArrayList<Machine> machines = new ArrayList<>();
    private int stage2_start = -2; // can be anything other than 1,0
    private int phaseNum = 0;
    private boolean isLeader;
    private int self_decision = 1; // 1 for right 0 for left
    private int r1_decision;
    private int num_of_mssg_sent = 0;
    private boolean vel_set = false ; 

}
