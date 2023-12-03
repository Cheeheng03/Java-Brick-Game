# BRICK BREAKER VIDEO GAME README
Re-implementation of a classic retro Brick Breaker Video Game developed and maintained through JavaFx which is part of my coursework given for the Developing Maintainable Software Module (COMP2042). 
The primary objective of Brick Breaker is to break all the bricks on the screen using a bouncing ball and a paddle. You control a horizontal paddle at the bottom of the screen using the left and right arrow keys.
The paddle's purpose is to deflect the bouncing ball and prevent it from falling off the screen. Points are awarded for each brick that is successfully broken. 
The main goal of the coursework is to maintain and extend the existing incomplete brick breaker game, not writing the game from scratch.

**IDE:** JetBrains IntelliJ IDEA Community Edition 2023.2.4\
**Java Version:** 15\
**SDK:** 19 - Oracle OpenJDK version 21.0.1 JavaFX Version: 11.0.2

# Compilation Instructions
1) Download the zip file
2) Unzip and extract the folder into a location in your computer that you remember
3) Download and Open IntelliJ IDEA Community Edition
4) Click "Open" and click on the coursework game folder that you have extracted just now
5) Click on the hamburger menu in the top left corner and follow instructions to add javafx libraries
```
File -> Project Structure -> Libraries -> Add -> Add the "lib" folder from your downloaded javafx-sdk-21.0.1 folder -> Apply
``` 
6) Click on 
```
Project -> CouseworkGame-master[LabTest2] -> src -> main -> java -> brickGame -> Main.java -> Run 'Main' in the top right corner to run the game
```

# Implemented and Working Properly
## Implementation based on refactoring
### 1) Supporting Single Responsibility by Splitting Up Classes Following MVC Pattern
The refactoring of the main class into distinct classes within the Model, View, and Controller pattern (MVC) aligns with the principle of single responsibility, ensuring that each class in the application has one reason to change and a clear purpose.
This approach not only simplifies the codebase, making it more maintainable and extendable, but also facilitates easier debugging and testing.
For instance, in the Model side in MVC, classes like Ball, Paddle, Block, Bonus, Physics, BlockSerializable, LoadSave and LoadGame that encapsulate the game's data and logic. 
Each of them follows Single Responsibility principle and is solely responsible for maintaining the state and behavior of the game.
Besides that, in the View aspect in the MVC pattern, classes like GameView, BlockView, and GameSound take charge of the presentation layer. 
GameView manages the UI elements and how the game is rendered on screen. 
BlockView is dedicated to the visual representation of each block, and GameSound controls the audio elements, enhancing player engagement through sound effects and music.
Last but not least, the last component in MVC is the controller which consist of classes involving GameController and GameEngine. GameController acts as intermediaries between the Model and View. 
GameController also handles user input and translates it into actions in the game model, while GameEngine orchestrates the game loop and updates the view accordingly

### 2) Meaningful Package Naming and Organization
After refactoring the classes mentioned above following the MVC pattern and the single responsibility principle. 
Three new packages namely Model, View, and Controller are introduced to organize the codebase more meaningfully. 
The package names reflect their corresponding roles in the MVC architecture, providing a clear and intuitive structure for developers to navigate. 
This organization enhances the overall readability of the code and simplifies the process of locating specific functionalities. 
The classes mentioned in the model, view and controller aspects above are organized and directed into the Model, View and Controller package accordingly.
Imports of the package's class are added whenever the class is used elsewhere which promotes encapsulation, re-usability and modularity.

### 3) Renaming Methods and Classes, Encapsulation and Deleting Unused Resources
Another crucial aspect addressed is the renaming of classes and methods, encapsulation, and the removal of unused resources.
Class and method renaming enhances code readability and clarity by adopting more logical and descriptive names for each class. 
This implementation makes the purpose and functionality of each class immediately apparent and clearer, contributing to the overall maintainability of the codebase.
Encapsulation is emphasized to encapsulate the internal details of each class, exposing only the necessary interfaces. 
For example, the bonus class is refactored and encapsulated to promote information hiding, preventing unintended interference with the internal workings of the classes and facilitating more robust and secure code.
Unused resources, such as redundant classes, methods, or variables, are systematically identified and removed as part of cleaning up the code. 
This boosts the efficiency of the codebase, reducing unnecessary complexity and potential sources of confusion. The elimination of unused resources also contributes to a more efficient and optimized application. 

