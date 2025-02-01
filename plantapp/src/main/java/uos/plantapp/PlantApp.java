package uos.plantapp;

import java.util.Random;
import javafx.util.Duration;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class PlantApp extends Application {
	Pane root;
	Scene scene;
	Canvas canvas;
	GraphicsContext gc;
	ArrayList<GameObject> list = new ArrayList<GameObject>();
	Random rnd = new Random(System.currentTimeMillis());
	private Button waterButton;
    private Button sunlightButton;
    private Subject subject;
    private Plant plant;
    private ProgressBar waterProgressBar;
    private ProgressBar sunLightProgressBar;
    private Label waterLevelLabel;
    private Label sunLightLevelLabel;
    private Label instructions;
    //Fields to end the game
    private Timeline gameTimer;
    private boolean isGameEnded = false;
    private boolean loseSoundPlayed = false; // Flag to track if lose sound has been played
    protected Image waterImg;

    //Adding media sounds
    Media waterSound = new Media(getClass().getResource("waterSound.mp3").toExternalForm());
    Media sunSound = new Media(getClass().getResource("sunSound.mp3").toExternalForm());
    Media winSound = new Media(getClass().getResource("winSound.mp3").toExternalForm());
    Media loseSound = new Media(getClass().getResource("loseSound.mp3").toExternalForm());
    
    //Create media players for each of the sounds
    MediaPlayer waterPlayer = new MediaPlayer(waterSound);
    MediaPlayer sunPlayer = new MediaPlayer(sunSound);
    MediaPlayer winPlayer = new MediaPlayer(winSound);
    MediaPlayer losePlayer = new MediaPlayer(loseSound);
    
	public static void main(String[] args) {
		launch(args);

	}
	
	AnimationTimer timer = new AnimationTimer() {
	    private long lastExecutionTime = 0;
	    private final long interval = 1_000_000_000; // 1 second in nanoseconds
	    

	    @Override
	    public void handle(long now) {
	        // Check if the game has ended
	        if (isGameEnded && !plant.isWithered()) {
	            // Stop the timer to freeze the game
	            timer.stop();
	            return; // Exit the handle method
	        }
	        
	        //Decrease water and sun light level manually every second, simulating the plant "needing" water and sun light
	        if (now - lastExecutionTime >= interval) {
	            for (GameObject obj : list) {
	                subject.setWaterLevel(subject.getWaterLevel() - 10);
	                subject.setSunlightLevel(subject.getSunlightLevel() - 10);
	                updateWaterProgressBar();
	                updateSunlightProgressBar();
	                if (subject.getWaterLevel() == 0 || subject.getSunlightLevel() == 0) {
	                    // Set withered and display message
	                    subject.setWithered(true);
	                    instructions.setText("Your plant has withered. Try focusing on both the water and sunlight because plants need both. Click Restart to try again. Good Luck!");
	                }
	                // Update plant state
	                plant.update();
	            }
	            
	            // Play the lose sound if the game has ended due to withering and the lose sound hasn't been played yet
	            if (plant.isWithered() && !loseSoundPlayed) {
	                // Play the lose sound
	                losePlayer.stop(); // Stop any previous instance
	                losePlayer.play();
	                // Disable buttons to prevent further interaction
	    		    waterButton.setDisable(true);
	    		    sunlightButton.setDisable(true);
	                loseSoundPlayed = true; // Set the flag to true
	            }
	            
	            // Update last execution time
	            lastExecutionTime = now;
	        }
	    }
	};

	

		@Override
		public void start(Stage primaryStage) throws Exception {
		    // Create canvas and graphics context
			//root = new Pane();
		    canvas = new Canvas(800, 600);
		    gc = canvas.getGraphicsContext2D();
		    
		    //Set canvas colour
		    gc.setFill(Color.LIGHTBLUE);
		    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		   

		    // Initialize plant and subject
		    subject = new Subject();
		    // Initialize plant and subject using the factory
		 // Initialize plant using the builder
		    PlantBuilder plantBuilder = new PlantBuilder(gc)
		            .setX(275)
		            .setY(110)
		            .setSubject(subject);
		    plant = plantBuilder.build();
		    //list.add(plant);
		    

		    // Create buttons
		    waterButton = new Button("Add Water");
		    waterButton.setOnAction(e -> {
		    	// Play the water sound
		        waterPlayer.stop(); // Stop any previous instance
		        waterPlayer.play();
		        subject.setWaterLevel(subject.getWaterLevel() + 10);
		        updateWaterProgressBar();
		        plant.update();
		        //System.out.println(plant.getWaterLevel());
		        //waterImg = new Image(Plant.class.getResource("wateringCan.png").toExternalForm());
		    });
		    waterButton.setLayoutX(20); // Set X position
	        waterButton.setLayoutY(20); // Set Y position

	        
	        System.out.println(subject.getWaterLevel());

		    sunlightButton = new Button("Add Sunlight");
		    sunlightButton.setOnAction(e -> {
		    	// Play the sun sound
		        sunPlayer.stop(); // Stop any previous instance
		        sunPlayer.play();
		        subject.setSunlightLevel(subject.getSunlightLevel() + 10);
		        updateSunlightProgressBar();
		        plant.update();
		    });
		    
		    // Create restart button
	        Button restartButton = new Button("Restart");
	        restartButton.setOnAction(e -> restartGame());

	        // Add restart button to layout
	        HBox restartBox = new HBox(restartButton);
	        restartBox.setLayoutX(20); // Adjust position as needed
	        restartBox.setLayoutY(20); // Adjust position as needed
	        


		    waterProgressBar = new ProgressBar(); 
		    waterProgressBar.setRotate(270);
		    waterProgressBar.setPrefSize(300, 25);
		    subject.setWaterLevel(100);//Manually set water level at 100 when the application starts
		    
		    
		    
		    sunLightProgressBar = new ProgressBar(); 
		    sunLightProgressBar.setRotate(270);
		    subject.setSunlightLevel(100); //Manually set sunlight level at 100 when the application starts
		    sunLightProgressBar.setPrefSize(300, 25);
		    
		    // Create labels for displaying water and sunlight levels
	        waterLevelLabel = new Label("Water Level: 0%");
	        sunLightLevelLabel = new Label("Sunlight Level: 0%");
		    
		    // Set CSS style for the sunLight progress bar to change its colour
		    String progressBarStyle = "-fx-accent: orange;";
		    sunLightProgressBar.setStyle(progressBarStyle);
		    
		    instructions = new Label("Welcome to this simple game, your aim is to keep the plant alive by providing water and sun to the plant! Quickly now the plant needs you!" + 
			        "\n" + "Use the buttons on screen or W and S on your keyboard");
		    instructions.setLayoutX(120);
		    instructions.setLayoutY(17);

		 // Layout for water-related elements
		    VBox waterBox = new VBox(10); // Vertical gap between elements
		    waterBox.getChildren().addAll(waterButton, waterLevelLabel, waterProgressBar);
		    waterBox.setLayoutX(45); 
		    waterBox.setLayoutY(225); 

		    // Layout for sunlight-related elements
		    VBox sunlightBox = new VBox(10); // Vertical gap between elements
		    sunlightBox.getChildren().addAll(sunlightButton, sunLightLevelLabel, sunLightProgressBar);
		    sunlightBox.setLayoutX(600); 
		    sunlightBox.setLayoutY(225); 

		    // Add canvas and layouts to the root layout
		    Pane root = new Pane();
		    root.setStyle("-fx-background-color: lightblue;");
		    root.getChildren().addAll(canvas, waterBox, sunlightBox, restartBox, instructions);
		    

		    // Create scene and set stage
		    Scene scene = new Scene(root);
		    primaryStage.setScene(scene);
		    primaryStage.show();
		    
	        // Add event handler for water key (W)
	        scene.setOnKeyPressed(e -> {
	        	//prevent the user from changing water or sunlight level after plant has withered or after the game is ended
	        	if (!plant.isWithered() && !isGameEnded) {
	            if (e.getCode() == KeyCode.W) {
	                // Add water action
			        waterPlayer.stop(); // Stop any previous instance
			        waterPlayer.play();
			        subject.setWaterLevel(subject.getWaterLevel() + 10);
			        updateWaterProgressBar();
			        plant.update();
	            }
	         // Add event handler for sunlight key (S)
	            else if (e.getCode() == KeyCode.S) {
	            	// Play the sun sound
			        sunPlayer.stop(); // Stop any previous instance
			        sunPlayer.play();
	                // Add sunlight action
	                subject.setSunlightLevel(subject.getSunlightLevel() + 10);
	                updateSunlightProgressBar();
	                plant.update();
	            }
	        	}
	        });
		    // Set up game timer to end after 45 seconds
		    //gameTimer = new Timeline(new KeyFrame(Duration.seconds(45), event -> endGame()));
	        gameTimer = new Timeline(new KeyFrame(Duration.seconds(15), event -> endGame())); //For the purposes of testing
		    gameTimer.setCycleCount(1); // Run only once
		    gameTimer.play();
		    
		    list.add(plant);
		    timer.start();
		}
		// Method to update water progress bar
		private void updateWaterProgressBar() {
		    double waterLevelPercentage = (double) subject.getWaterLevel() / 100.0;
		    waterProgressBar.setProgress(waterLevelPercentage);
		    waterLevelLabel.setText("Water Level: " + (int) (waterLevelPercentage * 100) + "%");
		}
		
		// Method to update water progress bar
		private void updateSunlightProgressBar() {
			double sunLightLevelPercentage = (double) subject.getSunlightLevel() / 100.0;
			sunLightProgressBar.setProgress(sunLightLevelPercentage);
			sunLightLevelLabel.setText("Sunlight Level: " + (int) (sunLightLevelPercentage * 100) + "%");
		}	
		
		private void restartGame() {
			waterPlayer.stop();
			sunPlayer.stop();
			winPlayer.stop();
			losePlayer.stop();
			isGameEnded = false;
			loseSoundPlayed = false;
	        // Reset plant and game state
	        subject.setWaterLevel(100);
	        subject.setSunlightLevel(100);
	        subject.setWithered(false);
	        plant.setMessageDisplayed(false);
	        instructions.setText("Welcome to this simple game, your aim is to keep the plant alive by providing water and sun to the plant! Quickly now the plant needs you!" + 
	        "\n" + "Use the buttons on screen or W and S on your keyboard");
	      
	        plant.update();
	        gameTimer.stop();
	        gameTimer.play();
	        timer.start();
	        // Re-enable the buttons
	        waterButton.setDisable(false);
	        sunlightButton.setDisable(false);
	    }
		
		private void endGame() {
			
			// Check if the game has already ended
		    if (isGameEnded) {
		        return; // Exit the method if the game has already ended
		        
		    }
		    
		    isGameEnded = true;
		    // Stop the game timer
		    gameTimer.stop();
		    
		    if(!plant.isWithered()) {
		    // Play the win sound
	        winPlayer.stop(); // Stop any previous instance
	        winPlayer.play();
		    // Display "win" message
		    instructions.setText("Congratulations! You have successfully sustained your plant. Press Restart to play again.");
		    }
		    
		    

		    // Disable buttons to prevent further interaction
		    waterButton.setDisable(true);
		    sunlightButton.setDisable(true);
			
		    
		}


}
