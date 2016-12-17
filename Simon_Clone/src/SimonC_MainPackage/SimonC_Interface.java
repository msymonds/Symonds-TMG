/**
 * 
 * Michael Symonds
 * The Symonds Memory Game
 * 6/3/2016
 * 
 * 
 * 
 * 
 */


package SimonC_MainPackage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimonC_Interface extends Application implements EventHandler<ActionEvent>  {
	
	// toggle to print status messages to the console while executing
	private final boolean debugMode = false; 
	
	// for audio media/sfx
	private List<String> audioPaks = new ArrayList<String>();
	private URL resource1, resource2, resource3, resource4, resource5, resource6;
	private AudioClip boop1, boop2, boop3, boop4, youWin, youFail;
	
	// Animation sequence (readySetGo)
    private Timeline timeline;
    private int ctr = 0;
	
    // for startPanel
    private GridPane startPanel;
    private ComboBox<String> sfxCB;
    private Slider seqLenSlider;
    private SquareButton startB;
    private Label 	directionsL, sfxThemeL, seqLenL, sliderL, 
    				separater, separater2, separater3, separater4;
    
    // for sfxPanel
    private VBox sfxPanel;
    private VBox setupPanel;

	// for Score Panel
	private GridPane scorePanel;
	private Label lab, lab2, goAgain, yesLabel;
	private SquareButton yes, no;
	
	// for titlePanel
	private VBox titlePanel;
	private Label titleLab;

	// for boardPanel
	private GridPane boardPanel;
	private SquareButton [] squareButtons;

	// for main panel
	private BorderPane mainPanel;
	private Scene myScene;
	private Image bGround;
	
	private GameStart game;
	private boolean okToCheck = false; // flag guard for checkPlayer()
		
	public static void main(String[] args){	launch(args); }

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		// Load audio source file locations
		FileMonster.initializeAudio("audio.txt", audioPaks);
		
		// 4 Big Buttons with action/mouse event handlers
		makeButtons();
		
		// setup startPanel
		makeStartPanel();
		
		// setup boardPanel
		makeBoardPanel();
		
		// setup scorePanel
		makeScorePanel();

		// setup titlePanel
		titlePanel = new VBox();
		titleLab = new Label(" S y m o n d s :   T h e   O t h e r  M e m o r y   G a m e ");
		titleLab.setTextAlignment(TextAlignment.CENTER);
		titleLab.setAlignment(Pos.TOP_CENTER);
		titleLab.setStyle("-fx-font-weight: bold;");
		titleLab.setTextFill(Color.WHITE);
		titleLab.setFont(new Font("Impact", 22));
		titlePanel.setAlignment(Pos.CENTER);
		titlePanel.getChildren().add(titleLab);

		// setup mainPanel
		mainPanel = new BorderPane();
		mainPanel.setTop(titlePanel);
		mainPanel.setCenter(boardPanel);
		mainPanel.setPrefSize(400,400);
		mainPanel.setPadding(new Insets(20.0));
		
		bGround = new Image("resources/images/UI_BG7.jpg");
		BackgroundImage bgi = new BackgroundImage(bGround, null, null, null, null);
		Background bg = new Background(bgi);
		mainPanel.setBackground(bg);
		
		// Actors go to one, please, and...ACTION!!!!
		myScene = new Scene(mainPanel);
		primaryStage.setTitle("The Symonds Memory Game");
		primaryStage.setHeight(640);
		primaryStage.setWidth(590);
		primaryStage.setResizable(false);
		primaryStage.setScene(myScene);
		primaryStage.show();
	}
	
	private void loadAudioResources(int choice){
		String f = audioPaks.get(choice);
		
		resource1 = getClass().getResource((f + "/boop1.wav").trim());
		resource2 = getClass().getResource((f + "/boop2.wav").trim());
		resource3 = getClass().getResource((f + "/boop3.wav").trim());
		resource4 = getClass().getResource((f + "/boop4.wav").trim());
		resource5 = getClass().getResource((f + "/youWin.wav").trim());
		resource6 = getClass().getResource((f + "/youFail.wav").trim());
	}
	
	private void prepareMedia(int choice){
		loadAudioResources(choice);
		boop1 = new AudioClip(resource1.toString());
		boop2 = new AudioClip(resource2.toString());
		boop3 = new AudioClip(resource3.toString());
		boop4 = new AudioClip(resource4.toString());
		youWin = new AudioClip(resource5.toString());
		youFail = new AudioClip(resource6.toString());
	}

	public class SquareButton extends Button {
		public final int ID;
		public SquareButton(int ID) {
			Font f = new Font("Comic Sans MS",12);
			this.setAlignment(Pos.CENTER);
			Arc arcShape = new Arc(2.5, 15.0, 50.0, 30.0, -90.0, 180);
			arcShape.setType(ArcType.OPEN);
			arcShape.setStrokeWidth(3);
			this.setShape(arcShape);
			this.setFont(f);
			this.ID = ID;
			this.setOnAction(SimonC_Interface.this);
		}
	} // SquareButton
	
	private void makeButtons(){
		squareButtons = new SquareButton[4];
		for(int i = 0; i < 4; i++){
			squareButtons[i] = new SquareButton(i);
			squareButtons[i].setPrefSize(120.0, 120.0);
		}
	
		squareButtons[0].setStyle("-fx-base: rgb(244, 144, 144);"); // light red
		squareButtons[1].setStyle("-fx-base: rgb(154, 144, 244);"); // light blue
		squareButtons[2].setStyle("-fx-base: rgb(144, 244, 154);"); // light green
		squareButtons[3].setStyle("-fx-base: rgb(244, 239, 144);"); // light yellow
		squareButtons[0].setRotate(-90);
		squareButtons[1].setRotate(180);
		squareButtons[3].setRotate(90);
				
		squareButtons[0].addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				boop1.play();
				squareButtons[0].setStyle("-fx-base: rgb(233, 32, 32);"); // bright red
				} });
		
		squareButtons[0].addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				squareButtons[0].setStyle("-fx-base: rgb(244, 144, 144);"); // light red
				} });
	
		squareButtons[1].addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				boop2.play();
				squareButtons[1].setStyle("-fx-base: rgb(45,42,234);"); // bright blue
				} });
		
		squareButtons[1].addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				squareButtons[1].setStyle("-fx-base: rgb(154, 144, 244);"); // light blue
				} });

		squareButtons[2].addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				boop3.play();
				squareButtons[2].setStyle("-fx-base: rgb(42, 244, 42);"); // bright green
				} });
		
		squareButtons[2].addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				squareButtons[2].setStyle("-fx-base: rgb(144, 244, 154);"); // light green
				} });

		squareButtons[3].addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				boop4.play();
				squareButtons[3].setStyle("-fx-base: rgb(233, 224, 42);"); // bright yellow
				} });
		
		squareButtons[3].addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				squareButtons[3].setStyle("-fx-base: rgb(244, 239, 144);"); // light yellow
				} });

	} // makeButtons
	
	@Override
	public void handle(ActionEvent e) {
		SquareButton theButton = (SquareButton) e.getSource();
		int squareNum = theButton.ID;
		if(debugMode)
			System.out.println("button press at ID# " + squareNum);
		
		// if startB was pressed, initialize new game
		if (squareNum == 5){
			int sfxChoice = sfxCB.getSelectionModel().selectedIndexProperty().intValue();
			int sequenceLength = (int)seqLenSlider.getValue();
			
			if(debugMode)
				System.out.println("SFX choice: " + sfxChoice + " Seq. Len: " + sequenceLength);
					
			startPanel.getChildren().remove(setupPanel);
			startPanel.add(scorePanel, 1, 1);
			prepareMedia(sfxChoice);
			startGame(sequenceLength);
		} // ssquareNum == 5 (game initialization process)
		else if (squareNum == 6)
		{
			yes.setDisable(true);
       	 	no.setDisable(true);
       	 	goAgain.setVisible(false);
       	 	yes.setVisible(false);
       	 	no.setVisible(false);
			resetBoard();
		}
		else if (squareNum == 7)
		{
			yes.setDisable(true);
       	 	no.setDisable(true);
       	 	goAgain.setVisible(false);
       	 	yes.setVisible(false);
       	 	no.setVisible(false);
			Timeline tl2 = new Timeline();
   			
   			EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                	lab2.setText("GOOD BYE!!");
                }};

            EventHandler<ActionEvent> event4 = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                   	System.exit(0);
                }};
                    
            tl2.getKeyFrames().add(new KeyFrame(new Duration(500), event3));
            tl2.getKeyFrames().add(new KeyFrame(new Duration(1500), event4));
            tl2.play();
		}
		else
		{
			if(okToCheck)
				checkPlayer(squareNum);
		}
	} // handle
	
	private void makeStartPanel(){
		sfxPanel = new VBox();
		setupPanel = new VBox();
		startPanel = new GridPane();
		startPanel.setAlignment(Pos.CENTER);
		startB = new SquareButton(5);
		Ellipse ellipse = new Ellipse(); {
				ellipse.setCenterX(50.0f);
				ellipse.setCenterY(50.0f);
				ellipse.setRadiusX(50.0f);
				ellipse.setRadiusY(25.0f);}
		startB.setShape(ellipse);
		startB.setText("START");
		startB.setPadding(new Insets(5));
		startB.setStyle("-fx-base: rgb(22, 224, 22); -fx-font-weight: bold;" ); // bright green
		sfxCB = new ComboBox<String>(FXCollections.observableArrayList(
			    "Original", "Agogo", "Big Ben", "Birds", "Bottle Blow",
			    "DJ Toolbox", "Droid", "Echo Drops", "Fast Perk", "Fret Noise",
			    "Gunshot", "Hip House", "Koto", "Ocarina", "Piano", 
			    "Schwarzenegger", "Telephone", "The Mod Knob", "Trinidad")	);
		sfxCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value) {
				prepareMedia(new_value.intValue());
			}
		});
		sfxCB.getSelectionModel().selectFirst();
		sfxCB.setEditable(false);
		sfxCB.setPrefHeight(5);
		directionsL = new Label("(What the hell is this?)");
		directionsL.setTextAlignment(TextAlignment.CENTER);
		directionsL.setTextFill(Color.HONEYDEW);
		sfxThemeL = new Label("Choose a sound theme!");
		sfxThemeL.setFont(new Font("Comic Sans MS", 12));
		sfxThemeL.setTextAlignment(TextAlignment.CENTER);
		sfxThemeL.setTextFill(Color.HONEYDEW);
		seqLenL = new Label("Choose a sequence length!");
		seqLenL.setFont(new Font("Comic Sans MS", 12));
		seqLenL.setTextAlignment(TextAlignment.CENTER);
		seqLenL.setTextFill(Color.HONEYDEW);
		sliderL = new Label();
		sliderL.setTextFill(Color.WHITE);
		separater = new Label(" ");
		separater.setTextAlignment(TextAlignment.CENTER);
		separater2 = new Label(" ");
		separater2.setTextAlignment(TextAlignment.CENTER);
		separater3 = new Label(" ");
		separater3.setTextAlignment(TextAlignment.CENTER);
		separater4 = new Label(" ");
		separater4.setTextAlignment(TextAlignment.CENTER);
		seqLenSlider = new Slider();
		Font f6 = new Font("Comic Sans MS",10);
		separater.setFont(f6);
		separater2.setFont(f6);
		separater3.setFont(f6);
		separater4.setFont(f6);
		sliderL.setFont(f6);
		seqLenSlider.setMin(5);
		seqLenSlider.setMax(25);
		seqLenSlider.setValue(10);
		sliderL.textProperty().setValue(
                "Sequence Length: " + String.valueOf((int) seqLenSlider.getValue()));
		seqLenSlider.setMajorTickUnit(2);
		seqLenSlider.setMinorTickCount(1);
		seqLenSlider.setBlockIncrement(1);
		seqLenSlider.valueProperty().addListener(new ChangeListener<Object>() {

            @Override
            public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
            	sliderL.textProperty().setValue(
                        "Sequence Length: " + String.valueOf((int) seqLenSlider.getValue()));

            }
        });
		GridPane.setColumnSpan(directionsL,  3);
		
		sfxPanel.getChildren().addAll(sfxThemeL, sfxCB, separater);
		setupPanel.getChildren().addAll(seqLenL, seqLenSlider, sliderL, separater2, startB, separater3, directionsL);
		
		startPanel.add(sfxPanel,  1,  0);
		startPanel.add(setupPanel,  1,  1);
		
		sfxPanel.setAlignment(Pos.CENTER);
		setupPanel.setAlignment(Pos.CENTER);
		
		GridPane.setHalignment(sfxPanel, HPos.CENTER);
		GridPane.setValignment(sfxPanel,  VPos.TOP);
		GridPane.setHalignment(setupPanel, HPos.CENTER);
		startPanel.setGridLinesVisible(false);
		
		// Tooltip panel for directionsL
		final Tooltip tooltip = new Tooltip();
		tooltip.setText(
		    "This is a simple game based on the original popular game\n" +
		    "Simon: The Memory Game. A sequence of sounds will\n"  +
		    "play starting with one, then adding a new sound every \n" +
		    "time the player can sucessfully repeat the sequence \n" +
		    "back in the proper order until the chosen maximum \n" +
		    "sequence length is reached, and the the player wins.\n\n" +
		    "If not, you FAIL, which is fine, because you probably\n" +
		    "won't remember losing anyway. Enjoy!!"
		);
		tooltip.setWrapText(true);
		directionsL.setTooltip(tooltip);
		directionsL.setOnMouseEntered(evt -> {           
			  //if (directionsL.getLayoutBounds().contains(evt.getX(), evt.getY())) {                
				    //if (!tooltip.isShowing()) {
				      tooltip.show(directionsL, evt.getX(), evt.getY());
				      
				    //}
				 // }
				});
		
		directionsL.setOnMouseExited(evt -> {
			  //if (directionsL.getLayoutBounds().contains(evt.getX(), evt.getY())) {
			    //if (!tooltip.isShowing())
			  //  	tooltip.show(directionsL, evt.getX(), evt.getY());
			 // } else {
				  tooltip.hide();
			 // }
			});
	}
	
	private void makeBoardPanel(){
		boardPanel = new GridPane();
		boardPanel.getColumnConstraints().add(new ColumnConstraints(150)); // column 0 is 150 wide
		boardPanel.getColumnConstraints().add(new ColumnConstraints(225)); // column 1 is 225 wide
		boardPanel.getColumnConstraints().add(new ColumnConstraints(150)); // column 2 is 150 wide
		boardPanel.getRowConstraints().add(new RowConstraints(150)); // column 0 is 150 wide
		boardPanel.getRowConstraints().add(new RowConstraints(225)); // column 1 is 225 wide
		boardPanel.getRowConstraints().add(new RowConstraints(150)); // column 2 is 150 wide
		boardPanel.setAlignment(Pos.CENTER);
		boardPanel.add(squareButtons[0], 1,  0);
		boardPanel.add(squareButtons[1], 0,  1);
		boardPanel.add(squareButtons[2], 2,  1);
		boardPanel.add(squareButtons[3], 1,  2);
		boardPanel.add(startPanel, 1, 1);
		GridPane.setHalignment(squareButtons[0],  HPos.CENTER);
		GridPane.setHalignment(squareButtons[1],  HPos.CENTER);
		GridPane.setHalignment(squareButtons[2],  HPos.CENTER);
		GridPane.setHalignment(squareButtons[3],  HPos.CENTER);
		GridPane.setHalignment(startPanel,  HPos.CENTER);
		//boardPanel.setGridLinesVisible(true);
	}
	
	private void makeScorePanel(){
		scorePanel = new GridPane();
		scorePanel.setAlignment(Pos.CENTER);
		lab = new Label("");
		lab2 = new Label("");
		goAgain = new Label("Another\nGame?");
		lab.setAlignment(Pos.CENTER);
		lab.setTextAlignment(TextAlignment.CENTER);
		lab.setFont(new Font("Comic Sans MS", 18));
		lab.setTextFill(Color.WHITE);
		lab.setStyle("-fx-font-weight: bold;");
		lab2.setTextAlignment(TextAlignment.CENTER);
		lab2.setAlignment(Pos.CENTER);
		lab2.setFont(new Font("Comic Sans MS", 18));
		lab2.setTextFill(Color.WHITE);
		lab2.setStyle("-fx-font-weight: bold;");
		goAgain.setAlignment(Pos.CENTER);
		goAgain.setTextAlignment(TextAlignment.CENTER);
		goAgain.setFont(new Font("Comic Sans MS", 14));
		goAgain.setTextFill(Color.WHITE);
		goAgain.setStyle("-fx-font-weight: bold;");
		yesLabel  = new Label("YES");
        yesLabel.setRotate(180);
        yesLabel.setStyle("-fx-base: rgb(22, 224, 22);"); // bright green
        yesLabel.setTextFill(Color.WHITE);
        yes = new SquareButton(6);
		no = new SquareButton(7);
		yes.setStyle("-fx-base: rgb(22, 224, 22);"); // bright green
		yes.setRotate(180);
		yes.setGraphic(new Group(yesLabel));
		yes.setPadding(new Insets(5));
		no.setText("NO");
		no.setPadding(new Insets(5));
		no.setStyle("-fx-base: rgb(233, 32, 32);"); // bright red
		scorePanel.add(lab, 1, 2);
		scorePanel.add(lab2, 1, 3);
		scorePanel.add(goAgain,  1,  3);
		scorePanel.add(yes,  0,  5);
		scorePanel.add(no,  2,  5);
		goAgain.setVisible(false);
		yes.setVisible(false);
		no.setVisible(false);
		yes.setDisable(true);
		no.setDisable(true);
		yes.setOnAction(SimonC_Interface.this);
		no.setOnAction(SimonC_Interface.this);
		GridPane.setHalignment(lab, HPos.CENTER);
		GridPane.setHalignment(lab2, HPos.CENTER);
		GridPane.setHalignment(yes,  HPos.RIGHT);
		GridPane.setHalignment(no,  HPos.LEFT);
		//scorePanel.setGridLinesVisible(true);
	}
	
	private void startGame(int length){
		game = new GameStart(length);
		if(debugMode)
			System.out.println(game.toString());
		Runnable task = () -> {
			try{readySetGo();}
        	catch(Exception e){
        		System.out.println("Thread task was interrupted in startGame()...\n" + e);
        	}
		};
		Timeline timeline2 = new Timeline();
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	new Thread(task).start();
            }};
		Duration dur = new Duration(750);
		timeline2.getKeyFrames().add(new KeyFrame(dur, event));
		timeline2.play();		
	} // startGame

	private void readySetGo(){
		 timeline = new Timeline();
		 
		 //create a keyFrame, the keyValue is reached at time 2s
	        Duration duration1 = Duration.millis(750);
	        Duration duration2 = Duration.millis(1750);
	        Duration duration3 = Duration.millis(2750);
	        Duration duration4 = Duration.millis(3500);
	        //one can add a specific action when the keyframe is reached
	        EventHandler<ActionEvent> onFinished1 = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	            	lab2.setText("Ready...");
	            }
	        };
	        
	        EventHandler<ActionEvent> onFinished2 = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	                 lab2.setText("Set...");
	            }
	        };
	        
	        EventHandler<ActionEvent> onFinished3 = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	                 lab2.setText("GO!!!");
	            }
	        };
	        
	        EventHandler<ActionEvent> onFinished4 = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	            	try{startSequenceThread(game);}
	            	catch(Exception e){
	            		System.out.println("Was interrupted in readySetGo()...");
	            	}
	            }
	        };
	 
	  KeyFrame keyFrame1 = new KeyFrame(duration1, onFinished1);
	  KeyFrame keyFrame2 = new KeyFrame(duration2, onFinished2);
	  KeyFrame keyFrame3 = new KeyFrame(duration3, onFinished3);
	  KeyFrame keyFrame4 = new KeyFrame(duration4, onFinished4);
	 
	        //add the keyframe to the timeline
	        timeline.getKeyFrames().add(keyFrame1);
	        timeline.getKeyFrames().add(keyFrame2);
	        timeline.getKeyFrames().add(keyFrame3);
	        timeline.getKeyFrames().add(keyFrame4);
	 
	        timeline.play();
	} // readySetGo
	
	private void startSequenceThread(GameStart game) {
		Runnable task = () -> {
			try{playSequence(game);}
        	catch(Exception e){
        		System.out.println("Was interrupted in startSequenceThread()...\n" + e);
        	}
		};
		new Thread(task).start();
	} // startSequenceThread()

	private void playSequence(GameStart game) throws InterruptedException{
		// prepare current sequence to play in keyframe timeline
		Timeline timeline1 = new Timeline();
		int sequenceCount = game.getSequenceCounter();
		Duration durs;
		EventHandler<ActionEvent> event;
		int standUnit = 750;
		ctr = 0;
		
		for(int i = 0; i <= sequenceCount; i++){
			durs = new Duration(standUnit * (i+1));
			event = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	            	playTone(game.sequence[ctr]);
	            	ctr++;
	            }};
	        timeline1.getKeyFrames().add(new KeyFrame(durs, event));
	    }
		
		// final event to re-enable button check
		event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	okToCheck = true;
            	if(debugMode)
            		System.out.println("gameOn is now true");
            }};
            durs = new Duration(standUnit * (sequenceCount + 2));
            timeline1.getKeyFrames().add(new KeyFrame(durs, event));
		
		if(debugMode)
		{
			ObservableList<KeyFrame> keyList = timeline1.getKeyFrames();
			System.out.println("# of KeyFrame events: " + keyList.size()
				+ "\n" + keyList.toString());
		}
			
		timeline1.play();
	} // playSequence()
	
	private void playTone(int sqNum){
		timeline = new Timeline();
		 
		 //create a keyFrame, the keyValue is reached at time speced in ms
	        Duration duration1 = Duration.millis(100);
	        Duration duration2 = Duration.millis(550);
	        Duration duration3 = Duration.millis(740);
	        
	        EventHandler<ActionEvent> onFinished1 = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	            	switch (sqNum){
	    			case 0: 
	    			{
	    				if(debugMode)
	    					System.out.println("In playTone(in press), case 0");
	    				squareButtons[0].setStyle("-fx-base: rgb(233, 32, 32);"); // bright red
	    				boop1.play();
	    				break;
	    			}
	    			case 1:
	    			{
	    				if(debugMode)
	    					System.out.println("In playTone(in press), case 1");
	    				squareButtons[1].setStyle("-fx-base: rgb(45,42,234);"); // bright blue
	    				boop2.play();
	    				break;
	    			}
	    			case 2:
	    			{
	    				if(debugMode)
	    					System.out.println("In playTone(in press), case 2");
	    				squareButtons[2].setStyle("-fx-base: rgb(42, 244, 42);"); // bright green
	    				boop3.play();
	    				break;
	    			}
	    			case 3:
	    			{
	    				if(debugMode)
	    					System.out.println("In playTone(in press), case 3");
	    				squareButtons[3].setStyle("-fx-base: rgb(233, 224, 42);"); // bright yellow
	    				boop4.play();
	    				break;
	    			}
	    			default: System.out.println("Error in playTone(in press), currentButton: " + sqNum);
	    			} // switch
	            }
	        };
	        
	        EventHandler<ActionEvent> onFinished2 = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	            	switch (sqNum){
	    			case 0: 
	    			{
	    				if(debugMode)
	    					System.out.println("In playTone(out press), case 0");
	    				squareButtons[0].setStyle("-fx-base: rgb(244, 144, 144);"); // light red
	    				break;
	    			}
	    			case 1:
	    			{
	    				if(debugMode)
	    					System.out.println("In playTone(out press), case 1");
	    				squareButtons[1].setStyle("-fx-base: rgb(154, 144, 244);"); // light blue
	    				break;
	    			}
	    			case 2:
	    			{
	    				if(debugMode)
	    					System.out.println("In playTone(out press), case 2");
	    				squareButtons[2].setStyle("-fx-base: rgb(144, 244, 154);"); // light green
	    				break;
	    			}
	    			case 3:
	    			{
	    				if(debugMode)
	    					System.out.println("In playTone(out press), case 3");
	    				squareButtons[3].setStyle("-fx-base: rgb(244, 239, 144);"); // light yellow
	    				break;
	    			}
	    			default: System.out.println("Error in playTone(out press), currentButton: " + sqNum);
	    			} // switch
	            }
	        };

	        EventHandler<ActionEvent> onFinished3 = new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	            	if(debugMode)
	            		System.out.println("timeline about to stop");
	            }
	         };
	            
	  	  KeyFrame keyFrame1 = new KeyFrame(duration1, onFinished1);
		  KeyFrame keyFrame2 = new KeyFrame(duration2, onFinished2);
		  KeyFrame keyFrame3 = new KeyFrame(duration3, onFinished3);
		  
	        timeline.getKeyFrames().add(keyFrame1);
	        timeline.getKeyFrames().add(keyFrame2);
	        timeline.getKeyFrames().add(keyFrame3);

	        timeline.play();
	        
	} // playTone()
	
	private void checkPlayer(int n){
		if(game.sequence[game.getCheckCounter()] == (n)){			
			if(game.getCheckCounter() == game.getSequenceCounter()){
				if(debugMode)
		    		System.out.println("gameOn is now false");
				okToCheck = false;
				if((game.getSequenceCounter() + 1) == game.sequence.length){
					awardPoints(true);
					return;
				}  // all player responses pass, end of sequence
				game.incrementSequenceCounter();
				game.resetCheckCounter();
				lab2.setText("" + game.getSequenceCounter());
				try{startSequenceThread(game);}
            	catch(Exception e){
            		System.out.println("Was interrupted in checkPlayer()...");
            	}
				return;
			}
			game.incrementCheckCounter();
		}
		else
		{
			awardPoints(false);
			return;
		}
	} // checkPlayer()
	
	private void awardPoints(boolean win){
		Timeline tl1 = new Timeline();
		Duration d1 = new Duration(500);
		Duration d2 = new Duration(2000);
		
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	if(win)
        		{
        			lab2.setText("OMG, You won!!");
        			youWin.play();
        		}
        		else
        		{
        			lab2.setText("FAIL!!!");
        			youFail.play();
        		}
            }};
            
        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
             public void handle(ActionEvent t) {
            	 lab2.setText("");
            	 yes.setDisable(false);
            	 no.setDisable(false);
            	 goAgain.setVisible(true);
            	 yes.setVisible(true);
            	 no.setVisible(true);
            	 
           		}};
            
        tl1.getKeyFrames().add(new KeyFrame(d1, event));
        tl1.getKeyFrames().add(new KeyFrame(d2, event2));
		tl1.play();
		
	} // awardPoints
	
	private void resetBoard(){
		game.resetCheckCounter();
		game.resetSequenceCounter();
		startPanel.getChildren().remove(scorePanel);
		startPanel.add(setupPanel, 1, 1);
	} // resetBoard
	
} // SimonC_Interface
