
import java.util.HashMap;
import java.util.HashSet;

public class Vertice {
	public String url;
	public HashSet<Vertice> neigbors;
	public HashMap<Vertice, Integer> distance;

	public Vertice(String url) {
		this.url = url;
		neigbors = new HashSet<Vertice>();
		distance = new HashMap<Vertice, Integer>();
	}
}
