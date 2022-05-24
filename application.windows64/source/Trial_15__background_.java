import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Trial_15__background_ extends PApplet {

/* Peace Okoko
 11/15
 Update
 */
//Declares sickle arraylist
ArrayList<Sickle> cell;
//Controls movement
PVector flow= new PVector(0.5f, 0);
//Keeps track of the # of times wall is being touched ,the crisis and time
int cellsTouching, crisisCount, stoppedCells, collideTo, timecheck, timepassed;
//Keeps track if crisis is occuring
boolean crisis, running;
//Tracks stickA values
float sticky;
//Stores new cells created
ArrayList<Integer> cellValues;
PImage BloodV;


public void setup() {
  BloodV=loadImage("Blood2.jpg");

  cellsTouching = 0;
  crisisCount = 0;
  running=false;
  crisis = false;
  
  timecheck=0;
  cellValues = new ArrayList<Integer>();
  cell= new ArrayList<Sickle>();
  for (int i=0; i<20; i++) {
    cell.add(new Sickle());
  }
}
public void draw() {
  //Background
  background(0);
  imageMode(CENTER);
  image(BloodV, height/2, width/2, 2000, 800);
  //Responsible for the timer
  timecheck=0;
  timepassed=0;
  //Integer that keeps track of the number of cells touching wall
  cellsTouching = 0;
  collideTo=0;
  stoppedCells=0;
  for (int i=0; i<cell.size(); i++) {
    Sickle c=cell.get(i);
    //If cell is stuck then the acceleration of that cell is 0
    if (c.isStuck()) {
      c.acceleration= new PVector(0, 0);
    }

    c.run();
    //If the value of sticka of the other sickle cell being compared is less than random(0.005,0.01)
    //Then the cells have collided.
    if (c.sticka < random(0.005f, 0.01f)) {
      for (int j = 0; j<cell.size(); j++) {
        if (j!=i)c.collide(cell.get(j));
      }
    }
    c.applyForce(flow);
    c.wallcheck();
    //Check if cells are touching wall increment cell is touching integer
    if (c.isTouching()) cellsTouching ++;
    //Only counts the number of cells that are slive and have stopped when a crisis happens
    if (c.stuck==true && c.lifespan>0) stoppedCells++;
    //Collide to only gives the number of cells that don't have a stickA high enough to get stuck
    //Does not include the cell that is already stuck that attractracted them
    if (c.collided==true &&c.lifespan>0) collideTo++; 
    //Keeps track of stickA values of cells and used to print them out
    sticky=c.sticka;


    //Lower lifespan
    c.decreaseLife();
    //If cells are dead then add a new cell
    if (c.isDead()) {
      cellValues.add(i);
      print("\n This is stickA value for a cell that is alive"+" "+sticky);
    }
  }
  //When running isn't true track the time
  if (!running) timepassed= millis()-timecheck;
  //If there are more than 4 cells touching and there is a crisis then a crisis is happening
  //Increment the crisis count
  if (cellsTouching > 1 && !crisis) {

    print("\n Cells collided:"+collideTo);
    print("\nCells stopped:"+stoppedCells);
    //Time is printed in seconds
    print("\n Time passed: "+(PApplet.parseFloat(timepassed)/1000)+" "+ "seconds");
    print("\n ALERT:A crisis is happening");
    crisisCount ++;
    crisis = true;
    //Controls timer when false timer stops
    running=false;
  }

  //When less than 4 cells are touching the wall there is no crisis
  else if (cellsTouching < 1) {
    timecheck=millis();
    crisis = false;
    running=true;
  }

  //Creates new cells and keeps track of them whenever lifespan is low
  //Stops creating new cells after 3 crisises
  for (int x = cellValues.size()-1; x >= 0; x--) {
    cell.remove(cellValues.get(x).intValue());
    if (crisisCount < 1)

      cell.add(new Sickle());
  }
  //Takes out old cells
  cellValues.clear();
}

class Cell {

  PVector location;
  PVector velocity;
  PVector acceleration;
  float mass;

  float lifespan;

  float altmax;
  //Checks for collissions
  boolean collided = false;

  Cell() {
    location =new PVector(0+random(0, 700), 0+random(0, 40));
    velocity=new PVector(0, 0);
    acceleration= new PVector(0.0001f, 0.000f);
    altmax=5;
    //The mass of a sickle cell
    mass=10;
    lifespan=120;
  }
  public void move() {
    //Updates the location and velocity whenever acceleration changes
    velocity.add(acceleration);
    velocity.limit(15);
    location.add(velocity);
    //lifespan-=0.1;//Lifespan goes dowm every ten days
  }
  //Finds acceleration and applies
  public void applyForce(PVector force) {
    PVector f=force.get();
    f.div(mass);
    if (!collided) acceleration.add(f);
  }

  //When two cells are close enough to each other they collide
  public void collide(Cell v) {
    //They interact when the distance is less than 10
    float distan= location.dist(v.location) ;
    if (distan < 10 ) {
      acceleration= new PVector(0, 0);
      //Update the velocity
      velocity = new PVector(0, 0);
      collided = true;
      //velocity.add(acceleration);
    }
  }
}
class Sickle extends Cell {
  //Innate ability to stick
  float sticka;
  float stickb;
  //The image the sickle cell is displayed as
  PImage sickle;
  //Has the cell touched wall? Is one cell stuck
  boolean touchWall, stuck;
  Sickle() {

    sickle = loadImage("Sickle.png");
    acceleration=new PVector(.20f, .30f);
    sticka=random(0.001f);
    stickb=random(0.26f);
    //If a cell has a value greater than the range in random it gets stuck
    if (sticka> random(0, 0.01f)) {
      //When cell is stuck stuck is true
      stuck = true;
    } else {
      stuck = false;
    }
    //Each cell starts out with a different lifespan
    lifespan=random(20);
    //Initially cells are not touching walls
    touchWall = false;
  }
  //Limits range of movement, puts pimage onto sickle
  public void display() {
    imageMode(CENTER);
    image(sickle, location.x, location.y, 25, 25);
    location.x=constrain(location.x, 1, width);
    location.y=constrain(location.y, 1, height);
  }
  //Applies drag force
  public void drag() {
    PVector drag=velocity.get();
    drag.normalize();
    float c=-0.01f;
    float speed=velocity.mag();
    drag.mult(c*speed*speed);
    applyForce(drag);
  }
  public void run() {
    display();
    if (!stuck) {
      move();
      drag();
    }
  }
  //Checks if there are any edge collisions occuring
  //Keeps the sickle cells in the screen
  public void edges() {
    if (location.x<0) {
      location.x=location.x+1;
    } else if (location.x>width) {
      location.x=1;
    }
    if (location.y<0) {
      location.y=0;
    } else if (location.y>height) {
      location.y=1;
    }
  }
  //Takes in a sickle cell
  public void collide(Sickle v) {
    //They interact when the distance is less than 10
    super.collide(v);
    float distan= location.dist(v.location) ;
    if (distan <10) {
      //If cells distance is less than ten and the cells are already not moving due to stickiness
      //The if statement was used to check the number of cells that got stuck to an already stuck cell
      //if (!stuck && lifespan >0)print("\nStuck together\n");
      //Stuck is true
      stuck = true;
    }
  }
  //checks whether sickle cells have stopped
  public void wallcheck() {
    if (!(location.x>=width-1 || location.x<=1 || location.y>=height-1 || location.y<=1)) {
      if (!stuck && lifespan>0) {
        print("\nStickA value of not collided: " + sticka + " \n");
      }//When cell is not stuck inside vessel snd has a lifespan
      touchWall=true;//Touch wall is true
      stuck = true;//Stuck is true
    }
  }
  public boolean isTouching() {
    return touchWall;//Returns whether the wall is being touched
  }
  public boolean isStuck() {//Returns whether cell has stopped
    return stuck;
  }
  public void decreaseLife() {//Decreases the lifespan each iteration
    lifespan-=.1f;
  }
  public boolean isDead() {//When lifespan is 0 cell is declared dead
    if (lifespan > 0) return false;
    else return true;
  }
}

  public void settings() {  size(700, 40); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "Trial_15__background_" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
