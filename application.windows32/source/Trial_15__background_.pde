/* Peace Okoko
 11/15
 Update
 */
//Declares sickle arraylist
ArrayList<Sickle> cell;
//Controls movement
PVector flow= new PVector(0.5, 0);
//Keeps track of the # of times wall is being touched ,the crisis and time
int cellsTouching, crisisCount, stoppedCells, collideTo, timecheck, timepassed;
//Keeps track if crisis is occuring
boolean crisis, running;
//Tracks stickA values
float sticky;
//Stores new cells created
ArrayList<Integer> cellValues;
PImage BloodV;


void setup() {
  BloodV=loadImage("Blood2.jpg");

  cellsTouching = 0;
  crisisCount = 0;
  running=false;
  crisis = false;
  size(700, 40);
  timecheck=0;
  cellValues = new ArrayList<Integer>();
  cell= new ArrayList<Sickle>();
  for (int i=0; i<20; i++) {
    cell.add(new Sickle());
  }
}
void draw() {
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
    if (c.sticka < random(0.005, 0.01)) {
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
    print("\n Time passed: "+(float(timepassed)/1000)+" "+ "seconds");
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
