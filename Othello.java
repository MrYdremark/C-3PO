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
  public Boolean makeMove(String move, String color) {
    if(!move.contains(":")) {
      return false;
    }
    String let = move.split(":")[0];
    String num = move.split(":")[1];
    int number = Integer.parseInt(num) -1;
    char derp = let.charAt(0);
    int letter = (int)(derp-65);
    
    boolean m1 = checkDir(number, letter, -1, 0, color, false, "");
    boolean m2 = checkDir(number, letter, 1, 0, color, false, "");
    boolean m3 = checkDir(number, letter, 0, -1, color, false, "");
    boolean m4 = checkDir(number, letter, 0, 1, color, false, "");
    boolean m5 = checkDir(number, letter, 1, 1, color, false, "");
    boolean m6 = checkDir(number, letter, -1, 1, color, false, "");
    boolean m7 = checkDir(number, letter, 1, -1, color, false, "");
    boolean m8 = checkDir(number, letter, -1, -1, color, false, "");

      if((board.get(let)[number] == null) && 
         (m1 || m2 || m3 || m4 || m5 || m6 || m7 || m8)) {
      board.get(let)[number] = color;
      return true;
    }
    return false;
  }

  public Boolean checkDir(int xPos, int yPos, 
                          int xMod, int yMod, 
                          String color, boolean check, String flip) {
    char y = (char)(65+yPos+yMod);
    int x = xPos+xMod;
    String[] test = board.get(Character.toString(y));

    if((xPos+xMod < 0) || (xPos+xMod > size -1) || (yPos+yMod < 0) || (yPos+yMod > size -1))
      return false;
    else if(test[x] == null) {
      return false;
    }
    else if((test[x].equals(color)) && (check == true)) {
      mothaFlippin(flip, color);
      return true;
    }
    else if((test[x].equals(color)) && (check == false))
      return false;
    else 
      return checkDir(x, (int)(y-65), xMod, yMod, color, true, flip+y+":"+x+",");
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