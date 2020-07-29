import java.util.*;
import java.lang.*;

public class Source {
	
	public static class Key {
		public static List<String> key = new ArrayList<String>();
	}
	
	public static void main(String[] args)throws Exception {
		int results = 0;
		String str = "KUHPVIBQKVOSHWHXBPOFUXHRPVLLDDWVOSKWPREDDVVIDWQRBHBGLLBBPKQUNRVOHQEIRLWOKKRDD";
		
		permutation("abcdefg");
		
		for (int i = 0; i < Key.key.size(); i++) {
			int shiftCount = 0;
			while(shiftCount++ < 26) {
				
				String get = decryptCT(Key.key.get(i),str);
				if (get.contains("THE")) {
					System.out.println(shiftCount+": String used: "+str);
					System.out.println("Key used: "+Key.key.get(i));
					System.out.println("Possible Decryption Text:"+get);
					System.out.println();
					results++;
				}

				str = CaesarShift(str, 1).toString();
			}
		}
		System.out.println("Number of results: "+results);
		

	}
	
	public static void permutation(String str) { 
	    permutation("", str); 
	}

	private static void permutation(String prefix, String str) {
	    int n = str.length();
	    if (n == 0) Key.key.add(prefix);
	    else {
	        for (int i = 0; i < n; i++)
	            permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
	    }
	}
  
	
	public static StringBuffer CaesarShift(String text, int s) { 
        StringBuffer result= new StringBuffer(); 
        for (int i=0; i<text.length(); i++) { 
            if (Character.isUpperCase(text.charAt(i))) { 
                char ch = (char)(((int)text.charAt(i) + s - 65) % 26 + 65); 
                result.append(ch); 
            } 
            else { 
                char ch = (char)(((int)text.charAt(i) + s - 97) % 26 + 97); 
                result.append(ch); 
            } 
        } 
        return result; 
    } 
	
	public static String decryptCT(String key, String text) {
		int[] arrange = arrangeKey(key);
		int lenkey = arrange.length;
		int lentext = text.length();
		
		int row = (int) Math.ceil((double) lentext / lenkey);
		
		String regex = "(?<=\\G.{"+row+"})";
		String[] get = text.split(regex);
		
		char[][] grid = new char[row][lenkey];
		
		for (int x = 0; x < lenkey; x++) {
			for (int y = 0; y < lenkey; y++) {
				if (arrange[x] == y) {
					for (int z = 0; z < row; z++) {
						grid[z][y] = get[arrange[y]].charAt(z);
					}
				}
			}
		}
		
		String dec = "";
		for (int x = 0; x < row; x++) {
			for (int y = 0; y< lenkey; y++) {
				dec = dec + grid[x][y];
			}
		}
		
		return dec;
	}
	
	public static int[] arrangeKey(String key) {
		String[] keys = key.split("");
		Arrays.parallelSort(keys);;
		int[] num = new int[key.length()];
		for (int x = 0; x < keys.length; x++) {
			for (int y = 0; y < key.length(); y++) {
				if (keys[x].contentEquals(key.charAt(y) + "")) {
					num[y] = x;
					break;
				}
			}
		}
		return num;
	}
	
    public static String encrypt(String str) {

        String[] get = str.split(" ");

        int maxlen = 0;
        String init = "";
        for (String a: get) {
            if (a.length() >= init.length())
                init = a;
        }

        maxlen = init.length();
        char[][] grid = new char[get.length][maxlen];

        for (int x = 0; x < get.length; x++) {
            String gt = get[x];
            for (int y = 0; y < maxlen; y++) {
                if (y != gt.length())
                    grid[x][y] = gt.charAt(y);
                else
                // can be replace with random alphabet also
                    grid[x][y] = 'X';
            }
        }

        StringBuilder cb = new StringBuilder();
        for (int x = 0; x < maxlen; x++) {
            for (int y = 0; y < get.length; y++) {
                cb.append(grid[y][x]);
            }
        }

        return cb.toString();
    }

    public static String[] decrypt(String str) {
        List<Integer> val = new ArrayList<Integer>();
        val.add(7);

        String[] dec = new String[val.size()];
        for (int x = 0; x < val.size(); x++) {
            int now = (int) val.get(x);
            String regex = "(?<=\\G.{" + now + "})";
            String[] get = str.split(regex);

            //transpose
            char grid[][] = new char[now][get.length];
            for (int y = 0; y < get.length; y++) {
                String nw = get[y];
                for (int z = 0; z < nw.length(); z++) {
                    grid[z][y] = nw.charAt(z);
                }
            }

            //combine
            dec[x] = "";
            for (int y = 0; y < now; y++) {
                for (int z = 0; z < get.length; z++) {
                    dec[x] = dec[x] + grid[y][z];
                }
                dec[x] = dec[x] + " ";
            }

        }


        return dec;
    }

}

/*
sources:
caesar shift:
https://stackoverflow.com/questions/19108737/java-how-to-implement-a-shift-cipher-caesar-cipher
https://beginnersbook.com/2015/04/convert-stringbuffer-to-string/

columnar transposition:
https://programmingcode4life.blogspot.com/2015/10/columnar-transposition-cipher-without.html
https://programmingcode4life.blogspot.com/2015/09/columnar-transposition-cipher.html
https://www.geeksforgeeks.org/columnar-transposition-cipher/

permutations:
https://www.geeksforgeeks.org/print-all-permutations-of-a-string-in-java/
https://stackoverflow.com/questions/4240080/generating-all-permutations-of-a-given-string

*/
