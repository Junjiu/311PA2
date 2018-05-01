

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) {


		
		 ArrayList<String> vertices = new ArrayList<String>();
	        vertices.add("A");
	        vertices.add("C");
	        NetworkInfluence n = new NetworkInfluence("bin/inf_graphs/mostInfSubMod0");
	        ArrayList<String> temp = n.mostInfluentialSubModular(vertices.size());
	        System.out.println(setEquality(temp, vertices));
	        for(String str : temp) {
	        	System.out.println(str);
	        }
	}
	static boolean setEquality(ArrayList<String> a, ArrayList<String> b) {
        return a.containsAll(b) && b.containsAll(a);
    }
}
