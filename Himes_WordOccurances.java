package application;
//Author Name: Zen Himes
//Date: 2/25/2022
//Program Name: Himes_FastFood
//Purpose: To graph word occurence in Macbeth
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
/**
 * @author Zen Himes
 */
public class Himes_WordOccurances extends Application {
	/**
	 * This is a program for finding word occurences and then printing them out on a bar graph
	 */
	private Button openBtn = new Button("Add txtFile");
	
	@Override
	public void start(Stage primaryStage) {
		/**
		 * Displays gui to user and makes a bar chart of the word occurence
		 * of a user picked text file
		 */
		try {
			openBtn.setText("Open .txt file");
			BorderPane root = new BorderPane();
			root.setTop(openBtn);
			/**
			 * Makes a button that allows the user to choose a text file
			 */
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			/**
			 * Makes the gui for the user at the start
			 */
			openBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					/**
					 * This reads the text file, makes a map of word occurences, orders that map, 
					 * sending the top 20 word occurences data to the bar graph
					 * @param arg0 - the button is clicked
					 */
					ObservableList<XYChart.Data<String, Integer>> data = FXCollections.observableArrayList();
					XYChart.Series<String, Integer> series = new XYChart.Series<>();
					FileChooser fc = new FileChooser();
					File selectedFile = fc.showOpenDialog(null);
					fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
					/**
					 * Opens dialogue for user that asks for a .txt file
					 */
					if (selectedFile != null) {
						ArrayList<String> textList = new ArrayList<String>();
						Scanner textScan = null;
						try {
							textScan = new Scanner(selectedFile);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						while (textScan.hasNext()) { //scans every line of the text and adds it to the ArrayList
							textList.add(textScan.next());
						}
						textScan.close();
						String listString ="";
						for (String s: textList) {
							listString += s+ " ";
						}
						/**
						 * turns the file into a list of strings 
						 * and then concatenates that list into one string in order to 
						 * pass it to the word frequency application
						 */
						Map<String, Integer> textMap = wordFrequency(listString);
						Map<String, Integer> sortedMap = textMap.entrySet()
								.stream().sorted((e2,e1)->
								Integer.compare(e1.getValue(), e2.getValue()))
								.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, 
										(e1,e2)-> e1, LinkedHashMap::new));
						/**
						 * creates word occurence map and then sorts that map.
						 */
						List<Map.Entry<String, Integer>> sortedList = new ArrayList(sortedMap.entrySet());
						/**
						 * Turns the sorted map into a list
						 */
						int count = 0;
						for (Entry<String,Integer> e : sortedList) {
							String k = String.valueOf(((Map.Entry<String, Integer>) e).getKey());
							Integer v = e.getValue();
							data.add(new Data<String, Integer>(k,v));
							count++;
						    if (count>=20||count>=sortedList.size()) {
						    	break;
						    }
						}   
						/**
						 * Sends the word occurence data to the XY chart data and if it goes over 20, it stops
						 */
						series.getData().addAll(data);
						CategoryAxis xAxis = new CategoryAxis();
						NumberAxis yAxis = new NumberAxis(0,800,100);
						xAxis.setLabel("Word");
						yAxis.setLabel("Frequency");
						BarChart<String, Integer> barChart = new BarChart(xAxis,yAxis);
						barChart.setTitle("Frequency of Words in Text File");
						barChart.getData().add(series);
						Group barGroup = new Group();
						barGroup.getChildren().add(barChart);
						Scene barScene = new Scene(barGroup, 600,400);
						primaryStage.setScene(barScene);
						primaryStage.show();
						/**
						 * Adds the data to the xychart series and then displays the chart in a new window for the user
						 */
					}
				}
			});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		/**
		 * Launches the JavaFX program
		 * @param args
		 */
	}
	public static Map<String, Integer> wordFrequency(String text) {
		/**
		 * Makes a map of word occurences with (word, times appeared) 
		 * @param text - a .txt file turned into a string
		 * @return wordMap - a map with a word as a key and the amount of times it occured as the value
		 */
		String trimmed = text.replaceAll("(?:--|[\\[\\]{}()+/\\\\])", " ");
		String lowercased = trimmed.toLowerCase();
		String words[] = lowercased.split(" ");
		Map<String, Integer> wordMap = new HashMap<>();
		for (String str:words) {
			if (wordMap.containsKey(str)) {
				wordMap.put(str, 1+wordMap.get(str));
			}
			else {
				wordMap.put(str, 1);
			}
		}
		/**
		 * Makes an array of words based on the string and 
		 * then for each word in the array, 
		 * if the word has been added to the word map, add 1 to value
		 * else add that word as a new key in the word map with a 1 as its value
		 */
		return wordMap;
	}
}
