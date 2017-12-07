package implementation;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
/************************************************************************************************************************
 * 
 * 			E N T I T I E S
 * 
 * 
 ***********************************************************************************************************************/

/*_______________________
  
 ________________________*/
class STATICS{	
	
	static int maxX = 5;
	static int maxY = 5;
	
	static Pattern reg = Pattern.compile("^(<|>|v|\\^|[0-9])$");
	
	static ArrayList<int[]> initialTrous = new ArrayList<>();
	
	static void setInitialTrous(HashMap<Integer, String[]> initialMap) {
		
		for (int key : initialMap.keySet()) {
			for (int x = 0 ; x < initialMap.get(key).length ; x++) {
				if(initialMap.get(key)[x].equals("H")) {
					int[] trou = {x,key};
					initialTrous.add(trou);
				}
			}
		}
	}
	
	static void printInitialTrous() {
		////System.err.println("-----initialTrous-----");
		for (int[] t : initialTrous) {
			////System.err.println(t[0]+" "+t[1]);
		}
	}
	
	static boolean isAllInHole(ArrayList<Balle> balles) {
		
		int match = 0;
		for (Balle b : balles) {
			for (int[] t : initialTrous) {
				if(t[0] == b.x && t[1] == b.y) {
					match++;
				}
			}
		}
		
		return (match == balles.size()) ? true : false;
	}
	
	
}
/*_______________________

________________________*/
class Balle{
	
	int id;
	int x;
	int y;
	int mov;
	
	@Override
	public String toString() {
		return "Balle [id=" + id + ", x=" + x + ", y=" + y + ", mov=" + mov + "]";
	}
		
}
/*_______________________

________________________*/
class Possibilite{
	
	int balleId;
	int x;
	int y;
	int mov;
	char dir;
	boolean visited;
	
	@Override
	public String toString() {
		return "Possibilite [balleId=" + balleId + ", x=" + x + ", y=" + y + ", mov=" + mov + ", dir=" + dir
				+ ", visited=" + visited + "]";
	}
	
	
}
/*_______________________

________________________*/
class Node{
	
	
	boolean bloquant = false;
	
	HashMap<Integer, String[]> map = new HashMap<>();
	HashMap<Integer, String[]> pattern = new HashMap<>();
	

	public ArrayList<Balle> listBalles = new ArrayList<>();
	public HashMap<Integer, ArrayList<Possibilite>> mapPos = new HashMap<>();
	