## Additional Features Implemented
### 1) Main Menu Interface
In the original Game, there is no main menu for the game, just two buttons (load game and start new game) which let users start the game or load the game from saved.
I have implemented a Main menu interface which uses its own Pane and has children elements including a background image, the load game button and the start new game button.
If the user chooses to start a new game or load a game from saved, the pane will switch from the main menu to the game root pane. 
The reason I implemented the main menu interface is to add some fun and attracting UI elements including background and buttons which increases the users' interest to play the game.

### 2) Pause Button
I added a Pause Button that functions to stop and resume the game but pausing and playing the timeline of the game engine to increase the playability of the game as the user can choose the pause the game if they have something in hand to do and resume the game later if they want to pick up from what they have left out without having to save the game or restart the whole game. 
This increases the satisfaction level of the player.

### 3) Background Music and Sound Effect
I have added a new feature called games sound which plays a background music in loop after the user starts the game to increase the overall attractiveness and funness of the game as it provides a new element to spice things up and stimulate extra emotions when the player reached a higher level with higher difficulty. 
Added sound effects when the ball hits the paddle of the game so that it instill satisfaction into players and adds liveliness and personality to the game's interaction. 
Added "you win" and "you lose" sound effect when the player wins or loses so that it pulls the player's attention and interest back into the game and to motivate them to start a new game.

### 4) New Block Types
I have added 5 different types of new block types including mystery block, freeze block, ghost block, wall block and count breaker block. Adding new block types enhances gameplay diversity and introduces exciting challenges for players. 
These new block types contribute to a more dynamic and engaging gaming experience. They introduce chances and penalty to the game so that it increases the overall strategy thinking of the player's mindset. 
This addition not only increases the overall difficulty but also provides players with additional incentives to explore different strategies for different levels, making the gaming experience more enjoyable.

#### Mystery block
The mystery block implements more or less the same logic with the choco bonus block. 
When the player hits the mystery block with the ball, the bonus rectangle will start to drop.
The player can choose to ignore or claim the mystery bonus element using their paddle. 
If the player chooses to catch, and has successfully captured the bonus with their paddle, a randomizer is used to decide whether the player is rewarded or penalised. 
It will be a 50% chance that the paddle will be lengthened or shorten, this added a mystery essence to the game and creates more probability for the user to design their game plan.

#### Freeze block
The freeze block implements an interesting block which adds a new feature composed of a penalty when the player hits the block using the ball. 
It freezes the paddle for 3 seconds, meaning that it would not let the user move the paddle for 3 seconds, and it increases the probability of losing a heart as the player could not save the ball from hitting the bottom of the screen with a freezing paddle.
This increases the difficulty as breaking the freeze block and triggering the freezing of the paddle is inevitable.

#### Ghost Block
The ghost block is a block which will only appear in the last level as it plays a big part in increasing the difficulty of the last level. 
It makes the ball's UI invisible for 1.5 seconds and lets the user predicts how will the ball bounces and where will it appear again after 1.5 seconds in order to catch it accurately without losing a life.

#### Wall block
The wall block is a block which will only appear in the last two levels, level 17 and level 18. 
This block mainly functions as a barrier or an unbreakable wall as it will not be destroyed after the block is hit.
It will only change the physics of the ball and deflects after hitting it. 
This block acts as the guard and increase the difficulty as the player has to control the ball to avoid colliding with that block in order to pass through it and collide with other desired blocks.

#### Count Breaker Block
The count breaker block is a block that functions as a block with HP, and the player has to hit the block multiple of times inorder to destroy it. 
It will only appear in level 17 of the game. 
The count or hits left to destroy the block is determined using a randomizer and set at a range between 10 and 20.

### 5) New Custom Levels
Implemented two new custom levels with customized layout of the blocks and the types of the blocks to increase the difficulty and provide players with fresh challenges. These custom levels feature carefully designed layouts and a diverse array of block types, adding an extra layer of complexity to the game. By introducing new custom levels, players are encouraged to sharpen their skills and think of new game play strategies.

