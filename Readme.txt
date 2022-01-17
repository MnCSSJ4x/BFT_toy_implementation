Problem Statement 

A set of autonomous machines are moving on the streets of a city. As the machines move, they
try to agree on the next action (change of direction) that they should perform. Let’s assume this
is a choice between turning left or right (relative to their current direction of motion), at periodic
intervals. Machines communicate by sending messages to each other.
Since some of the machines can be faulty, the remaining machines (the correct ones) need to
achieve the following objectives:
Objective 1: All the correct machines agree on the next action
Objective 2: If a correct machine proposes an action, then all the correct machines agree on
that action

The simulation proceeds in phases. At the start of each phase, the player (or main):
1. Identifies up to t machines as the faulty machines. Note: none of the correct machines
know which of the machines, if any, are faulty. They just know that there can be a
maximum of t faulty machines.
2. selects one of the machines as the leader. Note: the leader can also be faulty.
Once selected, the leader decides on the next action (move right or left), and transmits that
decision to all the machines (including itself).
Since the receiver of a message does not know if the sender is correct or faulty, machines run a
multi-round protocol as described later to try to arrive at a consensus. At the end of the protocol,
all correct machines are expected to have reached agreement as described in Objectives 1 and 2.
Once a machine believes it has reached agreement for that phase, it starts moving in that
direction until a new decision is reached in the next phase.

By definition, a correct machine always sends the same (and correct) message to all machines,
and specifically, if it is the leader, it sends its initial decision to all the machines. It also sends out
its messages within a certain bounded time.
In general, a faulty machine, on the other hand, can be malicious and do anything it wants. It
can send different messages to different machines, it can avoid sending messages to some
machines, it can send multiple messages to a given machine, it can delay the sending of certain
messages, etc. However, to simplify our solution, we will assume that the machines are not that
malicious, and make the following critical assumptions:
  ● The leader can be faulty, but still sends the same message to at least 2t+1 machines.
  That is, the leader may avoid sending a message to at most t machines. However, all 
  messages sent out by the leader are the same.
  ● A faulty machine either stays silent for a round, or sends the same message to all
  machines. This message can be incorrect though.
  ● All messages are sent and delivered within a very short time and are delivered correctly
  (no loss, no tampering in transit, etc)
  To reach agreement, each machine executes the following protocol:
    1. messages are sent in rounds, and each phase has rounds starting from 0, and
    increasing by one each time. To keep things simple, we assume each machine sends a
    message to itself too in each round.
    2. A message is a method invoked by the sender on the recipient, and contains the id of
    the sender, the phase number, the round number, and the decision value. Even for faulty
    machines the source ID, phase number and round number should be correct and as per
    the protocol. Only the decision value may be incorrect.
    3. However, it sends the same message to all machines. For simplicity, we will assume
    that the sender id is always filled in correctly.
    4. The protocol is executed by each machine as follows:
      a. Round 0: The leader (and only the leader) sends its decision to all machines.
      Note that a faulty leader can send different messages to different machines.
      b. Round 1:
        i. each machine transmits the value it received in Round 0, to all machines.
        ii. each machine keeps track of all the messages it receives in this round,
        and maintains the count of messages of each type.
        iii. At the end of the round, it uses the message with the larger count as its
        decision for Round 2.
      c. Round 2:
        i. each machine sends to all machines the decision value it chose (as
        majority value) in Round 1
        ii. each machine confirms that it gets at least 2t+1 identical values in this
        round, and terminates the protocol, with this value as its decision. If not, it
        flags an error, by printing out an error message, and does not participate
        further in this phase.It can take any action it wants after that, including
        staying stationary

The above is a (highly simplified) example of what is known as the Byzantine Generals problem
or Byzantine Fault Tolerance (BFT), a mechanism used in different applications including
blockchains!
This is a toy implementation of the problem. The GUI was provided to test the implemntation. Multiple codes from various implementation was collected to create a scenario to find out which machine is malfunctioning 
