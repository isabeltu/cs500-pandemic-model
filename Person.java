/*
I added the ability to die and the chances increase as you are infected for a longer amount of time. I also added a home that is
each person's spawn point and they are each randomly assigned one of four work places. There is also a hospital in the middle that
they have a chance of visiting between work and home where they stay until they either recover or die.

This behavior is important as it provides a way for people to either die or recover from the disease and the movement is less random
and more realistic.

I added an integer to represent their time infected. I added a bunch of booleans representing what stage of movement they are in
such as atHome, or goHome. I added a timeLeft integer that is a random integer within a range that is used to decide how long
a person stays at home or at work before they move on. The last four variables I added were doubles to store the value of each
persons coordinates of their home and workplace.

They days infected was set to 0. All the boolean variables for the stage of movement were set to false except for atHome which
is set to true. timeLeft is randomized to a number between 0 and 100. workX and workY are set to one of the four corners randomly
and homeX and homeY are set to their current spawn coordinates.


If a person was infected, there was a chance that death would be true depending on how long they were infected. There is always
one stage of movement boolean that is true and the rest are false. If atHome or atWork is true, timeLeft goes down one every
iteration of updateperson and when it reaches 0, atHome/atWork becomes false and either goWork/goHome or goHospital will be true
depending on the random probability. If goHome/goWork/goHospital is true, the person will move towards their home/work/hospital
until they are within a certain distance to it. if atHospital is true, they will leave as soon as they are not infected
and while they are infected, there will be a small chance every turn that they will recover

I didn't change the update person method


***I change this improvement by replacing the booleans with and int movementState that keeps track of where the person is/where
they are going other than that it is the same

***I drew the workplaces and hospital on the runner class to make it easier to see





I added a lockdown that goes into place once 30% of the population is infected

This is important to have some kind of emergency measure if the disease is getting out of control

I added two static variable, one to track the number infected and one to track the number alive

I added one to numAlive every time a person was constructed and one to numInfected everytime they were constructed initially
as infected

In the updatePerson method, if the person died numAlive and numInfected would both decrease by 1. If they recovered in the
hospital, numInfected would go down by one.

I changed the interact person method so that it checked the person they were infecting wasn't already infected as before it
was messing up the counting. So now whenever a new person is infected, numInfected increases by 1.




The last behavior I added was the ability to wear a mask to reduce your chances to spread the disease when wearing it.

This is important as half the population is taking safety precautions to help prevent the spread of the disease.

I added a boolean mask that represents whether the person is wearing a mask or not. I also made a small blue dot on the
person so it is easy to tell who is wearing a mask.

In the constructor each person had a 50% chance to spawn with a mask and they kept it on the whole time

There were no changes in the updatePerson method

In the interactWith method if the person p who is infected is wearing a mask, they have half the chance of passing it on to
other people



*/



import java.awt.Graphics;
import java.awt.Color;

public class Person
{



    //make sure to keep these 4 variables
    double x;
    double y;
    boolean infected;
    boolean dead;
    int time;
    //boolean atHome; 0
    //boolean goHome; 1
    //boolean atWork; 2
    //boolean goWork; 3
    //boolean atHospital; 4
    //boolean goHospital; 5
    int movementState = 0;
    int timeLeft;
    double workX;
    double workY;
    double homeX;
    double homeY;

    static int numInfected = 0;
    static int numAlive = 0;

    boolean mask;

    //add other class variables you might want (e.g. age, wearsMask, timeInfected, hasAnitbodies)
    
    //constructor
    public Person(int xMax, int yMax)
    {
        //put the person at a random position on the screen
        x = Math.random() * xMax;
        y = Math.random() * yMax;


        //home position is not too close to work or hospital
        while(Math.sqrt((workX-x)*(workX-x) + (workY-y)*(workY-y)) < 25 || Math.sqrt((300-x)*(300-x) + (220-y)*(220-y)) < 25){
            x = Math.random() * xMax;
            y = Math.random() * yMax;
        }

        homeX = x;
        homeY = y;

        
        //they have a 10% chance of being infected initially
        infected = Math.random() < 0.1;
        if(infected){
            numInfected++;
        }

        numAlive++;


        //50% chance they wear mask
        mask = Math.random() < 0.5;
      
        
        //no one is initially dead
        dead = false;

        time = 0;

        movementState = 0;

        timeLeft = (int)(Math.random() * 100);


        //choose one of four workplaces
        double r = Math.random();
        if(r<0.25){
            workX = 20;
            workY = 20;
        }

        else if(r<0.5){
            workX = 20;
            workY = 460;
        }

        else if(r<0.75){
            workX = 620;
            workY = 20;
        }

        else{
            workX = 620;
            workY = 460;
        }
    }
    
