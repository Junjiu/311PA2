import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

public class GraphUntility {
	public static HashMap<String, Vertice> DataToGraph(String fileName) {
		HashMap<String, Vertice> result = new HashMap<String, Vertice>();
		try {
			Reader graphDatafile;
			graphDatafile = new FileReader(new File(fileName));
			BufferedReader br = new BufferedReader(graphDatafile);
			String line;
			br.readLine();
			while((line = br.readLine()) != null) {
				//System.out.println(line);
				int index = line.substring(5).indexOf("wiki");
				String source = line.substring(0, index+3);
				String destination = line.substring(index+4);
				
				Vertice sourceVertice = result.getOrDefault(source, new Vertice(source));
				Vertice destinationVertice = result.getOrDefault(destination, new Vertice(destination));
				result.put(source, sourceVertice);
				result.put(destination, destinationVertice);
				sourceVertice.neigbors.add(destinationVertice);
				destinationVertice.neigbors.add(sourceVertice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(Entry<String, Vertice> entry : result.entrySet()) {
			//System.out.println(entry.getKey());
//			HashSet<Vertice> temp = entry.getValue().neigbors;
//			for(Vertice v : temp) {
//				System.out.println(v.url);
//			}
//			System.out.println("+=============");
		}
		return result;
	}
	public static void findDistance (Map<String ,Vertice> vertices) {
		for(Map.Entry<String, Vertice> entry : vertices.entrySet()){
			findDistanceHelper(vertices, entry.getKey(), new HashSet<String>());
			//System.out.println(entry.getValue().distance.get(vertices.get("/wiki/Iowa_State_Myths_and_Legends")));
			break;
		}
		
	}
	private static void findDistanceHelper(Map<String, Vertice> vertices, String startVertice, HashSet<String> isVisited ) {
		Queue<String> queue = new LinkedList<String>();
		queue.add(startVertice);
		int distance = 0;
		Vertice Vsource  = vertices.get(startVertice);
		Vsource.distance.put(Vsource, 0);
		isVisited.add(startVertice);
		while(!queue.isEmpty()) {
			distance ++;
			Vertice v = vertices.get(queue.poll());
			//System.out.println(v.neigbors.size());
			for(Vertice neigbor: v.neigbors) {
				if(isVisited.contains(neigbor.url)) continue;
				isVisited.add(neigbor.url);
				queue.add(neigbor.url);
				Vsource.distance.put(neigbor, distance);
				//System.out.println(distance);
			}
		}
	}
}
