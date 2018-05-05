/*Mohammad Uppal & Mohamed Seliman*/

package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import javafx.event.ActionEvent; 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ListController {
	@FXML ListView<String> listView = new ListView<String>();
	@FXML TextField songDisp = new TextField();
	@FXML TextField artistDisp = new TextField();
	@FXML TextField albumDisp = new TextField();
	@FXML TextField yearDisp = new TextField();
	@FXML Button deleteButton, editButton, addButton, addOk, addCancel, editOk, editCancel, deleteOk, deleteCancel;
	
	private ObservableList<Song> songList;
	private static int savedIndex;
	private static Stage savedStage;
	
	public void start() throws Exception, IOException{
		//create an ObservableList
		//from an ArrayList
		songList = FXCollections.observableArrayList();
		//Filename for songs list
		BufferedReader songsTxt = null;
		String fileName = "controller/songs.txt";
		String line;
		try {
            // FileReader reads text files in the default encoding.
			songsTxt = new BufferedReader(new FileReader(fileName));
		
			//Reads each line in txt file and gets rid of all spaces and makes everything uppercase
			//Each line is stored and split into array songInfo elements and a Song object is created with
			//the elements
            while((line = songsTxt.readLine()) != null && !line.isEmpty()) {
            	line.replaceAll("\\s+", "");
            	line.toUpperCase();
            	String [] songInfo = line.split(",");
            	Song song = new Song(songInfo[0], songInfo[1], songInfo[2], (songInfo[3].matches("\\d+") ? Integer.parseInt(songInfo[3]): -1));
       
            	//If addSong fails, then there were no songs or there was a duplicate
            	addFileSongs(song);
            	line = null; 
            }
            songsTxt.close();
		} catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        } 
		updateList();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

	        public void run() {
	        	try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
					for(int i = 0; i<songList.size(); i++){
						writer.write(songList.get(i).name + "," + songList.get(i).artist + "," + songList.get(i).album + "," + Integer.toString(songList.get(i).year) +"\n");
					}
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	
	            
	        }
	    }));
	}
	
	public void start(Stage mainStage) {                 
	   // select the first item
		savedStage = mainStage;
	   if(songList.isEmpty())
		   return;
	   listView.getSelectionModel().select(0);
	   Song song= songList.get(listView.getSelectionModel().getSelectedIndex());

	   if(song!=null){
		   songDisp.setText(song.name);
		   artistDisp.setText(song.artist);
		   albumDisp.setText(song.album);
		   yearDisp.setText(Integer.toString(song.year));
		   
		   // set listener for the items
	   }
	   listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showItem(mainStage)); 
	    
	}
	
	private void updateList(){ //Because list view is string only, need second list that has to be updated to affect other fields like artist		
        if(songList!=null)
    		songList.sort(null); //Alphabetically sort here
        
        ObservableList<String> temp = FXCollections.observableArrayList();
        for(Song song: songList) temp.add(song.name);

        listView.setItems(temp);
        listView.getSelectionModel().select(listView.getSelectionModel().getSelectedIndex());
	}

	private void addFileSongs(Song song){
		if(songList.indexOf(song)!= -1){
			Alert alert = new Alert (AlertType.INFORMATION);
    		alert.setTitle("Song Add Failure");
    		alert.setHeaderText("Song could not be added because a duplicate of the song exists!");
    		alert.setContentText("Song Name: " + song.name + "\nArtist Name: " + song.artist);
    		alert.showAndWait();
    		}
		else{
			songList.add(song);
		}
		return;
	}
	
	private void addSong(Song song){
		
		if(songList.indexOf(song)!= -1){
			Alert alert = new Alert (AlertType.INFORMATION);
    		alert.setTitle("Song Add Failure");
    		alert.setHeaderText("Song could not be added because a duplicate of the song exists!");
    		alert.setContentText("Song Name: " + song.name + "\nArtist Name: " + song.artist);
    		alert.showAndWait();
    	}
		else{
			songList.add(song);
			updateList();
			listView.getSelectionModel().select(songList.indexOf(song));
		}
		return;
	}
	
	private void removeSong(Song song){
		songList.remove(song);
		savedIndex = listView.getSelectionModel().getSelectedIndex();
		updateList();
		return;
	}
	
	 public void pressButton(ActionEvent e) {
		 Button b = (Button)e.getSource();
		 if(b!= addButton && songList.isEmpty())
			 return;
		 
		 if (b == deleteButton) {
			deleteOk.setDisable(false);
			deleteCancel.setDisable(false);
			
			deleteButton.setDisable(true);
			addButton.setDisable(true);
			editButton.setDisable(true);
		  }
		 else if(b == addButton){
			addOk.setDisable(false);
			addCancel.setDisable(false);
			
			songDisp.setDisable(false);
			artistDisp.setDisable(false);
			albumDisp.setDisable(false);
			yearDisp.setDisable(false);	
			
			songDisp.setText(null);
			artistDisp.setText(null);
			albumDisp.setText(null);
			yearDisp.setText(null);
			
			deleteButton.setDisable(true);
			addButton.setDisable(true);
			editButton.setDisable(true);
			
		 }
		 else if(b == editButton){
			editOk.setDisable(false);
			editCancel.setDisable(false);
			
			songDisp.setDisable(false);
			artistDisp.setDisable(false);
			albumDisp.setDisable(false);
			yearDisp.setDisable(false);
			
			deleteButton.setDisable(true);
			addButton.setDisable(true);
			editButton.setDisable(true);
			
		 }
	 }
	 
	public void extraPress(ActionEvent e){
		Button b = (Button)e.getSource();
		Song song;
		if(b!= addOk && b!=addCancel && songList.isEmpty())
			return;
		
		if(!songList.isEmpty())
			song = songList.get(listView.getSelectionModel().getSelectedIndex());
		else
			song = new Song(null,null,null,-1);
		
		if(b == deleteOk){
			removeSong(song);
			deleteOk.setDisable(true);
			deleteCancel.setDisable(true);
			
			deleteButton.setDisable(false);
			addButton.setDisable(false);
			editButton.setDisable(false);
		}
		else if(b == deleteCancel){
			deleteOk.setDisable(true);
			deleteCancel.setDisable(true);
			
			deleteButton.setDisable(false);
			addButton.setDisable(false);
			editButton.setDisable(false);
		}
		else if(b == addOk){
			Song temp = song;
			Song newSong;
			
			if(songDisp.getText()== null || artistDisp.getText()==null){
				Alert alert = new Alert (AlertType.INFORMATION);
	    		alert.setTitle("Song Add Failure");
	    		alert.setHeaderText("You must enter both the song name and artist!");
	    		alert.showAndWait();
	    		
	    		songDisp.setText(temp.name);
	    		artistDisp.setText(temp.artist);
	    		albumDisp.setText(temp.album);
	    		yearDisp.setText(Integer.toString(temp.year));	
			}
			else{
				if(yearDisp.getText() == null)
					yearDisp.setText("-1");
				
				newSong = new Song(songDisp.getText(), artistDisp.getText(), albumDisp.getText(), (yearDisp.getText().matches("\\d+") ? Integer.parseInt(yearDisp.getText()): -1));
				
				if(songList.indexOf(newSong)!= -1){
					Alert alert = new Alert (AlertType.INFORMATION);
					alert.setTitle("Song Add Failure");
					alert.setHeaderText("Song could not be added because a duplicate of the song exists!");
					alert.setContentText("Song Name: " + song.name + "\nArtist Name: " + song.artist);
					alert.showAndWait();
					
					songDisp.setText(temp.name);
					artistDisp.setText(temp.artist);
					albumDisp.setText(temp.album);
					yearDisp.setText(Integer.toString(temp.year));	    		
				}
				else{
					
					addSong(newSong);
				
					updateList();
				
					if(songList.size() == 1){ //just added a new song to an empty list
						listView.getSelectionModel().select(0);
						if(newSong!=null){
							songDisp.setText(newSong.name);
							artistDisp.setText(newSong.artist);
							albumDisp.setText(newSong.album);
							yearDisp.setText(Integer.toString(newSong.year));
						   
						// set listener for the items
							}
						listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showItem(savedStage)); 
					    
					}
				
			}
			addOk.setDisable(true);
			addCancel.setDisable(true);	
			songDisp.setDisable(true);
			artistDisp.setDisable(true);
			albumDisp.setDisable(true);
			yearDisp.setDisable(true);
			
			deleteButton.setDisable(false);
			addButton.setDisable(false);
			editButton.setDisable(false);
			}
		}
		else if(b == addCancel){
			addOk.setDisable(true);
			addCancel.setDisable(true);
			songDisp.setDisable(true);
			artistDisp.setDisable(true);
			albumDisp.setDisable(true);
			yearDisp.setDisable(true);
			
			songDisp.setText(song.name);
    		artistDisp.setText(song.artist);
    		albumDisp.setText(song.album);
    		yearDisp.setText(Integer.toString(song.year));
    		
    		deleteButton.setDisable(false);
			addButton.setDisable(false);
			editButton.setDisable(false);
		}
		else if(b == editOk){
			Song temp = new Song(song.name, song.artist, song.album, song.year); //temp object that copies song
			
			if(songDisp.getText().isEmpty() || artistDisp.getText().isEmpty()){
				Alert alert = new Alert (AlertType.INFORMATION);
	    		alert.setTitle("Song Edit Failure");
	    		alert.setHeaderText("You must enter both the song name and artist!");
	    		alert.showAndWait();
	    		
	    		songDisp.setText(song.name);
	    		artistDisp.setText(song.artist);
	    		albumDisp.setText(song.album);
	    		yearDisp.setText(Integer.toString(song.year));	
			}
			else{
				int savedIndex = songList.indexOf(song);
				boolean check = false;
				
				song.name = songDisp.getText();
				song.artist = artistDisp.getText();
				song.album = albumDisp.getText();
				song.year = (yearDisp.getText().matches("\\d+") ? Integer.parseInt(yearDisp.getText()): -1);
				
				for(int j = 0; j<songList.size(); j++){
					if(songList.get(j).equals(song) && j!=savedIndex)
						check = true;
				}
				
			if(check){ //Checks for duplicates by checking for each occurrence of song, meaning the one after
				Alert alert = new Alert (AlertType.INFORMATION);
	    		alert.setTitle("Song Add Failure");
	    		alert.setHeaderText("Song could not be added because a duplicate of the song exists!");
	    		alert.setContentText("Song Name: " + song.name + "\nArtist Name: " + song.artist);
	    		alert.showAndWait();
	    		songDisp.setText(temp.name);
	    		song.name = temp.name;
	    		artistDisp.setText(temp.artist);
	    		song.artist = temp.artist;
	    		albumDisp.setText(temp.album);
	    		song.album = temp.album;
	    		yearDisp.setText(Integer.toString(temp.year));
	    		song.year = temp.year;
	    	}
			updateList();
			listView.getSelectionModel().select(songList.indexOf(song));
			
			}
			editOk.setDisable(true);
			editCancel.setDisable(true);
			
			songDisp.setDisable(true);
			artistDisp.setDisable(true);
			albumDisp.setDisable(true);
			yearDisp.setDisable(true);
			
			deleteButton.setDisable(false);
			addButton.setDisable(false);
			editButton.setDisable(false);
			
		}
		else if(b == editCancel){
			songDisp.setText(song.name);
    		artistDisp.setText(song.artist);
    		albumDisp.setText(song.album);
    		yearDisp.setText(Integer.toString(song.year));	    		
 
			editOk.setDisable(true);
			editCancel.setDisable(true);
		
			songDisp.setDisable(true);
			artistDisp.setDisable(true);
			albumDisp.setDisable(true);
			yearDisp.setDisable(true);
			
			deleteButton.setDisable(false);
			addButton.setDisable(false);
			editButton.setDisable(false);
			
		}
		else
		return;
		
		
	}
	
	private void showItem(Stage mainStage) {
		if(listView.getSelectionModel().getSelectedIndex()==-1){
			listView.getSelectionModel().select(savedIndex);
			if(listView.getSelectionModel().getSelectedIndex()==-1)
			listView.getSelectionModel().select(savedIndex-1);
		}
		if(songList.isEmpty()){
			songDisp.setText("");
			artistDisp.setText("");
			albumDisp.setText("");
			yearDisp.setText("");
			return;
		}
			
		
		Song song = songList.get(listView.getSelectionModel().getSelectedIndex());
		 if(song!=null){
			   songDisp.setText(song.name);
			   artistDisp.setText(song.artist);
			   //albumDisp.setText(song.album != null ? song.album: "Not Available");
			   albumDisp.setText(song.album);
			   yearDisp.setText(Integer.toString(song.year));
			   // yearDisp.setText(song.year != -1? Integer.toString(song.year): "");
			   // set listener for the items
		 }
	} 
}