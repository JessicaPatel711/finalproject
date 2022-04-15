<h1 align="center"> ❌⭕ TIC TAC TOE ❌⭕ </h1>
<h3 align="center"> CSCI2020U - Software Systems and Integrations 👩🏽‍💻 </h3>
<h3 align="center"> Final Group Project (Winter 2022) </h3>

<!-- TABLE OF CONTENTS -->
<h2 id="table-of-contents"> :book: Table of Contents</h2>

<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project"> ➤ About The Project</a></li>
    <li><a href="#project-files-description"> ➤ Project Files Description</a></li>
    <li><a href="#getting-started"> ➤ Getting Started</a></li>
    <li><a href="#scenario1"> ➤ Scenario 1: Main Player Wins </a></li>
    <li><a href="#scenario2"> ➤ Scenario 2: Opponent Wins </a></li>
    <li><a href="#scenario3"> ➤ Scenario 3: Both Players Form a Tie </a></li>
    <li><a href="#acknowledgements"> ➤ Acknowledgements </a></li>
    <li><a href="#credits"> ➤ Credits</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
<h2 id="about-the-project"> :pencil: About The Project</h2>

<p align="justify"> 
  Tic-Tac-Toe is a two player game where each player takes turn placing an X or O, based on who chooses the tick, to form a vertical, horizontal, or diagonal line. First player to get three Os or Xs to form a straight line wins. For consistency, this project's main player will use X while the oponent will use O.
</p>

<!-- PROJECT FILES DESCRIPTION -->
<h2 id="project-files-description"> 📂 Project Files Description</h2>

<ul>
  <li><b>GameServer.java</b> - Used to set up the servers and run the main game. </li>
  <li><b>Player.java</b> - Sets up clients with game updates and player information.  </li>
  <li><b>InfoCenter.java</b> - Information center for running game that lets players know about the winner or whether the game is a tie.  </li>
  <li><b>TileBoard.java</b> - Sets up the tic-tac-toe board and displays each tile to play with and place marks by both players. </li>
  <li><b>UIConstants.java</b> - Stores constants to access when setting up application UI.  </li>
</ul>

<h3>Some other supporting files</h3>
<ul>
  <li><b>bg.jpg</b> - Background image. </li>
  <li><b>bg2.jpg</b> - Background image. </li>
</ul>

<!-- GETTING STARTED -->
<h2 id="getting-started"> 🕹️ Getting Started</h2>
<p>First, please clone the repo above. </p>
<p>Locally, you are able to start the game by <b> first navigating to the package folder </b> and typing the following commands in the command line:</p>
<pre><code>$ java GameServer.java</code></pre>
<pre><code>$ java Player.java</code></pre>
<pre><code>$ java Player.java</code></pre>

<p> 📽️ You can view our video demo on how to run the file and a test run of the game: https://www.loom.com/share/3f8da600c3c54b7fad111489ca4f2804</p>

<!-- SCENARIO1 -->
<h2 id="scenario1"> 🏆 Scenario 1: Main Player Wins </h2>

<p> When the main player (player who gets first turn) wins, the three Xs will be recorded with a "Winner" message display on the main player's window, accouncing the winner on both clients' server.</p>

<p align="center"> 
<img src="README_files/PlayerX_Won.jpg" alt="Player X Won" height="300px" width="300px">
<!--height="382px" width="737px"-->
</p>

<!-- SCENARIO2 -->
<h2 id="scenario2"> 🏆 Scenario 2: Opponent Wins </h2>

<p> When the opponent (player who gets second turn) wins, the three Os will be shown with a "Winner" message display on the opponent's window, accouncing the winner on both clients' server.</p>

<p align="center"> 
<img src="README_files/PlayerO_Won.jpg" alt="Player O Won" height="300px" width="300px">
<!--height="382px" width="737px"-->
</p>

<!-- SCENARIO3 -->
<h2 id="scenario3"> 🙅🏽‍♀️ Scenario 3: Both Player Forms a Tie</h2>

<p> If there comes a chance that no one has formed a straight line by the end of the game, the game will end with the "Tie!" massage displaying on both player's window.</p>

<p align="center"> 
<img src="README_files/Tie.jpg" alt="Tie" height="300px" width="300px">
<!--height="382px" width="737px"-->
</p>

<!-- Acknowledgements -->
<h2 id="acknowledgements"> 📃 Acknowledgements</h2>
This project was submitted as the final group project for CSCI 2020U “Software Systems and Integration” during Winter 2022.


<!-- CREDITS -->
<h2 id="credits"> 🤩 Credits</h2>

<p> Aanisha Newaz - 100788588 </p>
<p> Chioma Okechukwu - 100741742 </p>
<p> Japnit Ahuja  - 100790042 </p>
<p> Jessica Patel - 100785837 </p>
