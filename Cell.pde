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
    acceleration= new PVector(0.0001, 0.000);
    altmax=5;
    //The mass of a sickle cell
    mass=10;
    lifespan=120;
  }
  void move() {
    //Updates the location and velocity whenever acceleration changes
    velocity.add(acceleration);
    velocity.limit(15);
    location.add(velocity);
    //lifespan-=0.1;//Lifespan goes dowm every ten days
  }
  //Finds acceleration and applies
  void applyForce(PVector force) {
    PVector f=force.get();
    f.div(mass);
    if (!collided) acceleration.add(f);
  }

  //When two cells are close enough to each other they collide
  void collide(Cell v) {
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