#### Level 17
Level 17 is built using only count breaker block and wall block. 
6 count breaker blocks with random HP are set to be on top of the scene and two guard wall blocks functions to disrupt the user from breaking the count breaker blocks too easily as difficulty of the ball passing through the wall block is increased gradually.

#### Level 18
Level 18 is the final level of the game with the highest difficulty. 
The layout of the game is tested out and refined to maximize the difficulty but the game still can be won with a good game plan. 
The ghost blocks and wall blocks are used in this level to bring in more challenges and spice things up for the user.

### 6) Improved overall game UI and logic
Improved the game interface with additions including normal background Image, gold status background image, ball image, gold status ball image, new blocks images, Show win background image, game over background image, paddle image during freeze status, main menu background and buttons, and added final score in both scenarios where the game is over and the game is won.

# Implemented But Not Working Properly

### 1) Thread to Timeline conversion
#### Implementation - Timeline conversion
Modified the GameEngine by integrating an animation timeline, which proved more effective than threads for managing concurrency. 
This choice was motivated by the need for better control and synchronization in game animations. 
The timeline approach not only streamlines thread execution but also adeptly handles game animations, enhancing overall concurrency management.

#### Issue
However, a complication arises due to the utilization of a timeline, leading to the checkDestroyedCount method being invoked twice within the onPhysicsUpdate. Consequently, this double invocation triggers the nextLevel method twice as well. 
Consequently, when the conditions (destroyedBlockCount == initialBlockCount) are satisfied, the level is erroneously incremented by 2 instead of the expected 1. 
It is my belief that this issue stems from the adoption of a timeline instead of threads in the game implementation. 
The issue arises because of animation timeline in JavaFX, operates based on the rendering cycle, which occurs at the frame rate (120 fps in the game).
The onPhysicsUpdate method is tied to this rendering cycle and may be called multiple times within a single frame. 
When using threads, the execution is more independent of the rendering cycle, and the method is typically executed once per invocation. 
Therefore, the problem of the method being called twice within the same frame is less likely to occur with a thread-based approach. 
In summary, the timeline, being tightly coupled with the rendering cycle, can lead to multiple invocations of the method within a frame, causing unexpected behavior.

#### Steps taken to address the issue - Boolean Flag Added
Therefore, a boolean flag (isLevelTransitionInProgress) is introduced so that it ensures that the checkDestroyedCount is only called once by the onPhysicsUpdate method coupled with the rendering cycle once the conditions for advancing to the next level are met.

### 2) Corner Detection and Handling for Block Collision
#### Implementation - Collision Detection
Added corner detection for collision between the blocks and the ball for precise corner handling and logical ball deflecting angle and direction. 
The introduction of Epsilon for floating-point precision is a pivotal enhancement, increasing the accuracy of the collision detection calculations. 
Implementing Axis-Aligned Bounding Box (AABB) collision detection, the ball's trajectory is represented as a rectangle, refining hit detection accuracy. 
This approach considers corners, addressing specific corner hits with epsilon comparisons for precision to block margins. 
Specialized edge-crossing comparisons further contribute to the overall accuracy improvement, providing detailed information about the type of hit for a more realistic and precise game response.

#### Issue
Nevertheless, the ball's physics do not align with expectations, despite accurate corner detection.
The problem arises from setting collision flags using the hitCode returned from the checkHitToBlock method during collision detection. 
At times, when the ball speed is high and multiple detections occur in rapid succession, conflicting collision flags are set before the ball's physics are updated.
This conflict leads to the cancellation of certain collision flags, preventing the ball's physics from being altered, and creating an illusion that the ball is penetrating through the blocks.