    //how does a person change over time
    //maybe they move
    //if they're infected, do they recover?
    //do they die??
    public void updatePerson()
    {
        if (this.dead) {  //don't do anything if this person is dead
            return;
        }

        if (this.infected){
            time ++;
            //chance to die
            if(Math.random() + time*0.00001 > 0.999){
                this.dead = true;
                this.infected = false;
                numInfected--;
                numAlive--;
                
            }

        }
        
        //at home or at work
        if(movementState == 0 || movementState == 2){
            timeLeft--;
        }


        //leaving home
        if(movementState == 0 && timeLeft == 0){
            if(Math.random() < 0.05){
                movementState = 5;
            }
            else{
                movementState = 3;
            }
        }


        //leaving work
        if(movementState == 2 && timeLeft == 0){
            if(Math.random() < 0.05){
                movementState = 5;
            }
            else{
                movementState = 1;
            }
        }


        //going to work
        if(movementState == 3){

            if(Math.sqrt((workX-x)*(workX-x) + (workY-y)*(workY-y)) < 15){
                movementState = 2;
                timeLeft = (int)(Math.random() * 100) + 100;
            }

            else{
                x += (workX-x)/Math.sqrt((workX-x)*(workX-x) + (workY-y)*(workY-y));
                y += (workY-y)/Math.sqrt((workX-x)*(workX-x) + (workY-y)*(workY-y));
            }
        }


        //going home
        if(movementState == 1){

            if(Math.sqrt(homeX-x)*(homeX-x) + (homeY-y)*(homeY-y) < 1){
                movementState = 0;
                timeLeft = (int)(Math.random() * 100) + 100;
            }

            else{
                x += (homeX-x)/Math.sqrt((homeX-x)*(homeX-x) + (homeY-y)*(homeY-y));
                y += (homeY-y)/Math.sqrt((homeX-x)*(homeX-x) + (homeY-y)*(homeY-y));
            }
        }


        //going to hospital
        if(movementState == 5){

            if(Math.sqrt((320-x)*(320-x) + (240-y)*(240-y)) < 25){
                movementState = 4;
            }

            else{
                x += (300-x)/Math.sqrt((300-x)*(300-x) + (220-y)*(220-y));
                y += (220-y)/Math.sqrt((300-x)*(300-x) + (220-y)*(220-y));
            }
        }

        //at hospital

        if(movementState == 4){
            if(!infected){
                movementState = 1;
            }

            else{
                if(Math.random() < 0.01){
                    infected = false;
                    numInfected--;
                    
                }
            }
        }


        //lockdown
        if((double)numInfected/(double)numAlive > 0.3){
            if(infected){
                movementState = 5;
            }
            else if(movementState == 0){
                timeLeft = 200;
            }
            else{
                movementState = 1;
            }
        }


        
    }
    
    //how does this person interact with p?
    //how should we calculate the chance that p infects them??
    public void interactWith(Person p)
    {
        //if p is infected (and not dead) and they are within 10 units of us, then
        //there is a 1% chance they will infect us
        if (!p.dead && p.infected && Math.random() < 0.005 && !this.infected){
            if ((p.x - this.x) * (p.x - this.x) + (p.y - this.y) * (p.y - this.y) < 10 * 10){
                if(p.mask){
                    if(Math.random() < 0.5){
                        this.infected = true;
                        numInfected++;
                    }

                }
                else{
                    this.infected = true;
                    numInfected++;
                }
                
            }
        }
    }
    
    //You shouldn't need to modify the methods down here
    //Although if you want, you can modify the draw method
    //to give more feedback using the color 
    public boolean isInfected(){
        return infected;
    }
    
    public boolean isDead()
    {
        return dead;
    }
    
    public void draw(Graphics g)
    {
        //don't draw people below the boundary
        if (y > 480)
        {
            return;
        }
        //set color based on the person's status
        if (dead)
        {
            g.setColor(Color.black);
        }
        else if (infected)
        {
            g.setColor(Color.green);
        }
        else {
            g.setColor(Color.gray);
        }
        g.fillOval((int) x-5, (int) y-5, 10, 10);  //draw a radius 5 circle for the person

        if (mask){
            g.setColor(Color.blue);
            g.fillOval((int)x-2, (int) y-2, 5, 5);
        }
    }
}
