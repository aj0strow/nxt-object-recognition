# nxt-object-recognition

> Lab 5 of ECSE 211 at McGill, by AJ Ostrow and Vitaliy Kuzmin, Fall 2013. 

### bin commands

To clean the project:

```
$ bin/clean
```

To clean and compile:

```
$ bin/compile
```

To clean, compile and uplaod to NXT brick:

```
$ bin/upload
```

### structure

The Lab is about localizing, finding a block, and bringing it to a certain location. To do that, the robot has an ordered hierarchy of needs as follows:

1. stay in the grid (don't hit walls)
2. avoid wooden blocks and other robots
3. search for styrofoam blocks
4. bring block to score zone

The brick runs multiple threads. 1 for the Operator, and 1 for each poller (odometry, color sensing, ultrasonic distance sensing). The operator goes through a stack of controllers, and each controller returns whether the operator should try the next controller.

For example, the grid controller keeps the robot in the grid. If the robot is already in the grid, it will pass control to the next controller. The collision controller keeps the robot from hitting things. If the robot is not in harms way, it passes control on. The final controller is the motion controller, which moves the robot towards the destination (top of the path point stack).

To keep track of the position, waypoints, and angle calculations, there are a few helper classes. Also Configuration is used in lieu of convenient key-value stores in java (don't want to cast back / forth from Object). 

The robot keeps track of it's own motors, meaning most interaction is in the form of setting the forward (cm /s) and rotate (rad / s) speeds. 