#### Steps taken to address the issue - Collision Handling
Setting the physics of the ball by implementing handleBlockCollisions method in physics class where the key changes can be seen through the method receiving hitCode returned by the collision detection rather than setting the collision flags, and calling the set physics to ball method in physics class to update the ball’s physics after block collision. 
This increases the efficiency of the code. 
Added a cool-down time of 25 milliseconds in when calling checkHitToBlock method so that it ensures that only one hitCode is returned even when the ball is in a position where collision is still detected as it has yet to leave the block margin range in the given frame. With the one returned hitCode and passing it to set the ball physics after block collision, the ensures that the ball’s physics is only set one time per block collision, which avoids block penetration due to multiple ball physics which are conflicting (eg. Go up and go down) set, this results in the ball moving in the same direction as the physics set cancels out each other.
Added a slight angle change of 0.3 floating point value downwards to the Y velocity of the ball when the ball hits the corners as it avoids continuous destruction of two horizontal neighbouring blocks as it adds a slight angle change when the ball is moving a little bit too flat horizontally and at a high velocity. 
This eliminates the corner collision of the second block being detection as the angle of X deflection is too small that the ball also touches the corner of the neighbouring block when deflected from the first block. 
Besides that, added a previous hit code variable which stores the previous that has a threshold of 5 milliseconds before it is reset to no hit, this modification checks for the destruction of two blocks at once. 
It checks for double corner collision of the two blocks when the ball hits the middle edge of the neighbouring blocks and handle them accordingly following 6 conditions introduced. 
Thus, the penetration of the blocks can be avoided with the implementations and additions mentioned above.

# Features Not Implemented
### Implementation of More Special Blocks
The incorporation of additional special blocks, each with distinct functions such as generating a duplicate game ball that doesn't reduce the heart count but retains the ability to destroy blocks, has been held back. 
The decision is based on the perceived complexity and time-consuming nature of the implementation. 
Unfortunately, time constraints due to the approaching deadline for the coursework submission have hindered the implementation of this feature.

### More Additional Custom Levels
The introduction of further custom levels, each featuring a unique layout of blocks to enhance gameplay complexity, has been held in abeyance. 
Due to the complex and time-consuming design considerations required for each custom level, the implementation has been put aside. 
The decision is influenced by the impending deadline for the coursework submission, limiting the available time for the thorough development and testing of these additional custom levels.

### Enhanced UI Implementation
The goal of improving the User Interface (UI) to create a more visually appealing and user-friendly gaming experience as the original UI lacked certain aesthetic elements and user interactivity, necessitating enhancements for better engagement was shelved.
The implementation of special effects such as trail effect of the ball, particle explosion, screen shakes during block destruction and so on has been suspended due to the perceived complexity and time constraints nearing the coursework deadline.

# New Java Classes
The new classes involves new classes refactored from Main.java and those which are part of the additional features introduced.

Reasons to introduce new classes:

#### Encapsulation and Single Responsibility Principle:
Each class encapsulates its specific functionality, leading to a clear separation of concerns. 
This adherence to the single responsibility principle ensures that each class manages its own data and behavior, contributing to a well-organized and modular codebase.

#### Re-usability:
The modular nature of these classes enhances their re-usability. 
Functions and data structures designed for specific tasks can be reused across different parts of the application or in other projects, reducing redundancy and encouraging efficient code usage.

#### Simplification of Main Class:
Decomposing complex functionality of into smaller, more manageable subclasses simplifies larger, more complicated main class. 
This approach allows for a more focused and understandable code structure, where each class deals with a distinct aspect of the application.

#### Improved Readability and Maintainability:
Having distinct classes for separate functionalities improves the overall readability of the code. 
It becomes easier to navigate, understand, and modify the code, which is especially beneficial for long-term maintenance.

#### Enhanced Testing and Debugging:
Smaller, well-defined classes simplify the process of testing and debugging. 
Each class can be independently tested, allowing for more precise and effective identification and resolution of issues.

#### Flexibility and Scalability:
The use of separate classes provides a flexible foundation for future enhancements.
It's easier to add new features, modify existing ones, or scale the application when functionalities are modular and separated into different classes following single responsibility principle.

### Refactored New Classes:

#### 1) Ball Class
The refactoring of the Ball class from the Main class in the game application brought significant improvements. 
Key functionalities which are only related to the ball such as handling the ball's position, movement, and collision responses were encapsulated within this new, dedicated Ball class. 
This change made the Main class become more organized and efficient, reducing its complexity and focusing it more on overall game management. 
The Ball class now provides a clear, manageable, and extensible interface for all ball-related behaviors, significantly enhancing the code's readability and maintainability. 
The class is part of the model classes in Model View Controller (MVC) pattern.

#### 2) Paddle Class
This new Paddle class encapsulates all the functionalities specific to the paddle, including its position and movement.
By segregating these aspects into a separate class, the game's main logic is significantly streamlined. 
The Paddle class not only brings focus to the paddle-related operations but also contributes to a more organized and maintainable codebase in the Main class. 
This approach aligns with object-oriented design and single responsibility principles, promoting reusable, modular, and scalable code architecture. 
The class is part of the model classes in Model View Controller (MVC) pattern. 

