Marte Engine
Version: 0.1

1. Introduction

MarteEngine (ME) is a Java videogame engine with focus in a simple, clear API for fast game development.
Major inspiration comes from Flashpunk (http://flashpunk.net) and Slick forum (http://slick.javaunlimited.net/).


2. Contribute

MarteEngine is a opensource (see license.txt) project hosted on GitHun: do want to try, improve or help?
With Git this is easy and fun :D 


3. Concepts

Entity - Main object in game with render logic and update logic

World - Useful container for all game entity, help to organize game logic in differents states (Intro, Game, Pause, etc..)

Camera - Camera will follow an entity and scroll screen


4. Tutorial

In test/it.randomtower.test you can find few test

4.1 Hello World Marte Engine

HelloWorld.java is most simple ME example. You can read code and find few things:
1) all objects are subclasses of Entity class
2) for easy use, ME provide StaticActor, a simple way to diplay an image on screen
3) code is similar to Slick base code, because ME is build on top of it. So there are init,render and update functions
3.1) in init function we build basic entity
3.2) in update function we update logic, collision and siimlar stuff: this is done from ME static class and StaticActor.java
3.3) in render function we draw on screen the image: this is done from ME static class and StaticActor.java