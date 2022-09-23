import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Runner extends JPanel
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args)
    {
        RUNTHIS(200);
    }
    
    
    public static void RUNTHIS(int numPeople){
        JFrame frame = new JFrame("Disease Model: " + numPeople + " People");  
       
        Runner p = new Runner(numPeople);
        p.setFocusable(true);
        frame.add(p);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes closing the window exit the program
        frame.setVisible(true);
        frame.setSize(640, 580);
        frame.setResizable(false);
        
        //setup for main loop
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 20;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; //in nano seconds
        
        System.out.println("---------------------------- MODEL DATA (" + numPeople + ") -----------------------------");
        System.out.println("Time\t\tPercent Infected\tPercent Dead");
        
        while (true) {
            //calculate timing variables
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            
            p.requestFocusInWindow();
            p.update(updateLength / 1000000000.0);
            
            //tells the frame to call the panels paint method
            frame.repaint();
            
            //makes our program wait until its time for the next frame
            //NOTE: sleep is not exact so frames will not be exactly OPTIMAL_TIME apart
            try
            {
              Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
            }
            catch (Exception e) 
            {
              
            }
          
        }
    }
    
    ArrayList<Person> people;
    int frames;
    
    public Runner(int numPeople){
        people = new ArrayList<Person>();
        
        for (int i = 0; i < numPeople; i++)
        {
            people.add(new Person(640, 480));
        }

        frames = 0;
    }
    
    //dt is number of seconds since the last time update was called (usually around 0.04166)
    public void update(double dt){  
        for (int i = 0; i < people.size(); i++)
        {
            people.get(i).updatePerson();
        }
        for (int i = 0; i < people.size(); i++)
        {
            for (int j = 0; j < people.size(); j++)
            {
                if (i != j)
                {
                    people.get(i).interactWith(people.get(j));
                }
            }
        }
        frames += 1;
    }

    //do all graphics stuff here
    //g is the graphics context that is used to do all drawing
    public void paint(Graphics g){

        g.setColor(Color.red);

        g.fillOval(0, 0, 40, 40);
        g.fillOval(600, 0, 40, 40);
        g.fillOval(0, 440, 40, 40);
        g.fillOval(600, 440, 40, 40);

        g.setColor(Color.blue);

        g.fillOval(290, 210, 60, 60);



        for (int i = 0; i < people.size(); i++)
        {
            people.get(i).draw(g);
        }
        g.setColor(Color.black);
        g.fillRect(0, 480, 640, 15);
        
        //display data
        int numInfected = 0;
        int numDead = 0;
        for (int i = 0; i < people.size(); i++)
        {
            if (people.get(i).isInfected()){
                numInfected += 1;
            }
            if (people.get(i).isDead()){
                numDead += 1;
            }
        }
        //display stats at the bottom
        g.drawString("Time: " + frames, 50, 520);
        int pi = numInfected * 100 / (people.size() - numDead);
        g.drawString("Percent Infected: " + pi + "%", 50, 535);
        int pd = numDead * 100 / people.size();
        g.drawString("Percent Dead: " + pd + "%", 250, 520);
        if (frames % 20 == 0){  //print output every 20 time steps
            System.out.println(frames + "\t\t" + pi + "\t\t\t" + pd);
        }
    }
}