#### 3) GameModel Class
The GameModel class is introduced as a new class and refactored from the Main class and serves an important role as central point of the model classes in Model View Controller (MVC) pattern in enhancing the game's structure and functionality.
This class centralizes the core aspects of the model side of the game, including the instantiating and managing of the ball, paddle, blocks, bonuses, and physics objects used in the game. 
In short, this class is mainly used to create, update and manage the game state of the game. 
By encapsulating these elements, GameModel adheres to the principles of single responsibility and modularity, significantly cleaning up the Main class.
This refactoring into a dedicated GameModel class not only simplifies the overall game management by providing a clear, organized structure for handling game mechanics like collision detection and level progression, but also offers improved readability, modularity and maintainability of the code. 
The introduction of GameModel as a separate entity allows for easier extension and modification of game functionalities without interrupting the controller and view side of things, embodying a scalable and robust design approach in the game's development.

#### 4) GameView Class 
The introduction of the GameView class refactored from the main game logic centralizes all the UI-related elements and their functionalities, such as setting up and adding children into the root pane, set up and add to scene and stage, rendering the ball view, paddle view, and various UI elements like score, heart and level labels, update the UI of the game's background ball, the bonus UI when the bonus blocks are destroyed and paddle image and visibility when certain conditions such as Gold Status, Freeze Status, ghost Status. 
The GameView class serves as a crucial cornerstone as it is the central point of the View classes in the MVC pattern. 
By separating the visual aspects of the game into GameView, the code follows the separation of concerns principle, and complies to the Model View Controller (MVC) pattern, enhancing modularity and readability. 
This structural change simplifies both the maintenance and scalability of the game's interface, allowing for more focused management of the game's visual presentation and user interaction. 
The refactoring into GameView thus serves to streamline the main game logic, emphasizing cleaner code architecture and a more intuitive user experience.

#### 5) GameController Class
The GameController class represents a significant refactoring effort from the main game logic, aimed at encapsulating the control flow and event handling aspects of the game. 
The GameController class plays the most important role as the controller in the Model View Controller (MVC) pattern. 
This class is responsible for managing the game's state when conditions are met, handling user inputs, and coordinating between the GameModel and GameView.
By separating these concerns, the GameController enhances the overall structure and maintainability of the code. 
This modular approach simplifies the Main class, focusing it on initial setup of the GameModel, GameView objects and passing them into the newly created GameController object, then the GameController takes charge of the dynamic aspects of gameplay. 
This separation not only makes the code more organized and easier to understand as it follows the MVC pattern but also facilitates easier debugging and extension of game features. 
The introduction of GameController as a distinct class reflects a commitment to clean architecture and effective separation of concerns, crucial for scalable and maintainable game development.

#### 6) LoadGame Class
The introduction of the LoadGame class as a distinct new class after refactoring it from GameModel promotes code modularity and follows the principles of clean architecture. 
This class centralizes the responsibility of loading game states, thereby simplifying the GameModel class. 
GameModel now focuses on the core gameplay mechanics, like maintaining game state and handling game logic, while LoadGame deals exclusively with the complexities of restoring saved game data. 
This separation enhances code readability and maintainability, making it easier to manage and extend the game's loading functionality independently. 
Overall, LoadGame serves as a specialized component part of the Model classes in the MVC pattern, reinforcing the Single Responsibility Principle.

#### 7) BlockView Class
The introduction of the BlockView class is to separate the visual representation of blocks from their logical functionality, which was previously combined in the Block class. 
By isolating the graphical aspects into BlockView, the Block class now purely focuses on game logic, simplifying both debugging and future enhancements and segregating the model part of the code from the view as part of the principles in MVC pattern.
This change improved the overall structure and scalability of the code, following the separations of concerns principle

### Additional New Classes:

#### 1) GameSound Class
The addition of the GameSound class significantly enhances the user experience by integrating auditory feedback into the gameplay as mentioned in the additional features above. 
This class is distinct from the game's primary logic it adds on top of the UI as it is also part of the user experience when playing the game, involved in the view classes following the MVC model, focuses solely on managing and playing sounds, aligning with the principle of single responsibility.
This class is responsible for initialization of the background, hit sound, game win and game over sound effect using media and media player. 
The creation and separation of sound management into its own class rather than putting it in the GameController class allows for easier maintenance and updates to the sound system in the future, without impacting other components of the game.

