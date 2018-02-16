# Java Network Connect Four Game 

## Protocol

### JOIN [NAME]
Sent from client to server to join lobby

The server will respond with either J_OK or JERR.

### J_OK
Sent from server to client if a connection has been made and username is not taken.

### JERR
Sent from server to client if username is not available.

### DATA [NAME]: [MESSAGE]
A chat message sent from client to server, broadcasted to all clients in lobby

### QUIT [NAME]
Sent from client to server on exit

### LIST [NAME1 NAME2...]
Sent from server to all clients when a client enters or leaves

### GHCL [NAME] [NAME]
Sent from client to server to challenge a client to a game.

### GACK [NAME] [NAME]
Sent from client to server to accept a challenge

### GNAK [NAME] [NAME]
Sent from client to server to decline a challenge

### GERR
Sent from server to a gameinstance client if a client sends an invalid game move or it is not the clients turn.

### GAME [GAMEDATA]
Sent from server to gameinstance clients. GAME contains a copy of the game board so clients can see the game progress.

### GWIN [WINNERNAME]
Sent from server to gameinstance clients if the game is over

### MOVE [INTEGER]
Sent from gameinstance client to server, a gamemove

### TURN [NAME]
Sent from server to gameinstance clients