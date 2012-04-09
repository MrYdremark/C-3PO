import java.util.*;

class Othello {

    private int size = 8;
    private HashMap<String,String[]> board = new HashMap<String,String[]>();

    public Othello() {
	for(int i = 0; i < size; i++) {
	    char letter = (char)(65+i);
	    board.put(Character.toString(letter), new String[size]);
	}
	board.get("D")[3] = "x";
	board.get("D")[4] = "o";
	board.get("E")[3] = "o";
	board.get("E")[4] = "x";
    }
    public int gameOver() {
	int nX = 0;
	int nO = 0;
	Boolean moveX = false;
	Boolean moveO = false;

	for(int i = 0; i < size; i++) {
	    char ch = (char)(65+i);
	    String[] derp = board.get(Character.toString(ch));
	    for(int j = 1; j < size; j++) {
		
		if(derp[j] == null) {
		    moveX = (moveX || makeMove(""+(char)(65+i)+":"+j, "x", false));
		    moveO = (moveO || makeMove(""+(char)(65+i)+":"+j, "o", false));
		}
		else if(derp[j].equals("x"))
		    nX++;
		else if(derp[j].equals("o"))
		    nO++;
	    }
	}
	if (!moveX && !moveO) {
	    if(nX < nO)
		return 2;
	    else
		return 1;
	}
	else
	    return 0;
    }
    public Boolean makeMove(String moveStr, String color, Boolean move) {
	if(!moveStr.contains(":")) {
	    return false;
	}
	String let = moveStr.split(":")[0];
	String num = moveStr.split(":")[1];
	int number = Integer.parseInt(num) -1;
	char derp = let.charAt(0);
	int letter = (int)(derp-65);
	
	boolean m1 = checkDir(number, letter, -1, 0, color, false, "", move);
	boolean m2 = checkDir(number, letter, 1, 0, color, false, "", move);
	boolean m3 = checkDir(number, letter, 0, -1, color, false, "", move);
	boolean m4 = checkDir(number, letter, 0, 1, color, false, "", move);
	boolean m5 = checkDir(number, letter, 1, 1, color, false, "", move);
	boolean m6 = checkDir(number, letter, -1, 1, color, false, "", move);
	boolean m7 = checkDir(number, letter, 1, -1, color, false, "", move);
	boolean m8 = checkDir(number, letter, -1, -1, color, false, "", move);

	if((board.get(let)[number] == null) && 
	   (m1 || m2 || m3 || m4 || m5 || m6 || m7 || m8)) {
	    if(move)
		board.get(let)[number] = color;
	    return true;
	}
	return false;
    }

    public Boolean checkDir(int xPos, int yPos, 
			    int xMod, int yMod, 
			    String color, boolean check, String flips, Boolean flip) {
	char y = (char)(65+yPos+yMod);
	int x = xPos+xMod;
	String[] test = board.get(Character.toString(y));

	if((xPos+xMod < 0) || (xPos+xMod > size -1) || (yPos+yMod < 0) || (yPos+yMod > size -1))
	    return false;
	else if(test[x] == null) {
	    return false;
	}
	else if((test[x].equals(color)) && (check == true)) {
	    if(flip)
		mothaFlippin(flips, color);
	    return true;
	}
	else if((test[x].equals(color)) && (check == false))
	    return false;
	else 
	    return checkDir(x, (int)(y-65), xMod, yMod, color, true, flips+y+":"+x+",", flip);
    }


    public void mothaFlippin(String flip, String color) {
	String[] flippin = flip.split(",");
	for(int i = 0; i < flippin.length; i++) {
	    String y = flippin[i].split(":")[0];
	    int x = Integer.parseInt(flippin[i].split(":")[1]);
	    board.get(y)[x] = color;
	}
    }
    public HashMap giveme() {
	return board;
    }
}