#### 2) CustomLevel Interface
The CustomLevel interface in the game serves as a blueprint for creating diverse level designs which is implemented in classes like CustomLevel17 and CustomLevel18.
Its introduction centralizes the structure and functionality necessary for the two custom levels, ensuring consistency across the game.
This approach facilitates the addition of new custom levels with unique challenges, promoting scalability and maintainability of the game's architecture and ensures the uniformity and predictability in how custom levels are defined and interacted with.

#### 3) CustomLevel17
CustomLevel17 is a specialized class implementing the CustomLevel interface, designed to elevate the game's difficulty with a unique layout. 
It introduces count breaker and wall blocks, strategically placed to enhance the challenge as mentioned in the additional features above. 
CustomLevel17, crafted as a new individual class implementing the CustomLevel interface rather than adding into a bigger already complex GameModel class, this allows a high degree of customization and specialization. 
The architecture of CustomLevel17 implementing the modularity and separations of concerns, facilitates easier updates and modifications, ensuring that the level can evolve over time to meet the changing demands of players and maintain its challenging nature.

#### 4) CustomLevel18
Similar to CustomLevel18, CustomLevel18 represents a custom design within the game with custom layout and new blocks such as ghost block and wall block. 
CustomLevel18 is distinctively encapsulated in its own class instead of adding it into the GameModel class. 
The decision to implement it as a separate class reflects a commitment to maintainable and scalable game design, and it also promotes adherence to the single responsibility principle as the custom level classes are used solely for initialize newly added custom levels.
As a standalone new class, it holds the flexibility to be enhanced, maintained and reconfigured with ease in the future.

# Modified Java Classes
#### 1) Main Class
The main class is the class that has been modified the most as all the methods, functionalities, fields that are part of the game's Model, View and controller are all clumped up and unorganized in the Main class of original code provided. 
Before refactoring the code into new classes, all the methods in main are refactored into smaller methods so that the model, view and controller components are separated in the main method first to increase understandability, readability and modularity before having to refactor them out and extracting them into the desired classes. 
This greatly increased the efficiency of refactoring and eases the extracting and migrating process of methods into the classes they belong to. 
After that, the methods are extracted and modified into their respected and suitable classes like new Model classes (Ball, Paddle, Physics, LoadGame and GameModel), View Classes (GameView and BlockView) and Controller classes(Game Controller).
The refactoring and modification of the main class follows the MVC pattern, the single responsibility principle, the separation of concerns principle, aimed to increased modularity, understandability and readability etc.

#### 2) Score Class
Changed the parameters of the Score class to receive pane root object passed from the GameView class instead of main object as the UI components have been refactored into a new class called Game View and the creation of new pane root object is now instantiated inside the GameView class.
Thus, the score class receives the root passed from GamaView and add and remove the show labels following the timeline. 
Modified the showGameOver and showWin methods with additions of backgrounds, final score labels and restart button to enhance the navigation and game logic of the game. 
Added a functional interface GameRestartAction which ensures that the restart action is implemented in GameController and passed as arguments as part of the logic of the restart button displayed when showGameOver and showWin is called.

#### 3) GameEngine Class
Refined the GameEngine by implementing an animation timeline for gameplay, opting for this approach over threads to enhance concurrency control.
While placing the thread in the platform run could address concurrent modification and index out-of-bounds issues, the adoption of a timeline is preferred due to its suitability for managing game animation threads. 
The timeline provides a structured and efficient execution of threads, optimizing control over animation processes and ensuring the synchronization of thread execution. 
This modification contributes to improved concurrency management, streamlined execution, and a more robust handling of game animations compared to a conventional thread-based approach. 
Added two new methods in GameEngine class called pause and resume which pauses and play the timeline animation when called. This inclusion is implemented to seamlessly integrate the newly introduced Pause button along with its corresponding functionality.

#### 4) loadSave Class
Altered the loadSave Class by adding the saveGame method into the class and renaming the method as saveGameState because saving the current game state is part of the logic in the loadSave class. 
Refactored the methods inside loadSave to increase modularity, readability and introduced more logical functions grouping.
Added new fields related to the new implementations and additional features introduced to ensure the current game state saved and loaded accurately.

