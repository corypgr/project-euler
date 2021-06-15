I've been thinking about revisiting [Project Euler](https://projecteuler.net) for a while now, and I've decided to actually track my code this time around.

This repo is for my code solutions to the Project Euler problems. I've worked on this at least twice before, and either can't find the code from then (read: didn't try to find) or the other attempts were shared with someone. This time I thought I would start fresh.

When I started, my goal was to finish at least 1 problem each day. I didn't have a stopping point in mind. There are over 750 problems, so I could go with my goal for 2+ years. However, I've decided I will stop when I finish the first 100 problems. The Project Euler community doesn't want users to post their solutions to problems anywhere other than the internal problem threads, which are only accessible after submitting the right solution to a problem. The community will turn a blind eye for the first 100 problems. The reasoning behind the restriction is that by posting the solution online you are robbing others of the chance to solve the problem themselves. I don't agree with this view. I feel like a person who actually wants to solve a problem wouldn't search out the solution directly, so I should be free to post my solutions online. This difference of views makes me less interested in solving the problems if it means I must either do it somewhat secretively or openly break the rules of the community. Therefore, I will stop at Problem 100.

Project Euler gives a nice graphic to show how many problems you've solved. I had apparently already solved 80 problems when I started this, some beyond the 100 problem mark, so my graphic doesn't match up with my number of solutions here:

![Project Euler Progress](https://projecteuler.net/profile/corypgr.png)

Here are some direct stats (updated each time you run `mvn package`):

|||
|---|---|
| Date Started Solving       | 2021-03-15   |
| Problems Solved Goal       | 93   |
| Problems Actually Solved   | 99 |
| Due Date for next Solution | 2021-06-22  |

Build with:
```
mvn clean
mvn package
```

Generates a jar at ./target/ProjectEuler-1.0-SNAPSHOT-jar-with-dependencies.jar which can be executed using:
```
java -jar ./target/ProjectEuler-1.0-SNAPSHOT-jar-with-dependencies.jar [--problemNumber <number>]
```