	public Node parent = null;
	
	
	public void initialise() {
		
		/*
		 * balles
		 */
		for (int key : map.keySet()) {
			for (int i = 0; i < map.get(key).length; i++) {
				
				Pattern reg = Pattern.compile("^[0-9]$");
				Matcher match = reg.matcher(map.get(key)[i]);
				if(match.matches()) {
					//int[] balle = {key, i, value};
					//balles.add(balle);
					Balle balle = new Balle();
					balle.y = key;
					balle.x = i;
					balle.mov = Integer.valueOf(map.get(key)[i]);
					balle.id = listBalles.size();
					listBalles.add(balle);
				}
			}
		}
		
		/*
		 * mapPoss
		 */
		for (Balle b : listBalles) {
			
			
			ArrayList<Possibilite> listPoss = new ArrayList<>();
			
			//E
			if(b.x + b.mov < STATICS.maxX) {
				////System.err.println("DIR EAST");
				if (b.mov == 1) {
					if(map.get(b.y)[b.x + b.mov].equals("H")) {
						Possibilite p = new Possibilite();
						p.x = b.x + b.mov;
						p.y = b.y;
						p.mov = b.mov;
						p.dir = 'E';
						p.visited = false;
						p.balleId = b.id;
						listPoss.add(p);
					}
				}
				else {
					
					boolean block = false;
					String tst = "";
					for(int i = b.x + 1 ; i < b.x + b.mov ; i++) {
						////System.err.println("TEST ON "+pattern.get(b.y)[i]);
						tst += pattern.get(b.y)[i];
					}	
						Matcher match = STATICS.reg.matcher(tst);
						if(match.matches()) {							
							block = true;
						}
					
					////System.err.println("block = "+block+" b = "+b.x+" "+b.y);
					
					
					if(block == false) {
						
						if(map.get(b.y)[b.x + b.mov].equals("H") || map.get(b.y)[b.x + b.mov].equals(".")) {
							Possibilite p = new Possibilite();
							p.x = b.x + b.mov;
							p.y = b.y;
							p.mov = b.mov;
							p.dir = 'E';
							p.visited = false;
							p.balleId = b.id;
							listPoss.add(p);
						}
					}
					
					
				}
				
			}
			//W
			if(b.x - b.mov >= 0) {
				////System.err.println("TEST ON WEST");
				if(b.mov == 1) {
					if(map.get(b.y)[b.x - b.mov].equals("H")) {
						Possibilite p = new Possibilite();
						p.x = b.x - b.mov;
						p.y = b.y;
						p.mov = b.mov;
						p.dir = 'W';
						p.visited = false;
						p.balleId = b.id;
						listPoss.add(p);
					}
				}
				else {
					
					boolean block = false;
					String tst = "";
					for(int i = b.x - 1 ; i > b.x - b.mov  ; i--) {
						tst += pattern.get(b.y)[i];
					}
					
						////System.err.println("TEST ON "+pattern.get(b.y)[i]);
						Matcher match = STATICS.reg.matcher(tst);
						if(match.matches()) {
							
							block = true;
						}
					
					////System.err.println("block = "+block+" b = "+b.x+" "+b.y);
					
					if(block == false) {
						
						if(map.get(b.y)[b.x - b.mov].equals("H") || map.get(b.y)[b.x - b.mov].equals(".")) {
							Possibilite p = new Possibilite();
							p.x = b.x - b.mov;
							p.y = b.y;
							p.mov = b.mov;
							p.dir = 'W';
							p.visited = false;
							p.balleId = b.id;
							listPoss.add(p);
						}
						
					}
					
					
				}
				
			}
			
			//S
			if(b.y + b.mov < STATICS.maxY) {
				////System.err.println("TEST ON SOUTH");
				if(b.mov == 1) {
					if(map.get(b.y + b.mov)[b.x].equals("H")) {
						Possibilite p = new Possibilite();
						p.x = b.x;
						p.y = b.y + b.mov;
						p.mov = b.mov;
						p.dir = 'S';
						p.visited = false;
						p.balleId = b.id;
						listPoss.add(p);
					}
				}
				else {
					
					boolean block = false;
					String tst = "";
					for(int i = b.y + 1 ; i < b.y + b.mov ; i++) {
						tst += pattern.get(i)[b.x];
					}
					
						////System.err.println("TEST ON "+pattern.get(i)[b.x]);
						Matcher match = STATICS.reg.matcher(tst);
						if(match.matches()) {
													
							block = true;
						}
					
					//System.err.println("block = "+block+" b = "+b.x+" "+b.y);
					if(block == false) {
						if(map.get(b.y + b.mov)[b.x].equals("H") || map.get(b.y + b.mov)[b.x].equals(".")) {
							Possibilite p = new Possibilite();
							p.x = b.x;
							p.y = b.y + b.mov;
							p.mov = b.mov;
							p.dir = 'S';
							p.visited = false;
							p.balleId = b.id;
							listPoss.add(p);
						}
					}
					
					
				}
				
			}
			//N
			if(b.y - b.mov >= 0) {
				//System.err.println("TEST ON NORTH");
				if(b.mov == 1) {
					if(map.get(b.y - b.mov)[b.x].equals("H")) {
						Possibilite p = new Possibilite();
						p.x = b.x;
						p.y = b.y - b.mov;
						p.mov = b.mov;
						p.dir = 'N';
						p.visited = false;
						p.balleId = b.id;
						listPoss.add(p);
					}
				}
				else {
					
					boolean block = false;
					String tst = "";
					for (int i = b.y -1 ; i > b.y - b.mov  ; i--) {
						tst += pattern.get(i)[b.x];
					}
					
						//System.err.println("TEST ON "+pattern.get(i)[b.x]);
						Matcher match = STATICS.reg.matcher(tst);
						if(match.matches()) {

							block = true;
						}
					
					//System.err.println("block = "+block+" b = "+b.x+" "+b.y);
					
					if(block == false) {
						
						if(map.get(b.y - b.mov)[b.x].equals("H") || map.get(b.y - b.mov)[b.x].equals(".")) {
							Possibilite p = new Possibilite();
							p.x = b.x;
							p.y = b.y - b.mov;
							p.mov = b.mov;
							p.dir = 'N';
							p.visited = false;
							p.balleId = b.id;
							listPoss.add(p);
						}
					}
					
					
				}
				
			}
			
			mapPos.put(b.id, listPoss);
		}
		
		bloquant = (mapPos.size() == 0) 
				? true 
				: false;
		
	}
	
