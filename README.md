I've been thinking about revisiting [Project Euler](https://projecteuler.net) for a while now, and I've decided to actually track my code this time around.

This repo is for my code solutions to the Project Euler problems. I've worked on this at least twice before, and either can't find the code from then (read: didn't try to find) or the other attempts were shared with someone. This time I thought I would start fresh.

My goal is to finish at least 1 problem each day. Project Euler gives a nice graphic to show how many problems you've solved. I've apparently already solved 80 problems, so I'll focus on those problems first so that my graphic eventually matches my progress here:

![Project Euler Progress](https://projecteuler.net/profile/corypgr.png)

Here are some direct stats (updated each time you run `mvn package`):

|||
|---|---|
| Date Started Solving       | 2021-03-15   |
| Problems Solved Goal       | 8   |
| Problems Actually Solved   | 21 |
| Due Date for next Solution | 2021-04-05  |

Build with:
```
mvn clean
mvn package
```

Generates a jar at ./target/ProjectEuler-1.0-SNAPSHOT-jar-with-dependencies.jar which can be executed using:
```
java -jar ./target/ProjectEuler-1.0-SNAPSHOT-jar-with-dependencies.jar [--problemNumber <number>]
```
