/*Mohammad Uppal & Mohamed Seliman*/

package controller;

public class Song implements Comparable<Song> {
	public String name, artist, album; 
	public int year; 
	
	public Song(String name, String artist, String album, int year){
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}

	public int compareTo(Song s) {
		// TODO Auto-generated method stub
		return this.name.compareToIgnoreCase(s.name);
	}
	
	@Override
	public boolean equals(Object s) {
		if(!(s instanceof Song)) {
        return false;
    }
        //Overrides equals method for use of duplicate removal in arraylists implementation;
		//Checks name and artist
		Song s2  = (Song)s;
		if(this.name.equalsIgnoreCase(s2.name) && this.artist.equalsIgnoreCase(s2.artist)){
    		return true;
		}
		return false;
		
	}
	
	@Override
	public int hashCode(){
		int code;
		
		code = 27 + name.hashCode();
		code *= 27 + artist.hashCode();
		return code;
	}

}
