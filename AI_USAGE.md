# AI Usage

- Since basically no prior experience with Java (my only backend-experience is with Go from kood/Jõhvi), I used AI for helping me analyze the assignment and readmes and figure out where the main functionality is located for this assignment.
- In the AI instructions files I set that AI should help me learn and not change files.
- Ended up using it quite a bit, but used regular internet search as much.

## Assumptions

- Assuming that the assignment requires the book to be automatically loaned to the next member in queue, if it exists.
- Assuming success responses are conseidered fine in their current form: "Action completed->mN"
- Assuming adding a failure message to returnBook() doesn't count as changing API surface. 
- Assuming the returning is not tested with non-existing IDs in the queue.
- Assuming JWT Auth is left in README to see who will deal with it (i.e. it's probably a bonus). Made JWT changes into branch "feature/7-auth".
