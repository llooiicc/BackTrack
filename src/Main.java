import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.sound.midi.Synthesizer;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

class STATICS{	
	
	static int maxX = 5;
	static int maxY = 5;
	
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
		System.err.println("-----initialTrous-----");
		for (int[] t : initialTrous) {
			System.err.println(t[0]+" "+t[1]);
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
				
				if(Pattern.matches("[0-9]{1}", map.get(key)[i])) {
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
				System.err.println("DIR EAST");
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
					for(int i = b.x + 1 ; i < b.x + b.mov ; i++) {
						System.err.println("TEST ON "+pattern.get(b.y)[b.x + 1]);
						if (Pattern.matches("(<|>|v|\\^|h|[0-9]{1})", pattern.get(b.y)[b.x + 1])) {
							
							block = true;
						}
					}
					System.err.println("block = "+block);
					
					
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
				System.err.println("TEST ON WEST");
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
					for(int i = b.x - b.mov +1 ; i < b.x ; i++) {
						System.err.println("TEST ON "+pattern.get(b.y)[b.x - b.mov +1]);
						if (Pattern.matches("(<|>|v|\\^|h|[0-9]{1})", pattern.get(b.y)[b.x - b.mov + 1])) {
							
							block = true;
						}
					}
					System.err.println("block = "+block);
					
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
				System.err.println("TEST ON SOUTH");
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
					for(int i = b.y + 1 ; i < b.y + b.mov ; i++) {
						System.err.println("TEST ON "+pattern.get(i)[b.x]);
						if (Pattern.matches("(<|>|v|\\^|h|[0-9]{1})", pattern.get(i)[b.x])) {
							
							block = true;
						}
					}
					System.err.println("block = "+block);
					
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
				System.err.println("TEST ON NORTH");
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
					for (int i = b.y -1 ; i > b.y  ; i--) {
						block = Pattern.matches("(<|>|v|^|h|[0-9]{1})", pattern.get(i)[b.x]);
					}
					
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
			}
		}
		else if(pos.dir == 'N') {
			map.get(pos.y + pos.mov)[pos.x] = ".";
			for(int i = pos.y + pos.mov ; i > pos.y ; i--) {
				pattern.get(i)[pos.x] = "^";
			}
		}
		else if(pos.dir == 'E') {
			map.get(pos.y)[pos.x - pos.mov] = ".";
			for(int i = pos.x - pos.mov ; i < pos.x ; i++) {
				pattern.get(pos.y)[i] = ">";
			}
		}
		else if(pos.dir == 'W') {
			map.get(pos.y)[pos.x + pos.mov] = ".";
			for(int i = pos.x + pos.mov ; i > pos.x ; i--) {
				pattern.get(pos.y)[i] = "<";
			}
		}
		
		if(map.get(pos.y)[pos.x].equals("H")) {
			map.get(pos.y)[pos.x] = "0";
		}
		else {
			map.get(pos.y)[pos.x] = String.valueOf(pos.mov - 1);
		}
		
			
		
	}
	
	public void printMap() {
		
		for (int key : map.keySet()) {
			for (String c : map.get(key)) {
				System.err.print(c+" | ");
			}
			System.err.println();
		}
	}
	
	public void printPattern() {
		
		for (int key : pattern.keySet()) {
			for (String c : pattern.get(key)) {
				System.err.print(c+" | ");
			}
			System.err.println();
		}
	}
	
	public void printBalles() {
		for (Balle balle : listBalles) {
			System.err.println(balle);
		}
	}
	
	public void printossibilites() {
		for(int key : mapPos.keySet()) {
			for (Possibilite pos : mapPos.get(key)) {
				System.err.println(pos);
			}
		}
	}
	
	
	
	
}

public class Main {
	
	public static void parcourNodes(Node n) {
		
		if(STATICS.isAllInHole(n.listBalles) == true) {
			
			System.err.println("------- F O U N D ---------");
		}else {
			
			System.err.println(n);
			
			//possiblilite
			Possibilite p = n.getPossibilite();
			System.err.println("_______________________________________________");
			System.err.println(p+"\n");
			
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
				System.err.println();
				node.printPattern();
				//init node
				node.initialise();
				node.printBalles();
				node.printossibilites();
				
					//new Scanner(System.in).next();
				parcourNodes(node);
			}
			else {
				System.err.println("###### RETOUR ########");
					//new Scanner(System.in).next();
				System.err.println(node.parent);
				n.parent.printMap();
				n.parent.printBalles();
				n.parent.printossibilites();
				parcourNodes(n.parent);
			}
			
		}
		
		

		
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HashMap<Integer, String[]> initialMap = new HashMap<>();
//		String[] c =  {".","h","x"};
//		String[] c2 = {"h","1","h"};
//		String[] c3 = {"2","x","."};
//		initialMap.put(0, c);
//		initialMap.put(1, c2);
//		initialMap.put(2, c3);
//		STATICS.maxX = 3;
//		STATICS.maxY = 3;
		
//		String[] c =  {"4",".",".","x","x"};
//		String[] c2 = {".","h",".","h","."};
//		String[] c3 = {".",".",".","h","."};
//		String[] c4 = {".","2",".",".","2"};
//		String[] c5 = {".",".",".",".","."};
//		initialMap.put(0, c);
//		initialMap.put(1, c2);
//		initialMap.put(2, c3);
//		initialMap.put(3, c4);
//		initialMap.put(4, c5);
//		STATICS.maxX = 5;
//		STATICS.maxY = 5;
		


//		3..H.2
//		.2..H.
//		..H..H
//		.X.2.X
//		......
//		3..H..
//		String[] c =  {"3",".",".","h",".","2"};
//		String[] c2 = {".","2",".",".","h","."};
//		String[] c3 = {".",".","h",".",".","h"};
//		String[] c4 = {".","x",".","2",".","x"};
//		String[] c5 = {".",".",".",".",".","."};
//		String[] c6 = {"3",".",".","h",".","."};
//		initialMap.put(0, c);
//		initialMap.put(1, c2);
//		initialMap.put(2, c3);
//		initialMap.put(3, c4);
//		initialMap.put(4, c5);
//		initialMap.put(5, c6);
//		STATICS.maxX = 6;
//		STATICS.maxY = 6;
		
		String[] c  = {".","X","X","X",".","5","X","."};
		String[] c1 = {"X",".","4",".","X",".",".","X"};
		String[] c2 = {"X","4",".",".","X","3",".","X"};
		String[] c3 = {"X",".",".",".","X",".","X","."};
		String[] c4 = {".","X",".","X",".","H",".","X"};
		String[] c5 = {"X",".","H","X",".",".",".","X"};
		String[] c6 = {"X",".",".","X",".","H",".","X"};
		String[] c7 = {".","X","H",".","X","X","X","."};
		initialMap.put(0, c);
		initialMap.put(1, c1);
		initialMap.put(2, c2);
		initialMap.put(3, c3);
		initialMap.put(4, c4);
		initialMap.put(5, c5);
		initialMap.put(6, c6);
		initialMap.put(7, c7);
		STATICS.maxX = 8;
		STATICS.maxY = 8;
		
		STATICS.setInitialTrous(initialMap);
		STATICS.printInitialTrous();
		
		
		Node n = new Node();
		n.map = (HashMap<Integer, String[]>) initialMap.clone();
		n.pattern = (HashMap<Integer, String[]>) initialMap.clone();
		n.initialise();
		n.printMap();
		n.printBalles();
		n.printossibilites();
		System.err.println();
		
		parcourNodes(n);
		
	}

}