	public void copieMap(HashMap<Integer, String[]> cible , HashMap<Integer, String[]> source) {
		
		for (int key : source.keySet()) {
			String[] tab = new String[STATICS.maxX];
			int i = 0;
			for (String symb : source.get(key)) {
				tab[i] = symb;
				i++;
			}
			cible.put(key, tab);
		}
	}
	
	public Possibilite getPossibilite() {
		
		for (int key : mapPos.keySet()) {
			for (Possibilite pos : mapPos.get(key)) {
				if(pos.visited == false) {
					pos.visited = true;
					return pos;
				}
			}
		}
		return null;
	}
		
	public void setMapOnPossiblite(HashMap<Integer, String[]> map , Possibilite pos) {
		
		/*
		 * pattern
		 */
		if(parent.pattern == null || parent.pattern.size() < 1) {
			copieMap(pattern, map);
		}
		else {
			copieMap(pattern, parent.pattern);
		}
		
		
		/*
		 * map
		 */
		if(pos.dir == 'S') {
			map.get(pos.y - pos.mov)[pos.x] = ".";
			for(int i = pos.y - pos.mov ; i < pos.y ; i++) {
				pattern.get(i)[pos.x] = "v";
				map.get(i)[pos.x] = "v";
			}
			pattern.get(pos.y)[pos.x] = String.valueOf(pos.mov - 1);
		}
		else if(pos.dir == 'N') {
			map.get(pos.y + pos.mov)[pos.x] = ".";
			for(int i = pos.y + pos.mov ; i > pos.y ; i--) {
				pattern.get(i)[pos.x] = "^";
				map.get(i)[pos.x] = "^";
			}
		}
		else if(pos.dir == 'E') {
			map.get(pos.y)[pos.x - pos.mov] = ".";
			for(int i = pos.x - pos.mov ; i < pos.x ; i++) {
				pattern.get(pos.y)[i] = ">";
				map.get(pos.y)[i] = ">";
			}
		}
		else if(pos.dir == 'W') {
			map.get(pos.y)[pos.x + pos.mov] = ".";
			for(int i = pos.x + pos.mov ; i > pos.x ; i--) {
				pattern.get(pos.y)[i] = "<";
				map.get(pos.y)[i] = "<";
			}
		}
		
		if(map.get(pos.y)[pos.x].equals("H")) {
			map.get(pos.y)[pos.x] = "0";
		}
		else {
			map.get(pos.y)[pos.x] = String.valueOf(pos.mov - 1);
		}
		
		if((pos.mov - 1) == 0) {
			pattern.get(pos.y)[pos.x] = String.valueOf(0);
		}
		else {
			pattern.get(pos.y)[pos.x] = String.valueOf(pos.mov - 1);
		}
		
			
		
	}
	
	public void printMap() {
		
		for (int key : map.keySet()) {
			for (String c : map.get(key)) {
				//System.err.print(c+" | ");
			}
			//System.err.println();
		}
	}
	
