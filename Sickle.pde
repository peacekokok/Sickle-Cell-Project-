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
    acceleration=new PVector(.20, .30);
    sticka=random(0.001);
    stickb=random(0.26);
    //If a cell has a value greater than the range in random it gets stuck
    if (sticka> random(0, 0.2)) {
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
  void display() {
    imageMode(CENTER);
    image(sickle, location.x, location.y, 25, 25);
    location.x=constrain(location.x, 1, width);
    location.y=constrain(location.y, 1, height);
  }
  //Applies drag force
  void drag() {
    PVector drag=velocity.get();
    drag.normalize();
    float c=-0.01;
    float speed=velocity.mag();
    drag.mult(c*speed*speed);
    applyForce(drag);
  }
  void run() {
    display();
    if (!stuck) {
      move();
      drag();
    }
  }
  //Checks if there are any edge collisions occuring
  //Keeps the sickle cells in the screen
  void edges() {
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
  void collide(Sickle v) {
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
  void wallcheck() {
    if (!(location.x>=width-1 || location.x<=1 || location.y>=height-1 || location.y<=1)) {
      if (!stuck && lifespan>0) {
        print("\nStickA value of not collided: " + sticka + "");
      }//When cell is not stuck inside vessel snd has a lifespan
      touchWall=true;//Touch wall is true
      stuck = true;//Stuck is true
    }
  }
  boolean isTouching() {
    return touchWall;//Returns whether the wall is being touched
  }
  boolean isStuck() {//Returns whether cell has stopped
    return stuck;
  }
  void decreaseLife() {//Decreases the lifespan each iteration
    lifespan-=.1;
  }
  boolean isDead() {//When lifespan is 0 cell is declared dead
    if (lifespan > 0) return false;
    else return true;
  }
}