#### 5) Block Class
Altered the Block class by introducing a new constructor parameter that accepts an additional integer argument, hitsToDestroy, as part of the modification. 
This modification is specifically tailored for a new block introduction known as the Count Breaker HP block. 
This unique block possesses the characteristic of requiring multiple hits to break, recorded using the hitsToDestroy variable, primarily featured in level 17. 
So, a new method decrementCount is introduced to decrement the hitsToDestroy of the Count Breaker Block if conditions are met. 
Changed the implementation of checkHitToBlock, through the addition of Epsilon which significantly improves floating-point precision, boosting collision detection accuracy. 
Utilized Axis-Aligned Bounding Box (AABB) detection, the ball's path is depicted as a rectangle, enhancing hit detection precision. 
This method accounts for corners, addressing specific hits through epsilon comparisons for precise interactions with block margins. 
Implemented new methods, such as checkAndProcessHit, introducing a 25-millisecond cool-down to ensure that only one code is returned per block collision. 
This cool-down period is crucial as it allows time for the ball to move out of the collision detection range after its physics have been altered due to the collision. 
This eliminates duplicated and conflicting physics due to multiple hitCodes returned. Implemented new method resetHitFlagOnce for wall block and count breaker block as both of these blocks will not be destroyed after 1 collision.
Thus, the isAlreadyHit flag introduced is crucial for detecting multiple hits with the blocks. 
Lastly, added the instantiation of BlockView object and passing of current block instance as argument to BlockView constructor as the UI components of drawing the block is moved into the BlockView class.

#### 6) BlockSerializable class
Modified the BlockSerializable class by adding a new parameter in the constructor which receives new integer argument - countBreakerCount. 
The class is modified to accommodate the newly added Count Breaker HP block used when loading, reading and saving the game state involving the block.

#### 7) Bonus Class
Changed the code from integrating bonuses and accessing the fields directly within the Block class to a dedicated encapsulated Bonus class. 
This change aligns with the principles of separation of concerns and modularity. 
By encapsulating bonus-related functionalities in their own class, the bonus class achieves clearer responsibility division, improved maintainability, and enhanced re-usability.
This approach ensures easier updates, extensions, and testing, significantly boosting the room for improvement and additions of the game's development using the bonus class. 
This modification aimed to produce a more organized, efficient, and future-proof codebase.

# Unexpected Problems
I have encountered a lot of problems and learnt a lot through this coursework. 
Unexpected problems and challenges leaded me to a significant learning experience. 
These unanticipated hurdles not only tested problem-solving abilities but also provided invaluable insights into the complexities in software maintenance.

### 1) Complexity of the original incomplete code
The complexity of the original code especially the main class causes the difficulty to understand the code and operation of the whole game code. 
The methods are all clumped together and not hard to understand as it has low modularity and readability. 
Without knowing and comprehending how the game program really works, this makes it very hard to start fixing the errors and refactoring. 
This lack of understanding created a formidable barrier to effectively address errors and undertake necessary refactoring Moreover, being less familiar with JavaFX and thread management added a layer of complexity to the task.
To overcome these challenges, a systematic approach was adopted. 
The complex code was broken down into smaller, manageable components for better understanding. This helps with comprehension of the complex code flow and understanding the functionalities of the methods and classes one by one. 
Trial and errors were such a big part in debugging, refactoring and addition to improve understanding, error diagnose and code fixing and improvement. 
These initial steps laid the groundwork for subsequent improvements and laid the foundation for a more structured and comprehensible codebase.

### 2) Bugs that arose from refactoring and extension
As the refactoring process unfolded and new components were introduced, unforeseen bugs emerged. 
These issues, ranging from logical errors to unexpected behavior which disrupts the original working code were fixed thorough debugging and careful consideration of the interactions between different parts of the code. 
The challenge lay not only in identifying and fixing these bugs but also in ensuring that the changes did not introduce new issues or compromise existing functionalities. 
This phase required repetitive testing and a lot of trials and error to address the bugs systematically and maintain the integrity of the codebase. 
This ensures that the work progress and maintaining the fundamentals functionality of the game.

