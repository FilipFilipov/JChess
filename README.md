# JChess

Just something I made to see if I could come up with a working algorithm to validate chess moves and detect checks and checkmates (I'm not a chess person, so I suck at spotting those things myself). It doesn't have a AI or anything and it's pretty uncomfortable for people to play as well, since you have to pass the mouse back and forward.

It uses Java's swing library for the visual components and probably breaks most code quality guidelines and OOP principles, because I just started learning those recently. On the plus side, I think the comments explain what is happening well enough.

From my testing it should be able to do all the standard moves correctly, including en passant capture, castling and pawn-to-queen promotion. If you try doing something the game thinks you shouldn't be doing, it will throw a helpful (I hope) error message on the console. It also informs you when someone is in check, checkmate or if there a stalemate - although the last two also end the game and give you a graphical message to that effect.

The game is started from the shell scripts JChess.sh on Linux and JChess.cmd on Windows.