	public void printPattern() {
		

			for (int key : pattern.keySet()) {
				for (String c : pattern.get(key)) {
					//System.err.print(c+" | ");
				}
				//System.err.println();
			}
		
		
	}
	
	public void servePatternOnAPlate() {
		
		for (int key : pattern.keySet()) {
			for (String c : pattern.get(key)) {
				System.out.print(c);
			}
			System.out.println();
		}
	
	}
	
	public void printBalles() {
		for (Balle balle : listBalles) {
			//System.err.println(balle);
		}
	}
	
	public void printossibilites() {
		for(int key : mapPos.keySet()) {
			for (Possibilite pos : mapPos.get(key)) {
				//System.err.println(pos);
			}
		}
	}
	
	public void clearPattern() {
		
		for (int key : pattern.keySet()) {
			for (int i = 0 ; i < STATICS.maxX ; i++) {
				
				Pattern reg = Pattern.compile("^(X|H|[0-9])$");
				Matcher match = reg.matcher(pattern.get(key)[i]);
				if(match.matches()) {
					pattern.get(key)[i] = ".";
				}
			}
		}
	}
	
	
}






/************************************************************************************************************************
 * 
 * 			F I N D I N G
 * 
 * 
 ***********************************************************************************************************************/
class Finder{
	
	public static void parcourNodes(Node n) {
		
		if(STATICS.isAllInHole(n.listBalles) == true) {
			
			//System.err.println("------- F O U N D ---------");
			n.clearPattern();
			n.servePatternOnAPlate();
			
		}else {
			
			//System.err.println(n);
			
			//possiblilite
			Possibilite p = n.getPossibilite();
			//System.err.println("_______________________________________________");
			//System.err.println(p+"\n");
			
			Node node = new Node();
			
			
			if(p != null) {
				
				//nouveau noeud
				node.parent = n;
				// copie map
				node.copieMap(node.map, n.map);
				node.setMapOnPossiblite(node.map, p);
				//debug map
				node.printMap();
				//pattern
				//System.err.println();
				node.printPattern();
				//init node
				node.initialise();
				node.printBalles();
				node.printossibilites();
				
					//new Scanner(System.in).next();
				parcourNodes(node);
			}
			else {
				//System.err.println("###### RETOUR ########");
					//new Scanner(System.in).next();
				//System.err.println(node.parent);
				n.parent.printMap();
				n.parent.printBalles();
				n.parent.printossibilites();
				parcourNodes(n.parent);
			}
			
		}
		
		

		
	}
	
}





/************************************************************************************************************************
 * 
 * 			C O D I N G  G A M E  C L A S S
 * 
 * 
 ***********************************************************************************************************************/
class Solution {
	

    public static void main(String args[]) {
    	
    	HashMap<Integer, String[]> initialMap = new HashMap<>();
    	
        Scanner in = new Scanner(System.in);
        STATICS.maxX = in.nextInt();
        STATICS.maxY = in.nextInt();
        
        for (int i = 0; i < STATICS.maxY; i++) {
            String row = in.next();
            initialMap.put(i, row.split(""));
        }
        
        for(int k : initialMap.keySet()) {
        	for(String s : initialMap.get(k)) {
        		System.err.print(s);
        	}
        	System.err.println();
        }
        
        
        STATICS.setInitialTrous(initialMap);
		STATICS.printInitialTrous();
		

		Node n = new Node();
		n.map = (HashMap<Integer, String[]>) initialMap.clone();
		n.pattern = (HashMap<Integer, String[]>) initialMap.clone();
		n.initialise();
		n.printMap();
		n.printBalles();
		n.printossibilites();
		////System.err.println();
		
		Finder.parcourNodes(n);
       
        /*
.XXX.5X.
X.4.X..X
X4..X3.X
X...X.X.
.X.X.H.X
X.HX...X
X..X.H.X
.XH.XXX.
		*/
		
		/* test 7
4H5.....
......3.
......H.
.....H3.
........
....HH..
.43.....
..H3H... 
		*/
 
         
      
    }
}