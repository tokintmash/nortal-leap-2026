# AI Usage

- Since basically no prior experience with Java (my only backend-experience is with Go from kood/Jõhvi), I used AI for helping me analyze the assignment and readmes and figure out where the main functionality is located for this assignment.
- In the AI instructions files I set that AI should help me learn and not change files.
- Ended up using it quite a bit, but used regular internet search as much.
- Removing deleted member from Queue was first done with AI (currently commented out: removeMemberFromAllQueues()), I then realized it's a loop and refactored it with the help of AI.

Assumptions
- Assuming that the assignment requires the book to be automatically loaned to the next member in queue, if it exists.
- Assuming success responses are conseidered fine in their current form: "Action completed->mN"
- Assuming adding a failure message to returnBook() doesn't count as changing API surface.
- Assuming JWT Auth is left in README to see who will deal with it (i.e. it's probably a bonus). Decided to make sure required is done first.
